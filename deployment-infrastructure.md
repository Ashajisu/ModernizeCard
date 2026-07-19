# ModernizeCard 배포 인프라 및 자동화 가이드

최종 갱신: 2026-07-19

## 1. 인프라 개요

| 구성 요소 | 이전 (Before) | 현재 (After) |
|---|---|---|
| 백엔드 | 노트북 WinSW 서비스 + ngrok 포워딩 | Oracle Cloud VM (systemd 서비스) |
| DB | 별도 가상서버 (SSH 불가) | 백엔드와 동일 VM 내 MariaDB |
| 프론트엔드 | GitHub Pages (수동 `npm run deploy`) | GitHub Pages (GitHub Actions 자동 배포) |
| 백엔드 배포 | 수동 (jar 직접 교체) | GitHub Actions 자동 배포 |

---

## 2. 서버 정보

### 인스턴스
- **제공사**: Oracle Cloud (Always Free)
- **Shape**: VM.Standard.E2.1.Micro (AMD, 1/8 OCPU, 약 982Mi RAM)
- **OS**: Ubuntu 24.04
- **리전**: ap-tokyo-1 (도쿄)
- **Public IP**: `138.2.1.35` (Reserved/예약 IP로 고정)
- **접속 계정**: `ubuntu`

### 도메인 및 SSL
- **도메인**: `ashajisu.duckdns.org` (Duck DNS, 무료 동적 DNS)
- **SSL 인증서**: Let's Encrypt (Certbot으로 발급, 90일마다 자동 갱신)
- **리버스 프록시**: Nginx (443 → `localhost:9090`)

### 백엔드
- **스택**: Java 17 (JDK 17, Temurin) + Spring Boot (WAR/Tomcat 방식에서 순수 JAR 실행 방식으로 전환)
- **실행 포트**: 9090 (내부, 외부에는 Nginx 경유로만 노출)
- **서비스 관리**: systemd (`backend.service`)
- **배포 경로**: `/opt/backend/`
- **실행 파일**: 심볼릭 링크 `modernize` → 실제 jar (`ModernizeCard-x.x.x.jar`)

### DB
- **엔진**: MariaDB
- **위치**: 백엔드와 동일 VM (기존 별도 가상서버에서 마이그레이션 완료)
- **마이그레이션 방법**: DBeaver Data Transfer 기능으로 직접 이전
- **접속 제한**: 3306 포트는 홈 IP(등록된 `/32`)에서만 허용

### 리소스 관리
- **Swap**: 4GB 스왑 파일 구성 (`/swapfile`, 낮은 RAM 보완용)
- **JVM 힙 제한**: `-Xmx600m` (다른 프로세스와 메모리 경합 방지)

### 방화벽
- **도구**: firewalld
- **개방 포트**: 22(SSH, 홈 IP 제한), 80/443(Nginx, 공개), 3306(DB, 홈 IP 제한)
- **VCN Security List**: OS 방화벽과 별도로 동일하게 포트 규칙 등록 필요

---

## 3. 백엔드 자동 배포 (GitHub Actions)

### 트리거 조건
```yaml
on:
  push:
    branches: [main]
    paths:
      - 'src/**'
      - 'build.gradle'
```
(백엔드 소스가 저장소 루트의 `src/` 하위에 위치하는 구조)

### 처리 흐름
1. `actions/checkout` — 코드 체크아웃
2. `actions/setup-java` — JDK 17 설치
3. `./gradlew build -x test` — jar 빌드 (`build/libs/*.jar` 생성)
4. `appleboy/scp-action` — 빌드된 jar를 `/opt/backend/`로 전송
5. `appleboy/ssh-action` — 서버 접속 후:
   - `modernize` 심볼릭 링크를 최신 jar로 갱신
   - `systemctl restart backend`

### 필요한 GitHub Secrets
| Name | 값 |
|---|---|
| `VPS_HOST` | `138.2.1.35` |
| `VPS_USER` | `ubuntu` |
| `VPS_SSH_KEY` | 서버 접속용 개인키 전체 내용 |

### 서버 측 사전 설정
```bash
sudo visudo -f /etc/sudoers.d/backend-deploy
# 내용: ubuntu ALL=(ALL) NOPASSWD: /bin/systemctl restart backend, /bin/systemctl status backend
```

### 해결했던 주요 이슈
- `war` 플러그인 + `SpringBootServletInitializer` 제거 → 순수 `jar` 실행 방식으로 전환
- `providedRuntime 'spring-boot-starter-tomcat'` 제거 (war 전용 설정이라 충돌)
- Spring Security의 CORS는 `WebMvcConfigurer`가 아닌 `SecurityFilterChain`에 직접 연결 필요
- `appleboy/scp-action`과 `appleboy/ssh-action`은 입력 파라미터가 다름 (파일 전송 vs 명령 실행 역할 구분)

---

## 4. 프론트엔드 자동 배포 (GitHub Actions)

### 트리거 조건
```yaml
on:
  push:
    branches: [main]
    paths:
      - 'modernize/**'
```

### 처리 흐름
1. `actions/checkout` — 코드 체크아웃
2. `actions/setup-node` (Node 24) — Node.js 설치
3. `npm ci` — 의존성 설치
4. `npm run build` — 빌드 (내부적으로 `vue-tsc --noEmit && vite build` 실행, 결과물은 저장소 루트의 `dist/`에 생성)
5. `peaceiris/actions-gh-pages` — `dist/` 내용을 `gh-pages` 브랜치로 배포 (`.nojekyll` 자동 생성 포함)

### 필요한 권한
```yaml
permissions:
  contents: write
```
`GITHUB_TOKEN`은 GitHub이 실행마다 자동 발급하므로 별도 등록 불필요.

### 참고
- 기존 로컬 수동 배포(`npm run deploy`, `gh-pages` 패키지 사용)는 더 이상 실행할 필요 없음
- `.env`의 `VITE_API_URL` 값이 git에 커밋되어 있지 않다면, GitHub Secrets(`VITE_API_URL`)로 등록 후 빌드 시 주입 필요

---

## 5. 네트워크 접근 정책

| 접근 주체 | 대상 | 허용 방식 |
|---|---|---|
| 일반 사용자 (웹) | 프론트엔드 (GitHub Pages) | 완전 공개 |
| 프론트엔드 → 백엔드 API | `https://ashajisu.duckdns.org` (443) | 완전 공개 (CORS로 Origin 제한) |
| 개발자 (집) → DB | 3306 | 홈 IP `/32` 등록 (변경 시 수동 갱신 또는 SSH 터널 사용) |
| 개발자 (집) → 서버 | 22 (SSH) | 홈 IP `/32` 등록 |
| 백엔드 애플리케이션 (9090) | 외부 직접 노출 없음, Nginx 경유만 |

> 홈 IP 변경 시 대응 절차는 별도 문서 `db-network-access-guide.md` 참고.

---

## 6. 마무리 작업 (진행 예정/확인 필요)

- [ ] 노트북의 WinSW 백엔드 서비스 제거
- [ ] 노트북의 ngrok 프로세스 및 자동 실행 등록 제거
- [ ] 오래된 jar 파일 정리 정책 수립 (디스크 용량 관리)
- [ ] DB 정기 백업 스크립트 구성 (mysqldump + cron)

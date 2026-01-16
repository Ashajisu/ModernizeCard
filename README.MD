## 📌Git 관리 가이드

### 원격 저장소
* **원격경로**: http://github.com/jisu-arisys/ModernizeCard.git

<br>

### 버전관리

jdk : ms-21 (언어수준 17) 

<br>

### 프론트앤드 git pages 배포

.env 파일의 VITE_API_URL="https://doris-aquicultural-adequately.ngrok-free.dev" 경로 확인

```
cd modernize
npm run build
npm run deploy
```
빌드, .nojekyll 폴더생성, 배포
배포에 2분 정도 소요됩니다.
https://github.com/jisu-arisys/ModernizeCard/deployments 에서 Upates 완료여부를 확인하고
https://jisu-arisys.github.io/ModernizeCard/ 경로로 접속합니다. 쿠키를 지우고 다시 접속합니다.


### 윈도우에서 백엔드 항상 켜두기
build.gradle 버전변경
bootJar 실행
/build/libs/ModernizeCard-0.0.x.jar 파일 이동 > OneDrive/hab/ModernizeCard

윈도우에서
OneDrive/hab/ModernizeCard > D:\WinSW\ModernizeCard-0.0.x.jar 파일 이동
D:\WinSW\modernizecard.xml 실행할 파일 arguments 이름 수정  

윈도우 관리자 CMD 에서
D:
cd WinSW
modernizecard.exe stop
sc delete modernizecard (modernizecard.exe uninstall 안됨)
서비스 > 동작 > 새로고침 이후 modernizecard 서비스 사라진것 확인

modernizecard.exe install
sc start modernizecard
서비스 > 동작 > 새로고침 이후 modernizecard 서비스 등록 및 실행중 확인
-2026.1.27

```window CMD

cd C:\Users\USER\OneDrive - 주식회사 아리시스\hab\ModernizeCard
C:\Java64\jdk-21\bin\javaw -jar ./ModernizeCard-0.0.1.jar > app_yyyymmdd.log 2>&1
```

```window 포워딩
관리자권한 cmd
ngrok http 9090
```
이러면 백엔드 서버가 백그라운드에서 계속 동작합니다.


### 윈도우에서 백엔드 항상 켜두기
노트북에서 터미널 닫아도 계속 실행하려면 pm2 같은 프로세스 매니저를 씁니다:
```bash
npm install -g pm2
pm2 start server.js
pm2 save
pm2 startup
```

```mac

cd /Users/jisu/Desktop/hab
nohup java -jar ./ModernizeCard-0.1.jar > app_yyyymmdd.log 2>&1 &
```

```mac 포워딩
ngrok http 9090
```

### 인증서 90일 간격 갱신

```
certbot renew --quiet
```
갱신 후에는 백엔드 재시작이 필요합니다.

### 공인된 인증서 발급

ashajisu.iptime.org:80 경로가 접속 가능해야함.
먼저 ipTime 포트포워딩으로 80 접속을 허용하기.

```
brew install cerbot
sudo certbot certonly --standalone -d ashajisu.iptime.org
계정비밀번호
라이센스동의
```

## 테이블 생성시 collation 주의
테이블 자동생성시 '데이터 정렬'방법이 'utf8mb4_general_ci' 가 아니면 뷰 생성시
Illegal mix of collations for operation 'UNION' 이 발생합니다.
테이블의 정렬방법 변경시 각 필드의 정렬방법 옵션을 지워줘야합니다.


## 분개
계좌(account) → 전표(voucher) → 분개(entry) 관계


## 전표 구분
TRANSFER
계좌 ↔ 계좌
급여, 송금, 자동이체, 계좌이동
분개: 계좌 A 차변 / 계좌 B 대변

CARD
카드 승인 발생 시점
지출 확정, 부채 증가
분개: 비용 or 자산 감소 / 카드부채 증가

CARD_PAYMENT
카드 대금 결제일
부채 감소 + 계좌 감소
분개: 카드부채 차변 / 계좌 대변

CASH
현금 직접 사용
분개: 비용 or 자산 / 현금 감소

ADJUST
잔액 보정, 이월, 초기잔액
분개: 계좌 / 조정계정


## 성능 향상을 위한 필수 인덱스
CREATE INDEX idx_entry_account ON ledger.entry(account_id);
CREATE INDEX idx_entry_voucher ON ledger.entry(voucher_id);
CREATE INDEX idx_voucher_date ON ledger.voucher(voucher_date);
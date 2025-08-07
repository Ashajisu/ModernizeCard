## 📌Git 관리 가이드

### 원격 저장소
* **원격경로**: http://github.com/jisu-arisys/ModernizeCard.git

### 브랜치 구조
1. **main**: 최종 산출물

### 커밋 컨벤션
- **feat**: 새로운 기능 추가 및 새로운 페이지 추가
- **fix**: 버그 수정
- **docs**: 문서 수정
- **style**: 코드 포맷팅, 세미콜론 누락 등 (코드 변경 없음)
- **refactor**: 코드 리팩토링
- **chore**: 유지보수 및 기타 잡다한 작업 시 사용(코드를 수정했지만 기능 변경이 없는 경우)  

**예시:)**
```bash
git commit -m "feat: 활성 통화 건수 모니터링 페이지 개발 #4"
git commit -m "fix: 모니터링페이지 버그 수정"
git commit -m "docs: 모니터링페이지 문서 수정"
git commit -m "style: 모니터링페이지 코드 포맷팅, 세미콜론 누락 등 (코드 변경 없음)"
git commit -m "refactor: 모니터링페이지 코드 리팩토링"
git commit -m "chore: 유지보수 및 기타 잡다한 작업 시 사용(코드를 수정했지만 기능 변경이 없는 경우)"
```

<br>
<br>
## 📌Git 관리 가이드

### 원격 저장소
* **원격경로**: http://github.com/jisu-arisys/ModernizeCard.git

<br>
<br>

### 백엔드 항상 켜두기

노트북에서 터미널 닫아도 계속 실행하려면 pm2 같은 프로세스 매니저를 씁니다:
```bash
npm install -g pm2
pm2 start server.js
pm2 save
pm2 startup
```

이러면 백엔드 서버가 백그라운드에서 계속 동작합니다.

## 📌Git 관리 가이드

### 원격 저장소
* **원격경로**: http://github.com/jisu-arisys/ModernizeCard.git

<br>
<br>

### 프론트앤드 git pages 배포
```
cd modernize
npm run deploy
```
빌드, .nojekyll 폴더생성, 배포
배포에 2분 정도 소요됩니다.
https://github.com/jisu-arisys/ModernizeCard/deployments 에서 Upates 완료여부를 확인하고
https://jisu-arisys.github.io/ModernizeCard/ 경로로 접속합니다.

### 백엔드 항상 켜두기

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

```window

cd /원드라이브/hab/ModernizeCard
start java -jar ./ModernizeCard-0.0.1.jar > app_yyyymmdd.log 2>&1
```

```window 포워딩
ngrok http 9090
```
이러면 백엔드 서버가 백그라운드에서 계속 동작합니다.

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


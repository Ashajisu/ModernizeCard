
## git 관리
[ Zoom 프로젝트 git관리에 대해 공유드립니다. ]
* 원격경로 : http://192.168.1.107:3000/dev.lab/zoom.git   
1. main : 최종 산출물. dev 작업물에 대해 팀장님 검토 후 main 으로 병합할 예정입니다.
2. dev : 중간 취합. dev 를 기준으로 각자 브랜치를 만드시고 작업하시면 됩니다. 중간 완성물은 이 브랜치에 PR 보내시고 zoom 으로 알려주시면 merge 하겠습니다.
3. 개인 브랜치 : 각자 페이지 작업하시면 됩니다.
   1. 내 작업 브랜치로 이동 (체크아웃)
   ```
   git checkout /NAME/WORK
   ```
   2. 내 브랜치에 원격 dev 의 최신 내용 rebase 로 반영 ( 깃 > 리베이스 origin/dev > 충돌 발생하면 직접 해결해야 함. )
   ```
   git fetch origin            # 원격 최신 상태 가져오기
   git rebase origin/dev       # 내 브랜치에 rebase 적용
   ```
   3. 내 브랜치 내용 원격에 반영
   ```
   git push --force-with-lease
   ```
   4. Gitea 웹에서 PR을 생성
      브라우저에서 원격경로 접속 > New Pull Request 클릭 > base: dev, compare: MY/WORK 선택 후 PR 생성
   5. ![img_1.png](img_1.png)

## route 관리
[ 페이지 라우트 관련 공지 ]
* 경로 : 프로젝트명\modernize\src\router
* 파일명 : 예시 UmjisuRoutes.ts
* 추가할 경로 : 프로젝트명\modernize\src\router\index.ts

1. 해당경로에 각자의 ts 파일을 생성하시고,
2. index.ts 파일의 17번줄부터 추가하시면 됩니다.
3. 탭 이름은 route 페이지의 name 값이 출력됩니다.


## sidebarItem 관리
1. route 에 추가된 경로만
2. route.ts 파일의 path 값을 sidebarItem.ts 의 to 값에 매칭합니다.
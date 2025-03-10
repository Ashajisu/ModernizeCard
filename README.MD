
## git 관리
[ Zoom 프로젝트 git관리에 대해 공유드립니다. ]
* 원격경로 : http://192.168.1.107:3000/dev.lab/zoom.git   
1. main : 최종 산출물. main을 기준으로 각자 브랜치를 만드시고 작업하시면 됩니다.
2. dev : 중간 취합. 중간작업물은 이 브랜치에 push 하시면 됩니다. 오류가 없으면 팀장님 검토 후 main으로 보낼 예정입니다.
   - 
3. 개인 브랜치 : 각자 페이지 작업하시면 됩니다.

## route 관리
[ 페이지 라우트 관련 공지 ]
* 경로 : 프로젝트명\modernize\src\router
* 파일명 : 예시 UmjisuRoutes.ts
* 추가할 경로 : 프로젝트명\modernize\src\router\index.ts

1. 해당경로에 각자의 ts 파일을 생성하시고,
2. index.ts 파일의 17번줄부터 추가하시면 됩니다.


## sidebarItem 관리
1. route 에 추가된 경로만
2. route.ts 파일의 path 값을 sidebarItem.ts 의 to 값에 매칭합니다.
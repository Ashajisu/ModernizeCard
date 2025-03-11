## 🧭목차
[📌 Git 관리 가이드](#git-관리-가이드)   
[📌 Route 관리 가이드](#route-관리-가이드)  
[📌 SidebarItem 관리 가이드](#sidebaritem-관리-가이드)


## 📌Git 관리 가이드

> 💡 Zoom 프로젝트 git 컨벤션에 대해 공유드립니다.

### 원격 저장소
* **원격경로**: http://192.168.1.107:3000/dev.lab/zoom.git

### 브랜치 구조
1. **main**: 최종 산출물
   - dev 작업물에 대해 팀장님 검토 후 main으로 병합 예정

2. **dev**: 중간 취합
   - dev를 기준으로 각자 브랜치 생성
   - 중간 완성물은 이 브랜치에 PR 요청 후 zoom으로 알려주시면 merge 진행

3. **개인 브랜치**: 개인 개발 페이지 작업 브랜치
   - 개인 브랜치 생성 후 개발 후 원격에 반영
   - 머지가 완료된 브랜치는 리모트에서 삭제

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

### 작업 흐름

#### 1. 개인브랜치 생성 및 이동
```bash
git checkout -b /NAME/WORK/PAGE
```
#### 2. 원격 dev 최신 내용 반영 (충돌 없는 경우)
```bash
# 원격 최신 상태 가져오기
git fetch origin

# 내 브랜치에 rebase 적용
git rebase origin/dev

# 충돌이 없는 경우 (정상 진행)
- 충돌이 없으면 자동으로 리베이스가 완료됩니다
- 다음과 같은 메시지가 표시됩니다
    "Successfully rebased and updated refs/heads/YOUR_BRANCH."

# 충돌이 발생한 경우
# 충돌 파일 확인
git status

# 충돌 파일 수정 (에디터로 직접 수정)

# 수정 완료 후 파일 추가
git add .

# 리베이스 계속 진행
git rebase --continue

```
> 💡"git rebase --abort" 옵션을 이용해서 리베이스를 취소 할 수 있습니다.  
> 보통 충돌 해결하다 잘못 적용 한거 같을때 다시 리베이스 하기 전으로 원래대로 돌리고 싶을때 사용합니다.  
> 그리고 다시 "git rebase origin/dev" 하여 다시 처음부터 진행하면 됩니다.

#### 3. 나의 작업 브랜치 내용 원격에 반영(리베이스)
```bash
git push --force-with-lease
```
> 옵션 설명 "--force-with-lease" : "강제 푸시"를 하되, 내가 마지막으로 가져온 원격 브랜치 상태와 현재 원격 브랜치 상태가 일치하는지 확인한 후 푸시
>

####  4. PR 생성
- 브라우저에서 원격경로 접속
- New Pull Request 클릭
- base: dev, compare: MY/WORK 선택 후 PR 생성

![img_1.png](img_1.png)

<br>
<br>

## 📌route 관리 가이드
> 💡[ Zoom 프로젝트 vue route 관리에 대해 공유드립니다. ]
[ 페이지 라우트 관련 공지 ]
* 경로 : 프로젝트명\modernize\src\router
* 파일명 : 예시 UmjisuRoutes.ts
* 추가할 경로 : 프로젝트명\modernize\src\router\index.ts

1. 해당경로에 각자의 ts 파일을 생성하시고,
2. index.ts 파일의 17번줄부터 추가하시면 됩니다.
3. 탭 이름은 route 페이지의 name 값이 출력됩니다.

<br>
<br>

## 📌sidebarItem 관리 가이드
> 💡[ Zoom 프로젝트 sidebarItem 관리에 대해 공유드립니다. ]
1. route 에 추가된 경로만
2. route.ts 파일의 path 값을 sidebarItem.ts 의 to 값에 매칭합니다.
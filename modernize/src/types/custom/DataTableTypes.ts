type UserItem = {
    department: string; // 부서명
    team: string; // 팀명
    username: string; // 사용자명
    employeeId: string; // 사원번호
    position: string; // 직위
    email: string; // 메일주소
    phone?: string; // 전화번호 (옵션)
    mobile?: string; // 내선번호 (옵션)
    hireDate?: string; // 입사일자
    employmentStatus?: string; // 재직상태
    userRole?: string; // 사용자 권한
    activeStatus?: string; // 사용 유무
    zoomLicense?: string; // 줌 라이센스
    phoneLicense?: string; // 줌폰 라이센스
};

type RecordingItem = {
    type: string;       // 유형 (전수, 선택 등)
    direction: string;  // 구분 (인바운드, 아웃바운드)
    sender: string;     // 발신자 번호
    receiver: string;   // 수신자 번호
    department: string; // 부서
    team: string;       // 팀
    username: string;       // 사용자
    startTime: string;  // 통화 시작 시간
    endTime: string;    // 통화 종료 시간
    duration: string;   // 통화 시간
    play: string;       // 재생 (아이콘 버튼)
    download: string;   // 다운로드 (아이콘 버튼)
    id: string;         // ID (아이콘 버튼)
}

export type { UserItem, RecordingItem };
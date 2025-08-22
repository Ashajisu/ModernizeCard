type searchDialogType = {
    title: string;
    sub: object;
};

export type usageChart = {
    lable: string;
    cost: string;
    ratio: string;
};

export type StatsItem = {
    id: string;
    title: string;
    stat1: string;
    stat2: string;
    stat3: string;
    stat4: string;
    stat5: string;
    stat6: string;
    [key: string]: string;
};

export type SSCardItem = {
    id: string;
    transactionDate: string;
    cardType: string;
    usedCard: string;
    merchantName: string;
    approvalNumber: string;
    amount: string;
    purchaseType: string;
    usageType: string;
    currency: string;
    paymentDate: string;
    [key: string]: string;
};

export type SHCardItem = {
    id: string;
    transactionDate: string;
    cardType: string;
    usedCard: string;
    merchantName: string;
    approvalNumber: string;
    amount: string;
    purchaseType: string;
    usageType: string;
    currency: string;
    paymentDate: string;
    [key: string]: string;
};

type UserItem = {
    department: string; // 부서명
    team: string; // 팀명
    username: string; // 사용자명
    employeeId: string; // 사원번호
    position: string; // 직위
    email: string; // 메일주소
    phone: string; // 전화번호 (옵션)
    mobile: string; // 내선번호 (옵션)
    hireDate: string; // 입사일자
    employmentStatus: string; // 재직상태
    userRole: string; // 사용자 권한
    activeStatus: string; // 사용 유무
    zoomLicense: string; // 줌 라이센스
    phoneLicense: string; // 줌폰 라이센스
    // 인덱스 시그니처 추가
    [key: string]: string;
};

type ZoomPhoneItem = {
    department: string;  // 부서명
    team: string;        // 팀명
    username: string;    // 사용자명
    employeeId: string;  // 사원번호
    position: string; // 직위
    mobile: string; // 내선번호
    pstnNumber: string;  // PSTN 번호
    pullNumber: string;    // 당겨받기
    ipPhone: string;     // IP 전화기
    zoomLicense: string; // 줌폰 라이센스
    callRecording: string; // 통화녹음
    // 인덱스 시그니처 추가
    [key: string]: string;
};


type RecordingItem = {
    type: string;       // 유형 (전수, 선택 등)
    direction: string;  // 구분 (인바운드, 아웃바운드)
    caller_number: string;     // 발신자 번호
    callee_number: string;   // 수신자 번호
    department: string; // 부서
    team: string;       // 팀
    caller_name: string;       // 발신자 이름
    callee_name: string;       // 수신자 이름
    owner: string; //사용자이름
    startTime: string;  // 통화 시작 시간
    endTime: string;    // 통화 종료 시간
    duration: string;   // 통화 시간
    play: string;       // 재생할 음성파일 경로. public/audio/...
    download: string;   // 다운로드 (아이콘 버튼)
    id: string;         // ID (아이콘 버튼)
    // 인덱스 시그니처 추가
    [key: string]: string;
}

type RecordChat = {
    recordingId: string;
    recordingStart: string;
    recordingEnd: string;
    userId: string;
    chatHistory: {
        text: string;
        ts: string;
        end_ts: string;
        userId: string;
        username: string;
    }[];
};

type ExtensionItem = {
    number: string;
    provision: string;
    assignment: string;
    // 인덱스 시그니처 추가
    [key: string]: string;
};

type PstnItem = {
    id: string;
    number: string;
    product: string;
    user: string;
    sipGroup: string;
    country: string;
    incoming: string;
    outgoing: string;
    // 인덱스 시그니처 추가
    [key: string]: string;
};

export type { searchDialogType, UserItem, ZoomPhoneItem, RecordingItem, RecordChat, ExtensionItem, PstnItem };
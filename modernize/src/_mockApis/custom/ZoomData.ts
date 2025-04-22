import type {UserItem, ZoomPhoneItem, RecordingItem, RecordChat, searchDialogType} from '@/types/custom/DataTableTypes.ts'

const UsernameList:searchDialogType[] = [
    {
        title: '박00',
        sub : {
            username: '박00',
            team: "기술1팀",
            employeeId: "20019133",
        }
    },
    {
        title: '이OO',
        sub : {
            username: '이OO',
            team: "기술2팀",
            employeeId: "20019133",
        }
    },
    {
        title: '최OO',
        sub : {
            username: '최OO',
            team: "SPM팀",
            employeeId: "20019133",
        }
    },
    {
        title: '전OO',
        sub : {
            username: '전OO',
            team: "기술1팀",
            employeeId: "20019133",
        }
    },
]
// 사용자 데이터 리스트
const UserDataTables: UserItem[] = [
    {
        department: "기술팀",
        team: "기술1팀",
        username: "박OO",
        employeeId: "20019133",
        position: "사원",
        email: "happy@arisys.co.kr",
        phone: "",
        mobile: "",
        hireDate: "2021-05-01",
        employmentStatus: "재직",
        userRole: "사용자",
        activeStatus: "Y",
        zoomLicense: "Basic",
        phoneLicense: "",
    },
    {
        department: "기술팀",
        team: "기술2팀",
        username: "이OO",
        employeeId: "20355071",
        position: "대리",
        email: "good@arisys.co.kr",
        phone: "02-1234-5678",
        mobile: "010-1234-5678",
        hireDate: "2020-02-10",
        employmentStatus: "재직",
        userRole: "관리자",
        activeStatus: "Y",
        zoomLicense: "WorkplaceBiz",
        phoneLicense: "Phone Pro",
    },
    {
        department: "고객지원본부",
        team: "SPM팀",
        username: "박OO",
        employeeId: "20415072",
        position: "수석",
        email: "love@arisys.co.kr",
        phone: "02-9876-5432",
        mobile: "010-9876-5432",
        hireDate: "2019-08-15",
        employmentStatus: "재직",
        userRole: "사용자",
        activeStatus: "Y",
        zoomLicense: "WorkplaceBiz",
        phoneLicense: "Phone Pro",
    },
    {
        department: "기술팀",
        team: "기술1팀",
        username: "전OO",
        employeeId: "20680414",
        position: "과장",
        email: "joy@arisys.co.kr",
        phone: "02-1122-3344",
        mobile: "010-1122-3344",
        hireDate: "2017-11-20",
        employmentStatus: "재직",
        userRole: "슈퍼 관리자",
        activeStatus: "Y",
        zoomLicense: "WorkplaceBiz",
        phoneLicense: "Phone Pro, Power",
    },
];

const ZoomPhoneDataTables: ZoomPhoneItem[] = [
    {
        department: '영업팀',
        team: '영업1팀',
        username: '박OO',
        employeeId: '20019133',
        position: '사원',
        mobile: '02-2177-8151',
        pstnNumber: '•••',
        pullNumber: '영업2팀',
        ipPhone: 'Avaya J159_308152',
        zoomLicense: 'Phone Pro',
        callRecording: '선택녹취',
    },
    {
        department: '기술팀',
        team: '기술2팀',
        username: '이OO',
        employeeId: '20355071',
        position: '사원',
        mobile: '02-2177-8152',
        pstnNumber: '•••',
        pullNumber: '기술2팀',
        ipPhone: 'Avaya J159_308152',
        zoomLicense: 'Basic',
        callRecording: '선택녹취',
    },
    {
        department: '고객지원본부',
        team: 'SPM팀',
        username: '최OO',
        employeeId: '20415072',
        position: '사원',
        mobile: '02-2177-8153',
        pstnNumber: '•••',
        pullNumber: '기술2팀',
        ipPhone: 'Avaya J159_308152',
        zoomLicense: 'Phone Pro',
        callRecording: '전수녹취',
    },
    {
        department: '기술팀',
        team: '기술지원팀',
        username: '전OO',
        employeeId: '20680414',
        position: '사원',
        mobile: '02-2177-8154',
        pstnNumber: '•••',
        pullNumber: '기술2팀',
        ipPhone: 'Avaya J159_308152',
        zoomLicense: 'Phone Pro',
        callRecording: '미사용',
    },
];


const RecordingDataTables : RecordingItem[] = [
    {
        type: "유선",
        direction: "아웃바운드",
        caller_number: "8131",
        callee_number: "+82215889999",
        department: "고객지원본부",
        team: "기술1팀",
        owner: "박○○",
        caller_name: "박○○",
        callee_name:"82215889999",
        startTime: "2025-03-15T18:01",
        endTime: "2025-03-15T18:01",
        duration: "00:02:26",
        play: '/public/assets/audio/jM9ZLs9mQnqnONVfbMytdQ.wav',
        download: "연결",
        id: "12c81c9c8c674565a985dbdd33d652fa",
    },
    {
        type: "내선",
        direction: "인바운드",
        caller_number: "8131",
        callee_number: "+82221778100",
        department: "고객지원본부",
        team: "SPM팀",
        owner: "최○○",
        caller_name: "최○○",
        callee_name: "82221778100",
        startTime: "2025-03-15T18:01",
        endTime: "2025-03-15T18:01",
        duration: "00:00:25",
        play: '/public/assets/audio/fOfOImeRTmyulCXGN350hQ.wav',
        download: "미응답",
        id: "98c5bde553bd4594ace4ea6765ae0359",
    },
    {
        type: "모바일",
        direction: "인바운드",
        caller_number: "02-XXXX-XXXX",
        callee_number: "308153",
        department: "고객지원본부",
        team: "SPM팀",
        owner:"엄○○",
        caller_name: "xxx",
        callee_name: "엄○○",
        startTime: "2024-01-05T10:01",
        endTime: "2024-01-05T10:02",
        duration: "00:00:58",
        play: '/public/assets/audio/test.wav',
        download: "연결",
        id: "a2005990cf824105976dbecbb73e6c5c",
    },
];

const RecordChatExample: RecordChat[] = [
    {
        recordingId: "98c5bde553bd4594ace4ea6765ae0359",
        recordingStart: "2025-04-17T08:53:46Z",
        recordingEnd: "2025-04-17T08:53:53Z",
        userId: "8131",
        chatHistory: [
            {
                text: "그니까",
                ts: "00:00:00.000",
                end_ts: "00:00:00.730",
                userId: "82221778100",
                username: "82221778100"
            },
            {
                text: "주식회사 아리시스입니다.",
                ts: "00:00:00.920",
                end_ts: "00:00:02.830",
                userId: "82221778100",
                username: "82221778100"
            },
            {
                text: "경영지원부는 일반 영업부서.",
                ts: "00:00:03.210",
                end_ts: "00:00:05.639",
                userId: "82221778100",
                username: "82221778100"
            },
            {
                text: "이번 기술직.",
                ts: "00:00:05.640",
                end_ts: "00:00:06.930",
                userId: "8131",
                username: "박종승"
            },
            {
                text: "아니 이.",
                ts: "00:00:06.939",
                end_ts: "00:00:07.379",
                userId: "82221778100",
                username: "82221778100"
            }
        ]
    },
    {
        recordingId: "12c81c9c8c674565a985dbdd33d652fa",
        recordingStart: "2025-04-17T08:11:32Z",
        recordingEnd: "2025-04-17T08:11:39Z",
        userId: "8131",
        chatHistory: [
            {
                text: "감사합니다",
                ts: "00:00:01.300",
                end_ts: "00:00:02.110",
                userId: "82215889999",
                username: "82215889999"
            },
            {
                text: "국민의 평생금융파트너 table.",
                ts: "00:00:04.170",
                end_ts: "00:00:07.299",
                userId: "82215889999",
                username: "82215889999"
            },
        ]
    },
    {
        recordingId: "a2005990cf824105976dbecbb73e6c5c",
        recordingStart: "2025-04-17T08:11:32Z",
        recordingEnd: "2025-04-17T08:11:39Z",
        userId: "8131",
        chatHistory: [
            {
                text: "안녕하세요",
                ts: "00:00:00.000",
                end_ts: "00:00:01.110",
                userId: "82215889999",
                username: "82215889999"
            },
            {
                text: "아리시스 연구개발팀, 엄지수입니다.",
                ts: "00:00:01.110",
                end_ts: "00:00:04.170",
                userId: "82215889999",
                username: "82215889999"
            },
            {
                text: "다시 전화드릴게요.",
                ts: "00:00:04.170",
                end_ts: "00:00:06.299",
                userId: "82215889999",
                username: "82215889999"
            },
            {
                text: "자세한 문의사항은 메일로 남겨주세요.",
                ts: "00:00:07.299",
                end_ts: "00:00:10.000",
                userId: "82215889999",
                username: "82215889999"
            },
        ]
    },
];

export { UsernameList, UserDataTables, ZoomPhoneDataTables, RecordingDataTables, RecordChatExample };

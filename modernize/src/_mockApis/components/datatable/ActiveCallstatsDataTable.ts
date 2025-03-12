type Datatables = {
    id: string;
    callType: string;
    caller: string;
    receiver: string;
    callDuration: string;
    recordType: string;
};

const OutboundCallDatatables: Datatables[] = [
    {
        id: '1',
        callType: '아웃바운드 (외부 통화 데이터 구분용 텍스트)',
        caller: '308151',
        receiver: '010-XXXX-XXXX',
        callDuration: '00:00:36',
        recordType: 'Y'
    },
    {
        id: '2',
        callType: '아웃바운드',
        caller: '308152',
        receiver: '02-XXXX-XXXX',
        callDuration: '00:03:05',
        recordType: 'N'
    },
    {
        id: '3',
        callType: '인바운드',
        caller: '010-XXXX-XXXX',
        receiver: '308153',
        callDuration: '00:00:46',
        recordType: 'N'
    },
    {
        id: '4',
        callType: '인바운드',
        caller: '031-XXX-XXXX',
        receiver: '308154',
        callDuration: '00:01:17',
        recordType: 'N'
    },
    {
        id: '5',
        callType: '아웃바운드',
        caller: '308155',
        receiver: '010-XXXX-XXXX',
        callDuration: '00:02:43',
        recordType: 'Y'
    },
    {
        id: '6',
        callType: '인바운드',
        caller: '02-XXXX-XXXX',
        receiver: '308156',
        callDuration: '00:04:21',
        recordType: 'Y'
    },
    {
        id: '7',
        callType: '아웃바운드',
        caller: '308157',
        receiver: '031-XXX-XXXX',
        callDuration: '00:01:08',
        recordType: 'N'
    },
    {
        id: '8',
        callType: '인바운드',
        caller: '010-XXXX-XXXX',
        receiver: '308158',
        callDuration: '00:00:59',
        recordType: 'Y'
    },
    {
        id: '9',
        callType: '아웃바운드',
        caller: '308159',
        receiver: '02-XXXX-XXXX',
        callDuration: '00:03:47',
        recordType: 'Y'
    },
    {
        id: '10',
        callType: '인바운드',
        caller: '070-XXXX-XXXX',
        receiver: '308160',
        callDuration: '00:02:15',
        recordType: 'N'
    },
    {
        id: '11',
        callType: '아웃바운드',
        caller: '308161',
        receiver: '010-XXXX-XXXX',
        callDuration: '00:01:23',
        recordType: 'Y'
    },
    {
        id: '12',
        callType: '인바운드',
        caller: '02-XXXX-XXXX',
        receiver: '308162',
        callDuration: '00:04:15',
        recordType: 'N'
    },
    {
        id: '13',
        callType: '아웃바운드',
        caller: '308163',
        receiver: '031-XXX-XXXX',
        callDuration: '00:02:38',
        recordType: 'Y'
    },
    {
        id: '14',
        callType: '인바운드',
        caller: '010-XXXX-XXXX',
        receiver: '308164',
        callDuration: '00:00:47',
        recordType: 'N'
    },
    {
        id: '15',
        callType: '아웃바운드',
        caller: '308165',
        receiver: '02-XXXX-XXXX',
        callDuration: '00:03:22',
        recordType: 'Y'
    },
    {
        id: '16',
        callType: '인바운드',
        caller: '070-XXXX-XXXX',
        receiver: '308166',
        callDuration: '00:01:55',
        recordType: 'N'
    },
   
    
];

const ExtensionCallDataTables: Datatables[] = [
    {
        id: '1',
        callType: '내선 통화 (내선 통화 데이터 구분용 텍스트)',
        caller: '308151',
        receiver: '010-XXXX-XXXX',
        callDuration: '00:00:36',
        recordType: 'Y'
    },
    {
        id: '2',
        callType: '아웃바운드',
        caller: '308152',
        receiver: '02-XXXX-XXXX',
        callDuration: '00:03:05',
        recordType: 'N'
    },
    {
        id: '3',
        callType: '인바운드',
        caller: '010-XXXX-XXXX',
        receiver: '308153',
        callDuration: '00:00:46',
        recordType: 'N'
    },
    {
        id: '4',
        callType: '인바운드',
        caller: '031-XXX-XXXX',
        receiver: '308154',
        callDuration: '00:01:17',
        recordType: 'N'
    },
    {
        id: '5',
        callType: '아웃바운드',
        caller: '308155',
        receiver: '010-XXXX-XXXX',
        callDuration: '00:02:43',
        recordType: 'Y'
    },
    {
        id: '6',
        callType: '인바운드',
        caller: '02-XXXX-XXXX',
        receiver: '308156',
        callDuration: '00:04:21',
        recordType: 'Y'
    },
    {
        id: '7',
        callType: '아웃바운드',
        caller: '308157',
        receiver: '031-XXX-XXXX',
        callDuration: '00:01:08',
        recordType: 'N'
    },
    {
        id: '8',
        callType: '인바운드',
        caller: '010-XXXX-XXXX',
        receiver: '308158',
        callDuration: '00:00:59',
        recordType: 'Y'
    },
    {
        id: '9',
        callType: '아웃바운드',
        caller: '308159',
        receiver: '02-XXXX-XXXX',
        callDuration: '00:03:47',
        recordType: 'Y'
    },
    {
        id: '10',
        callType: '인바운드',
        caller: '070-XXXX-XXXX',
        receiver: '308160',
        callDuration: '00:02:15',
        recordType: 'N'
    },
    {
        id: '11',
        callType: '아웃바운드',
        caller: '308161',
        receiver: '010-XXXX-XXXX',
        callDuration: '00:01:23',
        recordType: 'Y'
    },
    {
        id: '12',
        callType: '인바운드',
        caller: '02-XXXX-XXXX',
        receiver: '308162',
        callDuration: '00:04:15',
        recordType: 'N'
    }
]

const CurrentCallDataTables: Datatables[] = [
    {
        id: '1',
        callType: '전체 통화 콜 수 (전체 통화 콜 수 데이터 구분용 텍스트)',
        caller: '308151',
        receiver: '010-XXXX-XXXX',
        callDuration: '00:00:36',
        recordType: 'Y'
    },
    {
        id: '2',
        callType: '아웃바운드',
        caller: '308152',
        receiver: '02-XXXX-XXXX',
        callDuration: '00:03:05',
        recordType: 'N'
    },
    {
        id: '3',
        callType: '인바운드',
        caller: '010-XXXX-XXXX',
        receiver: '308153',
        callDuration: '00:00:46',
        recordType: 'N'
    },
    {
        id: '4',
        callType: '인바운드',
        caller: '031-XXX-XXXX',
        receiver: '308154',
        callDuration: '00:01:17',
        recordType: 'N'
    },
    {
        id: '5',
        callType: '아웃바운드',
        caller: '308155',
        receiver: '010-XXXX-XXXX',
        callDuration: '00:02:43',
        recordType: 'Y'
    },
    {
        id: '6',
        callType: '인바운드',
        caller: '02-XXXX-XXXX',
        receiver: '308156',
        callDuration: '00:04:21',
        recordType: 'Y'
    },
    {
        id: '7',
        callType: '아웃바운드',
        caller: '308157',
        receiver: '031-XXX-XXXX',
        callDuration: '00:01:08',
        recordType: 'N'
    },
    {
        id: '8',
        callType: '인바운드',
        caller: '010-XXXX-XXXX',
        receiver: '308158',
        callDuration: '00:00:59',
        recordType: 'Y'
    },
    {
        id: '9',
        callType: '아웃바운드',
        caller: '308159',
        receiver: '02-XXXX-XXXX',
        callDuration: '00:03:47',
        recordType: 'Y'
    },
    {
        id: '10',
        callType: '인바운드',
        caller: '070-XXXX-XXXX',
        receiver: '308160',
        callDuration: '00:02:15',
        recordType: 'N'
    },
    {
        id: '11',
        callType: '아웃바운드',
        caller: '308161',
        receiver: '010-XXXX-XXXX',
        callDuration: '00:01:23',
        recordType: 'Y'
    },
    {
        id: '12',
        callType: '인바운드',
        caller: '02-XXXX-XXXX',
        receiver: '308162',
        callDuration: '00:04:15',
        recordType: 'N'
    },
    {
        id: '13',
        callType: '아웃바운드',
        caller: '308163',
        receiver: '031-XXX-XXXX',
        callDuration: '00:02:38',
        recordType: 'Y'
    },
    {
        id: '14',
        callType: '인바운드',
        caller: '010-XXXX-XXXX',
        receiver: '308164',
        callDuration: '00:00:47',
        recordType: 'N'
    },
    {
        id: '15',
        callType: '아웃바운드',
        caller: '308165',
        receiver: '02-XXXX-XXXX',
        callDuration: '00:03:22',
        recordType: 'Y'
    },
    {
        id: '16',
        callType: '인바운드',
        caller: '070-XXXX-XXXX',
        receiver: '308166',
        callDuration: '00:01:55',
        recordType: 'N'
    },
]

export {OutboundCallDatatables,CurrentCallDataTables,ExtensionCallDataTables};
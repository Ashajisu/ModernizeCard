type Menu = {
    id: string;
    menuid: string;
    main: string;
    url: string;
    sub: string;
    imagename: string;
    sortid: string;
    selectable: boolean;
};

type Code = {
    id: string;
    codename: string;
    maincode: string;
    etccode1: string;
    etccode2: string;
    changecode: string;
    usingcode: string;
};

type ZoomPhone ={
    phoneName: string;
    user: string;
    phoneNum: string;
    pstnNum: string;
    phoneStatus: string;
    modelName: string;
    manufacturer: string;
    macAddress: string;
    firmware: string;
    publicAddress: string;
    privateAddress: string;
    assignmentType: string;
    assignmentTarget: string;
};

type departmentCall ={
    date: string;
    dept: string;
    team: string;
    phonecase: string;
    calltotal: string;
    totaltime: string;
    inCalltotal: string;
    inTotaltime: string;
}


export type { Menu, Code, ZoomPhone, departmentCall };
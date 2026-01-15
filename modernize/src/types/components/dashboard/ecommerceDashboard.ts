/*Payment Gatway*/
type paymentGateway = {
    img: string;
    title: string;
    subtitle: string;
    rank: string;
    disable: string;
    bgcolor: string;
};

type recentTrans = {
    title: string;
    subtitle: string;
    textcolor: string;
    boldtext: boolean;
    line: boolean;
    link: string;
    url: string;
};


export type { paymentGateway,recentTrans }
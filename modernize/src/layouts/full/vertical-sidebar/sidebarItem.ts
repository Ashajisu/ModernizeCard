import {
    //https://tabler.io/icons 참고
    ApertureIcon,
    CreditCardIcon,
    PlusMinusIcon
} from 'vue-tabler-icons';

export interface menu {
    header?: string;
    title?: string;
    icon?: any;
    to?: string;
    chip?: string;
    chipBgColor?: string;
    chipColor?: string;
    chipVariant?: string;
    chipIcon?: string;
    children?: menu[];
    disabled?: boolean;
    type?: string;
    subCaption?: string;
}
const sidebarItem: menu[] = [
    { header: 'Home' },
    {
        title: 'Dashboard',
        icon: ApertureIcon,
        chipColor: 'surface',
        chipBgColor: 'secondary',
        to: '/home/dashboard'
    },

    { header: '카드정산' },
    {
        title: '신한카드',
        icon: CreditCardIcon,
        to: '/user/profile'
    },
    {
        title: '삼성카드',
        icon: CreditCardIcon,
        to: '/user/samsung'
    },
    {
        title: '국민카드',
        icon: CreditCardIcon,
        to: '/user/kookmin'
    },
    {
        title: '우리카드',
        icon: CreditCardIcon,
        to: '/user/woori'
    },
    {
        title: '농협카드',
        icon: CreditCardIcon,
        to: '/user/nh'
    },

    { header: '지출관리' },
    {
        title: '지출전체',
        icon: PlusMinusIcon,
        to: '/user/usage'
    }
];

export default sidebarItem;

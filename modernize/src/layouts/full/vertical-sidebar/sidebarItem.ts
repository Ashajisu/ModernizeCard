import {
    //https://tabler.io/icons 참고
    ApertureIcon,
    CreditCardIcon,
    PlusMinusIcon
} from 'vue-tabler-icons';
import { 
    IconBrandLinkedin,
    IconChevronRight,
    IconMenu2,
    IconLayoutDashboard,
    IconListDetails,
    IconPinInvoke
} from '@tabler/icons-vue';


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
    },
    
    { header: '복식부기' },
    {
        title: '대차대조표',
        icon: PlusMinusIcon,
        to: '/journal/balancesheet'
    },
    {
        title: '손익계산서',
        icon: IconBrandLinkedin,
        to: '/journal/incomestatement'
    },
    {
        title: '초기잔액등록',
        icon: IconPinInvoke,
        to: '/journal/openingbalance'
    },
    {
        title: '전표 수기입력',
        icon: IconChevronRight,
        to: '/journal/manualentry'
    },
    {
        title: '전표조회',
        icon: IconMenu2,
        to: '/journal/list'
    },
    {
        title: '계정별 잔액',
        icon: IconLayoutDashboard,
        to: '/journal/accountbalance'
    },
    {
        title: '월별 예산/지출통계',
        icon: IconListDetails,
        to: '/journal/budgetstats'
    }
];

export default sidebarItem;

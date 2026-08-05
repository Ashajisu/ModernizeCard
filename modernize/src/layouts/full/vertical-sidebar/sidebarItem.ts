import {
    //https://tabler.io/icons 는 이미지만 참고
    //이 라이브러리 하위에서 고르기
    ApertureIcon,
    BrandLinkedinIcon,
    CircuitCellPlusIcon,
    CreditCardIcon,
    FocusAutoIcon,
    FolderShareIcon,
    ListDetailsIcon,
    ListIcon,
    PlusMinusIcon,
    SquareChevronRightIcon,
    WashDryAIcon
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
    {
        title: '현대카드',
        icon: CreditCardIcon,
        to: '/user/hd'
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
        icon: CircuitCellPlusIcon,
        to: '/journal/balancesheet'
    },
    {
        title: '손익계산서',
        icon: BrandLinkedinIcon,
        to: '/journal/incomestatement'
    },
    {
        title: '초기잔액등록',
        icon: FolderShareIcon,
        to: '/journal/openingbalance'
    },
    {
        title: '전표 수기입력',
        icon: SquareChevronRightIcon,
        to: '/journal/manualentry'
    },
    {
        title: '전표조회',
        icon: ListIcon,
        to: '/journal/list'
    },
    {
        title: '계정과목관리',
        icon: WashDryAIcon,
        to: '/journal/accountmanagement'
    },
    {
        title: '계정별 잔액',
        icon: FocusAutoIcon,
        to: '/journal/accountbalance'
    },
    {
        title: '월별 예산/지출통계',
        icon: ListDetailsIcon,
        to: '/journal/budgetstats'
    }
];

export default sidebarItem;

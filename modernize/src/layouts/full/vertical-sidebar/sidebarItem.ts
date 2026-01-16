import {
    //https://tabler.io/icons 참고
    ApertureIcon,
    CreditCardIcon,
    PlusMinusIcon,
    RowInsertBottomIcon,
    TableIcon,
    TablePlusIcon,
    CashBanknoteIcon,
    MathSymbolsIcon
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
    },

    { header: '잔액관리' },
    {
        title: '계정관리',
        icon: CashBanknoteIcon,
        to: '/entry/account'
    },
    {
        title: '전표조회',
        icon: RowInsertBottomIcon,
        to: '/entry/voucher'
    },
    {
        title: '보조원장',
        icon: TableIcon,
        to: "/entry/ledger/account'"
    },
    {
        title: '전체원장',
        icon: TablePlusIcon,
        to: '/entry/ledger/all'
    },
    {
        title: '대차대조표',
        icon: MathSymbolsIcon,
        to: '/entry/sheet/balance'
    },
    {
        title: '손익계산서',
        icon: PlusMinusIcon,
        to: '/entry/sheet/income'
    }
];

export default sidebarItem;

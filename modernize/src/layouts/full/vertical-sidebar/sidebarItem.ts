import {
    CircleOffIcon,
    BoxMultipleIcon,
    AppsIcon,
    FileTextIcon,
    FileDotsIcon,
    FilesIcon,
    EditIcon,
    BorderAllIcon,
    BorderHorizontalIcon,
    BorderInnerIcon,
    BorderTopIcon,
    BorderVerticalIcon,
    BoxIcon,
    AlertCircleIcon,
    LoginIcon,
    UserPlusIcon,
    RotateIcon,
    CurrencyDollarIcon,
    ChartLineIcon,
    ChartAreaIcon,
    ChartDotsIcon,
    ChartArcsIcon,
    ChartCandleIcon,
    ChartDonut3Icon,
    ChartRadarIcon,
    ShoppingCartIcon,
    ApertureIcon,
    LayoutIcon,
    HelpIcon,
    UserCircleIcon,
    BoxAlignBottomIcon,
    BoxAlignLeftIcon,
    SettingsIcon,
    ZoomCodeIcon,
    StarIcon,
    AwardIcon,
    MoodSmileIcon,
    Message2Icon,
    PointIcon,
    AppWindowIcon,
    MailIcon,
    BasketIcon,
    CalendarIcon,
    BorderStyle2Icon,
    ColumnsIcon,
    RowInsertBottomIcon,
    EyeTableIcon,
    SortAscendingIcon,
    PageBreakIcon,
    FilterIcon,
    BoxModelIcon,
    ServerIcon,
    JumpRopeIcon,
    LayoutKanbanIcon,
    PhotoAiIcon,
    SearchIcon,
    SocialIcon,
    BinaryIcon,
    BrandTidalIcon,
    HomeIcon,
    FileInfoIcon,
    AddressBookIcon,
    BellDollarIcon,
    TicketIcon,
    FileCheckIcon,
    PhoneIcon,
    CloudComputingIcon,
    CameraIcon,
    PlayerPlayIcon
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
        to: '/dashboards/modern'
    },

    { header: '사용자 및 조직' },
    {
        title: '사용자',
        icon: UserPlusIcon,
        to: '/user/profile'
    },
    {
        title: '조직',
        icon: AddressBookIcon,
        to: '/user/organization',
    },
    {
        title: '폰 사용자',
        icon: UserCircleIcon,
        to: '/user/phone'
    },

    { header: '폰' },
    {
        title: '디바이스',
        icon: Message2Icon,
        to: '/statistics/device'
    },
    {
        title: '내선번호',
        icon: PhoneIcon,
        to: '/env/extension/list'
    },
    {
        title: 'PSTN',
        icon: CloudComputingIcon,
        to: '/env/pstn/list'
    },
    {
        title: '당겨받기 그룹',
        icon: FilesIcon,
        to: '/group/general'
    },

    {
        title: '과금',
        icon: CurrencyDollarIcon,
        to: '/user/billing'
    },
    {
        title: '녹취',
        icon: PlayerPlayIcon,
        to: '/user/recording'
    },

    { header: '모니터링' },
    {
        title: '일일 누적 통화',
        icon: FilesIcon,
        to: '/monitoring/real/daily/call'
    },
    {
        title: '활성통화',
        icon: FilesIcon,
        to: '/monitoring/real/active/call'
    },
    {
        title: '일일 누적 녹취',
        icon: FilesIcon,
        to: '/monitoring/real/daily/recording'
    },
    {
        title: '활성 녹취',
        icon: FilesIcon,
        to: '/monitoring/real/active/recording'
    },

    { header: '통계'},
    {
        title: '부서별 과금 통계',
        icon: FilesIcon,
        to: '/statistics/dept/billing'
    },
    {
        title: ' 부서별 유형 통계',
        icon: FilesIcon,
        to: '/statistics/dept/call'
    },
    {
        title: '일자별 수발신 통계',
        icon: FilesIcon,
        to: '/statistics/daily/call'
    },
    {
        title: '일자별 수발신 유형별 통계',
        icon: FilesIcon,
        to: '/statistics/daily/call/type'
    },
    {
        title: '사용량 통계',
        icon: FilesIcon,
        to: '/statistics/usage'
    },
    {
        title: '전화 통화 이력',
        icon: MailIcon,
        to: '/statistics/call/history'
    },
    {
        title: '전화 최대 사용 이력',
        icon: TicketIcon,
        to: '/statistics/call/history/max'
    },

    { header: '디바이스 현황'},
    {
        title: '폰 디바이스 현황',
        icon: LayoutKanbanIcon,
        to: '/group/device'
    },
    {
        title: 'SBC 현황',
        icon: CurrencyDollarIcon,
        to: '/group/sbc/status'
    },

    { header: '라이선스'},
    {
        title: '사용 현황',
        icon: UserCircleIcon,
        to: '/monitoring/license/daily'
    },
    {
        title: '사용 내역',
        icon: HelpIcon,
        to: '/monitoring/license/monthly'
    },

    { header: '환경설정'},
    {
        title: '메뉴 관리',
        icon: LayoutIcon,
        to: '/statistics/manage/menu',
    },
    {
        title: '코드관리',
        icon: HelpIcon,
        to: '/statistics/manage/code'
    },
]

export default sidebarItem;

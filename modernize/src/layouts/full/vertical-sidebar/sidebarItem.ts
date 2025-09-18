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
        to: '/home/dashboard'
    },

    { header: '카드정산' },
    {
        title: '신한카드',
        icon: UserPlusIcon,
        to: '/user/profile'
    },
    {
        title: '삼성카드',
        icon: UserPlusIcon,
        to: '/user/samsung'
    },
    {
        title: '국민카드',
        icon: UserPlusIcon,
        to: '/user/kookmin'
    },
    {
        title: '지출전체',
        icon: UserPlusIcon,
        to: '/user/usage'
    },
]

export default sidebarItem;

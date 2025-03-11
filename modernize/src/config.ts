export type ConfigProps = {
    Sidebar_drawer: any;
    Customizer_drawer: boolean;
    mini_sidebar: boolean;
    setHorizontalLayout: boolean;
    setRTLLayout: boolean;
    actTheme: string;
    boxed: boolean;
    setBorderCard: boolean;

    maxTabs : number;
    siteName : string;
    setSearchbar : boolean;
    setThemeToggle : boolean;
    setDashboard : boolean;
};

const config: ConfigProps = {
    Sidebar_drawer: null,
    Customizer_drawer: false,
    mini_sidebar: false,
    setHorizontalLayout: false, // Horizontal layout
    setRTLLayout: false, // RTL layout
    actTheme: 'BLUE_THEME',
    boxed: false,
    setBorderCard: false,

    maxTabs : 20,
    siteName : 'arisys',
    setSearchbar : true,
    setThemeToggle : false,
    setDashboard : true,

};

export default config;

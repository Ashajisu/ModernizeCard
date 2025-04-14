import { createVuetify } from 'vuetify';
import '@mdi/font/css/materialdesignicons.css';
import { mdi } from 'vuetify/iconsets/mdi'; // Vuetify 아이콘 인식을 위한 추가
import * as components from 'vuetify/components';
import * as directives from 'vuetify/directives';
//DragScroll
import { VueDraggableNext } from 'vue-draggable-next';

// VLabs Components
import { VTimePicker } from 'vuetify/labs/VTimePicker';
import { VTreeview } from 'vuetify/labs/VTreeview';

// import { BLUE_THEME} from '@/theme/LightTheme';
import { BLUE_THEME, AQUA_THEME, PURPLE_THEME, GREEN_THEME, CYAN_THEME, ORANGE_THEME } from '@/theme/LightTheme';
import {
    DARK_BLUE_THEME,
    DARK_AQUA_THEME,
    DARK_ORANGE_THEME,
    DARK_PURPLE_THEME,
    DARK_GREEN_THEME,
    DARK_CYAN_THEME
} from '@/theme/DarkTheme';

export default createVuetify({
    components: {
        ...components,
        draggable: VueDraggableNext,
        VTimePicker,
        VTreeview
    },
    directives,

    icons: {     //전역 sort icon 적용
        defaultSet: 'mdi',
        sets: { mdi },
        aliases: {
            sortAsc: 'mdi-triangle-small-up',
            sortDesc: 'mdi-triangle-small-down',
        },
    },

    theme: {
        defaultTheme: 'BLUE_THEME',
        themes: {
            BLUE_THEME,
            AQUA_THEME,
            PURPLE_THEME,
            GREEN_THEME,
            CYAN_THEME,
            ORANGE_THEME,
            DARK_BLUE_THEME,
            DARK_AQUA_THEME,
            DARK_ORANGE_THEME,
            DARK_PURPLE_THEME,
            DARK_GREEN_THEME,
            DARK_CYAN_THEME
        }
    },
    defaults: {
        VCard: {
            rounded: 'md'
        },
        VTextField: {
            variant: 'outlined',
            density: 'comfortable',
            color: 'primary'
        },
        VTextarea: {
            variant: 'outlined',
            density: 'comfortable',
            color: 'primary'
        },
        VSelect: {
            variant: 'outlined',
            density: 'comfortable',
            color: 'primary'
        },
        VListItem: {
            minHeight: '45px'
        },
        VTooltip: {
            location: 'top'
        },
    }
});

<script setup lang="ts">
import icon1 from '@/assets/images/svgs/icon-paypal.svg';
import icon2 from '@/assets/images/svgs/icon-office-bag.svg';
import icon3 from '@/assets/images/svgs/icon-master-card.svg';
import icon4 from '@/assets/images/svgs/icon-pie.svg';
import { computed, onMounted, ref } from 'vue';
import { apiClient } from '@/data/Axios';
import { formatMoney } from "@/utils/common";

const iconOptions = [
    { title: 'PayPal', value: icon1, color: 'primary', category: 'EXPENSE' },
    { title: 'Wallet', value: icon2, color: 'success', category: 'ASSET'  },
    { title: 'Card', value: icon3, color: 'warning', category: 'LIABILITY'  },
    { title: 'Refund', value: icon4, color: 'error', category: 'EQUITY'  }
];

function getIconByKey(key?: string | null): string {
    return iconOptions.find(item => item.category === key)?.value ?? icon1;
}
function getColorByKey(key?: string | null): string {
    return iconOptions.find(item => item.category === key)?.color ?? 'primary';
}

interface DashboardItem {
    title: string;
    subtitle: string;
    amount: string;
    negative: boolean;
    accountCode: string;
    sortOrder: number;
    category: string;
}

interface SettingItem {
    accountCode: string;
    accountName: string;
    category: 'ASSET' | 'LIABILITY';
    dashboardTag: string;
    checked: boolean;
}

const DEFAULT_TABS = ['급여', '비상금', '미지정'];
const MAIN_WIDGET_TAG = '급여';

const items = ref<DashboardItem[]>([]);
const loading = ref(false);

const dialog = ref(false);
const activeTab = ref(DEFAULT_TABS[0]);

// 설정화면 전체 목록 — 화면 진입 시 한 번만 불러오고, 탭은 이 배열을 클라이언트에서 필터링만 한다.
const allSettings = ref<SettingItem[]>([]);
const settingsLoading = ref(false);
const savingSettings = ref(false);

// 실제 존재하는 태그 목록 (기본 3개 + 데이터에 있는데 기본목록에 없는 태그가 있으면 추가)
const tabs = computed(() => {
    const extra = Array.from(new Set(allSettings.value.map((s) => s.dashboardTag))).filter(
        (t) => !DEFAULT_TABS.includes(t)
    );
    return [...DEFAULT_TABS, ...extra];
});

// 탭별 그룹핑 — dashboardTag 값이 바뀌면(select 조작) 여기서 자동으로 재계산되어 다른 탭으로 옮겨간 것처럼 보인다
const itemsByTag = computed(() => {
    const map: Record<string, SettingItem[]> = {};
    for (const tag of tabs.value) {
        map[tag] = allSettings.value.filter((s) => s.dashboardTag === tag);
    }
    return map;
});

const fetchItems = async () => {
    loading.value = true;
    try {
        items.value = await apiClient.get('/dashboard/items', { tag: MAIN_WIDGET_TAG });
    } finally {
        loading.value = false;
    }
};

onMounted(fetchItems);

const openSettings = async () => {
    dialog.value = true;
    settingsLoading.value = true;
    try {
        // 전체 계정을 한 번에 불러온다 — dashboard_item.dashboard_tag 기준으로 이미 설정된 데이터는
        // 여기서 바로 해당 태그 값으로 채워져 오므로, 급여로 설정된 계정은 자동으로 '급여' 탭에서 보인다.
        allSettings.value = await apiClient.get('/dashboard/items/settings');
    } finally {
        settingsLoading.value = false;
    }
};

const onSaveSettings = async () => {
    savingSettings.value = true;
    try {
        await apiClient.put('/dashboard/items/settings', {
            items: allSettings.value.map((s) => ({
                accountCode: s.accountCode,
                dashboardTag: s.dashboardTag,
                checked: s.checked
            }))
        });
        dialog.value = false;
        await fetchItems();
    } finally {
        savingSettings.value = false;
    }
};
</script>

<template>
    <v-card>
        <v-card-item>
            <div class="d-flex justify-space-between align-center">
                <v-card-title class="text-h5">계좌별 잔액</v-card-title>
                <v-btn icon size="small" variant="text" @click="openSettings">
                    <v-icon>mdi-cog</v-icon>
                </v-btn>
            </div>
            <v-card-subtitle class="text-subtitle-1 textSecondary">보이는 계정만 표시됩니다</v-card-subtitle>

            <div class="mt-sm-10 mt-5">
                <div class="d-flex align-center mt-6" v-for="item in items" :key="item.accountCode">
                    <v-avatar :class="'rounded-md bg-light' + getColorByKey(item.category)" size="40">
                        <img :src="getIconByKey(item.category)" :alt="getIconByKey(item.category)" width="25" />
                    </v-avatar>
                    <div class="pl-4 mt-n1">
                        <h5 class="text-h6">{{ item.title }}</h5>
                        <h6 class="text-subtitle-1 textSecondary mt-1">{{ item.subtitle }}</h6>
                    </div>
                    <div :class="'ml-auto font-weight-bold text-subtitle-1' + (item.negative ? ' text-medium-emphasis' : '')">
                        {{ formatMoney(item.amount) }}
                    </div>
                </div>

                <v-alert v-if="!loading && items.length === 0" type="info" variant="tonal" density="compact" class="mt-4">
                    설정에서 표시할 계정을 선택해주세요.
                </v-alert>
            </div>
        </v-card-item>
    </v-card>

    <v-dialog v-model="dialog" max-width="560">
        <v-card>
            <v-card-title>대시보드 계정 설정</v-card-title>

            <v-tabs v-model="activeTab">
                <v-tab v-for="tag in tabs" :key="tag" :value="tag">
                    {{ tag }} ({{ (itemsByTag[tag] ?? []).length }})
                </v-tab>
            </v-tabs>

            <v-card-text style="max-height: 60vh; overflow-y: auto">
                <v-progress-circular v-if="settingsLoading" indeterminate class="d-block mx-auto my-4" />

                <v-window v-else v-model="activeTab">
                    <v-window-item v-for="tag in tabs" :key="tag" :value="tag">
                        <v-list density="compact">
                            <v-list-item v-for="item in itemsByTag[tag] ?? []" :key="item.accountCode">
                                <template #prepend>
                                    <v-checkbox v-model="item.checked" density="compact" hide-details />
                                </template>
                                <v-row align="center" no-gutters>
                                    <v-col cols="6">
                                        <v-list-item-title>{{ item.accountName }}</v-list-item-title>
                                    </v-col>
                                    <v-col cols="6">
                                        <v-select
                                            v-model="item.dashboardTag"
                                            :items="tabs"
                                            density="compact"
                                            hide-details
                                        />
                                    </v-col>
                                </v-row>
                            </v-list-item>
                        </v-list>
                    </v-window-item>
                </v-window>
            </v-card-text>

            <v-card-actions>
                <v-spacer />
                <v-btn variant="text" @click="dialog = false">취소</v-btn>
                <v-btn color="primary" :loading="savingSettings" @click="onSaveSettings">저장</v-btn>
            </v-card-actions>
        </v-card>
    </v-dialog>
</template>

<style scoped></style>
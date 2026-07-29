<script setup lang="ts">
import UiParentCard from '@/components/shared/UiParentCard.vue';
import { onMounted, ref, computed } from 'vue';
import { apiClient } from '@/data/Axios';

interface AccountItem {
    id: number;
    code: string;
    name: string;
    category: 'ASSET' | 'LIABILITY' | 'EQUITY' | 'REVENUE' | 'EXPENSE';
    parentId: number | null;
    leaf: boolean;
    active: boolean;
}

const categoryLabel: Record<string, string> = {
    ASSET: '자산',
    LIABILITY: '부채',
    EQUITY: '자본',
    REVENUE: '수익',
    EXPENSE: '비용'
};
const categoryOptions = Object.keys(categoryLabel).map((value) => ({ title: categoryLabel[value], value }));

const accounts = ref<AccountItem[]>([]);
const loading = ref(false);
const errorMessage = ref('');

const dialog = ref(false);
const editingId = ref<number | null>(null);
const form = ref({
    code: '',
    name: '',
    category: 'EXPENSE',
    parentId: null as number | null,
    sortOrder: 0,
    active: true
});

const headers = ref<any[]>([
    { title: '코드', align: 'start', key: 'code' },
    { title: '계정과목명', align: 'start', key: 'name' },
    { title: '분류', align: 'center', key: 'category' },
    { title: '상위계정', align: 'start', key: 'parentName' },
    { title: '상태', align: 'center', key: 'active' },
    { title: '관리', align: 'center', key: 'actions', sortable: false }
]);

const accountsWithParentName = computed(() =>
    accounts.value.map((a) => ({
        ...a,
        parentName: a.parentId ? accounts.value.find((p) => p.id === a.parentId)?.name ?? '-' : '-'
    }))
);

const parentOptions = computed(() =>
    accounts.value
        .filter((a) => a.id !== editingId.value) // 자기 자신은 상위계정으로 선택 불가
        .map((a) => ({ title: `${a.name} (${a.code})`, value: a.id }))
);

const fetchAccounts = async () => {
    loading.value = true;
    try {
        accounts.value = await apiClient.get('/journal/accounts');
    } catch (e: any) {
        errorMessage.value = e?.response?.data?.message || '계정과목 목록 조회 실패';
    } finally {
        loading.value = false;
    }
};

onMounted(fetchAccounts);

const openCreate = () => {
    editingId.value = null;
    form.value = { code: '', name: '', category: 'EXPENSE', parentId: null, sortOrder: 0, active: true };
    errorMessage.value = '';
    dialog.value = true;
};

const openEdit = (item: AccountItem) => {
    editingId.value = item.id;
    form.value = {
        code: item.code,
        name: item.name,
        category: item.category,
        parentId: item.parentId,
        sortOrder: 0,
        active: item.active
    };
    errorMessage.value = '';
    dialog.value = true;
};

const onSave = async () => {
    errorMessage.value = '';
    try {
        if (editingId.value) {
            await apiClient.put(`/journal/accounts/${editingId.value}`, {
                name: form.value.name,
                category: form.value.category,
                parentId: form.value.parentId,
                sortOrder: form.value.sortOrder,
                active: form.value.active
            });
        } else {
            await apiClient.post('/journal/accounts', {
                code: form.value.code,
                name: form.value.name,
                category: form.value.category,
                parentId: form.value.parentId,
                sortOrder: form.value.sortOrder
            });
        }
        dialog.value = false;
        await fetchAccounts();
    } catch (e: any) {
        errorMessage.value = e?.response?.data?.message || '저장 중 오류가 발생했습니다.';
    }
};

const onToggleActive = async (item: AccountItem) => {
    try {
        const action = item.active ? 'deactivate' : 'activate';
        await apiClient.patch(`/journal/accounts/${item.id}/${action}`);
        await fetchAccounts();
    } catch (e: any) {
        alert(e?.response?.data?.message || '처리 중 오류가 발생했습니다.');
    }
};
</script>

<template>
    <v-row>
        <v-col cols="12">
            <UiParentCard title="계정과목 관리">
                <v-row>
                    <v-col cols="12" class="d-flex justify-end">
                        <v-btn color="primary" @click="openCreate">계정과목 추가</v-btn>
                    </v-col>
                </v-row>

                <v-data-table
                    :headers="headers"
                    :items="accountsWithParentName"
                    :loading="loading"
                    class="border rounded-md"
                    items-per-page="20"
                >
                    <template #item.category="{ item }">
                        {{ categoryLabel[item.category] }}
                    </template>
                    <template #item.active="{ item }">
                        <v-chip :color="item.active ? 'success' : 'grey'" size="small">
                            {{ item.active ? '활성' : '비활성' }}
                        </v-chip>
                    </template>
                    <template #item.actions="{ item }">
                        <v-btn size="small" variant="text" color="primary" @click="openEdit(item)">수정</v-btn>
                        <v-btn size="small" variant="text" :color="item.active ? 'error' : 'success'" @click="onToggleActive(item)">
                            {{ item.active ? '비활성화' : '재활성화' }}
                        </v-btn>
                    </template>
                </v-data-table>
            </UiParentCard>
        </v-col>

        <v-dialog v-model="dialog" max-width="500">
            <v-card>
                <v-card-title>{{ editingId ? '계정과목 수정' : '계정과목 추가' }}</v-card-title>
                <v-card-text>
                    <v-alert v-if="errorMessage" type="error" variant="tonal" density="compact" class="mb-3" style="white-space: pre-line">
                        {{ errorMessage }}
                    </v-alert>
                    <v-text-field v-model="form.code" label="계정코드" :disabled="!!editingId" density="compact" class="mb-2" />
                    <v-text-field v-model="form.name" label="계정과목명" density="compact" class="mb-2" />
                    <v-select v-model="form.category" :items="categoryOptions" label="분류" density="compact" class="mb-2" />
                    <v-select
                        v-model="form.parentId"
                        :items="parentOptions"
                        label="상위계정 (없으면 최상위)"
                        density="compact"
                        clearable
                        class="mb-2"
                    />
                    <v-text-field v-model.number="form.sortOrder" type="number" label="정렬순서" density="compact" class="mb-2" />
                    <v-switch v-if="editingId" v-model="form.active" label="활성 상태" density="compact" />
                </v-card-text>
                <v-card-actions>
                    <v-spacer />
                    <v-btn variant="text" @click="dialog = false">취소</v-btn>
                    <v-btn color="primary" @click="onSave">저장</v-btn>
                </v-card-actions>
            </v-card>
        </v-dialog>
    </v-row>
</template>

<style scoped></style>

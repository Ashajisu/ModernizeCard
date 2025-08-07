import { ref, computed, watch } from "vue";
import type { Ref } from "vue";
import type {FormField} from "@/types/custom/InputTypes";
import {parseExcel} from "@/common/excel/excelService";
import { alert, confirm } from "@/common/alertService";

// 📌 부서 맵 : 차후 api 로 호출할 예정
const deptOptions = ref<string[]>(['경영지원팀', '영업본부', '사업지원팀', '고객지원본부', '개발연구소']);

// 📌 팀 옵션 맵 : 차후 api 로 호출할 예정
const teamOptionsMap: Ref<Record<string, string[]>> = ref({
    '영업본부': ['영업1팀', '영업2팀','영업3팀'],
    '고객지원본부': ['기술1팀','기술2팀','PM팀','SPM 팀','상주팀(국민권익위원회)','상주팀(다산콜재단)','상주팀(유안타증권)','상주팀(경찰청 민원)'],
    '사업지원팀': ['팀장'],
    '경영지원팀': [],
    '개발연구소': []
});

export function useTableManager<T extends Record<string, any>>(
    tableList: Ref<T[]> = ref([]), // 기본값 빈배열인 원본 데이터
    formFields: Ref<FormField[]>,   // 검색 필드
    detailFields?: Ref<FormField[]> | null,    // 상세정보 필드
    identifierField: string = "employeeId"  // 테이블 식별자 필드
) {

    // ✅ 필드 옵션 자동 업데이트
    const updateFieldsOptions = (fields: Ref<FormField[]>, fieldName: string, options: any[]) => {
        watch ( options,
            () => {
                const targetField = fields.value.find(f => f.name === fieldName);
                if (targetField) {
                    targetField.options = options;
                }
            },
            { deep: true, immediate: true }
        );
    };
    // ✅ 부서 변경 시 팀 옵션 자동 업데이트
    const updateTeamOptions = (fields: Ref<FormField[]>, deptFieldName:string, targetFieldName:string, optionsMap: Ref<Record<string, string[]>>) => {
        watch(() => fields.value.find(field => field.name === deptFieldName)?.value,
            (newDepartment: any) => {
                const teamField = fields.value.find(field => field.name === targetFieldName);
                if (teamField) {
                    teamField.options = optionsMap.value[newDepartment || ''] || [];
                }
            },
            { deep: true, immediate: true } // 깊은 감지 및 즉시 초기화
        );
    };
    // ✅ 검색목록 자동 업데이트
    const updateFieldsSearchObj = (fields: Ref<FormField[]>, fieldTypes: string[], options: Ref<any[]>) => {
        watch(tableList,
            (newVal) => {
                if(newVal.length >0){
                    const searchFields = fields.value.filter(f => fieldTypes.includes(f.type)); // search 필드 필터
                    searchFields.forEach(field => {
                        field.searchObj = options.value;
                        // console.log( "updateFieldsSearchObj", field.name, field.searchObj);
                    });
                }
            },
            { deep: true, immediate: true } // 깊은 감지 및 즉시 초기화
        );
    }

    // 호출
    updateFieldsOptions(formFields,"department",deptOptions.value);
    updateTeamOptions(formFields, "department", "team", teamOptionsMap);
    updateFieldsSearchObj(formFields, ["search", "search_list"], tableList);
    if (detailFields) {
        updateFieldsOptions(detailFields, "department", deptOptions.value);
        updateTeamOptions(detailFields, "department", "team", teamOptionsMap);
        updateFieldsSearchObj(detailFields, ["search", "search_list"], tableList);
    }

    // 📌 검색어 상태
    const search = ref<Record<string, any>>({});

    // ✅ 검색 기능
    const onSearch = async (validateForm: any) => {
        search.value = await validateForm();
    };

    // ✅ 검색 초기화
    const resetSearch = () => {
        formFields.value.forEach(field => {
            field.value='';
        });
        search.value = {};
    };

    // ✅ 검색어로 테이블 필터링
    const filteredList = computed(() => {
        if (!search.value) return tableList.value;

        let isAllEmpty = true;
        for (const key in search.value) {
            if (search.value[key] !== "") {
                isAllEmpty = false;
                break;
            }
        }
        if (isAllEmpty) return tableList.value;

        return tableList.value.filter((item) =>
            Object.keys(search.value).every((key) => {
                const searchVal = search.value[key];
                const itemVal = item[key];

                if (Array.isArray(searchVal)) {
                    return searchVal.length === 0 || searchVal.some((val: string) => itemVal.toLowerCase() === val.toLowerCase());
                } else {
                    return !searchVal || itemVal.toLowerCase().includes(searchVal.toLowerCase());
                }
            })
        );
    });

    // 📌 선택된 ID 목록
    const selectedEmpId = ref<string[]>([]);

    // ✅ 체크박스 선택 변경
    const onSelectionChange = () => {
        console.log("onSelectionChange", selectedEmpId.value);
        if (selectedEmpId.value.length > 0 && detailFields) {
            updateUserFields(selectedEmpId.value[0]);
        }
    };

    // 📌 선택된 상세정보
    const selectedItem = ref<T | null>(null);

    // ✅ 선택된 상세정보 출력
    const updateUserFields = (selectedId: string) => {
        if (!detailFields) return;
        selectedItem.value = tableList.value.find((item) => item[identifierField] === selectedId) || null;
        if (selectedItem.value) {
            detailFields.value.forEach((field) => {
                field.value = selectedItem.value?.[field.name] ?? "";
            });
        }
    };
    // 📌 편집 모드
    const edit = ref<boolean>(false);

    // ✅ 편집 모드 변경
    const handleEdit = (bool: boolean) => {
        edit.value = bool;
    };

    // ✅ 새 사용자 추가
    const onNew = () => {
        selectedEmpId.value = ["new"];
        selectedItem.value = null;
        if (detailFields) {
            detailFields.value.forEach(field => {
                field.value = '';
            });
        }
        edit.value = true;
    };

    // ✅ 저장 (신규 등록 & 수정)
    const onSave = async (validateForm: any): Promise<boolean> => {
        const formData = await validateForm();
        if (formData) {
            const index = tableList.value.findIndex((item) => item[identifierField] === formData[identifierField]);
            if (index === -1) {
                tableList.value.push({ ...formData });
                await alert(`"${formData[identifierField]}" 가 등록되었습니다.`);
            } else {
                tableList.value[index] = { ...tableList.value[index], ...formData }; // 기존 필드 유지
                await alert(`"${formData[identifierField]}" 가 업데이트되었습니다.`);
            }
            console.debug('onSave:', formData);
            edit.value = false;
            return true; // 저장 성공
        }else {
            return false; // 저장 실패
        }
    };

    const onExcelSave = async (validateForm: () => Promise<File | null>, func?:Function | null): Promise<boolean> => {
        const file = await validateForm();
        if(!file){ return false; }
        const parsed:T[] = await parseExcel<T>(file); // useTableManager<T>에 따라 T는 이미 지정됨

        let data:T[] = [];
        if(!parsed){ return false; }
        if(func && typeof func === 'function'){
            data = await func(parsed);
            // console.log(data);
        }else {
            data = parsed;
        }

        if (Array.isArray(data) && data.length > 0) {
            let addedCount = 0;
            data.forEach((row:T) => {
                const identifier = row[identifierField];
                const index = tableList.value.findIndex((item) => item[identifierField] === identifier);

                if (index === -1) {
                    tableList.value.push({ ...row });
                    addedCount++;
                } else {
                    tableList.value[index] = { ...tableList.value[index], ...row };
                }
            });

            await alert(`엑셀에서 ${addedCount}건 등록, ${addedCount}건 업데이트 완료`);
            return true;
        } else {
            await alert("엑셀 파싱 실패 또는 데이터가 비어 있습니다.");
            return false;
        }
    };


    // ✅ 삭제 기능
    const onDelete = async (selected: string, func?: Function|null) => {
        const index = tableList.value.findIndex((item) => item[identifierField] === selected);
        if (index !== -1 && await confirm("삭제 하시겠습니까?")) {
            if (func) {
                const deleted = await func(selected);
            }
            const removed = tableList.value.splice(index, 1);
            if (removed[0]) {
                await alert(`${removed[0][identifierField]}이 삭제되었습니다.`);
            } else {
                await alert("삭제 실패");
            }
        }
    };

    return {
        tableList,
        search,
        onSearch,
        resetSearch,
        filteredList,
        selectedEmpId,
        onSelectionChange,
        selectedItem,
        updateUserFields,
        edit,
        handleEdit,
        onNew,
        onSave,
        onDelete,
        onExcelSave,
        deptOptions
    };
}

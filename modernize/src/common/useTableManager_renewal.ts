import { ref, computed, watch } from "vue";
import type { Ref } from "vue";
import type {FormField} from "@/types/custom/InputTypes";
import {parseExcel} from "@/common/excel/excelService";
import { alert, confirm } from "@/common/alertService";
import {deepClone, deleteToServer, excelSaveToServer} from "@/utils/common";
import {parsePath, getDeepValue, setDeepValue, deepMerge} from "@/utils/common";

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
                    fields.value
                        .filter((f) => ["search", "search_list"].includes(f.type))
                        .forEach((f) => (f.searchObj = tableList.value));
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

    const formSearch = (form: any) => {
        search.value = form;
    }

    // ✅ 검색 초기화
    const resetSearch = () => {
        formFields.value.forEach(field => {
            if (field.type === 'due') {
                // DateRange 필드는 객체 형태로 초기화
                field.value = { startDate: '', endDate: '' };
            } else if (Array.isArray(field.value)) {
                field.value = field.defaultValue ?? []; // 체크박스/멀티 선택 필드 초기화
            } else {
                field.value = field.defaultValue ?? ''; // 문자열, 기타 필드 초기화
            }
        });
        search.value = {};
    };

    //중첩 객체 안쪽 필드를 실제로 검사해야 함
    function hasCondition(obj: any): boolean {
        if (!obj || typeof obj !== "object") return false;
        for (const key in obj) {
            const val = obj[key];
            // ✅ 중첩 구조에서 재귀 탐색
            if (typeof val === "object" && !Array.isArray(val)) {
                if (hasCondition(val)) return true;
                continue;
            }
            // ✅ DateRange
            if (val && typeof val === "object" && "startDate" in val && "endDate" in val) {
                if (val.startDate || val.endDate) return true;
                continue;
            }
            // ✅ 배열
            if (Array.isArray(val)){
                if(val.length > 0){return true;} // length > 0이어야 true
                continue;
            }
            // ✅ 문자열
            if (typeof val === "string" && val.trim() !== "") return true;
            // ✅ 숫자 / boolean 검색값
            if (val !== "" && val !== null && val !== undefined) return true;
        }
        return false;
    }

    function matchesCondition(item: any, condition: any): boolean {
        for (const key in condition) {
            const condVal = condition[key];
            const itemVal = item[key];
            // ① 객체면 재귀 비교
            if (condVal && typeof condVal === "object" && !Array.isArray(condVal)) {
                if (!matchesCondition(itemVal ?? {}, condVal)) return false;
                continue;
            }
            // ② 값이 비어있으면 조건 무시 (통과)
            if (condVal === "" || condVal === null || condVal === undefined) {
                continue;
            }
            // ③ 배열 검색
            if (Array.isArray(condVal)) {
                if (condVal.length > 0) {
                    if (!condVal.includes(itemVal)) return false;
                }
                continue;
            }
            // ④ 문자열 검색 (부분 매칭)
            if (typeof condVal === "string") {
                if (!String(itemVal || "").toLowerCase().includes(condVal.toLowerCase())) return false;
                continue;
            }
            // ⑤ 기본 비교
            if (itemVal !== condVal) return false;
        }
        return true;
    }


    // ✅ 검색어로 테이블 필터링
    const filteredList = computed(() => {
        // ✅ 활성 조건 여부 재귀 확인
        const condition = search.value;
        //console.debug('condition:',condition);
        if (!hasCondition(condition)) return tableList.value;

        return tableList.value.filter(item => {
            //console.debug('item:',item);
            return matchesCondition(item, condition);
        });
    });

    // 📌 선택된 ID 목록
    const selectedEmpId = ref<string[]>([]);

    // ✅ 체크박스 선택 변경
    const onSelectionChange = () => {
        // console.log("onSelectionChange", selectedEmpId.value);
        if (selectedEmpId.value.length > 0 && detailFields) {
            updateUserFields(selectedEmpId.value[0]);
        }
    };

    // 📌 선택된 상세정보
    const selectedItem = ref<T | null>(null);

    // ✅ 선택된 상세정보 출력
    const updateUserFields = (selectedId: string) => {
        if (!detailFields) return;
        selectedItem.value = tableList.value.find((item) => getDeepValue(item, identifierField) === selectedId) || null;
        if (selectedItem.value) {
            detailFields.value.forEach((field) => {
                field.value = getDeepValue(selectedItem.value, field.name, "");
            });
        }
    };
    // 📌 편집 모드
    const edit = ref<boolean>(false);

    // ✅ 편집 모드 변경
    const handleEdit = (bool: boolean) => {
        edit.value = bool;

        // ❌ 편집 취소 시 원본 데이터로 복구
        if (!bool && selectedEmpId.value.length > 0) {
            updateUserFields(selectedEmpId.value[0]);
        }
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
        if (!formData) return false; // validation 실패

        const index = tableList.value.findIndex(
            (item) => getDeepValue(item, identifierField) === getDeepValue(formData, identifierField)
        );

        if (index === -1) {
            tableList.value.push(deepClone(formData));
            await alert(`"${getDeepValue(formData, identifierField)}" 가 등록되었습니다.`);
        } else {
            const existing = deepClone(tableList.value[index]);
            tableList.value[index] = deepMerge(existing, deepClone(formData));
            await alert(`"${getDeepValue(formData, identifierField)}" 가 업데이트되었습니다.`);
        }
        console.debug('onSave:', formData);
        edit.value = false;
        return true; // 저장 성공
    };

    const onExcelSave = async (validateForm: () => Promise<File | null>, url:string): Promise<boolean> => {
        const file = await validateForm();
        if(!file){ return false; }
        const parsed:T[] = await parseExcel<T>(file); // useTableManager<T>에 따라 T는 이미 지정됨

        let data:T[] = [];
        if(!parsed){ return false; }
        if(url && typeof excelSaveToServer === 'function'){
            data = await excelSaveToServer(parsed, url);
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
    const onDelete = async (selected: string, url: string) => {
        console.log(selected, url, identifierField);
        const index = tableList.value.findIndex((item) => getDeepValue(item, identifierField) === selected);
        console.log(index);
        if (index !== -1 && await confirm("삭제 하시겠습니까?")) {
            const deleted : boolean | undefined = await deleteToServer(selected, url);
            const removed = tableList.value.splice(index, 1);
            if (deleted) {
                await alert(`${removed[0][identifierField]}이 삭제되었습니다.`);
            } else {
                await alert("삭제 실패");
            }
        }
    };

    const setFieldOptions = (options: any[], fieldName : string = 'user.dept') => {
        const target = formFields.value.find(f => f.name === fieldName);
        if (target) {
            target.options = options;
        }
        if(detailFields){
            const target2 = detailFields.value.find(f => f.name === fieldName);
            if (target2) {
                target2.options = options;
            }
        }
        // console.debug('이용구분 선택란 데이터:', options);
    };
    //“중첩 객체를 포함한 행 하나를 통째로 교체하고 싶다
    function updateTableRow(updatedItem: any) {
        const index = tableList.value.findIndex(
            (item) => getDeepValue(item, identifierField) === getDeepValue(updatedItem, identifierField)
        );

        if (index !== -1) {
            tableList.value[index] = deepClone(updatedItem);
        } else {
            // 목록에 없으면 추가
            tableList.value.push(updatedItem);
        }
    }

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
        setFieldOptions,
        updateFieldsOptions,
        updateTableRow,
        formSearch
    };
}

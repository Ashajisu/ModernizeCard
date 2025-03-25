// 팀 옵션 맵
import type { FormField } from "@/types/custom/InputTypes";
import { watch } from "vue";

export const teamOptionsMap: Record<string, string[]> = {
    '기술팀': ['기술2팀', '기술1팀', '기술지원팀'],
    '영업팀': ['영업1팀', '영업2팀'],
    '고객지원본부': ['SPM팀'],
    '연구개발': ['연구개발팀']
};

// 부서 변경 시 팀 옵션 설정 함수
export function watchDepartmentChange(formFields: FormField[]) {
    watch(
        () => formFields.find(field => field.name === 'department')?.value,
        (newDepartment: any) => {
            const teamField = formFields.find(field => field.name === 'team');
            if (teamField) {
                teamField.options = teamOptionsMap[newDepartment || ''] || [];
            }
        }
    );
}

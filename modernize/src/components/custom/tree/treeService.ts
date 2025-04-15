import type {UserItem} from "@/types/custom/DataTableTypes";
import type {FormField} from "@/types/custom/InputTypes";
import {alert} from "@/common/alertService";
import {type Ref, ref} from "vue";
import TuiTree from "tui-tree";
import {cloneDeep} from "lodash";

export type FlatItem = {
    title: string;        // 조직 또는 사용자 이름
    code: string;           // 고유 아이디
    parentTitle: string;  // 상위 조직명
    parentCode: string;     // 상위 조직 ID
    level: number;        // 조직 레벨 (1, 2, 3, ...)
    index: number;        // 정렬 순서
    activeStatus: boolean; // 사용 여부
    user: UserItem | null;  // 사용자 정보 (사용자일 경우 UserItem 객체, 그렇지 않으면 빈 객체)
    // 인덱스 시그니처 추가
    [key: string]: any;
};

export const FlatTreeData:FlatItem[] = [
    {
        title: "최상위 조직정보 1",
        code: "A_1",
        parentTitle: "",
        parentCode: "",
        level: 1,
        index: 0,
        activeStatus: true,
        user: null
    },
    {
        title: "최상위 조직정보 2",
        code: "A_2",
        parentTitle: "",
        parentCode: "",
        level: 1,
        index: 1,
        activeStatus: true,
        user: null
    },
    {
        title: "2단계 조직정보 1",
        code: "B_1",
        parentTitle: "최상위 조직정보 1",
        parentCode: "A_1",
        level: 2,
        index: 0,
        activeStatus: true,
        user: null
    },
    {
        title: "2단계 조직정보 2",
        code: "B_2",
        parentTitle: "최상위 조직정보 1",
        parentCode: "A_1",
        level: 2,
        index: 1,
        activeStatus: true,
        user: null
    },
    {
        title: "2단계 조직정보 3",
        code: "B_3",
        parentTitle: "최상위 조직정보 1",
        parentCode: "A_2",
        level: 2,
        index: 0,
        activeStatus: true,
        user: null
    },
    {
        title: "3단계 조직정보 1",
        code: "C_1",
        parentTitle: "2단계 조직정보 1",
        parentCode: "B_1",
        level: 3,
        index: 0,
        activeStatus: true,
        user: null
    },
    {
        title: "3단계 조직정보 2",
        code: "C_2",
        parentTitle: "2단계 조직정보 2",
        parentCode: "B_2",
        level: 3,
        index: 0,
        activeStatus: true,
        user: null
    },
    {
        title: "3단계 조직정보 3",
        code: "C_3",
        parentTitle: "2단계 조직정보 3",
        parentCode: "B_3",
        level: 3,
        index: 0,
        activeStatus: true,
        user: null
    },
    {
        title: "3단계 조직정보 4",
        code: "C_4",
        parentTitle: "2단계 조직정보 3",
        parentCode: "B_3",
        level: 3,
        index: 1,
        activeStatus: true,
        user: null
    },
    {
        title: "3단계 조직정보 5",
        code: "C_5",
        parentTitle: "2단계 조직정보 3",
        parentCode: "B_3",
        level: 3,
        index: 2,
        activeStatus: true,
        user: null
    },
    {
        title: "박OO",
        code: "user_ad_205",
        parentTitle: "3단계 조직정보 1",
        parentCode: "C_1",
        level: 4,
        index: 0,
        activeStatus: true,
        user: {
            department: "기술팀",
            team: "기술1팀",
            username: "박OO",
            employeeId: "20019133",
            position: "사원",
            email: "happy@arisys.co.kr",
            phone: "",
            mobile: "",
            hireDate: "2021-05-01",
            employmentStatus: "재직",
            userRole: "사용자",
            activeStatus: "Y",
            zoomLicense: "Basic",
            phoneLicense: "",
        }
    },
    {
        title: "이OO",
        code: "user_ad_206",
        parentTitle: "3단계 조직정보 1",
        parentCode: "C_1",
        level: 4,
        index: 1,
        activeStatus: true,
        user: {
            department: "기술팀",
            team: "기술2팀",
            username: "이OO",
            employeeId: "20355071",
            position: "대리",
            email: "good@arisys.co.kr",
            phone: "02-1234-5678",
            mobile: "010-1234-5678",
            hireDate: "2020-02-10",
            employmentStatus: "재직",
            userRole: "관리자",
            activeStatus: "Y",
            zoomLicense: "WorkplaceBiz",
            phoneLicense: "Phone Pro",
        }
    },
    {
        title: "박OO",
        code: "user_ad_207",
        parentTitle: "3단계 조직정보 2",
        parentCode: "C_2",
        level: 4,
        index: 0,
        activeStatus: true,
        user: {
            department: "고객지원본부",
            team: "SPM팀",
            username: "박OO",
            employeeId: "20415072",
            position: "수석",
            email: "love@arisys.co.kr",
            phone: "02-9876-5432",
            mobile: "010-9876-5432",
            hireDate: "2019-08-15",
            employmentStatus: "재직",
            userRole: "사용자",
            activeStatus: "Y",
            zoomLicense: "WorkplaceBiz",
            phoneLicense: "Phone Pro",
        }
    },
    {
        title: "전OO",
        code: "user_ad_208",
        parentTitle: "3단계 조직정보 2",
        parentCode: "C_2",
        level: 4,
        index: 1,
        activeStatus: true,
        user: {
            department: "기술팀",
            team: "기술1팀",
            username: "전OO",
            employeeId: "20680414",
            position: "과장",
            email: "joy@arisys.co.kr",
            phone: "02-1122-3344",
            mobile: "010-1122-3344",
            hireDate: "2017-11-20",
            employmentStatus: "재직",
            userRole: "슈퍼 관리자",
            activeStatus: "Y",
            zoomLicense: "WorkplaceBiz",
            phoneLicense: "Phone Pro, Power",
        }
    },
];
let tree: any = null;
let initialFlatTreeData: FlatItem[] = cloneDeep(FlatTreeData); //초기 원본 데이터 보관
export let changedCodeList= ref<string[]>([]); //변경된 code 저장
export const selectedValue = ref<FlatItem>();  // 선택된 노드의 텍스트를 저장할 ref

export const initTree = (
    container: Ref<HTMLElement | null>,
) => {
    if (container.value) {
        tree = new TuiTree(container.value, {
            data: convertFlatToTree(FlatTreeData),
            nodeDefaultState: "opened",
            template: {
                internalNode:
                    '<div class="tui-tree-content-wrapper" style="padding-left: {{indent}}px">' +
                    '<button type="button" class="tui-tree-toggle-btn tui-js-tree-toggle-btn">' +
                    '<span class="tui-ico-tree"></span>' +
                    '{{stateLabel}} ' +
                    '</button>' +
                    '</button>' +
                    '<span class="tui-tree-text tui-js-tree-text">' +
                    '<span class="tui-tree-ico tui-ico-folder"></span>' +
                    '{{title}} ' +
                    '</span>' +
                    '</div>' +
                    '<ul class="tui-tree-subtree tui-js-tree-subtree">{{children}}</ul>',
                leafNode:
                    '<div class="tui-tree-content-wrapper" style="padding-left: {{indent}}px">' +
                    '<span class="tui-tree-text tui-js-tree-text">' +
                    '<span class="tui-tree-ico tui-ico-file"></span>' +
                    '{{title}}' +
                    '</span>' +
                    '</div>'
            }
        }).enableFeature("Draggable", {
            helperClassName: "tui-tree-drop",
            lineClassName: "tui-tree-line",
            isSortable: true,
            useHelper: true
        }).enableFeature('Selectable', {
            selectedClassName: 'tui-tree-selected',  // 선택된 노드에 추가될 클래스
        });

        // 부모 노드별로 데이터 재설정 :재귀적으로 자식 노드를 변경.
        function updateChildIndexes(parentId: any) {
            const children = tree.getChildIds(parentId);
            const parentNodeData:FlatItem = tree.getNodeData(parentId);
            const parentTitle = parentNodeData.title;
            const parentCode = parentNodeData.code;
            const level = (parentNodeData.level ?? 0) + 1;

            children.forEach((childId: any, index: number) => {
                tree.setNodeData(childId, {
                    index: index,
                    level: level,
                    parentTitle: parentTitle,
                    parentCode: parentCode
                });
                const nodeData = tree.getNodeData(childId);
                setFlat(nodeData.code, index, level, parentCode, parentTitle);
                const changedNodeData = tree.getNodeData(childId);
                // console.log('childrenNode:',changedNodeData);
                updateChildIndexes(childId);

            });
        }

        // 이동 이벤트 핸들러
        tree.on("move", (eventData: any) => {
            updateChildIndexes(eventData.newParentId);
            updateChildIndexes(eventData.originalParentId);
        });

        // 'select' 이벤트 리스너
        tree.on('select', (eventData: any) => {
            const nodeData = tree.getNodeData(eventData.nodeId);
            // console.log('select:',nodeData);
            selectedValue.value = { ...nodeData } as FlatItem; // 자동으로 부모컴포넌트에 바인딩.
            // emit('update:selectedValue', nodeData);
        });

        return tree;
    }
};
// 모든 노드 열기
export const openAllNodes = () => {
    if (!!tree) {
        console.log('open');
        tree.eachAll((node:any, nodeId:string)=>{
            tree.open(nodeId, false);
        });
    }
};

// 모든 노드 닫기
export const closeAllNodes = () => {
    if (!!tree) {
        console.log('close');
        tree.eachAll((node:any, nodeId:string)=>{
            tree.close(nodeId, false);
        });
    }
};

// 검색기능
export const searching = (newSearch:string)=>{
    const keyword = newSearch.replace(/\s+/g, "");
    // console.log(keyword);
    if (!!tree) {

        // 검색 실행
        const matchedNodeIds:string[] = tree.search((node: any, nodeId:string) => {
            const nodeData:FlatItem = tree.getNodeData(nodeId);
            if (!nodeData) return false;
            // console.log(nodeData);
            const textPool = [
                nodeData.title,
                nodeData.code
            ];
            return textPool.some(field =>
                (field ?? "").replace(/\s+/g, "").toLowerCase().includes(keyword)
            );
        });

        // 모든 노드를 닫고, 일치하는 노드만 열기.
        closeAllNodes();
        // console.log(matchedNodeIds);
        matchedNodeIds.forEach(nodeId => {
            tree.open(nodeId, true);
        });
    }
}

// Tree → flat 변환
export function flattenTree(tree: FlatItem[], parentCode?: string, parentTitle?: string): FlatItem[] {
    const flat: FlatItem[] = [];

    for (const node of tree) {
        flat.push({
            code: node.code,
            title: node.title,
            parentCode: parentCode?? '',
            parentTitle: parentTitle?? '',
            level: node.level,
            index: node.index?? 0,
            activeStatus: true,
            user: null
        });

        if (node.children) {
            flat.push(...flattenTree(node.children, node.code));
        }
    }

    return flat;
}

// flat → Tree 갱신
export function convertFlatToTree(flatData: FlatItem[]): FlatItem[] {
    const map = new Map<string, FlatItem>();
    const roots: FlatItem[] = [];

    // 먼저 map 에 모든 데이터를 TreeItem 형태로 넣기
    flatData.forEach(item => {
        map.set(item.code, { ...item, children: [] });
    });

    // 부모-자식 관계 연결
    flatData.forEach(item => {
        const treeItem = map.get(item.code)!;
        if (item.parentCode && map.has(item.parentCode)) {
            const parent = map.get(item.parentCode)!;
            parent.children!.push(treeItem);
        } else {
            roots.push(treeItem); // parentCode가 null이면 루트 노드
        }
    });

    // 자식 정렬
    const sortTree = (nodes: FlatItem[]) => {
        nodes.sort((a, b) => a.index - b.index);
        nodes.forEach(child => {
            if (child.children?.length) sortTree(child.children);
        });
    };
    sortTree(roots);

    return roots;
}
export const updateGroupDetailFields = (
    selectedId: string,
    identifierField: string,
    tableList: FlatItem[],
    groupFields: FormField[],
    userFields: FormField[],
) =>{
    const found = tableList.find((item) => item[identifierField] === selectedId);
    // console.log('found:',found);
    if(!!found){
        groupFields.forEach((field:FormField) => {
            field.value = found[field.name] ?? "";
        });
        const user:UserItem|undefined|null = found?.user;
        if (!!user) {
            // console.log('updateUserDetailFields:',user);
            userFields.forEach((field:FormField) => {
                field.value = user[field.name] ?? "";
            });
        }
    }
}

// 1회성 dirty 상태 트래커
export let isDirty = ref(false);
const setDirty = (bool:boolean)=>{
    if(!isDirty.value){isDirty.value=bool;} //false -> true; 1회만
    if(!bool){isDirty.value=bool} //true -> false; 무조건
}
//중복 없이 변경된 code 를 저장.
export const addChangedCode = (code: string) => {
    if (!changedCodeList.value.includes(code)) {
        changedCodeList.value.push(code);
    }
};
//드래그 앤 드롭시 호출 됨.
export const setFlat = (code:string, index:number, level:number, parentCode:string, parentTitle:string) =>{
    const found:FlatItem | undefined = FlatTreeData.find(item => item.code === code);
    if(!!found){
        setDirty(true);
        addChangedCode(code);
        found.index = index;
        found.level = level;
        found.parentTitle = parentTitle;
        found.parentCode = parentCode;
    }
    // console.log('setFlat: code- index,level,parentCode,parentTitle: ', code,index,level,parentCode,parentTitle);
    // console.log('setFlat: found: ', found);
}

// DB 에서 FlatTreeData 를 다시 호출해 지금까지의 변경사항을 초기화.
export const resetFlat = ()=>{
    //임시로 리로드 시킴.
    changedCodeList.value = []; //초기화
    setDirty(false);
}
// DB에 변경사항만 전달.
const getChangedItems = (codes:string[])=>{
    let changedItemList: {code:string, index:number, level:number, activeStatus:boolean}[] = [];
    changedCodeList.value.forEach((code:string)=>{
        const item = FlatTreeData.find((flat:FlatItem)=>flat.code === code);
        if(item){
            changedItemList.push({
                code: item.code,
                index: item.index,
                level: item.level,
                activeStatus: item.activeStatus,
            });
        }
    });
    return changedItemList;
}
// changedCodeList 의 데이터를 찾아서 changedItemList 를 만든 후 saveFlat 호출.
export const onSave = async () => {
    const items = getChangedItems(changedCodeList.value);
    console.log('onSave: ',items);
    // const result:number = await apiSave(items);
    const result=1;
    if(result>0){
        await alert(`업데이트되었습니다.`);
        return true; // 저장 성공
    }else{
        return false;
    }
}
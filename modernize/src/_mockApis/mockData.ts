// Axios.ts 의 요청에 대해 아직 준비되지 않은 백엔드를 대신해 임시로 설정된 데이터를 반환하는 mock API 구현
import { UserDataTables } from "@/_mockApis/custom/ZoomData";

// 문자열 url 과 문자열 methode 로 data 매핑
export const mockData:{ [key: string]: {[method in "GET" | "POST" | "PUT" | "PATCH" | "DELETE"]?: any} } = {
    "/api/zoom/users": {
        "GET": {
            data: UserDataTables,
            status: 200,
            headers: { "Content-Type": "application/json" }
        }, // 기존 데이터를 GET 요청에 매핑
    },
};
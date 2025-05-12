package com.test.zoom.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.zoom.service.JsonUtil;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** /zoom/users 로 받은 사용자목록 정보를 vue 화면에 맞게 UserItem 타입으로 구조를 변환**/
@Getter
@Setter
public class UserItem {
    private String department;        // 부서명
    private String team;             // 팀명
    private String username;         // 사용자명
    private String employeeId;       // 사원번호
    private String position;         // 직위
    private String email;            // 메일주소
    private String phone;            // 전화번호 (옵션)
    private String mobile;           // 내선번호 (옵션)
    private String hireDate;         // 입사일자
    private String employmentStatus; // 재직상태
    private String userRole;         // 사용자 권한
    private String activeStatus;     // 사용 유무
    private String zoomLicense;      // 줌 라이센스
    private String phoneLicense;     // 줌폰 라이센스

    // 추가 속성(인덱스 시그니처 역할)
    private Map<String, String> additionalInfo;


    private static final ObjectMapper objectMapper = JsonUtil.getObjectMapper();
    public static List<UserItem> mapUsers(String jsonResponse) throws JsonProcessingException {
        List<UserItem> userItems = new ArrayList<>();
        JsonNode rootNode = objectMapper.readTree(jsonResponse);

        JsonNode usersNode = rootNode.get("users");
        if (usersNode != null && usersNode.isArray()) {
            for (JsonNode userNode : usersNode) {
                userItems.add(mapJsonToUserItem(userNode));
            }
        }

        return userItems;
    }

    public static UserItem mapJsonToUserItem(JsonNode userNode) {
        UserItem userItem = new UserItem();
        String deptInfo = userNode.get("dept").asText(""); // "부서명 / 팀명"
        String[] deptParts = deptInfo.split("/");

        userItem.setDepartment(deptParts.length > 0 ? deptParts[0].trim() : "");
        userItem.setTeam(deptParts.length > 1 ? deptParts[1].trim() : "");
        userItem.setUsername(userNode.get("display_name").asText(""));
        userItem.setEmployeeId(userNode.get("id").asText(""));
        userItem.setPosition(""); // 직위 정보
        userItem.setEmail(userNode.get("email").asText(""));
        userItem.setPhone("");
        userItem.setMobile(""); // 내선번호
        userItem.setHireDate(userNode.get("user_created_at").asText(""));
        userItem.setEmploymentStatus(userNode.get("status").asText(""));
        userItem.setUserRole(userNode.get("role_id").asText(""));
        userItem.setActiveStatus(userNode.get("status").asText(""));
        userItem.setZoomLicense(String.valueOf(userNode.get("type").asInt()));
        userItem.setPhoneLicense(""); // 줌폰 라이센스 없으면 기본값 설정

        // 추가 정보 처리
        Map<String, String> additionalInfo = new HashMap<>();
        additionalInfo.put("lastLoginTime", userNode.get("last_login_time").asText(""));
        additionalInfo.put("language", userNode.get("language").asText(""));
        userItem.setAdditionalInfo(additionalInfo);
        return userItem;
    }
}
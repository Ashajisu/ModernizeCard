package com.test.zoom.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Map;

/**controller 어노테이션에 의해 스프링 빈으로 등록 되어야, 컴포넌트 스캔의 대상이 되어 value 어노테이션이 동작한다.**/
@Controller()
@RequestMapping("/zoom")
@PropertySource("classpath:zoom.properties")
@CrossOrigin(origins = "http://localhost:5173")
public class ZoomReqeustController {

    /** HTTP 통신을 위한 도구로 REST full API 웹 서비스와의 상호작용을 쉽게 외부 도메인에서 데이터를 가져오거나 전송할 때 사용되는 스프링 프레임워크의 클래스**/
    private final RestTemplate restTemplate = new RestTemplate();
    /** 권한요청용 base url **/
    @Value("${zoom.oauth.endpoint}")
    private String zoomOauthEndpoint;
    /** api 호출용 base url **/
    @Value("${zoom.api.base.url}")
    private String zoomApiBaseUrl;


    //    사용자 권한받기
    @Value("${ZOOM_CLIENT_ID}")
    private String zoomClientId;
    @Value("${ZOOM_CLIENT_SECRET}")
    private String zoomClientSecret;
    @Value("${ZOOM_REDIRECT_URI}")
    private String ZOOM_REDIRECT_URI;
    private String CLIENT_CREDENTIALS_ACCESS_TOKEN;
    private String AUTHORIZATION_CODE;
    private String USER_ACCESS_TOKEN;
    private String USER_REFRESH_TOKEN;

    //    서버 권한받기
    @Value("${ZOOM_SERVER_ACCOUNT_ID}")
    private String zoomAccountId;
    @Value("${ZOOM_SERVER_CLIENT_ID}")
    private String zoomServerClientId;
    @Value("${ZOOM_SERVER_CLIENT_SECRET}")
    private String zoomServerClientSecret;
    private String ACCOUNT_CREDENTIALS_ACCESS_TOKEN;

    //    사용자 권한과 서버 권한의 공동사용.
    private HttpHeaders tokenHeaders = new HttpHeaders();

    /** 사용자 권한을 받기위한 헤더 **/
    private HttpHeaders getClientHeaders() {
        //서버 권한 정보 초기화 : 동시사용 불가.
        ACCOUNT_CREDENTIALS_ACCESS_TOKEN = null;
        tokenHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        tokenHeaders.setBasicAuth(zoomClientId, zoomClientSecret, StandardCharsets.UTF_8);
        return tokenHeaders;
    }

    /** 서버 권한을 받기위한 헤더 **/
    private HttpHeaders getServerHeaders() {
        //사용자 권한 정보 초기화 : 동시사용 불가.
        CLIENT_CREDENTIALS_ACCESS_TOKEN = null;
        AUTHORIZATION_CODE = null;
        USER_ACCESS_TOKEN = null;
        USER_REFRESH_TOKEN = null;

        tokenHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        tokenHeaders.setBasicAuth(zoomServerClientId, zoomServerClientSecret, StandardCharsets.UTF_8);
        return tokenHeaders;
    }

    /** 부여받은 권한을 담은 헤더 : 사용자 권한과 서버 권한의 공동사용.**/
    private void setTokenHeaders(String token) {
        tokenHeaders = new HttpHeaders();
        tokenHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        tokenHeaders.setBearerAuth(token);
    }

    private void setModelObject(Model model) {
        model.addAttribute("clientToken", CLIENT_CREDENTIALS_ACCESS_TOKEN !=null? CLIENT_CREDENTIALS_ACCESS_TOKEN : "");
        model.addAttribute("accountToken", ACCOUNT_CREDENTIALS_ACCESS_TOKEN !=null? ACCOUNT_CREDENTIALS_ACCESS_TOKEN : "");
        model.addAttribute("authorizationCode",  AUTHORIZATION_CODE !=null? AUTHORIZATION_CODE : "");
        model.addAttribute("accessToken", USER_ACCESS_TOKEN !=null? USER_ACCESS_TOKEN : "");
        model.addAttribute("refreshToken", USER_REFRESH_TOKEN !=null? USER_REFRESH_TOKEN : "");
        model.addAttribute("isSuccess", CLIENT_CREDENTIALS_ACCESS_TOKEN!=null || ACCOUNT_CREDENTIALS_ACCESS_TOKEN!=null || USER_ACCESS_TOKEN!=null);
    }

    /** 기본화면 **/
    @GetMapping()
    public String index(Model model) {
        System.out.println("basic /zoom");
        setModelObject(model);
        return "zoom";
    }

    /** 모든 정보 초기화 **/
    @GetMapping("/resetToken")
    public String resetToken(Model model) {
        System.out.println("/resetToken");
        CLIENT_CREDENTIALS_ACCESS_TOKEN = null;
        ACCOUNT_CREDENTIALS_ACCESS_TOKEN = null;
        AUTHORIZATION_CODE = null;
        USER_ACCESS_TOKEN = null;
        USER_REFRESH_TOKEN = null;
        setModelObject(model);
        return "zoom";
    }

//  서버권한 토큰 요청

    /** 애플리케이션 소유자의 액세스 토큰입니다. 서버에서 API 에 액세스해야 할 때 (사용자 인증이 필요하지 않은 경우)
     * 새로 고침 토큰이 없습니다. 한 시간 후에 만료됩니다.
     * 계정 관리자는 이러한 앱 유형을 빌드하는 개발자가 사용할 수 있는 범위를 승인합니다.
     * 성공적인 응답
     * {"access_token":"~","token_type":"bearer","expires_in":3600,"scope":"marketplace:delete:event_subscription marketplace:read:list_event_subscriptions marketplace:update:event_subscription marketplace:write:event_subscription marketplace:write:websocket_connection","api_url":"<a href="https://api.zoom.us">...</a>"}
     * **/
    @GetMapping("/account_credentials/token")
    public ResponseEntity<String> accountCredentials() {
        String responseBody = null;
        String account_credentialsUrl = zoomOauthEndpoint + "/token";

        // 바디생성
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("grant_type", "account_credentials");
        requestBody.add("account_id", zoomAccountId);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestBody, getServerHeaders());

        try{
            ResponseEntity<String> response = restTemplate.exchange(
                    account_credentialsUrl,
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );

            System.out.println("account_credentials - response: " + response.getStatusCode() + response.getBody());

            // 토큰 저장
            responseBody = response.getBody();
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(responseBody);
            ACCOUNT_CREDENTIALS_ACCESS_TOKEN = jsonNode.get("access_token").asText();
            setTokenHeaders(ACCOUNT_CREDENTIALS_ACCESS_TOKEN);

        } catch (HttpClientErrorException e) {
            String messages = "account - error: " +
                    e.getStatusCode().value() +
                    e.getResponseBodyAsString() +
                    "tokenHeaders \n" +
                    tokenHeaders +
                    "account_credentialsUrl \n" +
                    account_credentialsUrl;
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(messages);

        } catch (JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing response");
        }
        return ResponseEntity.ok(responseBody);
    }

//    토큰을 가지고 api GET 요청.

    /** 전역변수 tokenRequestEntity 를 사용하여 GET 요청을 보냅니다.
     *
     * <br> 요청헤더에 엑세스 토큰이 필요합니다.
     * <br> -H "Authorization: Bearer USER_ACCESS_TOKEN"
     * <br> -H "Content-Type: application/x-www-form-urlencoded"
     * **/
    public String getApi(String getUrl, String attributeName, Model model) {
        String prettyJsonString = null;
        // 요청 가능여부 검증
        if(tokenHeaders == null) {
            System.out.printf("tokenHeaders is null. fail to get api %s\n", getUrl);
            return "";
        }

        try {
            HttpEntity<Map<String, Object>> tokenRequestEntity = new HttpEntity<>(tokenHeaders);
            // REST API 호출
            ResponseEntity<String> response = restTemplate.exchange(
                    getUrl,
                    HttpMethod.GET,
                    tokenRequestEntity,
                    String.class
            );

            // 응답 바디 JSON 형로 출력 : 4개의 공백으로 들여쓰기
            ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
            Object jsonObject = objectMapper.readValue(response.getBody(), Object.class);
            prettyJsonString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObject);

            System.out.println(prettyJsonString);

            // 모델에 데이터 추가
            model.addAttribute(attributeName, prettyJsonString);

        } catch (HttpClientErrorException e) {
            // 예외 처리
            model.addAttribute("messages", e.toString());
        } catch (JsonProcessingException e) {
            model.addAttribute("messages", e.toString());
        }
        setModelObject(model);
        return prettyJsonString;
    }


    /** 지정한 사용자의 정보 혹은 모든 사용자의 정보를 조회합니다.
     * <br><a href="https://developers.zoom.us/docs/api/users/#tag/users/GET/users/">...</a>{userId}
     * <br>성공시 응답 get a user: {"id":"~","first_name":"지수","last_name":"엄","display_name":"엄지수","email":"asha.jisu@gmail.com","type":1,"role_name":"Owner","pmi":8603532991,"use_pmi":false,"personal_meeting_url":"<a href="https://us04web.zoom.us/j/8603532991?pwd=uWaoSSmdvXaOHs6xYst7S3664Xa4Eu.1">...</a>","timezone":"Asia/Seoul","verified":0,"dept":"","created_at":"2020-07-12T04:17:16Z","last_login_time":"2024-12-18T05:47:59Z","last_client_version":"5.12.9.10650(win)","pic_url":"<a href="https://us04web.zoom.us/p/v2/...">...</a>","cms_user_id":"","jid":"g9zvjgl3rcq1anovgch2mg@xmpp.zoom.us","group_ids":[],"im_group_ids":[],"account_id":"~","language":"ko-KO","phone_country":"","phone_number":"","status":"active","job_title":"","cost_center":"","location":"","login_types":[1],"role_id":"0","cluster":"us04","user_created_at":"2020-07-12T04:17:16Z"}
     * <br>성공시 응답 List users : {"page_count":1,"page_number":1,"page_size":30,"total_records":1,"next_page_token":"","users":[{"id":"~","first_name":"지수","last_name":"엄","display_name":"엄지수","email":"asha.jisu@gmail.com","type":1,"pmi":8603532991,"timezone":"Asia/Seoul","verified":0,"dept":"","created_at":"2020-07-12T04:17:16Z","last_login_time":"2024-12-18T05:47:59Z","last_client_version":"5.12.9.10650(win)","pic_url":"<a href="https://us04web.zoom.us/p/v2/d0ba9e75f6f34051ceedc6ef868ddb29e10d8c34a0c16cfa232db3a0d04b91a1/d049bd1e-7fb5-46f7-ba29-5b058223a1dd-641">...</a>","language":"ko-KO","phone_number":"","status":"active","role_id":"0","user_created_at":"2020-07-12T04:17:16Z"}]}
     * **/
    @GetMapping("/users")
    public ResponseEntity<String> getUsers(@RequestParam String userId, Model model) {
        String usersUrl = zoomApiBaseUrl + "/users/" + userId;
        System.out.println(usersUrl);
        String responseBody = getApi(usersUrl, "users", model);
        return ResponseEntity.ok(responseBody);
    }

    /**사용자의 모든 스케줄러를 나열합니다. **/
    @GetMapping("/schedulers")
    public ResponseEntity<String> getScheduledMeetingIdZoomApi(@RequestParam String userId, Model model) {
        String schedulesUrl = zoomApiBaseUrl + "/users/" + userId + "/schedulers";
        System.out.println(schedulesUrl);
        String responseBody = getApi(schedulesUrl, "schedulers", model);
        return ResponseEntity.ok(responseBody);
    }

    /**사용자의 모든 회의정보를 검색합니다. **/
    @GetMapping("/meetings")
    public ResponseEntity<String> getMeetingIdZoomApi(@RequestParam String userId, Model model) {
        String meetingUrl = zoomApiBaseUrl + "/users/" + userId + "/meetings";
        System.out.println(meetingUrl);
        String responseBody = getApi(meetingUrl, "meetings", model);
        return ResponseEntity.ok(responseBody);
    }

    /** 전체 콜 목록을 조회합니다. **/
    @GetMapping("/phone/call_history")
    public ResponseEntity<String> getCallHistoriesZoomApi(Model model) {
        String callsUrl = zoomApiBaseUrl + "/phone/call_history";
        System.out.println(callsUrl);
        String responseBody = getApi(callsUrl, "logs", model);
        return ResponseEntity.ok(responseBody);
    }

    /** 콜 하나에 대한 자세한 정보를 조회합니다. **/
    @GetMapping("/phone/call_history_detail")
    public ResponseEntity<String> getCallDetailZoomApi(@RequestParam String callLogId, Model model) {
        String callUrl = zoomApiBaseUrl + "/phone/call_history_detail/" + callLogId;
        System.out.println(callUrl);
        String responseBody = getApi(callUrl, "log", model);
        return ResponseEntity.ok(responseBody);
    }
}

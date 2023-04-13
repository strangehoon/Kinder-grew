package com.sparta.finalproject.domain.attendance.service;

import com.sparta.finalproject.domain.attendance.dto.DefaultMessageDto;
import com.sparta.finalproject.global.dto.GlobalResponseDto;
import com.sparta.finalproject.global.response.CustomStatusCode;
import com.sparta.finalproject.global.response.exceptionType.AttendanceException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static com.sparta.finalproject.global.response.CustomStatusCode.MESSAGE_NOT_TRANSPORT;

@Service
public class MessageService extends HttpCallService{

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String MSG_FRIEND_SEND_SERVICE_URL = "https://kapi.kakao.com/v1/api/talk/friends/message/default/send";
    private static final String MSG_FRIEND_LIST_SERVICE_URL = "https://kapi.kakao.com//v1/api/talk/friends";
    private static final String SEND_SUCCESS_MSG = "메시지 전송에 성공했습니다.";
    private static final String SEND_FAIL_MSG = "메시지 전송에 실패했습니다.";

    private static final String SUCCESS_CODE = "0";
    public boolean sendToFriendMessage(String accessToken, DefaultMessageDto msgDto, Long kakaoId) {

        JSONObject linkObj = new JSONObject();
        linkObj.put("web_url", msgDto.getWebUrl());
        linkObj.put("mobile_web_url", msgDto.getMobileUrl());

        JSONObject templateObj = new JSONObject();
        templateObj.put("object_type", msgDto.getObjType());
        templateObj.put("text", msgDto.getText());
        templateObj.put("link", linkObj);
        templateObj.put("button_title", msgDto.getBtnTitle());

        HttpHeaders header = new HttpHeaders();
        header.set("Content-Type", APP_TYPE_URL_ENCODED);
        header.set("Authorization", "Bearer " + accessToken);

        HttpEntity<?> friendListRequestEntity = httpClientEntity(header, null);
        ResponseEntity<String> responseByFrined = httpRequest(MSG_FRIEND_LIST_SERVICE_URL, HttpMethod.GET, friendListRequestEntity);
        JSONObject jsonDataByFriend = new JSONObject(responseByFrined.getBody());
        String count = jsonDataByFriend.get("total_count").toString();
        JSONArray friendss = jsonDataByFriend.getJSONArray("elements");

        Long friendKakaoId = kakaoId;
        String uuid = null;
        for(int i = 0; i < friendss.length(); i++){
            JSONObject friend = friendss.getJSONObject(i);
            if(friendKakaoId == friend.getLong("id")){
                uuid =  friend.getString("uuid");
                break;
            }
        }
        if(uuid == null){
            return false;
        }

        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("template_object", templateObj.toString());
        JSONArray jsonArray = new JSONArray();
        jsonArray.put(uuid);
        parameters.add("receiver_uuids", jsonArray.toString());
        HttpEntity<?> messageRequestEntity = httpClientEntity(header, parameters);
        String resultCode = "";
        ResponseEntity<String> response = httpRequest(MSG_FRIEND_SEND_SERVICE_URL, HttpMethod.POST, messageRequestEntity);
        JSONObject jsonData = new JSONObject(response.getBody());
        resultCode = jsonData.get("successful_receiver_uuids").toString();

        return successCheck(resultCode);
    }

    public boolean successCheck(String resultCode) {
        if(resultCode.equals(SUCCESS_CODE)) {
            logger.info(SEND_SUCCESS_MSG);
            return true;
        }else {
            logger.debug(SEND_FAIL_MSG);
            return false;
        }

    }
}

package com.sparta.finalproject.domain.attendance.service;

import com.sparta.finalproject.domain.attendance.dto.DefaultMessageDto;
import com.sparta.finalproject.global.enumType.AttendanceStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;

@Service
public class CustomMessageService {

    @Autowired
    MessageService messageService;

    public boolean sendToFriendMessage(AttendanceStatus status, String token, String kindergartenName,
                                       String childName, String enterTime, String exitTime, Long kakaoId) {
        DefaultMessageDto myMsg = new DefaultMessageDto();
        myMsg.setBtnTitle(kindergartenName + " 유치원 바로가기");
        myMsg.setMobileUrl("");
        myMsg.setObjType("text");
        myMsg.setWebUrl("");
        myMsg.setText("[" + status +" 알림톡]\n" +
                childName + " 어린이가 " + status + "했습니다.\n" +
                "일시 : " + LocalDate.now() + "\n" +
                "등원 시간 : " + enterTime + "\n" +
                "하원 시간 : " + exitTime + "\n");

        System.out.println("myMsg.getText() = " + myMsg.getText());
        return messageService.sendToFriendMessage(token, myMsg, kakaoId);
    }

    public boolean sendToFriendMessage(AttendanceStatus status, String token, String kindergartenName, String childName, Long kakaoId) {
        DefaultMessageDto myMsg = new DefaultMessageDto();
        myMsg.setBtnTitle(kindergartenName + " 유치원 바로가기");
        myMsg.setMobileUrl("");
        myMsg.setObjType("text");
        myMsg.setWebUrl("");
        myMsg.setText("[" + status +" 알림톡]\n" +
                childName + " 학부모님.\n" +
                "위 " + status + " 알림 메시지는 저희 유치원 측 실수로 인하여 전송된 잘못된 메시지입니다. \n" +
                "실수로 인한 불편을 드려서 정말 죄송합니다.");

        System.out.println("myMsg.getText() = " + myMsg.getText());
        return messageService.sendToFriendMessage(token, myMsg, kakaoId);
    }
}

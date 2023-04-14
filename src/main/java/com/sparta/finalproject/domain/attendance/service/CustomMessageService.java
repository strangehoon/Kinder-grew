package com.sparta.finalproject.domain.attendance.service;

import com.sparta.finalproject.domain.attendance.dto.DefaultMessageDto;
import com.sparta.finalproject.global.enumType.AttendanceStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;

import static com.sparta.finalproject.global.enumType.AttendanceStatus.등원;
import static com.sparta.finalproject.global.enumType.AttendanceStatus.하원;

@Service
public class CustomMessageService {

    @Autowired
    MessageService messageService;

    public boolean sendToFriendMessage(AttendanceStatus status, String token, String kindergartenName,
                                       String childName, String enterTime, String exitTime, Long kakaoId) {
        DefaultMessageDto myMsg = new DefaultMessageDto();
        myMsg.setBtnTitle(kindergartenName + " 유치원 바로가기");
        myMsg.setMobileUrl("https://front-omega-vert.vercel.app/login");
        myMsg.setObjType("text");
        myMsg.setWebUrl("https://front-omega-vert.vercel.app/login");
        if(status.equals(등원)){
            myMsg.setText("[" + status +" 알림톡]\n" +
                    childName + " 어린이가 " + status + "했습니다.\n" +
                    "일시 : " + LocalDate.now() + "\n" +
                    "등원 시간 : " + enterTime + "\n" +
                    "하원 시간 :   \n");
        }
        else if(status.equals(하원)){
            myMsg.setText("[" + status +" 알림톡]\n" +
                    childName + " 어린이가 " + status + "했습니다.\n" +
                    "일시 : " + LocalDate.now() + "\n" +
                    "등원 시간 : " + enterTime + "\n" +
                    "하원 시간 : " + exitTime + "\n");
        }

        return messageService.sendToFriendMessage(token, myMsg, kakaoId);
    }

    public boolean sendToFriendMessage(AttendanceStatus status, String token, String kindergartenName, String childName, Long kakaoId) {
        DefaultMessageDto myMsg = new DefaultMessageDto();
        myMsg.setBtnTitle(kindergartenName + " 유치원 바로가기");
        myMsg.setMobileUrl("https://front-omega-vert.vercel.app/login");
        myMsg.setObjType("text");
        myMsg.setWebUrl("https://front-omega-vert.vercel.app/login");
        myMsg.setText("[" + status +" 알림톡]\n" +
                childName + " 학부모님.\n" +
                "위 " + status + " 알림 메시지는 저희 유치원 측 실수로 인하여 잘못 전송된 메시지입니다. 불편을 드려서 정말 죄송합니다.");

        return messageService.sendToFriendMessage(token, myMsg, kakaoId);
    }
}

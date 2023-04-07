package com.sparta.finalproject.controller;


import com.sparta.finalproject.domain.child.controller.ChildController;
import com.sparta.finalproject.domain.child.service.ChildService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

//@SpringBootTest @WebMvcTest는 Contorller를 테스트할때 사용한다.
//@SpringBootTest는 Bean으로 등록된 객체를 모두 메모리에 올린다.
//@WebTest는 테스트에 필요한 Bean을 직접 세팅해서 사용한다.
//참조 : https://astrid-dm.tistory.com/536
@WebMvcTest(ChildController.class)
public class ChildControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ChildService childService;
}
//    @InjectMocks
//    private ChildController childController;
//
//    //가짜 객체를 만들어 반환해주는 어노테이션
//    @Mock
//    private ChildService childService;


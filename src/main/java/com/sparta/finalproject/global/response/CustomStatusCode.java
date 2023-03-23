package com.sparta.finalproject.global.response;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CustomStatusCode {

    //Global
    NOT_VALID_REQUEST(400,  "유효하지 않은 요청입니다."),
    NOT_VALID_TOKEN(400,"유효한 토큰이 아닙니다."),
    TOKEN_NOT_FOUND(400,  "토큰이 없습니다."),

    //Success

    //User 관련
    SIGN_UP_SUCCESS(200, "회원 가입에 성공했습니다"),
    LOG_IN_SUCCESS(200, "로그인되었습니다."),

    //Classroom 관련
    CREATE_CLASSROOM_SUCCESS(200, "반이 생성되었습니다."),
    GET_CLASSROOM_SUCCESS(200,"반이 로드되었습니다."),
    SET_TEACHER_SUCCESS(200,"담임 선생님 정보가 설정되었습니다."),

    //ImagePost 관련
    CREATE_IMAGE_POST_SUCCESS(200,"사진 게시글이 등록되었습니다."),
    GET_IMAGE_LIST_SUCCESS(200,"사진 목록이 로드되었습니다."),
    GET_IMAGE_POST_PAGE_SUCCESS(200, "사진 게시글 목록이 로드되었습니다."),
    DELETE_IMAGE_POST_SUCCESS(200, "사진 게시글이 삭제되었습니다."),


    // child 관련
    GET_CHILDREN_SUCCESS(200, "반 별 아이들 목록이 로드되었습니다."),
    CREATE_CHILD_SUCCESS(200, "아이 정보가 생성되었습니다."),
    UPDATE_CHILD_SUCCESS(200, "아이 정보가 수정되었습니다."),
    SEARCH_CHILD_SUCCESS(200, "검색한 아이 정보가 로드되었습니다."),
    GET_CHILD_PROFILE_SUCCESS(200, "아이 정보가 로드되었습니다."),
    UPDATE_CHILD_ATTENDANCE_TIME_SUCCESS(200, "아이의 등하원 시간이 변경 되었습니다."),

    // absent 관련
    CREATE_ABSENT_SUCCESS(200, "결석 신청이 완료되었습니다. "),


    // exception
    CLASSROOM_NOT_FOUND(400, "반을 찾을 수 없습니다."),
    SET_TEACHER_INFO_FAIL(400, "선생님 정보가 설정되지 않았습니다."),
    IMAGE_POST_NOT_FOUND(400, "사진 게시글을 찾을 수 없습니다."),
    CHILD_NOT_FOUND(400, "아이를 찾을 수 없습니다."),
    FilE_CONVERT_FAIL(400, "파일 전환에 실패했습니다."),
    FILE_DELETE_FAIL(400, "파일 삭제에 실패했습니다.");




    private final int statusCode;
    private final String message;
}

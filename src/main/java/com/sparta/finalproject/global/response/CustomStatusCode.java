package com.sparta.finalproject.global.response;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CustomStatusCode {

    // Token 관련
    ACCESS_TOKEN_REISSUANCE_SUCCESS(200, "AccessToken 재발급이 완료 되었습니다."),
    REFRESH_TOKEN_NOT_FOUND(400,"유효한 RefreshToken이 없습니다"),
    ACCESS_TOKEN_EXPIRARION(401, "토큰이 만료되었습니다."),

    //User 관련
    SIGN_UP_SUCCESS(200, "회원 가입에 성공했습니다."),
    ESSENTIAL_INFO_EMPTY(200, "추가적인 정보 입력이 필요합니다."),
    ESSENTIAL_INFO_EXIST(201, "추가적인 정보가 이미 존재합니다."),
    APPROVAL_WAIT(202, "회원가입 승인 요청을 기다리고 있습니다"),
    KINDERGARTEN_INFO_EMPTY(203, "유치원을 생성해주세요."),
    REQUEST_SIGNUP_SUCCESS(200, "회원가입 요청이 완료되었습니다."),
    INSERT_PRINCIPAL_INFO_SUCCESS(200, "원장 선생님 추가 정보가 입력되었습니다."),
    PROFILE_INFO_GET_SUCCESS(200, "유저 프로필 정보 조회 성공."),
    FIND_TEACHER_LIST_SUCCESS(200, "선생님 목록이 조회되었습니다."),
    PROFILE_INFO_CHANGE_SUCCESS(200, "프로필 정보 수정 완료."),
    MODIFY_CLASSROOM_TEACHER_SUCCESS(200, "담임 선생님이 설정되었습니다."),
    USER_REJECTED(200, "회원 가입 요청이 거절되었습니다."),
    FIND_MEMBER_PAGE_SUCCESS(200, "멤버 관리 페이지가 조회되었습니다."),
    USER_AUTHORIZED(200, "회원 가입 요청이 승인되었습니다."),
    SEARCH_PARENT_SUCCESS(200, "검색한 학부모 목록이 로드되었습니다."),
    REMOVE_SUCCESS(200, "회원 탈퇴가 완료 되었습니다."),
    UNAUTHORIZED_USER(400, "인가되지 않은 사용자입니다."),
    USER_NOT_FOUND(400, "사용자를 찾을 수 없습니다."),
    DIFFERENT_ROLE(400, "권한이 없습니다."),
    TEACHER_NOT_FOUND(400, "선생님을 찾을 수 없습니다."),
    DUPLICATE_EMAIL(401, "이미 존재하는 이메일입니다."),
    DUPLICATE_PHONE_NUMBER(402, "이미 존재하는 연락처 혹은 비상 연락처입니다."),

    //Classroom 관련
    ADD_CLASSROOM_SUCCESS(200, "반이 생성되었습니다."),
    FIND_CLASSROOM_SUCCESS(200, "반이 로드되었습니다."),
    MODIFY_CLASSROOM_SUCCESS(200, "반 수정이 완료되었습니다."),
    CLASSROOM_LIST_SUCCESS(200, "반 리스트 정보들이 로드되었습니다."),
    CLASSROOM_NOT_EXIST(200, "반이 존재하지 않습니다."),
    REMOVE_CLASSROOM_SUCCESS(200, "반이 삭제되었습니다."),
    CLASSROOM_NOT_FOUND(400, "반을 찾을 수 없습니다."),
    CLASSROOM_NAME_EMPTY(400, "반 이름을 입력해주세요."),
    CLASSROOM_NAME_DUPLICATE(400, "반 이름이 중복입니다"),

    //ImagePost 관련
    ADD_IMAGE_POST_SUCCESS(200, "사진 게시글이 등록되었습니다."),
    FIND_IMAGE_LIST_SUCCESS(200, "사진 목록이 로드되었습니다."),
    FIND_IMAGE_POST_PAGE_SUCCESS(200, "사진 게시글 목록이 로드되었습니다."),
    DELETE_IMAGE_POST_SUCCESS(200, "사진 게시글이 삭제되었습니다."),
    IMAGE_POST_NOT_FOUND(400, "사진 게시글을 찾을 수 없습니다."),

    // child 관련
    ADD_CHILD_SUCCESS(200, "아이 정보가 생성되었습니다."),
    FIND_CHILDREN_SUCCESS(200, "아이들 목록이 로드되었습니다."),
    FIND_CHILD_SUCCESS(200, "아이 정보가 로드되었습니다."),
    MODIFY_CHILD_SUCCESS(200, "아이 정보가 수정되었습니다."),
    SEARCH_CHILD_SUCCESS(200, "검색한 아이 정보가 로드되었습니다."),
    GET_CHILD_ATTENDANCE_TIME(200, "아이의 등하원 시간이 로드되었습니다."),
    UPDATE_CHILD_ATTENDANCE_TIME_SUCCESS(200, "아이의 등하원 시간이 변경 되었습니다."),
    CHILD_NOT_FOUND(400, "아이를 찾을 수 없습니다."),

    // attendance 관련
    CHILD_ENTER_SUCCESS(200, "등원 처리가 완료되었습니다."),
    CHILD_ENTER_CANCEL(200, "등원 처리가 취소되었습니다."),
    CHILD_EXIT_SUCCESS(200, "하원 처리가 완료되었습니다."),
    CHILD_EXIT_CANCEL(200, "하원 처리가 취소되었습니다."),
    MONTH_ATTENDANCE_LIST_SUCCESS(200, "해당 반의 월별 출결 내역이 조회되었습니다."),
    DATE_ATTENDANCE_LIST_SUCCESS(200, "반 별 해당 날짜의 출결 내역이 조회되었습니다."),
    CHILD_MONTH_ATTENDANCE_SUCCESS(200, "자녀의 월별 출결 내역이 조회되었습니다."),
    LOAD_MANAGER_PAGE_SUCCESS(200, "관리자 페이지가 로드되었습니다."),
    NOT_FOUND_ATTENDANCE(400, "출결 정보를 찾을 수 없습니다."),
    MESSAGE_NOT_TRANSPORT(400, "아이의 학부모에게 메시지를 보낼 수 없습니다."),

    // absent 관련
    ADD_ABSENT_SUCCESS(200, "결석 신청이 완료되었습니다. "),
    DELETE_ABSENT_SUCCESS(200, "결석 신청이 취소되었습니다. "),
    INVALID_ABSENT_CANCEL_REQUEST(200, "잘못된 결석 취소 요청입니다."),
    HOLIDAY_ABSENT_NOT_ADD(400, "일요일을 시작일 혹은 끝일로 지정할 수 없습니다."),
    INVALID_ABSENT_ADD_REQUEST(400, "잘못된 결석 신청 요청입니다."),

    // kindergarten 관련
    SEARCH_KINDERGARTEN_SUCCESS(200, "검색 결과가 조회되었습니다."),
    SELECT_KINDERGARTEN_SUCCESS(200, "유치원 선택이 완료되었습니다."),
    KINDERGARTEN_NOT_FOUND(400, "유치원을 찾을 수 없습니다.");

    private final int statusCode;
    private final String message;
}

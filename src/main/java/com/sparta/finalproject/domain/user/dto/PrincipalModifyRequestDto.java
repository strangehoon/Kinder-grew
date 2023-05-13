package com.sparta.finalproject.domain.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class PrincipalModifyRequestDto extends CommonGetProfileImageRequestDto {

    @NotNull
    @Pattern(regexp = "^[a-zA-Zㄱ-ㅎㅏ-ㅣ가-힣]+([ ][a-zA-Zㄱ-ㅎㅏ-ㅣ가-힣]+){0,14}$", message = "빈 공간이 아닌, 특수문자를 제외하고 15자 이내로 입력 해주세요.")
    private String name;
    @NotNull
    @Pattern(regexp = "^01(?:0|1|[6-9])-\\d{3,4}-\\d{4}$", message = "빈 공간이 아닌, 01X-XXXX-XXXX 형태의 휴대폰 전화번호를 입력 해주세요.")
    private String phoneNumber;
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;
    @Pattern(regexp = "^(?:(?:[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,})|(?:))$", message = "address@example.com과 같은 이메일 형식으로 작성해주세요.")
    private String email;
}

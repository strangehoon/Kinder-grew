package com.sparta.finalproject.domain.child.dto;

import com.sparta.finalproject.domain.user.dto.CommonGetProfileImageRequestDto;
import com.sparta.finalproject.global.enumType.Gender;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class ChildRequestDto {
    private Long parentId;
    @NotNull
    @Size(max = 6, message = "이름은 6글자를 넘을 수 없습니다.")
    private String name;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birth;
    private Gender gender;
    private String significant;
    private MultipartFile image;
    private String dailyEnterTime;
    private String dailyExitTime;
    private boolean isCancelled;
}

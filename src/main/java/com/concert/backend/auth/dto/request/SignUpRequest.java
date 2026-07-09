package com.concert.backend.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record SignUpRequest(
        @NotBlank(message = "회원가입: 이메일은 필수입니다.")
        @Email(message = "회원가입: 올바른 이메일 형식이 아닙니다.")
        String email,

        @NotBlank(message = "회원가입: 비밀번호는 필수입니다.")
        @Size(min = 8, max = 20, message = "회원가입: 비밀번호는 8자 이상 20자 이하입니다.")
        @Pattern(
                regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[^A-Za-z\\d])[A-Za-z\\d\\W_]{8,20}$",
                message = "회원가입: 비밀번호는 영문, 숫자, 특수문자를 각각 1개 이상 포함해야 합니다."
        )
        String password,

        @NotBlank(message = "회원가입: 이름은 필수입니다.")
        @Size(min = 2, max = 50, message = "회원가입: 이름은 2자 이상 50자 이하입니다.")
        @Pattern(
                regexp = "^[가-힣A-Za-z]{2,50}$",
                message = "회원가입: 이름은 한글, 영문만 사용할 수 있습니다."
        )
        String name,

        @NotBlank(message = "회원가입: 휴대전화번호는 필수입니다.")
        @Pattern(
                regexp = "^010\\d{8}$",
                message = "회원가입: 휴대전화번호는 010으로 시작하는 11자리 숫자여야 합니다."
        )
        String phone
) {

}

package com.kbj.shop.domain;

import lombok.*;

import javax.validation.constraints.NotEmpty;

@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class MemberDto {

    @NotEmpty(message = "회원 이름은 필수 입니다.")
    private String name;
    private String city;
    private String street;
    private String zipCode;

}

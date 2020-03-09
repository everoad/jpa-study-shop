package com.kbj.shop.domain;

import lombok.*;

import javax.persistence.Embeddable;

// Embeddable을 사용할 경우 기본 생성자는 protected가 안전하다.
@Embeddable @Getter @Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Address {

    private String city;
    private String street;
    private String zipCode;

}

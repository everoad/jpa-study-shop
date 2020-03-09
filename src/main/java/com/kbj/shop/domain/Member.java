package com.kbj.shop.domain;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @Column(unique = true)
    private String name;

    @Embedded
    private Address address;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private List<Order> orders = new ArrayList<>();


    //==생성 메서드==//

    /**
     * Member Entity 생성
     * @param memberDto
     * @return
     */
    public static Member createEntity(MemberDto memberDto) {
        Member member = new Member();
        member.name = memberDto.getName();
        member.address = new Address(memberDto.getCity(), memberDto.getStreet(), memberDto.getZipCode());
        return member;
    }
}

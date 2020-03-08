package com.kbj.shop.service;

import com.kbj.shop.domain.Member;
import com.kbj.shop.repository.MemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;

    @Test
    public void 회원가입() throws Exception {
        // Given
        Member member = Member.builder()
                .name("kim")
                .build();

        // When
        Long savedId = memberService.join(member);

        // Then
        assertThat(member).isEqualTo(memberRepository.findById(savedId));
    }


    @Test
    public void 중복_회원_예외() throws Exception {
        // Given
        Member member1 = Member.builder()
                .name("kim")
                .build();

        Member member2 = Member.builder()
                .name("kim")
                .build();

        // When & Then
        assertThrows(IllegalStateException.class, () -> {
            memberService.join(member1);
            memberService.join(member2);
        });
    }


    @Test
    public void 회원_리스트_조회() throws Exception {
        // Given
        IntStream.range(0, 30).forEach(this::generateMember);

        // When
        List<Member> members = memberService.findMembers();

        // Then
        assertThat(members.size()).isEqualTo(30);
    }


    @Test
    public void 회원_한명_조회() throws Exception {
        // Given
        Member member = Member.builder().name("kim").build();
        memberRepository.save(member);

        // When
        Member findMember = memberService.findOne(member.getId());

        // Then
        assertThat(member).isEqualTo(findMember);
    }


    private void generateMember(int i) {
        Member member = Member.builder()
                .name("kim" + i)
                .build();
        memberRepository.save(member);
    }
}
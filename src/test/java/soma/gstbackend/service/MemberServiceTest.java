package soma.gstbackend.service;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import soma.gstbackend.domain.Member;
import soma.gstbackend.util.JwtUtil;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MemberServiceTest {
    @Autowired MemberService memberService;
    @Autowired JwtUtil jwtUtil;

    @Value("${test.test-account}")
    private String testAccount;

    @Value("${test.test-email}")
    private String testEmail;

    @Test
    @DisplayName("테스트 변수 검사")
    @Order(1)
    public void checkTestVariables() throws Exception {
        // given

        // when

        // then
        assertFalse(memberService.checkAccount(testAccount));
        assertFalse(memberService.checkEmail(testEmail));
    }

    @Test
    @DisplayName("회원가입")
    void join() throws Exception {
        // given
        Member testMember = Member.builder()
                .account(testAccount).email(testEmail).password("12345678").build();

        // when
        Map<String, Object> tokens =  memberService.join(testMember);

        // then
        assertTrue(memberService.findMember(testMember.getId()).equals(testMember));
    }

    @Test
    @DisplayName("로그인")
    void login() throws Exception {
        // given
        Member testMember = Member.builder()
                .account(testAccount).email(testEmail).password("12345678").build();
        Member testMember2 = Member.builder()
                .email(testEmail).password("12345678").build();

        // when
        Map<String, Object> tokens;
        tokens =  memberService.join(testMember);
        tokens = memberService.login(testMember2);

        // then
        // nothing
    }

    @Test
    @DisplayName("정보 수정")
    public void modify() throws Exception {
        // given

        // when

        // then

    }

    @Test
    @DisplayName("내 정보")
    void getInfo() throws Exception {
        // given
        Member testMember = Member.builder()
                .account(testAccount).email(testEmail).phoneNumber("010-1234-5678").password("12345678").build();

        // when
        Map<String, Object> tokens = memberService.join(testMember);
        Long memberId = jwtUtil.getIdFromToken("Bearer " + tokens.get("accessToken").toString());
        Member memberInfo = memberService.getInfo(memberId);

        // then
        assertTrue(memberInfo.getAccount() == testMember.getAccount());
        assertTrue(memberInfo.getEmail() == testMember.getEmail());
        assertTrue(memberInfo.getPhoneNumber() == testMember.getPhoneNumber());
    }

    @Test
    @DisplayName("유저 찾기")
    void findMember() throws Exception{
        // given
        Member testMember = Member.builder()
                .account(testAccount).email(testEmail).phoneNumber("010-1234-5678").password("12345678").build();

        // when
        Map<String, Object> tokens = memberService.join(testMember);
        Member member = memberService.findMember(testMember.getId());

        // then
        assertTrue(member.equals(testMember));
    }

    @Test
    @DisplayName("계정 삭제")
    void removeMember() throws Exception{
        // given
        Member testMember = Member.builder()
                .account(testAccount).email(testEmail).phoneNumber("010-1234-5678").password("12345678").build();

        // when
        Map<String, Object> tokens = memberService.join(testMember);
        Member member = memberService.findMember(testMember.getId());
        memberService.removeMember(testMember.getId());
        Member removedMember = memberService.findMember(testMember.getId());

        // then
        assertTrue(member.equals(testMember));
        assertTrue(removedMember == null);
    }

    @Test
    @DisplayName("이메일 중복 체크")
    void checkEmail() throws Exception {
        // given
        Member testMember = Member.builder()
                .account(testAccount).email(testEmail).phoneNumber("010-1234-5678").password("12345678").build();

        // when
        Boolean result = memberService.checkEmail(testEmail);
        Map<String, Object> tokens = memberService.join(testMember);
        Boolean result2 = memberService.checkEmail(testEmail);

        // then
        assertFalse(result);
        assertTrue(result2);
    }

    @Test
    @DisplayName("account 중복 체크")
    void checkAccount() throws Exception {
        // given
        Member testMember = Member.builder()
                .account(testAccount).email(testEmail).phoneNumber("010-1234-5678").password("12345678").build();

        // when
        Boolean result = memberService.checkAccount(testAccount);
        Map<String, Object> tokens = memberService.join(testMember);
        Boolean result2 = memberService.checkAccount(testAccount);

        // then
        assertFalse(result);
        assertTrue(result2);
    }
}
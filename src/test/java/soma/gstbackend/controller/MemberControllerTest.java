package soma.gstbackend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import soma.gstbackend.domain.Member;
import soma.gstbackend.domain.Role;
import soma.gstbackend.dto.member.LoginDTO;
import soma.gstbackend.dto.member.MemberModifyRequest;
import soma.gstbackend.dto.member.MemberRequest;
import soma.gstbackend.dto.token.TokenDTO;
import soma.gstbackend.dto.token.TokenInfoDTO;
import soma.gstbackend.service.MemberService;
import soma.gstbackend.util.JwtUtil;

import javax.servlet.http.Cookie;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static soma.gstbackend.ApiDocumentUtils.getDocumentRequest;
import static soma.gstbackend.ApiDocumentUtils.getDocumentResponse;

@WebMvcTest(MemberController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
@AutoConfigureMockMvc(addFilters = false)
class MemberControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;

    @MockBean MemberService memberService;
    @MockBean JwtUtil jwtUtil;

    @Test
    @DisplayName("회원가입")
    void signUp() throws Exception {
        //given
        MemberRequest request = new MemberRequest("test-member", "test-password", "010-1234-5678", "test@test.com");

        TokenDTO tokens = new TokenDTO(
                "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6ODgsImV4cCI6MTY2Mjk4ODcwMX0.L8OlWRqnlsZTzUDAi8RhkiCqdGRmigjjRTlnFVYcBMo",
                "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6ODgsImV4cCI6MTY2MzA3MTUwMX0.3rtD3AFDSXO5cVKO4ooPbesEALe1DJ1d5OzgjJt-Z7A"
        );

        given(memberService.join(any()))
                .willReturn(tokens);

        //when
        ResultActions result = this.mockMvc.perform(
                post("/members/signup")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        );

        //then
        result.andExpect(status().isOk())
                .andDo(document("members-signup",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestFields(
                                fieldWithPath("account").description("계정 이름"),
                                fieldWithPath("password").description("계정 비밀번호"),
                                fieldWithPath("phoneNumber").description("전화번호"),
                                fieldWithPath("email").description("이메일")
                        ),
                        responseHeaders(
                                headerWithName("Set-Cookie").description("리프레쉬 토큰 쿠키로 등록")
                        ),
                        responseFields(
                                fieldWithPath("accessToken").description("액세스 토큰"),
                                fieldWithPath("refreshToken").description("리프레쉬 토큰")
                        )
                ));
    }

    @Test
    @DisplayName("로그인")
    void login() throws Exception {
        //given
        LoginDTO request = LoginDTO.builder()
                .email("test-email@test.com")
                .password("test-password")
                .build();

        TokenDTO tokens = new TokenDTO(
                "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6ODgsImV4cCI6MTY2Mjk4ODcwMX0.L8OlWRqnlsZTzUDAi8RhkiCqdGRmigjjRTlnFVYcBMo",
                "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6ODgsImV4cCI6MTY2MzA3MTUwMX0.3rtD3AFDSXO5cVKO4ooPbesEALe1DJ1d5OzgjJt-Z7A"
        );
        given(memberService.login(any()))
                .willReturn(tokens);

        //when
        ResultActions result = this.mockMvc.perform(
                post("/members/login")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        );

        //then
        result.andExpect(status().isOk())
                .andDo(document("members-login",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestFields(
                                fieldWithPath("email").description("이메일"),
                                fieldWithPath("password").description("계정 비밀번호")
                        ),
                        responseHeaders(
                                headerWithName("Set-Cookie").description("리프레쉬 토큰 쿠키로 등록")
                        ),
                        responseFields(
                                fieldWithPath("accessToken").description("액세스 토큰"),
                                fieldWithPath("refreshToken").description("리프레쉬 토큰")
                        )
                ));
    }

    @Test
    @DisplayName("email 중복체크")
    public void check_email() throws Exception {
        // given
        Map<String, Object> request = new HashMap<>();
        request.put("email", "test@test.com");

        given(memberService.checkEmail(any()))
                .willReturn(true);
        // when
        ResultActions result = this.mockMvc.perform(
                post("/members/check-email")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect(status().isOk())
                .andDo(document("members-check-email",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestFields(
                                fieldWithPath("email").description("중복 체크할 email")
                        ),
                        responseFields(
                                fieldWithPath("isExist").description("중복 여부 (중복이면 true)")
                        )
                ));
    }

    @Test
    @DisplayName("account 중복체크")
    public void check_account() throws Exception {
        // given
        Map<String, Object> request = new HashMap<>();
        request.put("account", "testAccount");

        given(memberService.checkAccount(any()))
                .willReturn(true);
        // when
        ResultActions result = this.mockMvc.perform(
                post("/members/check-account")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect(status().isOk())
                .andDo(document("members-check-account",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestFields(
                                fieldWithPath("account").description("중복 체크할 account")
                        ),
                        responseFields(
                                fieldWithPath("isExist").description("중복 여부 (중복이면 true)")
                        )
                ));
    }

    @Test
    public void modify() throws Exception {
        // given
        MemberModifyRequest request = new MemberModifyRequest("test-account", "010-1234-5678", "test@test.com");

        doNothing().when(memberService).modify(any(), any());
        given(jwtUtil.getInfoFromToken(any()))
                .willReturn(new TokenInfoDTO(0L, Role.GUEST.getKey()));

        // when
        ResultActions result = this.mockMvc.perform(
                put("/members")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect(status().isOk())
                .andDo(document("members-modify",
                getDocumentRequest(),
                getDocumentResponse(),
                requestFields(
                        fieldWithPath("account").description("수정할 account 이름"),
                        fieldWithPath("email").description("수정할 email"),
                        fieldWithPath("phoneNumber").description("수정할 전화번호")
                )
        ));
    }

    @Test
    @DisplayName("토큰 재발급")
    @Disabled
    public void refresh() throws Exception {
        // given
        String testAccessToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6ODgsImV4cCI6MTY2Mjk4ODcwMX0.L8OlWRqnlsZTzUDAi8RhkiCqdGRmigjjRTlnFVYcBMo";
        String testRefreshToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6ODgsImV4cCI6MTY2MzA3MTUwMX0.3rtD3AFDSXO5cVKO4ooPbesEALe1DJ1d5OzgjJt-Z7A";


        doNothing().when(jwtUtil).validateRefreshToken(any());
        given(jwtUtil.getAccessToken(any(), any(), anyInt()))
                .willReturn(testAccessToken);

        Cookie cookie = new Cookie(
                "refreshToken",
                testRefreshToken + "; Path=/; Secure; HttpOnly; Expires=Mon, 03 Oct 2022 14:17:33 GMT;"
        );

        // when
        ResultActions result = this.mockMvc.perform(
                post("/members/refresh")
                        .cookie(cookie)
        );

        // then
        result.andExpect(status().isOk())
                .andDo(document("members-refresh",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        responseFields(
                                fieldWithPath("accessToken").description("액세스 토큰")
                        )
                ));
    }

    @Test
    @DisplayName("내 정보")
    void myInfo() throws Exception {
        //given
        Member testMember = Member.builder()
                .account("test-member").phoneNumber("010-1234-5678").email("test@test.com").build();

        String testToken = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6ODgsImV4cCI6MTY2Mjk4ODcwMX0.L8OlWRqnlsZTzUDAi8RhkiCqdGRmigjjRTlnFVYcBMo";

        given(memberService.getInfo(any()))
                .willReturn(testMember);
        given(jwtUtil.getInfoFromToken(any()))
                .willReturn(new TokenInfoDTO(0L, Role.GUEST.getKey()));

        //when
        ResultActions result = this.mockMvc.perform(
                get("/members/info")
                        .header("Authorization", testToken)
        );

        //then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("password").doesNotExist())
                .andDo(document("members-myinfo",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestHeaders(
                                headerWithName("Authorization").description("Bearer + 액세스 토큰")
                        ),
                        responseFields(
                                fieldWithPath("account").description("계정 이름"),
                                fieldWithPath("phoneNumber").description("전화번호"),
                                fieldWithPath("email").description("이메일")
                        )
                ));
    }
}
package soma.gstbackend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import soma.gstbackend.dto.member.LoginDTO;
import soma.gstbackend.dto.member.RequestDTO;
import soma.gstbackend.dto.member.ResponseDTO;
import soma.gstbackend.service.MemberService;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

    @Test
    @DisplayName("회원가입")
    void signUp() throws Exception {
        //given
        RequestDTO request = new RequestDTO("test-member", "test-password", "010-1234-5678", "test@test.com");

        Map<String, Object> tokens = new HashMap<>();
        tokens.put("access_token", "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6ODgsImV4cCI6MTY2Mjk4ODcwMX0.L8OlWRqnlsZTzUDAi8RhkiCqdGRmigjjRTlnFVYcBMo");
        tokens.put("refresh_token", "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6ODgsImV4cCI6MTY2MzA3MTUwMX0.3rtD3AFDSXO5cVKO4ooPbesEALe1DJ1d5OzgjJt-Z7A");

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
                        responseFields(
                                fieldWithPath("access_token").description("액세스 토큰"),
                                fieldWithPath("refresh_token").description("리프레쉬 토큰")
                        )
                ));
    }

    @Test
    @DisplayName("로그인")
    void login() throws Exception {
        //given
        LoginDTO request = LoginDTO.builder()
                .account("test-member")
                .password("test-password")
                .build();

        Map<String, Object> tokens = new HashMap<>();
        tokens.put("access_token", "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6ODgsImV4cCI6MTY2Mjk4ODcwMX0.L8OlWRqnlsZTzUDAi8RhkiCqdGRmigjjRTlnFVYcBMo");
        tokens.put("refresh_token", "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6ODgsImV4cCI6MTY2MzA3MTUwMX0.3rtD3AFDSXO5cVKO4ooPbesEALe1DJ1d5OzgjJt-Z7A");

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
                                fieldWithPath("account").description("계정 이름"),
                                fieldWithPath("password").description("계정 비밀번호")
                        ),
                        responseFields(
                                fieldWithPath("access_token").description("액세스 토큰"),
                                fieldWithPath("refresh_token").description("리프레쉬 토큰")
                        )
                ));
    }

    @Test
    @DisplayName("내 정보")
    void myInfo() throws Exception {
        //given
        ResponseDTO response = new ResponseDTO("test-member", "010-1234-5678", "test@test.com");
        String testToken = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6ODgsImV4cCI6MTY2Mjk4ODcwMX0.L8OlWRqnlsZTzUDAi8RhkiCqdGRmigjjRTlnFVYcBMo";

        given(memberService.getInfo())
                .willReturn(response);

        //when
        ResultActions result = this.mockMvc.perform(
                get("/members/info")
                        .header("Authorization", testToken)
        );

        //then
        result.andExpect(status().isOk())
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
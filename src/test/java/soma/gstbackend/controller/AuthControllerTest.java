package soma.gstbackend.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.restdocs.operation.RequestCookie;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import soma.gstbackend.domain.Member;
import soma.gstbackend.dto.token.TokenDTO;
import soma.gstbackend.enums.Role;
import soma.gstbackend.service.MemberService;
import soma.gstbackend.service.OAuthService;
import soma.gstbackend.util.JwtUtil;

import javax.servlet.http.Cookie;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static soma.gstbackend.ApiDocumentUtils.getDocumentRequest;
import static soma.gstbackend.ApiDocumentUtils.getDocumentResponse;

@WebMvcTest(AuthController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
@AutoConfigureMockMvc(addFilters = false)
public class AuthControllerTest {

    @Autowired MockMvc mockMvc;
    @MockBean JwtUtil jwtUtil;
    @MockBean MemberService memberService;
    @MockBean OAuthService oAuthService;

    @Nested
    class SilentLoginTest {
        @Test
        @DisplayName("Access, Refresh 토큰 재발급")
        public void refreshTokens() throws Exception {
            // given
            String testAccessToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6ODgsImV4cCI6MTY2Mjk4ODcwMX0.L8OlWRqnlsZTzUDAi8RhkiCqdGRmigjjRTlnFVYcBMo";
            String testRefreshToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpZCI6ODgsImV4cCI6MTY2MzA3MTUwMX0.3rtD3AFDSXO5cVKO4ooPbesEALe1DJ1d5OzgjJt-Z7A";
            Member testMember = Member.builder()
                    .account("test-member").phoneNumber("010-1234-5678").email("test@test.com").role(Role.GUEST).build();
            TokenDTO tokenDTO = new TokenDTO(testAccessToken, testRefreshToken);

            doNothing().when(jwtUtil).validateRefreshToken(any());
            given(jwtUtil.getIdFromToken(any()))
                    .willReturn(0L);
            given(memberService.getInfo(any()))
                    .willReturn(testMember);
            given(jwtUtil.getTokens(any(), any()))
                    .willReturn(tokenDTO);

            Cookie cookie = new Cookie(
                    "refreshToken",
                    testRefreshToken + "; Path=/; Secure; HttpOnly; Expires=Mon, 03 Oct 2022 14:17:33 GMT;"
            );

            // when
            ResultActions result = mockMvc.perform(
                    get("/auth/silent-login")
                            .cookie(cookie)
            );

            // then
            result.andExpect(status().isOk())
                    .andDo(document("auth-silent-login",
                            getDocumentRequest(),
                            getDocumentResponse(),
                            responseHeaders(
                                    headerWithName("Set-Cookie").description("리프레쉬 토큰 쿠키로 등록")
                            ),
                            responseFields(
                                    fieldWithPath("accessToken").description("액세스 토큰")
                            )
                    ));
        }
    }
}

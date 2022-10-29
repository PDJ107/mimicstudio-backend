package soma.gstbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import soma.gstbackend.domain.Member;
import soma.gstbackend.dto.token.AccessTokenDTO;
import soma.gstbackend.dto.token.TokenDTO;
import soma.gstbackend.service.MemberService;
import soma.gstbackend.service.OAuthService;
import soma.gstbackend.util.JwtUtil;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final OAuthService oAuthService;
    private final MemberService memberService;
    private final JwtUtil jwtUtil;

    @GetMapping("/login")
    public ResponseEntity oAuthlogin(@AuthenticationPrincipal OAuth2User oAuth2User) {
        TokenDTO tokens = oAuthService.login(oAuth2User);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Set-Cookie", "refreshToken=" + tokens.getRefreshToken()
                + "; Max-Age=604800; Path=/; Secure; HttpOnly");

        return ResponseEntity.ok()
                .headers(headers)
                .body(new AccessTokenDTO(tokens.getAccessToken()));
    }

    @GetMapping("/google")
    public void googleLogin(HttpServletResponse httpServletResponse) throws IOException {
        httpServletResponse.sendRedirect("http://localhost:8080/oauth2/authorization/google");
    }

    @GetMapping("/silent-login")
    public ResponseEntity silentLogin(@CookieValue String refreshToken) throws Exception {
        String token = "Bearer " + refreshToken;
        jwtUtil.validateRefreshToken(token);

        Member member = memberService.getInfo(jwtUtil.getIdFromToken(refreshToken));
        TokenDTO tokens = jwtUtil.getTokens(member.getId(), member.getRole().getKey());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Set-Cookie", "refreshToken=" + tokens.getRefreshToken()
                + "; Max-Age=604800; Path=/; Secure; HttpOnly");

        return ResponseEntity.ok()
                .headers(headers)
                .body(new AccessTokenDTO(tokens.getAccessToken()));
    }

    @GetMapping("/token/access")
    public ResponseEntity getAccessToken(@CookieValue String refreshToken) throws Exception {

        String token = "Bearer " + refreshToken;
        jwtUtil.validateRefreshToken(token);

        Long id = jwtUtil.getIdFromToken(token);
        Member member = memberService.findMember(id);
        return ResponseEntity.ok().body(new AccessTokenDTO(jwtUtil.getAccessToken(id, member.getRole().getKey())));
    }

    @GetMapping("/token/refresh")
    public ResponseEntity getRefreshToken(@CookieValue String refreshToken) throws Exception {
        String token = "Bearer " + refreshToken;
        jwtUtil.validateRefreshToken(token);

        Member member = memberService.getInfo(jwtUtil.getIdFromToken(refreshToken));
        TokenDTO tokens = jwtUtil.getTokens(member.getId(), member.getRole().getKey());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Set-Cookie", "refreshToken=" + tokens.getRefreshToken()
                + "; Max-Age=604800; Path=/; Secure; HttpOnly");

        return ResponseEntity.ok()
                .headers(headers)
                .build();
    }
}

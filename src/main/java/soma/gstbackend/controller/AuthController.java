package soma.gstbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import soma.gstbackend.annotation.Auth;
import soma.gstbackend.dto.token.TokenInfoDTO;
import soma.gstbackend.service.OAuthService;
import soma.gstbackend.util.JwtUtil;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final OAuthService oAuthService;
    private final JwtUtil jwtUtil;

    @GetMapping("/login")
    public ResponseEntity login(@AuthenticationPrincipal OAuth2User oAuth2User) {
        return ResponseEntity.ok().body(oAuthService.login(oAuth2User));
    }

    @PostMapping("/refresh")
    public ResponseEntity tokenRefresh(@CookieValue String refreshToken) throws Exception {

        String token = "Bearer " + refreshToken;
        jwtUtil.validateRefreshToken(token);

        TokenInfoDTO tokenInfo = jwtUtil.getInfoFromToken(token);

        Map<String, Object> responseToken = new HashMap<>();
        responseToken.put("accessToken", jwtUtil.getAccessToken(tokenInfo.getId(), tokenInfo.getRole(), 1)); //
        return ResponseEntity.ok().body(responseToken);
    }
}

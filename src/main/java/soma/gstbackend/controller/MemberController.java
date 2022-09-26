package soma.gstbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import soma.gstbackend.annotation.Auth;
import soma.gstbackend.dto.member.LoginDTO;
import soma.gstbackend.dto.member.MemberRequest;
import soma.gstbackend.service.MemberService;
import soma.gstbackend.util.JwtUtil;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;
    private final JwtUtil jwtUtil;

    @PostMapping("/signup")
    public ResponseEntity signUp(@RequestBody @Valid MemberRequest request) throws Exception {
        Map<String, Object> responseToken = memberService.join(request.toEntity());

        HttpHeaders headers = new HttpHeaders();

        String refreshToken = responseToken.get("refreshToken").toString();
        responseToken.remove("refreshToken");
        headers.add("Set-Cookie", "refreshToken=" + refreshToken
                + "; Max-Age=604800; Path=/; Secure; HttpOnly");

        return ResponseEntity.ok()
                .headers(headers)
                .body(responseToken);
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid LoginDTO request) throws Exception {
        Map<String, Object> token = memberService.login(request.toEntity());

        HttpHeaders headers = new HttpHeaders();

        String refreshToken = token.get("refreshToken").toString();
        token.remove("refreshToken");
        headers.add("Set-Cookie", "refreshToken=" + refreshToken
                        + "; Max-Age=604800; Path=/; Secure; HttpOnly");

        return ResponseEntity.ok()
                .headers(headers)
                .body(token);
    }

    @PostMapping("/refresh")
    public ResponseEntity tokenRefresh(@CookieValue String refreshToken) throws Exception {

        String token = "Bearer " + refreshToken;
        jwtUtil.validateRefreshToken(token);

        Long memberId = jwtUtil.getIdFromToken(token);

        Map<String, Object> responseToken = new HashMap<>();
        responseToken.put("accessToken", jwtUtil.getAccessToken(memberId, 1));
        return ResponseEntity.ok().body(responseToken);
    }

    @Auth
    @GetMapping("/info")
    public ResponseEntity myInfo() throws Exception {
        return ResponseEntity.ok().body(memberService.getInfo());
    }
}

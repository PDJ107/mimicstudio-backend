package soma.gstbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import soma.gstbackend.annotation.Auth;
import soma.gstbackend.dto.member.*;
import soma.gstbackend.service.MemberService;
import soma.gstbackend.util.JwtUtil;

import javax.servlet.http.HttpServletRequest;
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

    @Auth
    @PutMapping
    public ResponseEntity modifyInfo(HttpServletRequest request, @RequestBody @Valid MemberModifyRequest memberModifyRequest) throws Exception {
        Long id = jwtUtil.getIdFromToken(request.getHeader("Authorization"));
        memberService.modify(id, memberModifyRequest.toEntity());
        return ResponseEntity.ok().build();
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
    public ResponseEntity myInfo(HttpServletRequest request) throws Exception {
        Long id = jwtUtil.getIdFromToken(request.getHeader("Authorization"));
        MemberResponse response = MemberResponse.from(memberService.getInfo(id));
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/check-email")
    public ResponseEntity checkEmail(@RequestBody String email) throws Exception {
        return ResponseEntity.ok().body(new MemberCheckResponse(memberService.checkEmail(email)));
    }

    @PostMapping("/check-account")
    public ResponseEntity checkAccount(@RequestBody String account) throws Exception {
        return ResponseEntity.ok().body(new MemberCheckResponse(memberService.checkAccount(account)));
    }
}

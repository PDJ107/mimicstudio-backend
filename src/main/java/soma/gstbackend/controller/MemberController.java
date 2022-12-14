package soma.gstbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import soma.gstbackend.dto.member.*;
import soma.gstbackend.dto.token.AccessTokenDTO;
import soma.gstbackend.dto.token.TokenDTO;
import soma.gstbackend.service.MemberService;
import soma.gstbackend.util.JwtUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;
    private final JwtUtil jwtUtil;

    @PostMapping("/signup")
    public ResponseEntity signUp(@RequestBody @Valid MemberRequest request, HttpServletResponse response) throws Exception {
        TokenDTO tokens = memberService.join(request.toEntity());

//        HttpHeaders headers = new HttpHeaders();
//
//        headers.add("Set-Cookie", "refreshToken=" + tokens.getRefreshToken()
//                + "; Max-Age=604800; Path=/; Secure; HttpOnly");

        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", tokens.getRefreshToken())
                .path("/")
                .secure(true)
                .httpOnly(true)
                .sameSite("None")
                .maxAge(604800)
                .build();

        response.addHeader("Set-Cookie", refreshTokenCookie.toString());

        return ResponseEntity.ok()
                .body(new AccessTokenDTO(tokens.getAccessToken()));
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid LoginDTO request, HttpServletResponse response) throws Exception {
        TokenDTO tokens = memberService.login(request.toEntity());

//        HttpHeaders headers = new HttpHeaders();
//
//        headers.add("Set-Cookie", "refreshToken=" + tokens.getRefreshToken()
//                        + "; Max-Age=604800; Path=/; Secure; HttpOnly");

        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", tokens.getRefreshToken())
                .path("/")
                .secure(true)
                .httpOnly(true)
                .sameSite("None")
                .maxAge(604800)
                .build();

        response.addHeader("Set-Cookie", refreshTokenCookie.toString());

        return ResponseEntity.ok()
                .body(new AccessTokenDTO(tokens.getAccessToken()));
    }

    @PutMapping
    public ResponseEntity modifyInfo(HttpServletRequest request, @RequestBody MemberModifyRequest memberModifyRequest) throws Exception {
        //Long id = jwtUtil.getInfoFromToken(request.getHeader("Authorization"));
        Long id = jwtUtil.getIdFromToken(request.getHeader("Authorization"));
        memberService.modify(id, memberModifyRequest.toEntity());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/me")
    public ResponseEntity myInfo(HttpServletRequest request) throws Exception {
        //Long id = jwtUtil.getIdFromToken(request.getHeader("Authorization"));
        Long id = jwtUtil.getIdFromToken(request.getHeader("Authorization"));
        MemberResponse response = MemberResponse.from(memberService.getInfo(id));
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/check-email")
    public ResponseEntity checkEmail(@RequestBody Map<String, String> requestMap) throws Exception {
        return ResponseEntity.ok().body(new MemberCheckResponse(memberService.checkEmail(requestMap.get("email"))));
    }

    @PostMapping("/check-account")
    public ResponseEntity checkAccount(@RequestBody Map<String, String> requestMap) throws Exception {
        return ResponseEntity.ok().body(new MemberCheckResponse(memberService.checkAccount(requestMap.get("account"))));
    }
}

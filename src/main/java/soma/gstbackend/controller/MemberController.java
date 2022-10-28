package soma.gstbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import soma.gstbackend.dto.member.*;
import soma.gstbackend.dto.token.AccessTokenDTO;
import soma.gstbackend.dto.token.TokenDTO;
import soma.gstbackend.dto.token.TokenInfoDTO;
import soma.gstbackend.service.MemberService;
import soma.gstbackend.util.JwtUtil;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;
    private final JwtUtil jwtUtil;

    @PostMapping("/signup")
    public ResponseEntity signUp(@RequestBody @Valid MemberRequest request) throws Exception {
        TokenDTO tokens = memberService.join(request.toEntity());

        HttpHeaders headers = new HttpHeaders();

        headers.add("Set-Cookie", "refreshToken=" + tokens.getRefreshToken()
                + "; Max-Age=604800; Path=/; Secure; HttpOnly");

        return ResponseEntity.ok()
                .headers(headers)
                .body(new AccessTokenDTO(tokens.getAccessToken()));
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid LoginDTO request) throws Exception {
        TokenDTO tokens = memberService.login(request.toEntity());

        HttpHeaders headers = new HttpHeaders();

        headers.add("Set-Cookie", "refreshToken=" + tokens.getRefreshToken()
                        + "; Max-Age=604800; Path=/; Secure; HttpOnly");

        return ResponseEntity.ok()
                .headers(headers)
                .body(new AccessTokenDTO(tokens.getAccessToken()));
    }

    @PutMapping
    public ResponseEntity modifyInfo(HttpServletRequest request, @RequestBody @Valid MemberModifyRequest memberModifyRequest) throws Exception {
        //Long id = jwtUtil.getInfoFromToken(request.getHeader("Authorization"));
        TokenInfoDTO tokenInfo = jwtUtil.getInfoFromToken(request.getHeader("Authorization"));
        memberService.modify(tokenInfo.getId(), memberModifyRequest.toEntity());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/info")
    public ResponseEntity myInfo(HttpServletRequest request) throws Exception {
        //Long id = jwtUtil.getIdFromToken(request.getHeader("Authorization"));
        TokenInfoDTO tokenInfo = jwtUtil.getInfoFromToken(request.getHeader("Authorization"));
        MemberResponse response = MemberResponse.from(memberService.getInfo(tokenInfo.getId()));
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

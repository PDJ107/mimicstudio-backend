package soma.gstbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import soma.gstbackend.annotation.Auth;
import soma.gstbackend.dto.MemberRequestDto;
import soma.gstbackend.dto.MemberResponseDto;
import soma.gstbackend.entity.Member;
import soma.gstbackend.service.MemberService;
import soma.gstbackend.util.JwtUtil;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity signUp(@RequestBody @Valid MemberRequestDto memberRequestDto) throws Exception {
        Map<String, Object> tokens = memberService.join(memberRequestDto.toEntity());
        return ResponseEntity.ok().body(tokens);
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid MemberRequestDto memberRequestDto) throws Exception {
        Map<String, Object> tokens = memberService.login(memberRequestDto.toEntity());
        return ResponseEntity.ok().body(tokens);
    }

    @Auth
    @GetMapping("/info")
    public ResponseEntity myInfo() throws Exception {
        return ResponseEntity.ok().body(memberService.getInfo());
    }
}

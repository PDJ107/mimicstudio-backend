package soma.gstbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import soma.gstbackend.annotation.Auth;
import soma.gstbackend.dto.member.LoginDTO;
import soma.gstbackend.dto.member.RequestDTO;
import soma.gstbackend.service.MemberService;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity signUp(@RequestBody @Valid RequestDTO request) throws Exception {
        Map<String, Object> tokens = memberService.join(request.toEntity());
        return ResponseEntity.ok().body(tokens);
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid LoginDTO request) throws Exception {
        Map<String, Object> tokens = memberService.login(request.toEntity());
        return ResponseEntity.ok().body(tokens);
    }

    @Auth
    @GetMapping("/info")
    public ResponseEntity myInfo() throws Exception {
        return ResponseEntity.ok().body(memberService.getInfo());
    }
}

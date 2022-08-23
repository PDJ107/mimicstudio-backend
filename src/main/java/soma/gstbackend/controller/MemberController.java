package soma.gstbackend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import soma.gstbackend.dto.MemberRequestDto;
import soma.gstbackend.service.MemberService;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/member")
    public ResponseEntity signup(@RequestBody @Valid MemberRequestDto memberRequestDto) {
        memberService.join(memberRequestDto.toEntity());
        return ResponseEntity.accepted().build();
    }
}

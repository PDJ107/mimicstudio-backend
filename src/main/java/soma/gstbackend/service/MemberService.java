package soma.gstbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import soma.gstbackend.dto.SimpleResponse;
import soma.gstbackend.dto.member.MemberResponse;
import soma.gstbackend.domain.Member;
import soma.gstbackend.exception.ErrorCode;
import soma.gstbackend.exception.MemberException;
import soma.gstbackend.repository.MemberRepository;
import soma.gstbackend.util.JwtUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;

    private final PasswordEncoder encoder;

    private final JwtUtil jwtUtil;

    public Map<String, Object> join(Member member) throws Exception {
        // account 중복 체크
        if(memberRepository.findByAccount(member.getAccount()) != null) {
            throw new MemberException(ErrorCode.Account_Already_Exists);
        }

        // email 중복 체크
        if(memberRepository.findByEmail(member.getEmail()) != null) {
            throw new MemberException(ErrorCode.Email_Already_Exists);
        }

        // 비밀번호 암호화
        String password = encoder.encode(member.getPassword());
        member.setPassword(password);

        memberRepository.save(member);
        return jwtUtil.getTokens(member.getId());
    }

    @Transactional(readOnly = true)
    public Map<String, Object> login(Member member) throws Exception {
        Member userData = memberRepository.findByEmail(member.getEmail());
        if(userData == null) {
            throw new MemberException(ErrorCode.User_Invalid_Request);
        }

        // 비밀번호 비교
        if(!encoder.matches(member.getPassword(), userData.getPassword())) {
            throw new MemberException(ErrorCode.User_Invalid_Request);
        }

        return jwtUtil.getTokens(userData.getId());
    }

    @Transactional(readOnly = true)
    public Member getInfo(Long id) throws Exception {
        return findMember(id);
    }

    @Transactional(readOnly = true)
    public Member findMember(Long memberId) { return memberRepository.findOne(memberId);}

    public void removeMember(Long memberId) {
        if(memberRepository.findOne(memberId) == null) {
            throw new MemberException(ErrorCode.Member_Not_Found);
        }
        memberRepository.remove(memberId);
    }

    @Transactional(readOnly = true)
    public Boolean checkEmail(String email) {
        return memberRepository.findByEmail(email) != null;
    }

    @Transactional(readOnly = true)
    public Boolean checkAccount(String account) {
        return memberRepository.findByAccount(account) != null;
    }
}

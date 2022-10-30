package soma.gstbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import soma.gstbackend.domain.Member;
import soma.gstbackend.dto.token.TokenDTO;
import soma.gstbackend.exception.ErrorCode;
import soma.gstbackend.exception.MemberException;
import soma.gstbackend.repository.MemberRepository;
import soma.gstbackend.util.JwtUtil;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;

    private final PasswordEncoder encoder;

    private final JwtUtil jwtUtil;

    public TokenDTO join(Member member) throws Exception {
        // account 중복 체크
        //if(memberRepository.findByAccount(member.getAccount()) != null) {
//        if(checkAccount(member.getAccount())) {
//            throw new MemberException(ErrorCode.Account_Already_Exists);
//        }

        // email 중복 체크
        if(checkEmail(member.getEmail())) {
            throw new MemberException(ErrorCode.Email_Already_Exists);
        }

        // 비밀번호 암호화
        String password = encoder.encode(member.getPassword());
        member.setPassword(password);

        memberRepository.save(member);
        return jwtUtil.getTokens(member.getId(), member.getRole().getKey());
    }

    public void modify(Long id, Member memberInfo) {
        Member member = memberRepository.findOne(id);

        // email 중복 체크
        if(!member.getEmail().equals(memberInfo.getEmail()) && checkEmail(memberInfo.getEmail())) {
            throw new MemberException(ErrorCode.Email_Already_Exists);
        }

        // account 중복 체크
        if(!member.getAccount().equals(memberInfo.getAccount()) && checkAccount(memberInfo.getAccount())) {
            throw new MemberException(ErrorCode.Account_Already_Exists);
        }

        member.setAccount(memberInfo.getAccount());
        member.setEmail(memberInfo.getEmail());
        member.setPhoneNumber(memberInfo.getPhoneNumber());
    }

    @Transactional(readOnly = true)
    public TokenDTO login(Member member) throws Exception {
        Optional<Member> userData = memberRepository.findByEmail(member.getEmail());
        if(userData.isEmpty()) {
            throw new MemberException(ErrorCode.User_Invalid_Request);
        }
        Member user = userData.get();

        // 비밀번호 비교
        if(!encoder.matches(member.getPassword(), user.getPassword())) {
            throw new MemberException(ErrorCode.User_Invalid_Request);
        }

        return jwtUtil.getTokens(user.getId(), user.getRole().getKey());
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
    public Boolean checkEmail(String email) { return memberRepository.findByEmail(email).isPresent(); }

    @Transactional(readOnly = true)
    public Boolean checkAccount(String account) {
        return memberRepository.findByAccount(account).isPresent();
    }
}

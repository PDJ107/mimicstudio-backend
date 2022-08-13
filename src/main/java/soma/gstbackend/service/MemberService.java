package soma.gstbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import soma.gstbackend.entity.Member;
import soma.gstbackend.exception.ErrorCode;
import soma.gstbackend.exception.MemberException;
import soma.gstbackend.repository.MemberRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;

    public void join(Member member) {
        memberRepository.save(member);
    }

    @Transactional(readOnly = true)
    public Member findMember(Long memberId) { return memberRepository.findOne(memberId);}

    public void removeMember(Long memberId) {
        if(memberRepository.findOne(memberId) == null) {
            throw new MemberException(ErrorCode.Member_Not_Found);
        }
        memberRepository.remove(memberId);
    }
}

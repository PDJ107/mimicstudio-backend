package soma.gstbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import soma.gstbackend.domain.Member;
import soma.gstbackend.enums.Role;
import soma.gstbackend.dto.auth.OAuthAttributes;
import soma.gstbackend.dto.token.TokenDTO;
import soma.gstbackend.repository.MemberRepository;
import soma.gstbackend.util.JwtUtil;

import javax.transaction.Transactional;
import java.util.Collections;

@Service
@RequiredArgsConstructor
@Transactional
public class OAuthService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final MemberRepository memberRepository;
    //private final HttpSession httpSession;
    private final JwtUtil jwtUtil;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2UserService delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName();

        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        //Member member = saveOrUpdate(attributes);
        Member member = attributes.toEntity();

        //httpSession.setAttribute("user", new SessionUser(member));

        return new DefaultOAuth2User(
                Collections.singleton(
                        new SimpleGrantedAuthority(member.getRole().getKey())
                ),
                attributes.getAttributes(),
                attributes.getNameAttributeKey()
        );
    }

    public TokenDTO login(OAuth2User oAuth2User) {
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        Member member = memberRepository.findByEmail(oAuth2User.getAttribute("email"))
                .map(entity -> entity.update(oAuth2User.getAttribute("name")))
                .orElse(
                        Member.builder()
                                .email(email)
                                .account(name)
                                .role(Role.GUEST).build()
                );

        memberRepository.save(member);
        return jwtUtil.getTokens(member.getId(), member.getRole().getKey());
    }
}

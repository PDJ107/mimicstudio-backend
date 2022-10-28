package soma.gstbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import soma.gstbackend.domain.AuthToken;
import soma.gstbackend.repository.AuthRepository;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {
    private final AuthRepository authRepository;

    public AuthToken findToken(Long memberId) { return authRepository.findToken(memberId); }

    public void saveOrUpdate(AuthToken authToken) {
        AuthToken tokenFromDB = findToken(authToken.getId());
        if(tokenFromDB == null) authRepository.save(authToken);
        else {
            tokenFromDB.setRefreshToken(authToken.getRefreshToken());
        }
    }
}

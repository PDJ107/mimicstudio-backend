package soma.gstbackend.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import soma.gstbackend.domain.AuthToken;
import soma.gstbackend.dto.token.TokenDTO;
import soma.gstbackend.dto.token.TokenInfoDTO;
import soma.gstbackend.exception.AuthException;
import soma.gstbackend.exception.ErrorCode;
import soma.gstbackend.service.AuthService;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Component
@RequiredArgsConstructor
public class JwtUtil {

    @Value("${jwt.secret-key}")
    private String SECRET_KEY;

    private final AuthService authService;

    private int accessExpiredHour = 1;
    private int refreshExpiredHour = 24;

    private Map<String, Object> getHeader() {
        Map<String, Object> headers = new HashMap<>();

        headers.put("typ", "JWT");
        headers.put("alg","HS256");

        return headers;
    }

    private Map<String, Object> getPayload(Long memberId) {
        Map<String, Object> payloads = new HashMap<String, Object>();
        payloads.put("id", memberId);

        return payloads;
    }

    private Date getExpireDate(int hour) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.HOUR_OF_DAY, hour);

        return calendar.getTime();
    }

    public String getAccessToken(Long memberId, String role) {

        Claims claims = Jwts.claims()
                .setIssuedAt(new Date())
                .setExpiration(getExpireDate(accessExpiredHour));

        claims.put("id", memberId);
        claims.put("role", role);

        String token = Jwts.builder().
                setHeader(getHeader()).
                setClaims(claims).
                signWith(SignatureAlgorithm.HS256, SECRET_KEY.getBytes()).compact();

        return token;
    }

    public String getRefreshToken(Long memberId) {
        Claims claims = Jwts.claims()
                .setIssuedAt(new Date())
                .setExpiration(getExpireDate(refreshExpiredHour));

        claims.put("id", memberId);

        String token = Jwts.builder().
                setHeader(getHeader()).
                setClaims(claims).
                signWith(SignatureAlgorithm.HS256, SECRET_KEY.getBytes()).compact();

        authService.saveOrUpdate(AuthToken.builder().id(memberId).refreshToken(token).build());

        return token;
    }

    public TokenDTO getTokens(Long memberId, String role) {

        String accessToken = getAccessToken(memberId, role);
        String refreshToken = getRefreshToken(memberId);

        return new TokenDTO(accessToken, refreshToken);
    }

    public void validateToken(String token) throws Exception {
        if ( token == null) {
            throw new AuthException(ErrorCode.Token_Is_Null);
        } else if (!token.startsWith("Bearer ")){
            throw new AuthException(ErrorCode.Invalid_Token_Bearer);
        }
        token = token.substring(7); // "Bearer " 제거
        //System.out.println(token);
        try {
            Jwts.parser().setSigningKey(SECRET_KEY.getBytes()).parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e1){
            throw new AuthException(ErrorCode.Expired_Token);
        } catch(Throwable e2){
            throw new AuthException(ErrorCode.Invalid_Token);
        }
    }

    public boolean validateTokenWithoutException(String token) {
        if (token == null || !token.startsWith("Bearer ")) return false;
        token = token.substring(7); // "Bearer " 제거
        //System.out.println(token);
        try {
            Jwts.parser().setSigningKey(SECRET_KEY.getBytes()).parseClaimsJws(token).getBody();
            return true;
        } catch (ExpiredJwtException e1){
            return false;
        } catch(Throwable e2){
            return false;
        }
    }

    public void validateRefreshToken(String token) throws Exception {
        validateToken(token);

        // refresh validate logic...
        Long memberId = getInfoFromToken(token).getId();
        if(!token.equals(authService.findToken(memberId))) {
            throw new AuthException(ErrorCode.Logged_Out_Refresh_Token);
        }
    }

    public TokenInfoDTO getInfoFromToken(String token) {
        //validateTokenWithoutException(token);
        token = token.substring(7); // "Bearer " 제거
        Claims claims = Jwts.parser().setSigningKey(SECRET_KEY.getBytes()).parseClaimsJws(token).getBody();
        return new TokenInfoDTO(claims.get("id", Long.class), claims.get("role", String.class));
    }

    public String resolveToken(HttpServletRequest request) {
        return request.getHeader("Authorization");
    }

    public Authentication getAuthentication(String accessToken) {
        TokenInfoDTO tokenInfo = getInfoFromToken(accessToken);
        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(tokenInfo.getRole()));
        return new UsernamePasswordAuthenticationToken(tokenInfo.getId(), "", authorities);
    }
}

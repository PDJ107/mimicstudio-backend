package soma.gstbackend.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import soma.gstbackend.exception.AuthException;
import soma.gstbackend.exception.ErrorCode;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

    @Value("${jwt.secret-key}")
    private String SECRET_KEY;

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

    public String getAccessToken(Long memberId, int expireHour) {

        String token = Jwts.builder().
                setHeader(getHeader()).
                setClaims(getPayload(memberId)).
                setExpiration(getExpireDate(expireHour)).
                signWith(SignatureAlgorithm.HS256, SECRET_KEY.getBytes()).compact();

        return token;
    }

    public Map<String, Object> getTokens(Long memberId) {
        Map<String, Object> tokens = new HashMap<>();

        tokens.put("accessToken", getAccessToken(memberId, 1));
        tokens.put("refreshToken", getAccessToken(memberId, 24));

        return tokens;
    }

    public void validateToken(String token) throws Exception {
        if ( token == null) {
            throw new AuthException(ErrorCode.Token_Is_Null);
        } else if (!token.startsWith("Bearer ")){
            throw new AuthException(ErrorCode.Invalid_Token_Bearer);
        }
        token = token.substring(7); // "Bearer " 제거
        System.out.println(token);
        try {
            Claims claims = Jwts.parser().setSigningKey(SECRET_KEY.getBytes()).parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e1){
            throw new AuthException(ErrorCode.Expired_Token);
        } catch(Throwable e2){
            throw new AuthException(ErrorCode.Invalid_Token);
        }
    }

    public void validateRefreshToken(String token) throws Exception {
        validateToken(token);

        // refresh validate logic...
    }

    public Long getIdFromToken(String token) throws Exception {
        validateToken(token);
        token = token.substring(7); // "Bearer " 제거
        Claims claims = Jwts.parser().setSigningKey(SECRET_KEY.getBytes()).parseClaimsJws(token).getBody();
        return claims.get("id", Long.class);
    }
}

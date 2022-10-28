package soma.gstbackend.config.entrypoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import soma.gstbackend.exception.AuthException;
import soma.gstbackend.exception.BaseException;
import soma.gstbackend.exception.ErrorCode;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private static AuthException exception = new AuthException(ErrorCode.Unauthorized_Request);

    @Override
    public void commence(HttpServletRequest httpServletRequest,
                         HttpServletResponse httpServletResponse,
                         AuthenticationException e) throws IOException, ServletException {

        httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
        httpServletResponse.setCharacterEncoding("utf-8");
        httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());

        httpServletResponse.getWriter().println(
                "{\n" +
                        "    \"code\": \"" + exception.getCode() + "\",\n" +
                        "    \"status\": \"" + exception.getStatus() + "\",\n" +
                        "    \"exceptionName\": \"" + exception.getExceptionName() + "\",\n" +
                        "    \"message\": \"" + exception.getMessage() + "\"\n" +
                        "}"
        );
    }
}

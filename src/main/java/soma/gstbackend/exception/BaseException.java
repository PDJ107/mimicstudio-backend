package soma.gstbackend.exception;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@JsonIgnoreProperties({"cause", "stackTrace", "localizedMessage", "suppressed"})
@Getter
public class BaseException extends RuntimeException {
    private String code;
    private HttpStatus status;
    private String detail;
    private String exceptionName;

    public BaseException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.status = errorCode.getStatus();
        this.exceptionName = this.getClass().getName();
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}

package soma.gstbackend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import soma.gstbackend.exception.BaseException;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ExceptionController {

    private String getDetail(Throwable e) {
        String className = e.getStackTrace()[0].getClassName();
        String methodName = e.getStackTrace()[0].getMethodName();
        int lineNumber = e.getStackTrace()[0].getLineNumber();

        return className + "." + methodName + "(Line:" + lineNumber + ")";
    }
    private ResponseEntity makeErrorResponse(Throwable e, HttpStatus status) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", e.getMessage());
        response.put("datail", getDetail(e));
        response.put("exceptionName", e.getClass().getSimpleName());
        response.put("status", status);

        return new ResponseEntity(response, status);
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity errorHandling(Throwable e) {

        if(e instanceof BaseException) {
            BaseException baseException = (BaseException) e;
            baseException.setDetail(getDetail(e));
            return new ResponseEntity(baseException, baseException.getStatus());
        }
        else if(e instanceof MethodArgumentNotValidException) {
            return makeErrorResponse(e, HttpStatus.BAD_REQUEST);
        }
        else {
            return makeErrorResponse(e, HttpStatus.BAD_GATEWAY);
        }
    }
}

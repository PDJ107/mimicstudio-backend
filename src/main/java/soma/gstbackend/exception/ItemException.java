package soma.gstbackend.exception;

public class ItemException extends BaseException {
    public ItemException(ErrorCode errorCode) {
        super(errorCode);
    }
}

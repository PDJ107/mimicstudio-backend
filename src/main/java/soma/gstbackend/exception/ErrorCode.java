package soma.gstbackend.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    Member_Not_Found("MEMBER_01", "존재하지 않는 회원입니다.", HttpStatus.NOT_FOUND),

    Item_Not_Found("ITEM_01", "존재하지 않는 3D 아이템 입니다.", HttpStatus.NOT_FOUND),

    Category_Not_Found("CATEGORY_01", "존재하지 않는 카테고리입니다.", HttpStatus.NOT_FOUND),

    // AWS
    SQS_Transfer_Failed("AWS_01", "SQS 메시지 전송 실패", HttpStatus.BAD_GATEWAY),
    ;
    private String code;
    private String message;
    private HttpStatus status;
}

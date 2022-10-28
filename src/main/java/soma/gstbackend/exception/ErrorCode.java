package soma.gstbackend.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    Member_Not_Found("USER_01", "존재하지 않는 회원입니다.", HttpStatus.NOT_FOUND),
    User_Invalid_Request("USER_02", "email 또는 password가 틀렸습니다.", HttpStatus.BAD_REQUEST),
    Account_Already_Exists("USER_03", "이미 존재하는 Account 입니다.", HttpStatus.BAD_REQUEST),
    Account_Is_Null("USER_04", "account가 Null입니다.", HttpStatus.BAD_REQUEST),
    Password_Is_Null("USER_05", "password가 Null입니다.", HttpStatus.BAD_REQUEST),
    User_Id_Is_Null("USER_06", "User id가 Null입니다.", HttpStatus.BAD_REQUEST),
    Email_Already_Exists("USER_07", "이미 존재하는 Email 입니다.", HttpStatus.BAD_REQUEST),

    Token_Is_Null("TOKEN_01", "토큰이 Null 입니다.", HttpStatus.UNAUTHORIZED),
    Expired_Token("TOKEN_02", "토큰이 만료됐습니다.", HttpStatus.UNAUTHORIZED),
    Invalid_Token("TOKEN_03", "토큰이 잘못됐습니다.", HttpStatus.UNAUTHORIZED),
    Invalid_Token_Bearer("TOKEN_04", "토큰이 Bearer로 시작하지 않습니다.", HttpStatus.UNAUTHORIZED),
    Invalid_Token_User_Id("TOKEN_05", "토큰의 User Id에 해당하는 유저를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    Logged_Out_Token("TOKEN_06", "로그아웃된 토큰입니다.", HttpStatus.UNAUTHORIZED),
    Logged_Out_Refresh_Token("TOKEN_07", "로그아웃된 리프레쉬 토큰입니다.", HttpStatus.UNAUTHORIZED),

    Item_Not_Found("ITEM_01", "존재하지 않는 3D 아이템 입니다.", HttpStatus.NOT_FOUND),

    Category_Not_Found("CATEGORY_01", "존재하지 않는 카테고리입니다.", HttpStatus.NOT_FOUND),

    // AWS
    SQS_Transfer_Failed("AWS_01", "SQS 메시지 전송 실패", HttpStatus.BAD_GATEWAY),

    // Auth
    Unauthorized_Request("AUTH_01", "허용되지 않은 API입니다. 로그인 해주세요.", HttpStatus.UNAUTHORIZED)
    ;
    private String code;
    private String message;
    private HttpStatus status;
}

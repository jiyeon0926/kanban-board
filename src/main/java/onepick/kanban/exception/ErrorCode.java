package onepick.kanban.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    // 400
    INVALID_WORKSPACE_ID(HttpStatus.BAD_REQUEST, "유효하지 않은 워크스페이스 ID입니다."),
    INVALID_INVITEE_ID(HttpStatus.BAD_REQUEST, "유효하지 않은 초대 ID입니다."),
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "요청이 유효하지 않습니다."),
    INVALID_INVITE_STATUS(HttpStatus.BAD_REQUEST, "유효하지 않은 초대 상태 값입니다."),

    // 401
    UNAUTHORIZED_INVITE(HttpStatus.UNAUTHORIZED, "초대 권한이 없습니다."),

    // 403
    INVALID_WORKSPACE_DELETER(HttpStatus.FORBIDDEN, "워크스페이스 삭제 권한이 없는 사용자입니다."),
    INVALID_WORKSPACE_CREATOR(HttpStatus.FORBIDDEN, "워크스페이스 생성 권한이 없는 사용자입니다."),
    INVALID_WORKSPACE_INVITER(HttpStatus.FORBIDDEN, "워크스페이스 초대 권한이 없는 사용자입니다."),
    INVALID_WORKSPACE_INVITEE(HttpStatus.FORBIDDEN, "초대 받은 사용자가 아닙니다."),

    // 404
    WORKSPACE_NOT_FOUND(HttpStatus.NOT_FOUND, "워크스페이스를 찾을 수 없습니다."),
    USER_NOT_FOUND_DELETER(HttpStatus.NOT_FOUND, "삭제 요청 사용자를 찾을 수 없습니다."),
    USER_NOT_FOUND_CREATOR(HttpStatus.NOT_FOUND, "생성 요청 사용자를 찾을 수 없습니다."),
    USER_NOT_FOUND_INVITER(HttpStatus.NOT_FOUND, "초대 요청 사용자를 찾을 수 없습니다."),
    USER_NOT_FOUND_INVITEE(HttpStatus.NOT_FOUND, "다음 이메일이 존재하지 않습니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),

    ;

    private final HttpStatus httpStatus;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}

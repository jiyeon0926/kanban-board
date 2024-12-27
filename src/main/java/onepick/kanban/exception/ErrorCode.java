package onepick.kanban.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    INVALID_WORKSPACE_ID(HttpStatus.BAD_REQUEST, "유효하지 않은 워크스페이스 ID입니다."),
    INVALID_INVITEE_ID(HttpStatus.BAD_REQUEST, "유효하지 않은 초대 ID입니다."),
    WORKSPACE_NOT_FOUND(HttpStatus.NOT_FOUND, "워크스페이스를 찾을 수 없습니다."),
    USER_NOT_FOUND_DELETER(HttpStatus.NOT_FOUND, "삭제 요청 사용자를 찾을 수 없습니다."),
    USER_NOT_FOUND_CREATOR(HttpStatus.NOT_FOUND, "생성 요청 사용자를 찾을 수 없습니다."),
    USER_NOT_FOUND_INVITER(HttpStatus.NOT_FOUND, "초대 요청 사용자를 찾을 수 없습니다."),
    INVALID_WORKSPACE_DELETER(HttpStatus.FORBIDDEN, "워크스페이스 삭제 권한이 없는 사용자입니다."),
    INVALID_WORKSPACE_CREATOR(HttpStatus.FORBIDDEN, "워크스페이스 생성 권한이 없는 사용자입니다."),
    INVALID_WORKSPACE_INVITER(HttpStatus.FORBIDDEN, "워크스페이스 초대 권한이 없는 사용자입니다.");

    ;

    private final HttpStatus httpStatus;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}

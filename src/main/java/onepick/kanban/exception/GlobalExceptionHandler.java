package onepick.kanban.exception;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import java.nio.file.AccessDeniedException;
import onepick.kanban.common.CommonResponseBody;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AuthenticationException.class)
    protected ResponseEntity<CommonResponseBody<Void>> handleAuthException(
            AuthenticationException e) {
        HttpStatus statusCode = e instanceof BadCredentialsException
                ? HttpStatus.FORBIDDEN
                : HttpStatus.UNAUTHORIZED;

        return ResponseEntity
                .status(statusCode)
                .body(new CommonResponseBody<>(e.getMessage()));
    }

    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<CommonResponseBody<Void>> handleAccessDeniedException(AccessDeniedException e) {

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new CommonResponseBody<>(e.getMessage()));
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    protected ResponseEntity<CommonResponseBody<Void>> handleAuthorizationDeniedException(AuthorizationDeniedException e) {

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new CommonResponseBody<>(e.getMessage()));
    }

    @ExceptionHandler(JwtException.class)
    protected ResponseEntity<CommonResponseBody<Void>> handleJwtException(JwtException e) {
        HttpStatus httpStatus = e instanceof ExpiredJwtException ? HttpStatus.UNAUTHORIZED : HttpStatus.FORBIDDEN;

        return ResponseEntity
                .status(httpStatus)
                .body(new CommonResponseBody<>(e.getMessage()));
    }

    @ExceptionHandler(ResponseStatusException.class)
    protected ResponseEntity<CommonResponseBody<Void>> handleResponseStatusExceptions(ResponseStatusException e) {
        return ResponseEntity
                .status(e.getStatusCode())
                .body(new CommonResponseBody<>(e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<CommonResponseBody<Void>> handleOtherExceptions(Exception e) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new CommonResponseBody<>(e.getMessage()));
    }
}

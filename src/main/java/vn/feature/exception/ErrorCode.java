package vn.feature.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import vn.feature.util.MessageKeys;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(500, MessageKeys.APP_UNCATEGORIZED_500, HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1001, MessageKeys.APP_AUTHORIZATION_403, HttpStatus.BAD_REQUEST),
    USER_EXISTED(1002, MessageKeys.USER_EXISTED, HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(1003, MessageKeys.USER_ID_REQUIRED, HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(1004, MessageKeys.PASSWORD_REQUIRED, HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1005, MessageKeys.USER_EXISTED, HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(1006, MessageKeys.APP_AUTHORIZATION_403, HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(403, MessageKeys.APP_AUTHORIZATION_403, HttpStatus.FORBIDDEN),
    INVALID_DOB(1008, "Your age must be at least {min}", HttpStatus.BAD_REQUEST);

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;
}

package vn.feature.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@AllArgsConstructor
public enum  ErrorCode {
    UNCATEGORIZED_EXCEPTION(500, "app.uncategorized.500", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1001, "app.authorization.403", HttpStatus.BAD_REQUEST),
    USER_EXISTED(1002, "user.existed", HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(1003, "user_id.required", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(1004, "password.required", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1005, "user.existed", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(1006, "app.authorization.403", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(403, "app.authorization.403", HttpStatus.FORBIDDEN),
    INVALID_DOB(1008, "Your age must be at least {min}", HttpStatus.BAD_REQUEST);

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;
}

package codefod.com.springbootmentor.common.exception;

import codefod.com.springbootmentor.common.constant.ErrorEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CodefodException extends RuntimeException {

    private final Integer httpStatus;
    private final String errorCode;
    private final String message;

    public CodefodException(String msg) {
        this(ErrorEnum.INVALID_INPUT_COMMON, msg);
    }

    public CodefodException(ErrorResponse errorResponse) {
        super(errorResponse.getMessage());
        this.httpStatus = errorResponse.getHttpStatus();
        this.errorCode = errorResponse.getErrorCode();
        this.message = errorResponse.getMessage();
    }

    public CodefodException(ErrorEnum errorEnum, String... args) {
        super(String.format(errorEnum.getMessage(), (Object[]) args));
        this.httpStatus = errorEnum.getHttpStatus();
        this.errorCode = errorEnum.getCode();
        this.message = String.format(errorEnum.getMessage(), (Object[]) args);
    }

    public CodefodException(ErrorEnum errorEnum) {
        super(errorEnum.getMessage());
        this.httpStatus = errorEnum.getHttpStatus();
        this.errorCode = errorEnum.getCode();
        this.message = errorEnum.getMessage();
    }

    public ErrorResponse convertToErrorResponse() {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setHttpStatus(this.httpStatus);
        errorResponse.setErrorCode(this.errorCode);
        errorResponse.setMessage(this.getMessage());
        errorResponse.setSuccess(Boolean.FALSE);
        return errorResponse;
    }
}
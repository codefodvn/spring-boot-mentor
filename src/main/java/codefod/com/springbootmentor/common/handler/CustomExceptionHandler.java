package codefod.com.springbootmentor.common.handler;

import codefod.com.springbootmentor.common.constant.ErrorEnum;
import codefod.com.springbootmentor.common.dto.CodefodResponse;
import codefod.com.springbootmentor.common.exception.CodefodException;
import codefod.com.springbootmentor.common.exception.InputValidationException;
import java.nio.file.AccessDeniedException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @SuppressWarnings("java:S1452")
    @ExceptionHandler(value = CodefodException.class)
    public ResponseEntity<?> handleGRuntimeException(CodefodException ex) {
        return createResponse(ex, ex.getMessage());
    }

    @SuppressWarnings("java:S1452")
    @ExceptionHandler(value = InputValidationException.class)
    public ResponseEntity<?> handleInputValidation(InputValidationException ex) {
        log.error("InputValidation", ex);
        CodefodException codefodException = new CodefodException(ErrorEnum.INVALID_INPUT);
        codefodException.initCause(ex.getCause());
        return createResponse(codefodException, null, ex.getError());
    }

    @SuppressWarnings("java:S1452")
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleAllException(Exception exc) {
        log.error("AllException", exc);
        CodefodException ge = new CodefodException(ErrorEnum.INTERNAL_SERVER_ERROR);
        ge.initCause(exc.getCause());
        return createResponse(ge, ge.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleAccessDeniedException(AccessDeniedException exc) {
        log.error("AccessDeniedException", exc);
        CodefodException ge = new CodefodException(ErrorEnum.ACCESS_DENIED);
        ge.initCause(exc.getCause());
        return createResponse(ge, exc.getMessage());
    }

    @SuppressWarnings("all")
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status,
            WebRequest request) {
        log.error("ArgumentNotValidException", ex);
        InputValidationException inputValidationException = new InputValidationException(
                ex.getBindingResult());
        CodefodException ge = new CodefodException(ErrorEnum.INVALID_INPUT);
        ge.initCause(ex.getCause());
        return (ResponseEntity<Object>) createResponse(ge, ge.getMessage(),
                inputValidationException.getError());
    }

    private ResponseEntity<?> createResponse(CodefodException ge, String message) {
        return createResponse(ge, message, null);
    }

    private <T> ResponseEntity<?> createResponse(CodefodException ge, String message, T data) {
        CodefodResponse<T> responseObject = new CodefodResponse<>();
        responseObject.setErrorCode(ge.getErrorCode());

        Integer httpStatus = null;
        try {
            httpStatus = ge.getHttpStatus();
            if (message != null) {
                responseObject.setMessage(message);
            }
            responseObject.setTraceId(MDC.get("traceId"));
        } catch (Exception ex) {
            log.error("error: ", ex);
        }

        if (!StringUtils.isBlank(message)) {
            responseObject.setMessage(message);
        }

        if (data != null) {
            responseObject.setData(data);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json; charset=utf-8");

        return new ResponseEntity<>(responseObject, headers,
                HttpStatus.valueOf(httpStatus != null ? httpStatus : 400));
    }
}
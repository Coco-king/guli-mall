package top.codecrab.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import top.codecrab.common.response.ErrorCodeEnum;
import top.codecrab.common.response.R;

/**
 * @author codecrab
 * @since 2021年04月23日 9:59
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RRException.class)
    public R handleException(RRException e) {
        log.error("RRException异常：{}", e.getMsg());
        return R.error(e.getCode(), e.getMsg());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public R handleException(MethodArgumentNotValidException e) {
        FieldError error = e.getBindingResult().getFieldError();
        if (error != null) {
            String errors = error.getDefaultMessage();
            log.error("MethodArgumentNotValidException异常：{}", errors);
            return R.error(ErrorCodeEnum.VALID_EXCEPTION.getCode(), errors);
        }
        log.error("MethodArgumentNotValidException异常：{}", e.getMessage());
        return R.error();
    }

    /**
     * Controller上一层相关异常
     */
    @ExceptionHandler({
            NoHandlerFoundException.class,
            HttpRequestMethodNotSupportedException.class,
            HttpMediaTypeNotSupportedException.class,
            MissingPathVariableException.class,
            MissingServletRequestParameterException.class,
            TypeMismatchException.class,
            HttpMessageNotReadableException.class,
            HttpMessageNotWritableException.class,
            HttpMediaTypeNotAcceptableException.class,
            ServletRequestBindingException.class,
            ConversionNotSupportedException.class,
            MissingServletRequestPartException.class,
            AsyncRequestTimeoutException.class
    })
    public R handleServletException(Exception e) {
        log.error(e.getMessage(), e);
        return R.setResult(ErrorCodeEnum.SERVLET_ERROR);
    }

    @ExceptionHandler(Throwable.class)
    public R handleException(Throwable t) {
        log.error(t.getMessage(), t);
        return R.error(ErrorCodeEnum.UNKNOWN_EXCEPTION);
    }
}

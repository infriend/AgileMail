package edu.agiledev.agilemail.advice;

import edu.agiledev.agilemail.exception.BaseException;
import edu.agiledev.agilemail.pojo.model.R;
import edu.agiledev.agilemail.pojo.model.ReturnCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 *
 * @author Nosolution
 * @version 1.0
 * @since 2022/2/12
 */
@RestControllerAdvice
@Slf4j
public class GlobalAdvice {


    /**
     * 所有失败请求的响应的出口
     *
     * @param ex 待处理的BaseException类，代表某个请求在预设范围内失败了
     * @return 状态码为非200的响应
     */
    @ExceptionHandler(BaseException.class)
    public R<String> handleBaseException(BaseException ex) {
        log.error("Handle BaseException [{}]", ex.toString());
        ex.printStackTrace();
        return R.error(ex.getCode(), ex.getMessage());
    }


    /**
     * 一般性的异常处理器
     *
     * @param ex 未预知的异常
     * @return 状态码为500的响应，带有异常message
     */
    @ExceptionHandler(Exception.class)
    public R<String> handleException(Exception ex) {
        log.error("Handle Exception [{}]", ex.toString());
        ex.printStackTrace();
        return R.error(ReturnCode.ERROR.getCode(), ex.getMessage());
    }
}

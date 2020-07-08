package com.sunnyz.iiwebapi.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


@ControllerAdvice
public class GlobalExceptionHandler {
    Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public AjaxResponse customException(Exception e) {
        logger.error(e.getMessage(), e);
        return AjaxResponse.error(e.getMessage() == null ? e.getClass().getName() : e.getMessage());
    }
}

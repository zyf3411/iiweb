package com.sunnyz.iiwebapi.base;

public class BizException extends RuntimeException {
    public BizException(String message) {
        this(message, null);
    }

    public BizException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}

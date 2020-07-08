package com.sunnyz.iiwebapi.base;

import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;
import java.util.stream.Collectors;

public class AjaxResponse {
    private int status;
    private String description;
    private Object data;

    public AjaxResponse(int status, String description, Object data) {
        this.status = status;
        this.description = description;
        this.data = data;
    }

    public static AjaxResponse success() {
        return new AjaxResponse(200, "success", null);
    }

    public static AjaxResponse success(Object data) {
        return new AjaxResponse(200, "success", data);
    }

    public static AjaxResponse success(String description) {
        return new AjaxResponse(200, description, null);
    }

    public static AjaxResponse success(String description, Object data) {
        return new AjaxResponse(200, description, data);
    }

    public static AjaxResponse error(String description) {
        return new AjaxResponse(400, description, null);
    }

    public static AjaxResponse error(String description, Object data) {
        return new AjaxResponse(400, description, data);
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }


    /**
     * 实体验证 错误响应
     *
     * @param bindingResult
     * @return
     */
    public static AjaxResponse error(BindingResult bindingResult) {
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        if (fieldErrors != null || fieldErrors.size() > 0) {
            return AjaxResponse.error(StringUtils.join(fieldErrors.stream().map(r -> r.getDefaultMessage()).collect(Collectors.toList()), "<br/>"));
        }
        return AjaxResponse.error("未知错误");
    }
}

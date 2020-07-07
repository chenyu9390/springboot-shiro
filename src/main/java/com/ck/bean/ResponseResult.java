package com.ck.bean;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseResult {
    private String code;
    private String message;
    private Object data;

    public ResponseResult(String code, String message){
        this.code = code;
        this.message = message;
    }

    public ResponseResult(ResponseStatus responseStatus) {
        this.code = responseStatus.getKey();
        this.message = responseStatus.getValue();
    }

    public ResponseResult(ResponseStatus responseStatus, Object data) {
        this.data = data;
        this.code = responseStatus.getKey();
        this.message = responseStatus.getValue();
    }
}

package com.jung.notify.api.response.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommonResult {

    private int status;

    private String code;

    private String message;
}
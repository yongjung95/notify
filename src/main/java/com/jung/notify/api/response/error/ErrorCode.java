package com.jung.notify.api.response.error;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ErrorCode {

    // common
    PARAMETER_IS_EMPTY(400,"C001","파라미터가 올바르지 않습니다."),
    NOT_EMAIL_PATTERN(400,"C002","이메일 형식이 올바르지 않습니다."),


    // member
    DUPLICATION_ID(400,"M001","중복된 아이디 입니다."),
    LINE_TOKEN_IS_NOT_FOUND(400,"M002","등록된 라인 토큰이 없습니다."),

    MEMBER_IS_NOT_FOUND(400, "M003", "등록된 회원 정보가 없습니다."),
    DUPLICATION_MEMBER(400, "M004", "등록된 회원 정보가 있습니다."),

    // keyword
    DUPLICATION_KEYWORD(400,"K001","중복된 키워드 입니다."),
    KEYWORD_IS_NOT_FOUND(400,"K002","해당 키워드가 없습니다."),

    // stock
    STOCK_IS_NOT_FOUND(400, "S001", "주식 정보가 없습니다."),

    ;
    private final String code;
    private final String message;
    private int status;

    ErrorCode(final int status, final String code, final String message) {
        this.status = status;
        this.message = message;
        this.code = code;
    }

    public String getMessage() {
        return this.message;
    }

    public String getCode() {
        return code;
    }

    public int getStatus() {
        return status;
    }
}

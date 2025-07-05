package com.sangle.Network.Social.Exception;

public enum ErorrCode {
    USER_EXISTED(101,"user existed"),
    ID_USER_NOT_FOUND(102,"id not found"),
    USERNAME_EXISTED(103,"username already exists"),
    USERPROFILE_NOT_FOUND(301,"user not found"),

    NOT_FOUND_CODE(105,"booking not found confirmation code"),
    USER_NOT_FOUND(106,"user not found"),

    POST_NOT_FOUND(107,"Post not found"),


    COMMENT_NOT_FOUND(108,"Comment not found"),
    INVALID_DATE(401,"invalid date"),
    INVALID_DATE_RANGE(402,"check in date must be before check out date"),
    INTERNAL_SERVER_ERROR(500, "Lỗi hệ thống, vui lòng thử lại sau"),
    UNAUTHENTICATED(600,"unauthenticated"),

    ;

    ErorrCode(int code, String message) {
        this.code=code;
        this.message=message;
    }

    private int code;
    private String message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

package com.sangle.Network.Social.Exception;

public class AppException extends RuntimeException {
    private ErorrCode erorrCode;
    public AppException(ErorrCode erorrCode) {
        super(erorrCode.getMessage());
        this.erorrCode=erorrCode;
    }

    public ErorrCode getErorrCode() {
        return erorrCode;
    }

    public void setErorrCode(ErorrCode erorrCode) {
        this.erorrCode = erorrCode;
    }
}

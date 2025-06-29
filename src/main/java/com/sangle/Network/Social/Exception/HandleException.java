package com.sangle.Network.Social.Exception;

import com.sangle.Network.Social.DTO.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class HandleException {
    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse> handlingAppException(AppException appException)
    {
        ErorrCode erorrCode=appException.getErorrCode();
        ApiResponse apiResponse=new ApiResponse();
        apiResponse.setMessage(erorrCode.getMessage());
        apiResponse.setCode(erorrCode.getCode());

        return ResponseEntity.badRequest().body(apiResponse);

    }
}

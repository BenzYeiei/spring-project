package com.demo.firstProject.Exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class HandleException {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ObjectException> Response(BaseException e) {
        ObjectException responseException = new ObjectException(
                e.getMessage(),
                e.getCode().value(),
                e.getPath(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(responseException, e.getCode());
    }

}

package com.rj.Enotes_API_Service.exception;

import java.io.FileNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.rj.Enotes_API_Service.util.CommonUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice 
public class GlobalExceptionHandler {


    @ExceptionHandler(Exception.class)
    public ResponseEntity<?>handleException(Exception e){

        log.error("GlobalExceptionHandler :: handleException ::", e.getMessage());
       // return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        return  CommonUtil.createErrorResponseMessage(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<?>handleNullPointerException(Exception e){
        log.error("GlobalExceptionHandler :: handleNullPointerException ::", e.getMessage());
       // return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        return  CommonUtil.createErrorResponseMessage(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

        
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?>handleResourceNotFoundException(Exception e){
       // return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        return  CommonUtil.createErrorResponseMessage(e.getMessage(), HttpStatus.NOT_FOUND);

    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<?>handleValidationException(ValidationException e){

       // return new ResponseEntity<>(e.getErrors(), HttpStatus.BAD_REQUEST);
        return  CommonUtil.createErrorResponse(e.getErrors(), HttpStatus.BAD_REQUEST);

    }
    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<?>fileNotFoundException(ValidationException e){

       // return new ResponseEntity<>(e.getErrors(), HttpStatus.BAD_REQUEST);
        return  CommonUtil.createErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND);

    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?>handleIllegalArgumentException(IllegalArgumentException e){

       // return new ResponseEntity<>(e.getErrors(), HttpStatus.BAD_REQUEST);
        return  CommonUtil.createErrorResponseMessage(e.getMessage(), HttpStatus.BAD_REQUEST);

    }

    
    @ExceptionHandler(ExistDataException.class)
    public ResponseEntity<?>handleExistDataException(ExistDataException e){

        return  CommonUtil.createErrorResponse(e.getMessage(), HttpStatus.CONFLICT);

    }

}

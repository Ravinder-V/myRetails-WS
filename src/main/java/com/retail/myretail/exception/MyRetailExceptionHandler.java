package com.retail.myretail.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.retail.myretail.bean.ErrorMessage;



@RestControllerAdvice
public class MyRetailExceptionHandler  extends ResponseEntityExceptionHandler{

	@ExceptionHandler(ProductNotFoundException.class)
	public final ResponseEntity<Object> handleUserNotFoundException(ProductNotFoundException ex, WebRequest request) {
        ErrorMessage error = new ErrorMessage(ex.getMessage()+" Not Found");
        return new ResponseEntity<Object>(error, HttpStatus.NOT_FOUND);
    }
	
}

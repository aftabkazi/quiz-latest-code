package com.quiz.exception.JSONParse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import com.quiz.response.GlobalResponse;

/* If there is any syntax error in JSON while submitting to handle that this code is written 
 * With the help of ResponseEntityExceptionHandler class we can customize the error message to display to user
 * It is an abstract class
 * 
 */

@ControllerAdvice
public class JsonParseExceptionController  extends  ResponseEntityExceptionHandler {
	
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
            HttpStatus status, WebRequest request) {
        /*
         ** keep if block ( future use)
    		if (body == null) {
        		System.out.println("inside if ");
            	body = "Invalid JSON";
        	}
         **
        */
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new GlobalResponse(null,"Invalid JSON",false));
    }
}

package com.webnongsan.greenshop.controlleradvice;



import com.webnongsan.greenshop.customerexception.DataNotFoundException;
import com.webnongsan.greenshop.customerexception.InvalidParamException;
import com.webnongsan.greenshop.customerexception.UsernameNotFoundException;
import com.webnongsan.greenshop.model.response.ErrorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.mail.MessagingException;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@ControllerAdvice
public class ControllerAdvisor extends ResponseEntityExceptionHandler {

    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleDataNotFoundException(DataNotFoundException ex, WebRequest request) {
        ErrorResponse result = new ErrorResponse();
        result.setError(ex.getMessage());
        List<String> details = new ArrayList<>();
        result.setDetails(details);
        return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(MessagingException.class)
    public ResponseEntity<ErrorResponse> handlerMessagingException(MessagingException ex, WebRequest request) {
        ErrorResponse result = new ErrorResponse();
        result.setError(ex.getMessage());
        List<String> details = new ArrayList<>();
        result.setDetails(details);
        return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(InvalidParamException.class)
    public ResponseEntity<ErrorResponse> handleInvalidParamException(InvalidParamException ex, WebRequest request){
        ErrorResponse result = new ErrorResponse();
        result.setError(ex.getMessage());
        List<String> details = new ArrayList<>();
        result.setDetails(details);
        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUsernameNotFoundException(UsernameNotFoundException ex, WebRequest request){
        ErrorResponse result = new ErrorResponse();
        result.setError(ex.getMessage());
        List<String> details = new ArrayList<>();
        result.setDetails(details);
        return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
    }

}

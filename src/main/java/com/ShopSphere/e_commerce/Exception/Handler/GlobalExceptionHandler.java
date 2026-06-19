package com.ShopSphere.e_commerce.Exception.Handler;

import com.ShopSphere.e_commerce.Exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<String> handleUserAlreadyExistsException(UserAlreadyExistsException e) {
        return new ResponseEntity<> (e.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e){
        Map<String, String> errors = new LinkedHashMap<>();

        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        for(FieldError fieldError : fieldErrors){
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<String> handleInvalidCredentialsException(InvalidCredentialsException e) {
        return new ResponseEntity<> (e.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(CategoryAlreadyExistsException.class)
    public ResponseEntity<String> handleCategoryAlreadyExistsException(CategoryAlreadyExistsException e){
        return new ResponseEntity<> (e.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<String> handleCategoryNotFoundException(CategoryNotFoundException e){
        return new ResponseEntity<> (e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<String> handleProductNotFoundException(ProductNotFoundException e){
        return new ResponseEntity<> (e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CategoryDeleteException.class)
    public ResponseEntity<String> handleCategoryDeleteException(CategoryDeleteException e){
        return new ResponseEntity<> (e.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e){
        return new ResponseEntity<> (e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException e){
        return new ResponseEntity<> (e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CartNotFoundException.class)
    public ResponseEntity<String> handleCartNotFoundException(CartNotFoundException e){
        return new ResponseEntity<> (e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CartItemNotFoundException.class)
    public ResponseEntity<String> handleCartItemNotFoundException(CartItemNotFoundException e){
        return new ResponseEntity<> (e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CartEmptyException.class)
    public ResponseEntity<String> handleCartEmptyException(CartEmptyException e){
        return new ResponseEntity<> (e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<String> handleOrderNotFoundException(OrderNotFoundException e){
        return new ResponseEntity<> (e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> handleIllegalStateException(IllegalStateException e){
        return new ResponseEntity<> (e.getMessage(), HttpStatus.BAD_REQUEST);
    }

}

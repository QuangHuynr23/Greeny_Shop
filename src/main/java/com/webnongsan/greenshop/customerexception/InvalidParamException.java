package com.webnongsan.greenshop.customerexception;

public class InvalidParamException extends RuntimeException{
    public InvalidParamException(String message) {

        super(message);
    }
}

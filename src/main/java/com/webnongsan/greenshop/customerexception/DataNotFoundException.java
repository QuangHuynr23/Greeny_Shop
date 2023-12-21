package com.webnongsan.greenshop.customerexception;


public class DataNotFoundException extends RuntimeException{
    public DataNotFoundException(String message) {
        super(message);
    }
}

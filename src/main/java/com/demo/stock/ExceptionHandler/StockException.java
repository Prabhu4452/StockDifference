package com.demo.stock.ExceptionHandler;

public class StockException extends RuntimeException {

    private String errorCode;
    private String errorMessage;

    public StockException(String errorMessage)
    {
        this.errorMessage=errorMessage;
    }
    public StockException(String errorCode,String errorMessage)
    {
        this.errorCode=errorCode;
        this.errorMessage=errorMessage;
    }
    public String getErrorCode(){
            return errorCode;
    }
    public String getErrorMessage() {
            return errorMessage;
        }
    }


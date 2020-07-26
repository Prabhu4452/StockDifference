package com.demo.stock.Controller;

import com.demo.stock.ExceptionHandler.ExchangeValueErrors;
import com.demo.stock.ExceptionHandler.StockException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(StockException.class)
    public ResponseEntity<ExchangeValueErrors> mapExceptionOnSearchingRecordOnId(StockException ex)
    {
        ExchangeValueErrors exchangeValueErrors = new ExchangeValueErrors(ex.getErrorCode(), ex.getErrorMessage());
        return new  ResponseEntity<ExchangeValueErrors>(exchangeValueErrors, HttpStatus.NOT_FOUND);
    }
}

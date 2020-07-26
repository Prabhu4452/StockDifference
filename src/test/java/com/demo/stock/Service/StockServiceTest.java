package com.demo.stock.Service;

import com.demo.stock.Domain.StockDto;
import com.demo.stock.ExceptionHandler.StockException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;
import yahoofinance.YahooFinance;

import java.io.IOException;
import java.math.BigDecimal;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
public class StockServiceTest {

    @InjectMocks
    StockService stockService;

    @Spy
    YahooFinance yahooFinance;

    @Test
    public void shouldGetResponseFromYahoo() throws IOException {
        String symbol = "RELIANCE.BO";
        StockDto stockDto = new StockDto();
        stockDto.setSymbol(symbol);
        stockDto.setPrice(BigDecimal.valueOf(23));
        stockService.getCompleteStockFromYahoo(symbol);
        Assert.assertEquals(symbol, stockService.getCompleteStockFromYahoo(symbol).getSymbol());
    }

    @Test(expected = StockException.class)
    public void shouldThrowExceptionWhenNotFound() throws IOException {
        String symbol = "RELIANCE";
        StockDto stockDto = new StockDto();
        stockDto.setSymbol(symbol);
        stockDto.setPrice(BigDecimal.valueOf(23));
        stockService.getCompleteStockFromYahoo(symbol);
    }

    @Test(expected = StockException.class)
    public void shouldThrowExceptionWhenCompanyInvalid() throws IOException {
        String symbol = "RELIANCE";
        StockDto stockDto = new StockDto();
        stockDto.setSymbol(symbol);
        stockDto.setPrice(BigDecimal.valueOf(23));
        stockService.getCompleteStockFromYahoo(symbol);
    }

    @Test
    public void shouldGetResponseFromYahooWhenThereAreMultipleValuesAsPRequestParams() throws IOException {
        String symbol = "RELIANCE.BO,RELIANCE.NS,INFY.BO,INFY.NS";
        String[] symbols = new String[]{"RELIANCE,INFY"};
        StockDto stockDto = new StockDto();
        stockDto.setSymbol(symbol);
        stockDto.setPrice(BigDecimal.valueOf(23));
        stockService.getStockFromYahooWithMappingToStockDto(symbol);
        Assert.assertEquals("INFY", stockService.getStockFromYahooWithMappingToStockDto(symbol).get(0).getName());
        Assert.assertEquals("RELIANCE", stockService.getStockFromYahooWithMappingToStockDto(symbol).get(1).getName());
    }

    @Test
    public void shouldGetResponseFromYahooAsPotentialDifference() throws IOException {
        String symbol = "RELIANCE.BO,RELIANCE.NS";
        String[] symbols = new String[]{"RELIANCE,INFY"};
        Assert.assertNotNull(stockService.profit(symbol));
    }

    @Test(expected = StockException.class)
    public void shouldThrowExceptionWhenThereIsOnlyOneCompany() throws IOException {
        String symbol = "RELIANCE.BO";
        String[] symbols = new String[]{"RELIANCE,INFY"};
        Assert.assertNull(stockService.profit(symbol));
    }
}
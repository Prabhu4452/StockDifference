package com.demo.stock.Controller;

import com.demo.stock.Domain.StockDto;
import com.demo.stock.Domain.StockExchangeDto;
import com.demo.stock.Service.StockService;
import com.itextpdf.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.util.List;

@RestController
public class StockController {

    @Autowired
    StockService stockService;

    @GetMapping(value = "/yahoocompletestockdata")
    public Stock getCompleteStockFromYahooService(@RequestParam String symbol) throws IOException {
        return stockService.getCompleteStockFromYahoo(symbol);
    }

    @GetMapping(value = "/yahoocompletestockdataMappedToStockDto")
    public List<StockExchangeDto>  getCompleteStockFromYahooServiceMappedToDto(@RequestParam String symbol) throws IOException {
        return stockService.getStockFromYahooWithMappingToStockDto(symbol);
    }

    @GetMapping(value = "/arbitrage")
    public BigDecimal getStockFromYahooServices(@RequestParam String symbol) throws IOException {
        return stockService.profit(symbol);
    }

    @GetMapping(value = "/stockExchangeReport.pdf" , produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> getStockFromYahooInPdfFormatMappedToStockExchange(@RequestParam String symbol)
            throws DocumentException, IOException, URISyntaxException {
        return stockService.getStockFromYahooInPdfMappedToStockRxchangeDto(symbol);
    }

}

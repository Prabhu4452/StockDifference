package com.demo.stock.Service;

import com.demo.stock.Domain.StockDto;
import com.demo.stock.Domain.StockExchangeDto;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.util.*;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Component
public class StockService {


    public Stock getCompleteStockFromYahoo(String comp) throws IOException {
        Stock stock = YahooFinance.get(comp);
        return stock;
    }

    public List<StockExchangeDto> getStockFromYahooWithMappingToStockDto(String comp) throws IOException {
        List<StockDto> stockDtoList;
        List<StockExchangeDto> stockExchangeDtoList = new ArrayList<>();
        Map<String, BigDecimal[]> tempMap = new HashMap<>();

        String[] symbols = null;

        symbols = comp.split(",");

        Map<String, Stock> stock = YahooFinance.get(symbols);
        stockDtoList = stock.values().stream().map(stock1 -> toStockDto(stock1)).collect(Collectors.toList());

        int n = comp.split(",").length;
        for (int i = 0; i < n; i++) {

            String[] detailArray = null;
            String temp = stockDtoList.get(i).getSymbol();

            detailArray = temp.split("[.]");
            if (tempMap.containsKey(detailArray[0])) {
                BigDecimal[] nsebse = new BigDecimal[2];
                nsebse = tempMap.get(detailArray[0]);
                if (detailArray[1].equals("BO")) {
                    nsebse[0] = stockDtoList.get(i).getBsePrice();
                } else {
                    nsebse[1] = stockDtoList.get(i).getNsePrice();
                }
                tempMap.put(detailArray[0], nsebse);
            } else {
                BigDecimal[] nsebse = new BigDecimal[2];
                if (detailArray[1].equals("BO")) {
                    nsebse[0] = stockDtoList.get(i).getBsePrice();
                } else {
                    nsebse[1] = stockDtoList.get(i).getNsePrice();
                }
                tempMap.put(detailArray[0], nsebse);

            }
        }

        Iterator<String> iterator = tempMap.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            BigDecimal[] temp = tempMap.get(key);
            if (temp[0] == null || temp[1] == null) {
                iterator.remove();
            }
        }


            List<StockExchangeDto> list = new ArrayList<>();
            for (String tempMap1 : tempMap.keySet()) {
                StockExchangeDto exchangeDto = toStockExchangeDto(tempMap1, tempMap.get(tempMap1));
                list.add(exchangeDto);
            }
            stockExchangeDtoList = list;

            return stockExchangeDtoList;

    }

    public BigDecimal profit(String comp) throws IOException {
        List<StockDto> stockDtoList;
        Map<String, Stock> stock = YahooFinance.get(comp.split(","));
        stockDtoList = stock.values().stream().map(stock1 -> toDto(stock1)).collect(Collectors.toList());
        BigDecimal profit = stockDtoList.get(0).getPrice().subtract(stockDtoList.get(1).getPrice());
        return profit;
    }


    public StockDto toDto(Stock stock) {
        StockDto stockDto = new StockDto();
        stockDto.setSymbol(stock.getSymbol());
        stockDto.setPrice(stock.getQuote().getPrice());
        return stockDto;
    }

    public StockDto toStockDto(Stock stock) {
        StockDto stockDto = new StockDto();
        stockDto.setName(stock.getName());
        stockDto.setSymbol(stock.getSymbol());
        if(stock.getStockExchange().equals("BSE")) {
            stockDto.setBsePrice(stock.getQuote().getPrice());
        }
        if(stock.getStockExchange().equals("NSE")) {
            stockDto.setNsePrice(stock.getQuote().getPrice());
        }
        return stockDto;
    }

    public StockExchangeDto toStockExchangeDto(String key, BigDecimal[] detailMap) {
        StockExchangeDto stockExchangeDto = new StockExchangeDto();
        stockExchangeDto.setName(key);
        stockExchangeDto.setNsePrice(detailMap[1]);
        stockExchangeDto.setBsePrice(detailMap[0]);
        stockExchangeDto.setPotentialProfits(stockExchangeDto.getBsePrice().subtract(stockExchangeDto.getNsePrice()));
        return stockExchangeDto;
    }

    public ResponseEntity<InputStreamResource> getStockFromYahooInPdfMappedToStockRxchangeDto(String comp) throws IOException, DocumentException, URISyntaxException {
        List<StockDto> stockDtoList;
        List<StockExchangeDto> stockExchangeDtoList = new ArrayList<>();
        Map<String, BigDecimal[]> tempMap = new HashMap<>();
        Map<String, Stock> stock = YahooFinance.get(comp.split(","));
        stockDtoList = stock.values().stream().map(stock1 -> toStockDto(stock1)).collect(Collectors.toList());

        int n = comp.split(",").length;
        for(int i=0; i<n; i++) {

            String[] detailArray = null;
            String temp = stockDtoList.get(i).getSymbol();

            detailArray = temp.split("[.]");
            if (tempMap.containsKey(detailArray[0])) {
                BigDecimal[] nsebse = new BigDecimal[2];
                nsebse = tempMap.get(detailArray[0]);
                if(detailArray[1].equals("BO")) {
                    nsebse[0] = stockDtoList.get(i).getBsePrice();
                }
                else {
                    nsebse[1] = stockDtoList.get(i).getNsePrice();
                }
                tempMap.put(detailArray[0], nsebse);
            } else {
                BigDecimal[] nsebse = new BigDecimal[2];
                if(detailArray[1].equals("BO")) {
                    nsebse[0] = stockDtoList.get(i).getBsePrice();
                }
                else {
                    nsebse[1] = stockDtoList.get(i).getNsePrice();
                }
                tempMap.put(detailArray[0], nsebse);

            }
        }

        Iterator<String> iterator = tempMap.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            BigDecimal[] temp = tempMap.get(key);
            if (temp[0] == null || temp[1] == null) {
                iterator.remove();
            }
        }

        List<StockExchangeDto> list = new ArrayList<>();
        for (String tempMap1 : tempMap.keySet()) {
            StockExchangeDto exchangeDto = toStockExchangeDto(tempMap1, tempMap.get(tempMap1));
            list.add(exchangeDto);
        }
        stockExchangeDtoList = list;

        ByteArrayInputStream bis = transactionReportForSTockExchangeDto(stockExchangeDtoList);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=stockExchangeReport.pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));

    }

    public static ByteArrayInputStream transactionReportForSTockExchangeDto(List<StockExchangeDto> stockExchangeDtos) {

        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(60);
            table.setWidths(new int[]{5, 3, 3, 3});

            Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);

            PdfPCell hcell;
            hcell = new PdfPCell(new Phrase("STOCK", headFont));
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(hcell);

            hcell = new PdfPCell(new Phrase("NSE", headFont));
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(hcell);

            hcell = new PdfPCell(new Phrase("BSE", headFont));
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(hcell);

            hcell = new PdfPCell(new Phrase("PROFIT", headFont));
            hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(hcell);


            for (StockExchangeDto stockExchangeDto : stockExchangeDtos) {

                PdfPCell cell;

                cell = new PdfPCell(new Phrase(stockExchangeDto.getName()));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(stockExchangeDto.getNsePrice().toString()));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(stockExchangeDto.getBsePrice().toString()));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

                cell = new PdfPCell(new Phrase(stockExchangeDto.getPotentialProfits().toString()));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(cell);

            }

            PdfWriter.getInstance(document, out);
            document.open();
            document.add(table);

            document.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        return new ByteArrayInputStream(out.toByteArray());
    }
}

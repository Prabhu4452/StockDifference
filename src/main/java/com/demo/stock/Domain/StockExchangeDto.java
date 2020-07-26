package com.demo.stock.Domain;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class StockExchangeDto {
    private String name;
    private BigDecimal nsePrice;
    private BigDecimal bsePrice;
    private BigDecimal potentialProfits;
}

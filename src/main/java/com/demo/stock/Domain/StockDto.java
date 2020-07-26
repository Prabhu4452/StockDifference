package com.demo.stock.Domain;


import java.math.BigDecimal;

public class StockDto {

    private String symbol;
    private String name;
    private BigDecimal Price;
    private BigDecimal nsePrice;
    private BigDecimal bsePrice;
    private BigDecimal potentialProfits;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getNsePrice() {
        return nsePrice;
    }

    public void setNsePrice(BigDecimal nsePrice) {
        this.nsePrice = nsePrice;
    }

    public BigDecimal getBsePrice() {
        return bsePrice;
    }

    public void setBsePrice(BigDecimal bsePrice) {
        this.bsePrice = bsePrice;
    }

    public BigDecimal getPotentialProfits() {
        return potentialProfits;
    }

    public void setPotentialProfits(BigDecimal potentialProfits) {
        this.potentialProfits = potentialProfits;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public BigDecimal getPrice() {
        return Price;
    }

    public void setPrice(BigDecimal tickerPrice) {
        this.Price = tickerPrice;
    }

}

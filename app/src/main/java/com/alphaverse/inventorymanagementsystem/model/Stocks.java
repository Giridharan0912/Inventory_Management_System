package com.alphaverse.inventorymanagementsystem.model;

public class Stocks {
    private String StockName;
    private String StockNos;

    public Stocks(String stockName, String stockNos) {
        this.StockName = stockName;
        this.StockNos = stockNos;
    }

    public Stocks() {
    }

    public String getStockName() {
        return StockName;
    }

    public void setStockName(String stockName) {
        this.StockName = stockName;
    }

    public String getStockNos() {
        return StockNos;
    }

    public void setStockNos(String stockNos) {
        this.StockNos = stockNos;
    }
}

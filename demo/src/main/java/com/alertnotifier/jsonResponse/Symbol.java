package com.alertnotifier.jsonResponse;

public class Symbol {
    public String symbolName;
    public Price price = new Price();

    public void setSymbol(String symbol){
        this.symbolName = symbol;
    }

    public Symbol(String symbolName , Price price){
        this.symbolName = symbolName;
        this.price = price;
    }

}

package com.alertnotifier.jsonResponse;

import com.fasterxml.jackson.annotation.*;

import java.util.ArrayList;
import java.util.*;

public class Data {

    public List<Symbol> symbols = new ArrayList<>();
    public HashMap<String, Price> map = new HashMap<>();

    // public void setSymbol(String symbol){
    // this.symbolName = symbol;
    // }

    @JsonAnySetter
    public void anySetter(String symbolName, Price price) {
        map.put(symbolName, price);
        // map.forEach((k , v) -> v.setSymbol(k));

        symbols.add(new Symbol(symbolName, map.get(symbolName)));

        // for every loop clear the symbols arrayList for newer entries;
    }
}

package com.restocktime.monitor.util;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CurrencyConverter {

    private Map<String, Double> conversionRates;
    private DecimalFormat decimalFormat;
    private String currency;


    public CurrencyConverter(Map<String, Double> conversionRates, String currency) {
        this.conversionRates = new HashMap<>();
        this.currency = currency;
        decimalFormat = new DecimalFormat("#.##");

        for(String key: conversionRates.keySet()){
            this.conversionRates.put(key, (conversionRates.get(key)/conversionRates.get(currency)));
        }
    }

    public CurrencyConverter(CurrencyConverter currencyConverter){
        this.conversionRates = currencyConverter.conversionRates;
        this.currency = currencyConverter.currency;
        this.decimalFormat = currencyConverter.decimalFormat;
    }

    public String getCurrencyStr(Double price){
        List<String> prices = new ArrayList<>();
        for(String key : conversionRates.keySet()){
            prices.add(decimalFormat.format(Math.floor(price * conversionRates.get(key) * 100) / 100) + " " + key + (key.equals(currency)?" (Base)":""));
        }

        return String.join(" / ", prices);
    }
}

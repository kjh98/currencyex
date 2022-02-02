package com.ex.currencyex.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class CurrencyDto {
    private boolean success;
    private int timestamp;
    private String source;
    private Map<String, Double> quotes;
    private Map<String, String> error;
}

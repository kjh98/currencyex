package com.ex.currencyex.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class CurrencyDto {
    private boolean success;
    private int timestamp;
    private String source;
    private Map<String, Double> quotes;
//    private Map<String, String> error;
}

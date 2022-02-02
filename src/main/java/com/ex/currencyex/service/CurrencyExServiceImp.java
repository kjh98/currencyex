package com.ex.currencyex.service;

import com.ex.currencyex.dto.CurrencyDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CurrencyExServiceImp implements CurrencyExService{
    @Value("${currencyLayer.accessKey}")
    private String accessKey;
    @Value("${currencyLayer.url}")
    private String url;
    @Value("${currencyLayer.source}")
    private String source;
    @Value("${currencyLayer.currencies}")
    private String currencies;
    @Value("${currencyLayer.cycleTime}")
    private int cycleTime;

    private RestTemplate restTemplate;
    private CurrencyDto currency;


    @Override
    public CurrencyDto getCurrency() {
            currency = restTemplate.getForObject(
                    url + "?access_key=" + accessKey
                            + "&source=" + source
                            + "&currencies=" + currencies,
                    CurrencyDto.class);
        return currency;
    }

}

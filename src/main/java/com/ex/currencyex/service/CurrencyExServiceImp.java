package com.ex.currencyex.service;

import com.ex.currencyex.dto.CurrencyDto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.DeserializationFeature;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.annotate.JsonValue;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.TimeUnit;


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

    private RestTemplate restTemplate = new RestTemplate();
    private CurrencyDto currency = new CurrencyDto();
    String inputLine = null;
    StringBuffer outResult = new StringBuffer();




    @Override
    public CurrencyDto getCurrency(){
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try{
            System.out.println("try 들어옴");
            URL getUrl =  new URL(url +"access_key=" + accessKey + "&source=" + source + "&currencies=" + currencies);
            HttpURLConnection conn = (HttpURLConnection)getUrl.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept-Charset", "UTF-8");
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(10000);
            OutputStream os = conn.getOutputStream();
            os.flush();
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            while ((inputLine = in.readLine()) != null) {
                outResult.append(inputLine);
            }
            conn.disconnect();
            String jsonObject = outResult.toString();
            currency = mapper.readValue(jsonObject, CurrencyDto.class);

        }catch (IOException e){
            e.printStackTrace();
        }
        return currency;
    }

    //현재 시간과 API를 통해 호출한 timestamp의 차이가
    //application.properties에 저장해 놓은 주기 시간보다 크면 API를 호출하도록 하는 메소드
    private boolean ischeck(){
        if(currency == null){
            return true;
        }
        long currentTime = TimeUnit.MICROSECONDS.toMinutes(System.currentTimeMillis());
        return currentTime - currency.getTimestamp() > cycleTime;
    }

}

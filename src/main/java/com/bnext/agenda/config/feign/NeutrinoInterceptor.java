package com.bnext.agenda.config.feign;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import feign.RequestInterceptor;
import feign.RequestTemplate;

@Component
public class NeutrinoInterceptor implements RequestInterceptor {

	@Value("${neutrino.api.api-key}")
    private String apiKey;

    @Value("${neutrino.api.user-id}")
    private String userId;
    
    
    private static final String API_KEY = "api-key";
    private static final String USER_ID = "user-id";


    @Override
    public void apply(RequestTemplate requestTemplate) {
        requestTemplate.header(API_KEY, apiKey);
        requestTemplate.header(USER_ID, userId);
    }
}

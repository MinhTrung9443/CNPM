package com.cnpm.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
@Slf4j
public class CaptchaValidator {

    private final RestTemplate restTemplate;
    private String V2_SECRET_KEY="6LfhgooqAAAAAKsZIsx4D9blIoyPyyEozsXcKIFD";

    public boolean isValidCaptcha(String captcha) {
        String url = "https://www.google.com/recaptcha/api/siteverify";
//        String params = "?secret=6LdzW5EUAAAAAK-i3g0r8ok09NFbQzEjwfGd016u&response=" + captcha;
        String params = "?secret=" + V2_SECRET_KEY + "&response=" + captcha;
        String completeUrl = url + params;
        CaptchaResponse resp = restTemplate.postForObject(completeUrl, null, CaptchaResponse.class);
        return resp.isSuccess();
    }
}
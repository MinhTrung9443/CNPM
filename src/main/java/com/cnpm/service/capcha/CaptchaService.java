package com.cnpm.service.capcha;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.regex.Pattern;

@Service
public class CaptchaService {

    @Autowired
    private CaptchaSettings captchaSettings;

    @Autowired
    private RestTemplate restTemplate;

    private static final Pattern RESPONSE_PATTERN = Pattern.compile("[A-Za-z0-9_-]+");

    public void processResponse(String response) {
        if (!responseSanityCheck(response)) {
            throw new RuntimeException("Response contains invalid characters");
        }

        URI verifyUri = URI.create(String.format(
            "https://www.google.com/recaptcha/api/siteverify?secret=%s&response=%s",
            captchaSettings.getSecret(), response));

        GoogleResponse googleResponse = restTemplate.getForObject(verifyUri, GoogleResponse.class);

        if (!googleResponse.isSuccess()) {
            throw new RuntimeException("reCaptcha validation failed");
        }
    }

    private boolean responseSanityCheck(String response) {
        return response != null && RESPONSE_PATTERN.matcher(response).matches();
    }
}

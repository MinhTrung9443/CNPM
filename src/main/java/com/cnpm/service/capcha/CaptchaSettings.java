package com.cnpm.service.capcha;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Component
@ConfigurationProperties(prefix = "google.recaptcha.key")
@Data
public class CaptchaSettings {
    private String site;
    private String secret;

    // standard getters and setters
}
package com.cnpm.dto.google;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class GoogleProperties {
	@Value("${google.client.id}")
    private String clientId;

    @Value("${google.client.secret}")
    private String clientSecret;

    @Value("${google.redirect.uri}")
    private String redirectUri;

    @Value("${google.grant.type}")
    private String grantType;

    @Value("${google.token.url}")
    private String tokenUrl;

    @Value("${google.user.info.url}")
    private String userInfoUrl;

    @Value("${google.scope}")
    private String scope;
 // Getters
    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public String getGrantType() {
        return grantType;
    }

    public String getTokenUrl() {
        return tokenUrl;
    }

    public String getUserInfoUrl() {
        return userInfoUrl;
    }

    public String getScope() {
        return scope;
    }
}

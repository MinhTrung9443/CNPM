package com.cnpm.dto.google;

import lombok.Data;

@Data
public class GoogleTokenRequest {
	private String client_id;
    private String client_secret;
    private String redirect_uri;
    private String code;
    private String grant_type;
    public GoogleTokenRequest(String client_id, String client_secret, String redirect_uri, String code) {
        this.client_id = client_id;
        this.client_secret = client_secret;
        this.redirect_uri = redirect_uri;
        this.code = code;
        this.grant_type = "authorization_code";
    }
}


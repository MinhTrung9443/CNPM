package com.cnpm.dto.google;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GoogleTokenResponse {
	@JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("expires_in")
    private int expiresIn;

    @JsonProperty("token_type")
    private String tokenType;

    @JsonProperty("scope")
    private String scope; // Trường scope

    @JsonProperty("id_token") // Thêm trường id_token
    private String idToken;
}

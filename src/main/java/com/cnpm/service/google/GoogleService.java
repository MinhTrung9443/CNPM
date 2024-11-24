package com.cnpm.service.google;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.*;
import org.springframework.web.client.RestTemplate;

import com.cnpm.dto.google.GoogleProperties;
import com.cnpm.dto.google.GooglePojo;
import com.cnpm.dto.google.GoogleTokenRequest;
import com.cnpm.dto.google.GoogleTokenResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class GoogleService {
	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private GoogleProperties ggprop;

	public String getToken(final String code) throws Exception {
		String link = ggprop.getTokenUrl();

		GoogleTokenRequest tokenRequest = new GoogleTokenRequest(ggprop.getClientId(),
				ggprop.getClientSecret(), ggprop.getRedirectUri(), code);

		String response = restTemplate.postForObject(link, tokenRequest, String.class);

		return extractAccessToken(response);
	}

	private String extractAccessToken(String response) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		GoogleTokenResponse tokenResponse = mapper.readValue(response, GoogleTokenResponse.class);
		return tokenResponse.getAccessToken();
	}

	public GooglePojo getUserInfo(final String accessToken) throws Exception {
		String link = ggprop.getUserInfoUrl() + accessToken;
		String response = restTemplate.getForObject(link, String.class);
		return objectMapper.readValue(response, GooglePojo.class);
	}
}

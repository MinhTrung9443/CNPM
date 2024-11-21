package com.cnpm.service.facebook;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

import org.springframework.stereotype.Component;

import com.cnpm.dto.facebook.FacebookProperties;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.Version;

@Component
public class FacebookService {
	public String getToken(final String code) throws IOException {

		String link =FacebookProperties.FACEBOOK_LINK_GET_TOKEN+"client_id="+FacebookProperties.FACEBOOK_CLIENT_ID
				+"&client_secret="+FacebookProperties.FACEBOOK_CLIENT_SECRET+"&redirect_uri="+FacebookProperties.FACEBOOK_REDIRECT_URI+"&code="+code;

		URI uri = URI.create(link);
		URL url = uri.toURL();
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");

		int responseCode = connection.getResponseCode();
		if (responseCode == HttpURLConnection.HTTP_OK) { // thành công
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String inputLine;
			StringBuilder response = new StringBuilder();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			// Parse JSON để lấy access token
			ObjectMapper mapper = new ObjectMapper();
			JsonNode node = mapper.readTree(response.toString()).get("access_token");
			return node.textValue();
		} else {
			throw new IOException("Error: " + responseCode);
		}
	}

	public com.restfb.types.User getUserInfo(final String accessToken) {
		FacebookClient facebookClient = new DefaultFacebookClient(accessToken,
				FacebookProperties.FACEBOOK_CLIENT_SECRET, Version.LATEST);
		return facebookClient.fetchObject("me", com.restfb.types.User.class, Parameter.with("fields", "id,name,email,birthday,gender,location"));
	}
}

package com.cnpm.dto.google;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class GoogleResponse {
    private boolean success;
    private String challengeTs;
    private String hostname;
    private ErrorCode[] errorCodes;

    @JsonIgnore
    public boolean hasClientError() {
        if (errorCodes == null) return false;
        for (ErrorCode error : errorCodes) {
            if (error == ErrorCode.InvalidResponse || error == ErrorCode.MissingResponse) {
                return true;
            }
        }
        return false;
    }

    public enum ErrorCode {
        MissingSecret, InvalidSecret, MissingResponse, InvalidResponse;

        private static final Map<String, ErrorCode> errorsMap = Map.of(
                "missing-input-secret", MissingSecret,
                "invalid-input-secret", InvalidSecret,
                "missing-input-response", MissingResponse,
                "invalid-input-response", InvalidResponse
        );

        @JsonCreator
        public static ErrorCode forValue(String value) {
            return errorsMap.get(value.toLowerCase());
        }
    }

    // Getters và Setters
}

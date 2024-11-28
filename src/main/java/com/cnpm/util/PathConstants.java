package com.cnpm.util;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PathConstants {
	  @Value("${app.upload.directory}")
	    private String uploadDirectory;

	    public static String UPLOAD_DIRECTORY;

	    @PostConstruct
	    public void init() {
	        UPLOAD_DIRECTORY = uploadDirectory;
	    }
}

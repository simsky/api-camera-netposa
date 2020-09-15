package com.welton.video.np;

import feign.Headers;
import feign.Param;
//import feign.Param;
import feign.RequestLine;

public interface EventPushRemote {
	@RequestLine("POST {path}")
	@Headers("Content-Type: application/json")
	String push(String content, @Param("path") String path);
}

package com.thankApi.service;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;

public interface StreamingService {

	public HashMap<String, Object> streaming(HttpServletRequest request, HttpServletResponse response, HttpHeaders headers, String videoName) throws Exception;
}

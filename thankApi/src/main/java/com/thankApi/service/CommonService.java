package com.thankApi.service;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface CommonService {
	
	public HashMap<String, Object> selectJusoApi(HttpServletRequest request, HttpServletResponse response) throws Exception;

	public HashMap<String, Object> naverShortUrl(HttpServletRequest request, HttpServletResponse response) throws Exception;

	public HashMap<String, Object> updateFile(HttpServletRequest request, HttpServletResponse response) throws Exception;
	
}

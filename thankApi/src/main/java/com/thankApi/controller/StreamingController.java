package com.thankApi.controller;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.support.ResourceRegion;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import com.thankApi.service.StreamingService;

@Controller
public class StreamingController {

	@Value("${streaming.location}")
	private String fileLocation;

	@Autowired
	private StreamingService streamingService;

	/**
	 * 영상 스트리밍
	 */
	@GetMapping(value = "/video/{videoName}")
	public ResponseEntity<ResourceRegion> getVideo(HttpServletRequest request, HttpServletResponse response,
			@RequestHeader HttpHeaders headers, @PathVariable String videoName) throws Exception {
		
		try {
			HashMap<String, Object> streamMap = streamingService.streaming(request, response, headers, videoName);
			if ("Y".equals(String.valueOf(streamMap.get("result")))) {
				return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
						.contentType(MediaTypeFactory.getMediaType((UrlResource)streamMap.get("video")).orElse(MediaType.APPLICATION_OCTET_STREAM))
						.body((ResourceRegion) streamMap.get("resourceRegion"));
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
	}
}

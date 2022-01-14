package com.thankApi.service;

import java.io.File;
import java.util.HashMap;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.support.ResourceRegion;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRange;
import org.springframework.stereotype.Service;

import com.thankApi.controller.HomeController;

@Service("SteamingService")
public class StreamingServiceImpl implements StreamingService{

	@Value("${streaming.location}")
	private String fileLocation;
	
	private Logger log = LogManager.getLogger(StreamingServiceImpl.class);
	
	/**
	 * 영상 스트리밍
	 */
	@Override
	public HashMap<String, Object> streaming(HttpServletRequest request, HttpServletResponse response, HttpHeaders headers, String videoName) throws Exception {
		HashMap<String, Object> returnMap = new HashMap<String, Object>();
		
		try {	
			// 파일 위치
			UrlResource video = new UrlResource("file:" + fileLocation + videoName + ".mp4");

			File file = new File(fileLocation + videoName + ".mp4");
			// 파일 존재 하는지 확인
			if( !file.exists() ) { // 파일이 없을 때
				returnMap.put("result", "N");
				log.error("No File");
				return returnMap;
			}
			ResourceRegion resourceRegion;
			final long chunkSize = 1000000L;
			long contentLength = video.contentLength();
			Optional<HttpRange> optional = headers.getRange().stream().findFirst();
			HttpRange httpRange;
			returnMap.put("video", video);
			if( optional.isPresent() ) {
				httpRange = optional.get();
				long start = httpRange.getRangeStart(contentLength);
				long end = httpRange.getRangeEnd(contentLength);
				long rangeLength = Long.min(chunkSize, end - start + 1);
				resourceRegion = new ResourceRegion(video, start, rangeLength);
				returnMap.put("resourceRegion", resourceRegion);
				returnMap.put("result", "Y");
			} else {
				long rangeLength = Long.min(chunkSize, contentLength);
				resourceRegion = new ResourceRegion(video, 0, rangeLength);
				System.out.println("option2");
				returnMap.put("resourceRegion", resourceRegion);
				returnMap.put("result", "Y");
				resourceRegion = (ResourceRegion) returnMap.get("resourceRegion");
			}
		} catch (Exception e) {
			e.printStackTrace();
			returnMap.put("result", "N");
		}
		return returnMap;
	}
}

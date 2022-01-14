package com.thankApi.service;

import java.io.File;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thankApi.dao.CommonDao;
import com.thankApi.util.CommonUtil;
import com.thankApi.util.NaverApi;
@Service("CommonService")
public class CommonServiceImpl implements CommonService {
	
	private static final Logger  log =  LogManager.getLogger(CommonServiceImpl.class);
	
	// 네이버 클라이언트 아이디
	@Value("${naver.client}")
	private String CLIENT_ID;
	// 네이버 클라이언트 비밀번호
	@Value("${naver.secret}")
	private String CLIENT_SECRET;
	
	@Value("${file.path}")
	private String FILE_PATH;
	
	@Autowired
	private CommonDao dao;

	@Override
	public HashMap<String, Object> selectJusoApi(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return null;
	}
	/**
	 * Shorten Url
	 * 원본 URL을 받아 Naver URL을 조회하여 맵핑
	 */
	@Override
	public HashMap<String, Object> naverShortUrl(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HashMap<String, Object> returnMap = new HashMap<String, Object>(); // 데이터 리턴객체
		String originalURL = "https://developers.naver.com/notice";	// 오리지날 URL
		String apiURL = "https://openapi.naver.com/v1/util/shorturl?url=" + originalURL; // Naver Short url Api 주소 :: Get방식
		String changeUrl = ""; // 변경될 URL

		HashMap<String, String> requestHeaders = new HashMap<String, String>();
		requestHeaders.put("X-Naver-Client-Id", CLIENT_ID);
		requestHeaders.put("X-Naver-Client-Secret", CLIENT_SECRET);
		String responseBody = NaverApi.shortUrlGet(apiURL, requestHeaders);

		try {
			JSONParser parser = new JSONParser();
			JSONObject jObj = (JSONObject) parser.parse(responseBody);
			JSONObject jResult = (JSONObject) jObj.get("result");
			changeUrl = CommonUtil.nvl(jResult.get("url"));
			
			returnMap.put("changeUrl", changeUrl);
			returnMap.put("result", true);
			returnMap.put("message", "ShortUrl 변경에 성공했습니다.");
			// TODO DB에 저장 하여 값 처리
		} catch (Exception e) {
			returnMap.put("result", false);
			returnMap.put("message", "ShortUrl 변경에 실패했습니다.");
			e.printStackTrace();
		}
		return returnMap;
	}
	
	/**
	 * 폴더에 있는 파일목록의  DB화
	 */
	@Transactional
	@Override
	public HashMap<String, Object> updateFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		HashMap<String, Object> titleMap = new HashMap<String, Object>();
		
		String folderNm = "";
		String oriFileNm = ""; // 원본 파일 이름
//		String extNm = ""; // 확장자 이름
		int result = 0;
		int successCnt = 0;
		int gallerySeq = 0;
		long lastTime= 0;
		boolean titleBoolean = false;
		
		// 폴더 경로 조회
		File dir = new File(FILE_PATH);
		// 폴더 내에 있는 폴더 목록 조회
		File[] folderArr = dir.listFiles();
		
		String pattern = "yyyyMMddHHmmss";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		
		try {
			// DB에 저장되어 있는 폴더 목록 조회
			List<HashMap<String, Object>> authList = dao.selectGalleryAuthList();
			// 현재 폴더 목록과 저장된 폴더 목록 비교
			for(int i = 0; i < folderArr.length; i++) {
				titleBoolean = false;
				// 폴더 이름 
				folderNm = folderArr[i].getName();
				// 폴더 수정일 
				lastTime = folderArr[i].lastModified();
				Date lastModifiedDate = new Date( lastTime );
				titleMap.put("REG_DATE", simpleDateFormat.format(lastModifiedDate));
				// 폴더명 DB처리
				titleMap.put("AUTHOR", folderNm);
				File imgDir = new File(folderArr[i].toString());
				File[] imgDirArr = imgDir.listFiles();
				
				for (HashMap<String, Object> authMap : authList) {
					// 폴더명이 일치할 경우
					if(folderNm.equals((String)authMap.get("AUTHOR"))) {
						// DB에 저장된 수치가 일치하지 않을경우 삭제 후 다시 집어넣기
						if(imgDirArr.length > (int)(long) authMap.get("CNT") || imgDirArr.length < (int)(long) authMap.get("CNT") ) {
							dao.deleteGalleryDetail(authMap);
							dao.deleteGallery(authMap);
						} else {
							titleBoolean = true;
							continue;	
						}
					}
				}
				
				if( titleBoolean ) {
					continue;
				}
				// 폴더 이름 저장
				gallerySeq = dao.selectGallerySeq();
				titleMap.put("GALLERY_SEQ", gallerySeq);
				result = dao.insertGallery(titleMap);
				
				if(result < 1) {
					continue;
				}
				// 파일 처리
				try {
					if( folderNm != null && !"".equals(folderNm)) {
						File dir2 = new File(folderArr[i].toString());
						File[] filesArr = dir2.listFiles(); // 파일 리스트화
						for(int j = 0; j < filesArr.length; j++) {
							HashMap<String, Object> returnMap = new HashMap<String, Object>();
							returnMap.put("FILE_NAME", filesArr[j].getName()); // 파일 이름 저장
							returnMap.put("FILE_PATH", "/"+folderNm+"/"); // 파일경로
							returnMap.put("FILE_ETC", CommonUtil.getExtension(oriFileNm)); // 확장자
							returnMap.put("GALLERY_SEQ", gallerySeq);
							returnMap.put("PAGE_SEQ", j+1);
							result = dao.insertGalleryDetail(returnMap); 
						}
					} else {
						log.debug("No folder");
						continue;
					}
					successCnt += result;
				} catch (Exception e) {
					e.printStackTrace();
				}
				log.error(folderNm+":: Clear");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if( successCnt > 0 ) {
			resultMap.put("result", true);
		} else {
			resultMap.put("result", false);
		}
		
		return resultMap;
	}
}











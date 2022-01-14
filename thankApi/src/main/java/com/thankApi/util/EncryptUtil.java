package com.thankApi.util;

import java.security.MessageDigest;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thankApi.common.Constants;

public class EncryptUtil {
	
	private static final Logger log = LoggerFactory.getLogger(EncryptUtil.class);
	
	/**
	 * 비밀번호 암호화 모듈  SHA512 복호화 불가능
	 * id, pw 인자를 받아 암호화
	 */
	public static String encryptSHA512(String id, String pw) throws Exception {
		String returnPassWord = "";
		// 아이디 + 비밀번호 + 고유키
		returnPassWord = id + pw + Constants.SECRET_KEY;
		try {
			MessageDigest sh = MessageDigest.getInstance("SHA-512");
			sh.update(returnPassWord.getBytes());
			byte byteData[] = sh.digest();
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < byteData.length; i++) {
				sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
			}
			returnPassWord = sb.toString();

		} catch (Exception e) {
			log.debug("Encrypt Error - NoSuchAlgorithmException");
			returnPassWord = null;
		}
		return returnPassWord;
	}
	/**
	 * 10자리 영문 + 숫자 랜덤 코드
	 */
	public static String tempPw() throws Exception {
		Random rnd = new Random();

		StringBuffer buf = new StringBuffer();
		// 영문자(소문자) 9개 + 숫자 1개 조합
		for (int i = 0; i < 1; i++) {
			buf.append((char) ((int) (rnd.nextInt(26)) + 97));
		}
		for (int i = 0; i < 9; i++) {
			buf.append((rnd.nextInt(10)));
		}
		return buf.toString();
	}
	/**
	 * 이름 가운데 마스킹 처리
	 * 문자 길이가 2자리 이상일경우 마스킹 처리
	 * 1자일경우 처리 안함
	 */
	public static String getMaskedName(String targetName) throws Exception {
		String maskedName = ""; // 마스킹 이름
		String firstName = ""; // 성
		String middleName =""; // 이름 중간
		String lastName = ""; // 이름 끝
		int lastNameStartPoint = 0;; // 이름 시작 포인터
		
		if(targetName != null && !"".equals(targetName)) {
			if(targetName.length() > 1) {
				firstName = targetName.substring(0, 1);
				lastNameStartPoint = targetName.indexOf(firstName);
			}
			if(targetName.trim().length() > 2) {
				middleName = targetName.substring(lastNameStartPoint+1, targetName.trim().length()-1);
				lastName = targetName.substring(lastNameStartPoint + (targetName.trim().length() - 1), targetName.trim().length());				
			} else {
				middleName = targetName.substring(lastNameStartPoint+1, targetName.trim().length());
			}
			String makers = "";
			for(int i=0 ; i < middleName.length(); i++) {
				makers +="*";
			}
			lastName = middleName.replace(middleName, makers)+lastName;
			maskedName = firstName + lastName;
		} else {
			maskedName = targetName;
		}
		return maskedName;
	}
}

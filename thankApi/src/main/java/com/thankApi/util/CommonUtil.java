package com.thankApi.util;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.regex.Pattern;

public class CommonUtil {

	/**
	 * 왼쪽 공백 제거
	 * @param value
	 * @param length
	 * @param padChar
	 * @return 
	 * @throws Exception
	 */
	public static String lpad(String value, int length, String padChar) throws Exception {
		StringBuilder sbAddChar = new StringBuilder();

		for (int i = value.length(); i < length; i++) {
			sbAddChar.append(padChar);
		}

		sbAddChar.append(value);

		return sbAddChar.toString();
	}
	
	/**
	 * 오른쪽 공백 제거
	 * @param value
	 * @param length
	 * @param padChar
	 * @return
	 * @throws Exception
	 */
	public static String rpad(String value, int length, String padChar) throws Exception {
		StringBuilder sbAddChar = new StringBuilder();

		sbAddChar.append(value);

		for (int i = value.length(); i < length; i++) {
			sbAddChar.append(padChar);
		}

		return sbAddChar.toString();
	}

	/**
	 * value를 pattern 으로 변환한다. 1234567, #,### -> 1,234,567 getFormatNumber
	 * @param value
	 * @param pattern
	 * @return
	 */
	public static String getFormatNumber(String value, String pattern) {
		StringBuffer buffer = new StringBuffer();
		DecimalFormat df = new DecimalFormat(pattern);
		try {
			buffer.append(df.format(Long.parseLong(value)));
		} catch (NumberFormatException e) {
			buffer.append(df.format(Double.parseDouble(value)));
		}
		return buffer.toString();
	}
	
	/**
	 * 확장자를 리턴한다. String
	 */
	public static String getExtension(String s) {
		return s.lastIndexOf(".") > -1 ? s.substring(s.lastIndexOf(".") + 1) : "";
	}

	/**
	 * String 문자열을 받아 천단위 구분 기호를 처리한다. String
	 */
	public static String makeComma(String str) {

		StringBuffer sb = new StringBuffer();

		int str_size = str.length(); // 받은 문자열의 길이
		int bgn_size = str_size % 3; // 콤마를 찍기전 자리수

		if (bgn_size != 0)
			sb.append(str.substring(0, bgn_size));
		if ((str_size % 3 != 0) && str_size > 3)
			sb.append(",");

		for (int i = 0; i < (str_size - bgn_size) / 3; i++) {

			sb.append(str.substring(bgn_size + i * 3, bgn_size + i * 3 + 3));
			if (i < (str_size - bgn_size) / 3 - 1)
				sb.append(",");
		}

		return sb.toString();
	}
	
	/**
	 * 핸드폰번호를 핸드폰 포맷으로 변경 request : 00000000000 response : 000-0000-0000 or original
	 */
	public static String convertPhoneFormat2(String number) {

		// 핸드폰 번호 패턴에 맞는지 확인
		String regEx = "(\\d{3})(\\d{3,4})(\\d{4})";
		// 패턴이 맞지 않을 경우 전화번호 그대로 반환
		if (!Pattern.matches(regEx, number)) {
			return number;
		}
		// 패턴이 맞을 경우 "-"를 추가하여 반환
		return number.replaceAll(regEx, "$1-$2-$3");
	}
	
	// '-' 문자 제거
	public static String removeHyphen(Object value) {
		String returnValue = (String) value;
		returnValue = returnValue.replace("-", "");
		return returnValue;
	}

	/**
	 * 전달 값의 null 여부 및 공백여부를 판단하여 지정된 기본값을 반환한다.
	 * 
	 * @param value
	 * @param defaultValue
	 * @return
	 * @throws Exception
	 */
	public static String nvl(Object value, String defaultValue) throws Exception {
		if (value == null || "".equals(value))
			return defaultValue;
		else
			return (String) value;
	}

	/**
	 * 전달 값의 null 여부 및 공백여부를 판단하여 해당할 경우 빈 문자열을 반환한다.
	 * 
	 * @param value
	 * @return
	 * @throws Exception
	 */
	public static String nvl(Object value) throws Exception {
		return nvl(value, "");
	}
}

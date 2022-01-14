package com.thankApi.controller;

import java.lang.reflect.Method;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.thankApi.service.CommonService;

@Controller
@RestController
@RequestMapping("common")
public class CommonController {

	private static final Logger  log =  LogManager.getLogger(CommonController.class);
	@Autowired
	private CommonService commonService;
	
	HttpSession session;

	/**
	 * common json 데이터 처리
	 * @param request
	 * @param response
	 * @param serviceId
	 * @param actionId
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/{serviceId}/{actionId}", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody HashMap<String, Object> commonJson(HttpServletRequest request, HttpServletResponse response, @PathVariable("serviceId") String serviceId, @PathVariable("actionId") String actionId) throws Exception {
		System.out.println("작동");
		HashMap<String, Object> returnMap = new HashMap<String, Object>(); // 데이터 리턴객체
		try {
			/**
			 * actionId를 통해 호출해야할 method를 찾을 수 있다.
			 * 이때 해당 method가 받는 인자의 클래스를 전달하여 정확하게 어떤 메소드인지를 찾는다.
			 * classes 배열변수의 인자가 찾으려 하는 메소드(actionID)의 인자와 다를경우 NoSuchMethodExceptoin이 발생한다.
			 */
			Class[] classes = { HttpServletRequest.class, HttpServletResponse.class };
			Method method = commonService.getClass().getDeclaredMethod(actionId, classes); // method 지정

			// 해당 메소드를 실행한다. method.invoke({해당 메소드를 실행할 클래스객체}, {해당 메소드가 받아야 할 인자});
			Object[] obj = { request, response };
			returnMap = (HashMap<String, Object>) method.invoke(commonService, obj);
		} catch (Exception e) {
			returnMap.put("result", false);
			returnMap.put("message", "ShortUrl 변경에 실패했습니다.");
			e.printStackTrace();
			return returnMap;
		}
		return returnMap;
	}
}

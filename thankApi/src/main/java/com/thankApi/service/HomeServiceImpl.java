package com.thankApi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thankApi.dao.HomeDao;

@Service("HomeService")
public class HomeServiceImpl  implements HomeService{
	
	@Autowired
	private HomeDao dao;
	
}

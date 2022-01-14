package com.thankApi.dao;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Service;

@Service("HomeDao")
public class HomeDao {
	
	@Resource(name = "sqlSessionTemplate")
	private SqlSessionTemplate sqlSession;
	// db 테스트
	public void ipSelect() {
		List<HashMap<String, Object>> authList = sqlSession.selectList("HomeSQL.selectGallery");
	}
	
}

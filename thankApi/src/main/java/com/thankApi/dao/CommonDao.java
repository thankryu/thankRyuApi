package com.thankApi.dao;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Service;

@Service("CommonDao")
public class CommonDao {

	@Resource(name = "sqlSessionTemplate")
	private SqlSessionTemplate sqlSession;
	
	public List<HashMap<String, Object>> selectGalleryAuthList() {
		return sqlSession.selectList("ViewSQL.selectGalleryAuthList");
	}

	public void deleteGalleryDetail(HashMap<String, Object> paramMap) {
		sqlSession.delete("ViewSQL.deleteGalleryDetail", paramMap);
	}

	public void deleteGallery(HashMap<String, Object> paramMap) {
		sqlSession.delete("ViewSQL.deleteGalleryDetail", paramMap);
	}

	public int selectGallerySeq() {
		return sqlSession.selectOne("ViewSQL.selectGallerySeq");
	}

	public int insertGallery(HashMap<String, Object> paramMap) {
		return sqlSession.insert("ViewSQL.insertGallery", paramMap);
	}

	public int insertGalleryDetail(HashMap<String, Object> paramMap) {
		return sqlSession.insert("ViewSQL.insertGalleryDetail", paramMap);
	}

}

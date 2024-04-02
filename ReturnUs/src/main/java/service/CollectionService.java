package service;

import javax.servlet.http.HttpServletRequest;

import dto.Collection;

public interface CollectionService {
	void insertCollect(HttpServletRequest request) throws Exception;
	void collectionList(HttpServletRequest request) throws Exception;
	void collectionDetail(HttpServletRequest request) throws Exception;
	void modifyCollection(Collection collection) throws Exception;
}

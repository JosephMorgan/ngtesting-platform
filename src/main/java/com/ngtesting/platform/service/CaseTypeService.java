package com.ngtesting.platform.service;

import com.ngtesting.platform.model.TstCaseType;

import java.util.List;

public interface CaseTypeService extends BaseService {
	List<TstCaseType> list(Integer orgId);

	TstCaseType get(Integer id);

	TstCaseType save(TstCaseType vo, Integer orgId);
	boolean delete(Integer id);

	boolean setDefaultPers(Integer orgId, Integer orgId2);

	boolean changeOrderPers(Integer id, String act, Integer orgId);

//    void createDefaultBasicDataPers(Integer id);
}

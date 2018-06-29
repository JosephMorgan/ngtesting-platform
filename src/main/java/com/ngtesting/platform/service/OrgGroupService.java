package com.ngtesting.platform.service;

import com.alibaba.fastjson.JSONArray;
import com.ngtesting.platform.model.TstOrgGroup;
import com.ngtesting.platform.vo.Page;

import java.util.List;

public interface OrgGroupService extends BaseService {

	Page listByPage(Long orgId, String keywords, String disabled, Integer currentPage, Integer itemsPerPage);
	List search(Long orgId, String keywords, JSONArray exceptIds);

	TstOrgGroup save(TstOrgGroup vo, Long orgId);
	boolean delete(Long id);

//	void initDefaultBasicDataPers(TestOrg org);

	List<TstOrgGroup> genVos(List<TstOrgGroup> pos);
	TstOrgGroup genVo(TstOrgGroup user);

}

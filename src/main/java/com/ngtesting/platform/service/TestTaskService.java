package com.ngtesting.platform.service;

import com.alibaba.fastjson.JSONObject;
import com.ngtesting.platform.model.TstCaseInRun;
import com.ngtesting.platform.model.TstTask;
import com.ngtesting.platform.model.TstSuite;
import com.ngtesting.platform.model.TstUser;

import java.util.List;

public interface TestTaskService extends BaseService {

	List<TstCaseInRun> lodaCase(Integer runId);
	TstTask getById(Integer caseId);
	TstTask save(JSONObject json, TstUser optUser);

	boolean importSuiteCasesPers(TstTask run, List<TstSuite> suites);

	TstTask saveCases(Integer projectId, Integer caseProjectId, Integer planId, Integer runId, Object[] ids, TstUser optUser);

	TstTask saveCases(JSONObject json, TstUser optUser);

	void addCasesBySuitesPers(Integer suiteId, List<Integer> suiteIds);
	void addCasesPers(Integer suiteId, List<Integer> caseIds);

	TstTask delete(Integer id, Integer userId);
	TstTask closePers(Integer id, Integer userId);

    void closePlanIfAllRunClosedPers(Integer planId);

	List<TstTask> listByPlan(Integer id);

    List<TstTask> genVos(List<TstTask> pos);
	TstTask genVo(TstTask po);

	List<TstCaseInRun> genCaseVos(List<TstCaseInRun> ls);
	TstCaseInRun genCaseVo(TstCaseInRun po);


}

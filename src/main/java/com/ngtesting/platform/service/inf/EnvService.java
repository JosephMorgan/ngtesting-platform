package com.ngtesting.platform.service.inf;

import com.alibaba.fastjson.JSONObject;
import com.ngtesting.platform.model.TstEnv;
import com.ngtesting.platform.model.TstUser;

import java.util.List;

public interface EnvService extends BaseService {

	List<TstEnv> list(Integer projectId, String keywords, String disabled);

	TstEnv getById(Integer id);
	TstEnv save(JSONObject json, TstUser optUser);
	TstEnv delete(Integer vo, Integer userId);

    boolean changeOrderPers(Integer id, String act, Integer orgId);

	List<TstEnv> listVos(Integer projectId);

	List<TstEnv> genVos(List<TstEnv> pos);
	TstEnv genVo(TstEnv po);
}

package com.ngtesting.platform.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ngtesting.platform.config.Constant;
import com.ngtesting.platform.dao.CaseDao;
import com.ngtesting.platform.dao.TestSuiteDao;
import com.ngtesting.platform.dao.TestTaskDao;
import com.ngtesting.platform.model.TstCase;
import com.ngtesting.platform.model.TstUser;
import com.ngtesting.platform.service.CaseHistoryService;
import com.ngtesting.platform.service.CaseService;
import com.ngtesting.platform.utils.BeanUtilEx;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Service
public class CaseServiceImpl extends BaseServiceImpl implements CaseService {
    @Autowired
    CaseDao caseDao;
    @Autowired
    TestSuiteDao testSuiteDao;
    @Autowired
    TestTaskDao testTaskDao;

//    @Autowired
//    CaseCommentsService caseCommentsService;
//    @Autowired
//    CaseAttachmentService caseAttachmentService;
    @Autowired
    CaseHistoryService caseHistoryService;

	@Override
	public List<TstCase> query(Integer projectId) {
        List<TstCase> ls = caseDao.query(projectId);

        return ls;
	}

    @Override
    public List<TstCase> queryForSuiteSelection(Integer projectId, Integer caseProjectId, Integer suiteId) {
        Integer id = caseProjectId == null? projectId: caseProjectId;

        List<TstCase> pos = caseDao.query(projectId);
        List<Integer> selectIds = testSuiteDao.listCaseIds(suiteId);
        genVos(pos, selectIds);

        return pos;
    }

    @Override
    public List<TstCase> queryForTaskSelection(Integer projectId, Integer caseProjectId, Integer taskId) {
        Integer id = caseProjectId == null? projectId: caseProjectId;

        List<TstCase> pos = caseDao.query(projectId);
        List<Integer> selectIds = testTaskDao.listCaseIds(taskId);
        genVos(pos, selectIds);

        return pos;
    }

    @Override
	public TstCase getById(Integer caseId) {
		TstCase po = caseDao.getDetail(caseId);

		return po;
	}

    @Override
    public TstCase renamePers(JSONObject json, TstUser user) {
        Integer id = json.getInteger("id");
        String name = json.getString("name");
        Integer pId = json.getInteger("pId");
        Integer projectId = json.getInteger("projectId");

        return renamePers(id, name, pId, projectId, user);
    }

	@Override
	public TstCase renamePers(Integer id, String name, Integer pId, Integer projectId, TstUser user) {
        TstCase po = new TstCase();
        Constant.CaseAct action;

        boolean isNew;
        if (id != null && id > 0) {
            isNew = true;

            po = caseDao.get(id);

            po.setUpdateById(user.getId());
            po.setUpdateTime(new Date());
            action = Constant.CaseAct.rename;
        } else {
            isNew = false;

            po.setLeaf(true);
            po.setId(null);
            po.setpId(pId);
            po.setType("functional");
            po.setPriority("medium");
            po.setContent("");
            po.setOrdr(getChildMaxOrderNumb(po.getpId()));

            po.setCreateById(user.getId());
            po.setCreateTime(new Date());
            action = Constant.CaseAct.create;
        }
        po.setName(name);
        po.setReviewResult(null);
        po.setProjectId(projectId);

        if (isNew) {
            caseDao.save(po);
        } else {
            caseDao.update(po);
        }

        updateParentIfNeededPers(po.getpId());
        caseHistoryService.saveHistory(user, action, po,null);

        TstCase ret = caseDao.getDetail(po.getId());
        return ret;
	}

	@Override
	public TstCase movePers(JSONObject json, TstUser user) {
        Integer srcId = json.getInteger("srcId");

        Integer targetId = json.getInteger("targetId");
        String moveType = json.getString("moveType");
        Boolean isCopy = json.getBoolean("isCopy");

        TstCase src = caseDao.getDetail(srcId);
        TstCase target = caseDao.getDetail(targetId);

        Integer parentId = src.getpId();

        TstCase testCase;
        Constant.CaseAct action;

        if (isCopy) {
            testCase = new TstCase();
            BeanUtilEx.copyProperties(testCase, src);
            testCase.setCreateTime(new Date());
            testCase.setUpdateTime(null);

            // 先清空
            testCase.setSteps(new LinkedList());
            testCase.setHistories(new LinkedList());
            testCase.setComments(new LinkedList());
            testCase.setAttachments(new LinkedList());

            testCase.setId(null);
            action = Constant.CaseAct.copy;
        } else {
            testCase = src;
            action = Constant.CaseAct.move;
        }

        if ("inner".equals(moveType)) {
            testCase.setpId(target.getId());
        } else if ("prev".equals(moveType)) {
//            String hql = "update TstCase c set c.ordr = c.ordr+1 where c.ordr >= ? and c.pId=? and id!=?";
//            getDao().queryHql(hql, target.getOrdr(), target.getpId(), testCase.getId());

            caseDao.addOrderForTargetAndNextCases(testCase.getId(), target.getOrdr(), target.getpId());

            testCase.setpId(target.getpId());
            testCase.setOrdr(target.getOrdr());
        } else if ("next".equals(moveType)) {
//            String hql = "update TstCase c set c.ordr = c.ordr+1 where c.ordr > ? and c.pId=? and id!=?";
//            getDao().queryHql(hql, target.getOrdr(), target.getpId(), testCase.getId());

            caseDao.addOrderForNextCases(testCase.getId(), target.getOrdr(), target.getpId());

            testCase.setpId(target.getpId());
            testCase.setOrdr(target.getOrdr() + 1);
        }

        boolean isParent = false;
        if (isCopy) {
            isParent = cloneStepsAndChildrenPers(testCase, src);

            caseDao.save(testCase);
        } else {
            caseDao.update(testCase);
        }

        updateParentIfNeededPers(parentId);
        updateParentIfNeededPers(targetId);

        TstCase ret = caseDao.getDetail(testCase.getId());
        if (isCopy && isParent) {
            loadNodeTree(ret);
        }

        caseHistoryService.saveHistory(user, action, testCase,null);
        return ret;
	}

    @Override
    public void loadNodeTree(TstCase po) {

        List<TstCase> children = getChildren(po.getId());
        for (TstCase childPo : children) {
            po.getChildren().add(childPo);

            loadNodeTree(childPo);
        }
    }

    @Override
    public void createSample(Integer projectId, TstUser user) {
        TstCase root = new TstCase();
        root.setName("测试用例");
        root.setLeaf(false);
        root.setProjectId(projectId);
        root.setCreateById(user.getId());
        root.setCreateTime(new Date());
        root.setOrdr(0);
        root.setVersion(0);

        caseDao.create(root);

        TstCase testCase = new TstCase();
        testCase.setName("新特性");
        testCase.setType("functional");
        testCase.setPriority("medium");
        testCase.setEstimate(10);
        testCase.setContentType("steps");
        testCase.setpId(root.getId());
        testCase.setProjectId(projectId);
        testCase.setCreateById(user.getId());
        testCase.setCreateTime(new Date());
        testCase.setLeaf(false);
        testCase.setOrdr(0);
        root.setVersion(0);
        caseDao.create(testCase);
        caseHistoryService.saveHistory(user, Constant.CaseAct.create, testCase,null);

        TstCase testCase2 = new TstCase();
        testCase2.setName("新用例");
        testCase2.setType("functional");
        testCase2.setPriority("medium");
        testCase2.setEstimate(10);
        testCase2.setContentType("steps");
        testCase2.setpId(testCase.getId());
        testCase2.setProjectId(projectId);
        testCase2.setCreateById(user.getId());
        testCase2.setCreateTime(new Date());
        testCase2.setLeaf(true);
        testCase2.setOrdr(0);
        caseDao.create(testCase2);
        caseHistoryService.saveHistory(user, Constant.CaseAct.create, testCase2,null);
    }

    @Override
    public void create(TstCase testCase) {
        caseDao.create(testCase);
    }

    @Override
	public TstCase save(JSONObject json, TstUser user) {
        TstCase testCaseVo = JSON.parseObject(JSON.toJSONString(json), TstCase.class);

        Constant.CaseAct action;

        TstCase testCasePo;
        if (testCaseVo.getId() != null && testCaseVo.getId() > 0) {
            action = Constant.CaseAct.update;

            caseDao.update(testCaseVo);

//            testCasePo = (TstCase)get(TstCase.class, testCaseVo.getId());
//            copyProperties(testCasePo, testCaseVo);
//
//            testCasePo.setUpdateById(user.getId());
//            testCasePo.setUpdateTime(new Date());
        } else {
            action = Constant.CaseAct.create;

            testCaseVo.setId(null);
            testCaseVo.setLeaf(true);
            testCaseVo.setCreateById(user.getId());
            testCaseVo.setOrdr(getChildMaxOrderNumb(testCaseVo.getpId()));
            caseDao.save(testCaseVo);

//            testCasePo = new TstCase();
//            copyProperties(testCasePo, testCaseVo);
//            testCasePo.setId(null);
//            testCasePo.setLeaf(true);
//            testCasePo.setOrdr(getChildMaxOrderNumb(testCasePo.getpId()));
//
//            testCasePo.setCreateById(user.getId());
//            testCasePo.setCreateTime(new Date());
        }

        caseHistoryService.saveHistory(user, action, testCaseVo,null);

        TstCase ret = caseDao.getDetail(testCaseVo.getId());
		return ret;
	}

    @Override
	public TstCase saveField(JSONObject json, TstUser user) {
		Integer id = json.getInteger("id");
		String prop = json.getString("prop");
		String value = json.getString("value");
		String label = json.getString("label");

		caseDao.updateProp(id, prop, value);

        TstCase testCase = caseDao.getDetail(id);
        caseHistoryService.saveHistory(user, Constant.CaseAct.update, testCase,label);

		return testCase;
	}

	@Override
	public void delete(Integer id, TstUser user) {
        caseDao.delete(id);

        TstCase testCase = caseDao.get(id);
        updateParentIfNeededPers(testCase.getpId());
        caseHistoryService.saveHistory(user, Constant.CaseAct.delete, testCase,null);
	}

    @Override
    public void updateParentIfNeededPers(Integer pid) {
//        getDao().querySql("{call update_case_parent_if_needed(?)}", pid);
    }

    @Override
    public boolean cloneStepsAndChildrenPers(TstCase testCase, TstCase src) {
//	    boolean isParent = false;
//
//        for (TstCaseStep step : src.getSteps()) {
//            TstCaseStep step1 = new TstCaseStep(testCase.getId(), step.getOpt(), step.getExpect(), step.getOrdr());
//            saveOrUpdate(step1);
//            testCase.getSteps().add(step1);
//        }
//
//        List<TstCase> children = getChildren(src.getId());
//        for(TstCase child : children) {
//            TstCase clonedChild = new TstCase();
//            BeanUtilEx.copyProperties(clonedChild, child);
//            // 不能用以前的
//            clonedChild.setComments(new LinkedList());
//            clonedChild.setSteps(new LinkedList());
//            clonedChild.setHistories(new LinkedList());
//            clonedChild.setAttachments(new LinkedList());
//
//            clonedChild.setId(null);
//            clonedChild.setpId(testCase.getId());
//
//            saveOrUpdate(clonedChild);
//            cloneStepsAndChildrenPers(clonedChild, child);
//        }
//
//        return children.size() > 0;

        return true;
    }

    @Override
    public List<TstCase> getChildren(Integer caseId) {
//        DetachedCriteria dc = DetachedCriteria.forClass(TstCase.class);
//        dc.add(Restrictions.eq("pId", caseId));
//
//        dc.add(Restrictions.eq("deleted", Boolean.FALSE));
//        dc.add(Restrictions.eq("disabled", Boolean.FALSE));
//
//        dc.addOrder(Order.asc("pId"));
//        dc.addOrder(Order.asc("ordr"));
//
        List<TstCase> children = caseDao.getChildren(caseId);
        return children;
    }

	private Integer getChildMaxOrderNumb(Integer parentId) {
		Integer maxOrder = caseDao.getChildMaxOrderNumb(parentId);

		if (maxOrder == null) {
			maxOrder = 0;
		}

		return maxOrder + 1;
	}

    @Override
    public TstCase changeContentTypePers(Integer id, String contentType) {
//        TstCase testCase = (TstCase)get(TstCase.class, id);
//        testCase.setContentType(contentType);
//        testCase.setReviewResult(null);
//        saveOrUpdate(testCase);
//
//        return testCase;

        caseDao.changeContentTypePers(id, contentType);
        TstCase testCase = caseDao.getDetail(id);
        return testCase;
    }

    @Override
    public TstCase reviewResult(Integer id, Boolean result) {
//        TstCase testCase = (TstCase)get(TstCase.class, id);
//        testCase.setReviewResult(pass);
//        saveOrUpdate(testCase);
//
        caseDao.reviewResult(id, result);
        TstCase testCase = caseDao.getDetail(id);
        return testCase;
    }

    @Override
    public void genVos(List<TstCase> pos, List<Integer> selectIds) {
        for (TstCase po: pos) {
            genVo(po, selectIds);
        }
    }

    @Override
    public void genVo(TstCase po, List<Integer> selectIds) {
        if (selectIds != null && selectIds.contains(po.getId())) {
            po.setChecked(true);
        }
    }

}


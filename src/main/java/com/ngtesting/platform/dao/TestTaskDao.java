package com.ngtesting.platform.dao;

import com.ngtesting.platform.model.TstCaseInTask;
import com.ngtesting.platform.model.TstMsg;
import com.ngtesting.platform.model.TstTask;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TestTaskDao {
    List<TstMsg> query(@Param("userId") Integer userId);

    List<TstTask> listByPlan(@Param("planId") Integer planId);

    List<TstCaseInTask> listCases(@Param("id") Integer id);
    List<Integer> listCaseIds(@Param("id") Integer id);
}

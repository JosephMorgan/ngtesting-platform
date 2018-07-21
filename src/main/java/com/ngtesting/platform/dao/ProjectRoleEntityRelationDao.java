package com.ngtesting.platform.dao;

import com.ngtesting.platform.model.TstMsg;
import com.ngtesting.platform.model.TstProjectRoleEntityRelation;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProjectRoleEntityRelationDao {
    List<TstMsg> query(@Param("userId") Integer userId);

    List<TstProjectRoleEntityRelation> listByProject(@Param("projectId") Integer projectId);

    void changeRole(@Param("projectId") Integer projectId,
                    @Param("projectRoleId") Integer projectRoleId,
                    @Param("entityId") Integer entityId);

    void addRole(@Param("orgId") Integer orgId,
                 @Param("projectId") Integer projectId,
                 @Param("projectRoleId") Integer projectRoleId,
                 @Param("entityId") Integer entityId,
                 @Param("type") String type);
}

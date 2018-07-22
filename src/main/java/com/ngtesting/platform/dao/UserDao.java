package com.ngtesting.platform.dao;

import com.ngtesting.platform.model.TstUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserDao {
    List<TstUser> query();
    TstUser get(Integer userId);
    TstUser getByEmail(String nickname);
    TstUser getByEmailAndPassword(@Param("email") String email,
                                  @Param("password") String password);
    TstUser getByToken(String token);

    void save(TstUser record);
    void update(TstUser record);

    void setDefaultOrg(@Param("id") Integer id,
                       @Param("orgId") Integer orgId);

    void setDefaultPrj(@Param("id") Integer id,
                       @Param("prjId") Integer prjId);

    List<TstUser> search(@Param("orgId") Integer orgId,
                         @Param("keywords") String keywords,
                         @Param("exceptIds") String exceptIds);
}

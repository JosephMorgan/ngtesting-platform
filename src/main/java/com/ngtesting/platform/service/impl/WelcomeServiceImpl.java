package com.ngtesting.platform.service.impl;

import com.ngtesting.platform.model.SysNums;
import com.ngtesting.platform.service.inf.WelcomeService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WelcomeServiceImpl extends BaseServiceImpl implements WelcomeService {

    @Override
    public List<SysNums> test() {
//        DetachedCriteria dc = DetachedCriteria.forClass(SysNums.class);
//        dc.add(Restrictions.eq("key", Long.valueOf(1)));
//        List<SysNums> pos = findAllByCriteria(dc);
//
//        return pos;

        return null;
    }

}


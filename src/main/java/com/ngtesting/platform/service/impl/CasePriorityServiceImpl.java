package com.ngtesting.platform.service.impl;

import com.ngtesting.platform.dao.CasePriorityDao;
import com.ngtesting.platform.model.TstCasePriority;
import com.ngtesting.platform.service.CasePriorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CasePriorityServiceImpl extends BaseServiceImpl implements CasePriorityService {
    @Autowired
	CasePriorityDao casePriorityDao;

	@Override
	public List<TstCasePriority> list(Integer orgId) {
		List<TstCasePriority> ls = casePriorityDao.list(orgId);

		return ls;
	}

    @Override
    public TstCasePriority get(Integer id) {
        return casePriorityDao.get(id);
    }

    @Override
	public TstCasePriority save(TstCasePriority vo, Integer orgId) {
        vo.setOrgId(orgId);

        if (vo.getId() == null) {
            Integer maxOrder = casePriorityDao.getMaxOrdrNumb(orgId);
            if (maxOrder == null) {
                maxOrder = 0;
            }
            vo.setOrdr(maxOrder + 10);

            casePriorityDao.save(vo);
        } else {
            casePriorityDao.update(vo);
        }

        return vo;
	}

	@Override
	public boolean delete(Integer id) {
        casePriorityDao.delete(id);

        return true;
	}

	@Override
	public boolean setDefaultPers(Integer id, Integer orgId) {
        casePriorityDao.removeDefault(orgId);
        casePriorityDao.setDefault(id, orgId);

        return true;
	}

	@Override
	public boolean changeOrderPers(Integer id, String act, Integer orgId) {
        TstCasePriority curr = casePriorityDao.get(id);
        TstCasePriority neighbor = null;
        if ("up".equals(act)) {
            neighbor = casePriorityDao.getPrev(curr.getOrdr(), orgId);
        } else if ("down".equals(act)) {
            neighbor = casePriorityDao.getNext(curr.getOrdr(), orgId);
        }
        if (neighbor == null) {
            return false;
        }

        Integer currOrder = curr.getOrdr();
        Integer neighborOrder = neighbor.getOrdr();
        casePriorityDao.setOrder(id, neighborOrder);
        casePriorityDao.setOrder(neighbor.getId(), currOrder);

        return true;
	}

}

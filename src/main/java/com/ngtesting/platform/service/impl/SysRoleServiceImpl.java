package com.ngtesting.platform.service.impl;

import com.ngtesting.platform.model.SysRole;
import com.ngtesting.platform.service.SysRoleService;
import com.ngtesting.platform.vo.Page;
import org.springframework.stereotype.Service;

@Service
public class SysRoleServiceImpl extends BaseServiceImpl implements SysRoleService {

	@Override
	public Page listByPage(Integer orgId, String keywords, String disabled, Integer currentPage, Integer itemsPerPage) {
//        DetachedCriteria dc = DetachedCriteria.forClass(SysRole.class);
//        dc.add(Restrictions.eq("orgId", orgId));
//
//        dc.add(Restrictions.eq("deleted", Boolean.FALSE));
//
//        if (StringUtil.isNotEmpty(keywords)) {
//        	dc.add(Restrictions.like("name", "%" + keywords + "%"));
//        }
//        if (StringUtil.isNotEmpty(disabled)) {
//        	dc.add(Restrictions.eq("disabled", Boolean.valueOf(disabled)));
//        }
//
//        dc.addOrder(Order.asc("id"));
//        Page page = findPage(dc, currentPage * itemsPerPage, itemsPerPage);
//
//		return page;

		return null;
	}

	@Override
	public SysRole save(SysRole vo, Integer orgId) {
//		if (vo == null) {
//			return null;
//		}
//
//		SysRole po = new SysRole();
//		if (vo.getId() != null) {
//			po = (SysRole) get(SysRole.class, vo.getId());
//		}
//
//		po.setName(vo.getName());
//		po.setDescr(vo.getDescr());
//		po.setDisabled(vo.getDisabled());
//
//		saveOrUpdate(po);
//		return po;

		return null;
	}

	@Override
	public boolean delete(Integer id) {
//		SysRole po = (SysRole) get(SysRole.class, id);
//		po.setDeleted(true);
//		saveOrUpdate(po);

		return true;
	}

	@Override
	public boolean disable(Integer id) {
//		SysRole po = (SysRole) get(SysRole.class, id);
//		po.setDisabled(!po.getDisabled());
//		saveOrUpdate(po);

		return true;
	}
}
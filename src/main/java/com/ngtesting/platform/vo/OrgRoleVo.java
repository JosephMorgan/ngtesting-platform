package com.ngtesting.platform.vo;


public class OrgRoleVo extends BaseVo {
	private static final long serialVersionUID = 2846494844575998128L;
	
	private String code;
    private String name;
    private String descr;
    
    private Long orgId;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public String getDescr() {
		return descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}
    
    

}

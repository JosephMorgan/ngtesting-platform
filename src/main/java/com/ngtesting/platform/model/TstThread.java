package com.ngtesting.platform.model;


public class TstThread extends BaseModel {
	private static final long serialVersionUID = 4361282584845454604L;
	private String subject;
    private String descr;
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getDescr() {
		return descr;
	}
	public void setDescr(String descr) {
		this.descr = descr;
	}

}

package com.ngtesting.platform.model;

public class TstEnv extends BaseModel {
    private static final long serialVersionUID = -8999964583887292845L;
    private String name;
    private String descr;

    private Integer projectId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

}

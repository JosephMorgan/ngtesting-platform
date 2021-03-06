package com.ngtesting.platform.vo;


import com.ngtesting.platform.config.ConstantIssue;

import java.util.List;
import java.util.Map;

public class IsuJqlFilter {
    private static final long serialVersionUID = -5923944030125754321L;

    String id;
    String label;
    ConstantIssue.IssueFilterType type;
    ConstantIssue.IssueFilterInput input;
    Map values;
    List<String> operators;

    public IsuJqlFilter(String id, String label,
                        ConstantIssue.IssueFilterType type, ConstantIssue.IssueFilterInput input) {
        this.id = id;
        this.label = label;
        this.type = type;
        this.input = input;

        if (ConstantIssue.IssueFilterInput.string.equals(type)) {
            this.operators = ConstantIssue.OperatorsForString;
        } else if (ConstantIssue.IssueFilterInput.date.equals(type)) {
            this.operators = ConstantIssue.OperatorsForDate;
        }
    }

    public IsuJqlFilter(String id, String label,
                        ConstantIssue.IssueFilterType type, ConstantIssue.IssueFilterInput input, Map values) {
        this.id = id;
        this.label = label;
        this.type = type;
        this.input = input;

        this.values = values;
        if (ConstantIssue.IssueFilterInput.select.equals(type)) {
            this.operators = ConstantIssue.OperatorsForSelect;
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public ConstantIssue.IssueFilterType getType() {
        return type;
    }

    public void setType(ConstantIssue.IssueFilterType type) {
        this.type = type;
    }

    public ConstantIssue.IssueFilterInput getInput() {
        return input;
    }

    public void setInput(ConstantIssue.IssueFilterInput input) {
        this.input = input;
    }

    public Map getValues() {
        return values;
    }

    public void setValues(Map values) {
        this.values = values;
    }

    public List<String> getOperators() {
        return operators;
    }

    public void setOperators(List<String> operators) {
        this.operators = operators;
    }
}

package com.itberries.technopark.itberries.responses.models;

public class StepResponse {
    private Integer id;
    private String name;
    private String type;
    private Boolean isCompleted;
    private Integer parentId;
    private Integer childId;

    public Integer getParentId() {
        return parentId;
    }

    public StepResponse(Integer id, String name, String type, Boolean isCompleted, Integer parentId, Integer childId) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.isCompleted = isCompleted;
        this.parentId = parentId;
        this.childId = childId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public StepResponse() {
    }

    public Integer getChildId() {
        return childId;
    }

    public void setChildId(Integer childId) {
        this.childId = childId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getCompleted() {
        return isCompleted;
    }

    public void setCompleted(Boolean completed) {
        isCompleted = completed;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}

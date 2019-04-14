package com.itberries.technopark.itberries.models;

public class UserState {
    private Long id;
    private Long userId;
    private Long sectionId;
    private Long subsectionId;
    private Long stepId;
    private Boolean hasPassedApplication;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getSectionId() {
        return sectionId;
    }

    public void setSectionId(Long sectionId) {
        this.sectionId = sectionId;
    }

    public Long getSubsectionId() {
        return subsectionId;
    }

    public void setSubsectionId(Long subsectionId) {
        this.subsectionId = subsectionId;
    }

    public Long getStepId() {
        return stepId;
    }

    public void setStepId(Long stepId) {
        this.stepId = stepId;
    }

    public Boolean getHasPassedApplication() {
        return hasPassedApplication;
    }

    public void setHasPassedApplication(Boolean hasPassedApplication) {
        this.hasPassedApplication = hasPassedApplication;
    }
}

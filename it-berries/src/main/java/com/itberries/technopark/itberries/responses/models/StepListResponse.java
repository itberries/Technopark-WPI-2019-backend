package com.itberries.technopark.itberries.responses.models;

import com.itberries.technopark.itberries.models.Step;

import java.util.List;

public class StepListResponse {
    private List<Step> steps;
    private Step currentStep;

    public StepListResponse(List<Step> steps, Step currentStep) {
        this.steps = steps;
        this.currentStep = currentStep;
    }

    public List<Step> getStepResponses() {
        return steps;
    }

    public void setStepResponses(List<Step> stepResponses) {
        this.steps = stepResponses;
    }

    public Step getCurrentStep() {
        return currentStep;
    }

    public void setCurrentStep(Step currentStep) {
        this.currentStep = currentStep;
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

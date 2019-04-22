package com.itberries.technopark.itberries.models;

public class HanbookReward {
    private Long id;
    private Long scoreLimit;
    private Long rewardId;

    public HanbookReward(Long id, Long scoreLimit, Long rewardId) {
        this.id = id;
        this.scoreLimit = scoreLimit;
        this.rewardId = rewardId;
    }

    public HanbookReward() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getScoreLimit() {
        return scoreLimit;
    }

    public void setScoreLimit(Long scoreLimit) {
        this.scoreLimit = scoreLimit;
    }

    public Long getRewardId() {
        return rewardId;
    }

    public void setRewardId(Long rewardId) {
        this.rewardId = rewardId;
    }
}

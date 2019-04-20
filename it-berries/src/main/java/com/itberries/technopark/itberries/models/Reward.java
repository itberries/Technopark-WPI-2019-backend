package com.itberries.technopark.itberries.models;


public class Reward {
    private Long id;
    private String note;
    private String imageUrl;

    public Reward(Long id, String note, String imageUrl) {
        this.id = id;
        this.note = note;
        this.imageUrl = imageUrl;
    }

    public Reward() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}

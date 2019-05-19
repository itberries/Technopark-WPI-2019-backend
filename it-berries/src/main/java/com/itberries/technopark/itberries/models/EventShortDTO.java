package com.itberries.technopark.itberries.models;

public class EventShortDTO {

    private Long id;
    private String title;
    private String date;
    private String city;
    private String previewUrl;

    public EventShortDTO() {
    }

    public EventShortDTO(String title, String date, String city, String previewUrl, Long id) {
        this.title = title;
        this.date = date;
        this.city = city;
        this.previewUrl = previewUrl;
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPreviewUrl() {
        return previewUrl;
    }

    public void setPreviewUrl(String previewUrl) {
        this.previewUrl = previewUrl;
    }
}

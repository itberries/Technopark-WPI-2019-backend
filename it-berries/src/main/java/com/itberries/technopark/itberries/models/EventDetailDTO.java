package com.itberries.technopark.itberries.models;

public class EventDetailDTO {
    private String title;
    private String date;
    private String city;
    private String imageUrl;
    private String description;
    private String detailsUrl;

    public EventDetailDTO() {
    }

    public EventDetailDTO(String title, String date, String city, String imageUrl, String description, String detailsUrl) {
        this.title = title;
        this.date = date;
        this.city = city;
        this.imageUrl = imageUrl;
        this.description = description;
        this.detailsUrl = detailsUrl;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDetailsUrl() {
        return detailsUrl;
    }

    public void setDetailsUrl(String detailsUrl) {
        this.detailsUrl = detailsUrl;
    }
}

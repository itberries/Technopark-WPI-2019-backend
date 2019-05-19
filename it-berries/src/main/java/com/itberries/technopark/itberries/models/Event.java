package com.itberries.technopark.itberries.models;

import java.util.Objects;

public class Event {
    private Long id;
    private String title;
    private String date;
    private String city;
    private String previewUrl;
    private String imageUrl;
    private String description;
    private String detailsUrl;

    public Event() {
    }

    public Event(String title, String date, String city, String previewUrl, String imageUrl, String description, String detailsUrl, Long id) {
        this.title = title;
        this.date = date;
        this.city = city;
        this.previewUrl = previewUrl;
        this.imageUrl = imageUrl;
        this.description = description;
        this.detailsUrl = detailsUrl;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Objects.equals(id, event.id) &&
                Objects.equals(title, event.title) &&
                Objects.equals(date, event.date) &&
                Objects.equals(city, event.city) &&
                Objects.equals(previewUrl, event.previewUrl) &&
                Objects.equals(imageUrl, event.imageUrl) &&
                Objects.equals(description, event.description) &&
                Objects.equals(detailsUrl, event.detailsUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, date, city, previewUrl, imageUrl, description, detailsUrl);
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", date='" + date + '\'' +
                ", city='" + city + '\'' +
                ", previewUrl='" + previewUrl + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", description='" + description + '\'' +
                ", detailsUrl='" + detailsUrl + '\'' +
                '}';
    }
}

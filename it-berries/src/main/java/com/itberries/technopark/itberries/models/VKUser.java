package com.itberries.technopark.itberries.models;

public class VKUser {
    private String id;
    private String first_name;
    private String last_name;
    private Boolean is_closed;
    private Boolean can_access_closed;
    private String deactivated;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public Boolean getIs_closed() {
        return is_closed;
    }

    public void setIs_closed(Boolean is_closed) {
        this.is_closed = is_closed;
    }

    public Boolean getCan_access_closed() {
        return can_access_closed;
    }

    public void setCan_access_closed(Boolean can_access_closed) {
        this.can_access_closed = can_access_closed;
    }

    public String getDeactivated() {
        return deactivated;
    }

    public void setDeactivated(String deactivated) {
        this.deactivated = deactivated;
    }
}

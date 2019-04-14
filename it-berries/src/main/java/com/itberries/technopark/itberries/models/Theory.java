package com.itberries.technopark.itberries.models;

public class Theory {
    private Long id;
    private Long id_subsection;
    private String text;
    private String img;
    private Integer page;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId_subsection() {
        return id_subsection;
    }

    public void setId_subsection(Long id_subsection) {
        this.id_subsection = id_subsection;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

}

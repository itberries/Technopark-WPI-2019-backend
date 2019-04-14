package com.itberries.technopark.itberries.models;

public class Subsection {
    private Long id;
    private Long id_section;
    private String name;
    private Integer parentId;
    private Integer childId;

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getChildId() {
        return childId;
    }

    public void setChildId(Integer childId) {
        this.childId = childId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId_section() {
        return id_section;
    }

    public void setId_section(Long id_section) {
        this.id_section = id_section;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}

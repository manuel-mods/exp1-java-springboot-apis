package com.example.demo;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Movie {
    // the movie class have name, title, category, and year
    @JsonProperty("id")
    private int id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("title")
    private String title;

    @JsonProperty("category")
    private String category;

    @JsonProperty("year")
    private int year;

    // the constructor of the class
    public Movie(int id, String name, String title, String category, int year) {
        this.id = id;
        this.name = name;
        this.title = title;
        this.category = category;
        this.year = year;
    }

    // the getter of the id
    public int getId() {
        return id;
    }

    // the setter of the id
    public void setId(int id) {
        this.id = id;
    }

    // the getter of the name
    public String getName() {
        return name;
    }

    // the setter of the name
    public void setName(String name) {
        this.name = name;
    }

    // the getter of the title
    public String getTitle() {
        return title;
    }

    // the setter of the title
    public void setTitle(String title) {
        this.title = title;
    }

    // the getter of the category
    public String getCategory() {
        return category;
    }

}

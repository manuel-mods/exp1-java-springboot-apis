package com.example.demo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

public class Movie {
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

    @JsonProperty("synopsis")
    private String synopsis;

    @JsonPropertyOrder({ "id", "name", "title", "category", "year", "synopsis" })

    // the constructor of the class
    public Movie(int id, String name, String title, String category, int year, String synopsis) {
        this.id = id;
        this.name = name;
        this.title = title;
        this.category = category;
        this.year = year;
        this.synopsis = synopsis;

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

    // the setter of the category
    public void setCategory(String category) {
        this.category = category;
    }

    // the getter of the year
    public int getYear() {
        return year;
    }

    // the setter of the year
    public void setYear(int year) {
        this.year = year;
    }

    // the setter of the synopsis
    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    // the getter of the synopsis
    public String getSynopsis() {
        return synopsis;
    }

}

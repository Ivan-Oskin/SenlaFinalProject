package com.oskin.ad_board.dto.request;

import jakarta.validation.constraints.*;

public class AdRequest {
    @NotBlank(message = "the title cannot be empty")
    String title;
    @Min(value = 0, message = "the price should be positive or null")
    int price;
    @Size(max= 2000,message="the description cannot exceed 2000 characters")
    String description;
    @NotEmpty(message = "The ad must have a city")
    String city;

    public AdRequest() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCity() {
        return city;
    }

    public String getCityLowerCase() {
        return city.toLowerCase();
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

package com.oskin.ad_board.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class ProfileRequest {
    @NotBlank(message = "the name cannot be empty")
    String name;
    @NotBlank(message = "the surname cannot be empty")
    String surname;
    @Min(value = 18, message = "the age must be over 18 and under 100")
    @Max(value = 100, message = "the age must be over 18 and under 100")
    int age;
    @NotBlank(message = "the city cannot be empty")
    String city;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
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

    public ProfileRequest() {
    }
}

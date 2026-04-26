package com.oskin.ad_board.dto.request;

import java.time.LocalDateTime;

public class ProfileRequest {
    String name;
    String surname;

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

    int age;
    String city;
}

package com.oskin.ad_board.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "profiles")
public class Profile implements IIdentified {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
    @Column(name = "name")
    private String name;
    @Column(name = "surname")
    private String surname;
    @Column(name = "age")
    private int age;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id")
    private City city;
    @Column(name = "rating")
    private double rating;
    @Column(name = "rating_count")
    private int ratingCount;
    @CreationTimestamp
    @Column(name = "created_date_time", updatable = false)
    private LocalDateTime createdDateTime;

    public Profile(int id,
                   User user,
                   String surname,
                   String name,
                   int age,
                   City city,
                   double rating,
                   int ratingCount,
                   double ratingSum) {
        this.id = id;
        this.user = user;
        this.surname = surname;
        this.name = name;
        this.age = age;
        this.city = city;
        this.rating = rating;
        this.ratingCount = ratingCount;
    }

    public Profile() {
    }

    public Profile(User user,
                   String name,
                   String surname,
                   int age,
                   double rating,
                   City city,
                   int ratingCount,
                   double ratingSum) {
        this.user = user;
        this.name = name;
        this.surname = surname;
        this.age = age;
        this.rating = rating;
        this.city = city;
        this.ratingCount = ratingCount;
    }

    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

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

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount(int ratingCount) {
        this.ratingCount = ratingCount;
    }

    public LocalDateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(LocalDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
    }
}

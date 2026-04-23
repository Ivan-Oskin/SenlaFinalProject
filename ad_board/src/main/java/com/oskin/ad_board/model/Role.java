package com.oskin.ad_board.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "roles")
public class Role implements IIdentified {
    @Id
    private int id;
    @Column(name = "name")
    @Enumerated(EnumType.STRING)
    private RoleName role;

    public Role() {
    }

    public RoleName getName() {
        return role;
    }

    @Override
    public int getId() {
        return id;
    }

    @JsonIgnore
    public void setStandardUser() {
        this.role = RoleName.USER;
        this.id = 2;
    }
}

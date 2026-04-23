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
    private RoleName roleName;

    public Role() {
    }

    public RoleName getRoleName() {
        return roleName;
    }

    @Override
    public int getId() {
        return id;
    }

    @JsonIgnore
    public void setStandardUser() {
        this.roleName = RoleName.USER;
        this.id = 2;
    }
}

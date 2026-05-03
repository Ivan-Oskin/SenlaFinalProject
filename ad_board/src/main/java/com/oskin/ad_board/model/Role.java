package com.oskin.ad_board.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;

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

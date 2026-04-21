package com.oskin.ad_board.model;

public class User implements IIdentified {
    private int id;
    private String mail;
    private String password;
    private int roleId;

    public User(String mail, String password, int roleId) {
        this.mail = mail;
        this.password = password;
        this.roleId = roleId;
    }

    public User(int id, String mail, String password, int roleId) {
        this.id = id;
        this.mail = mail;
        this.password = password;
        this.roleId = roleId;
    }

    public User() {
    }

    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }
}

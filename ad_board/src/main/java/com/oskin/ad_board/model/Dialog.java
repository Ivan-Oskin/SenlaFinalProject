package com.oskin.ad_board.model;

import jakarta.persistence.*;

@Entity
@Table(name = "dialogs")
public class Dialog implements IIdentified {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ad_id")
    private Ad ad;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id")
    private User buyer;

    public Dialog() {
    }

    public Dialog(Ad ad, User buyer) {
        this.ad = ad;
        this.buyer = buyer;
    }

    public User getBuyer() {
        return buyer;
    }

    public void setBuyer(User buyer) {
        this.buyer = buyer;
    }

    public Ad getAd() {
        return ad;
    }

    public void setAd(Ad ad) {
        this.ad = ad;
    }

    public Dialog(int id, Ad ad, User buyer) {
        this.id = id;
        this.ad = ad;
        this.buyer = buyer;
    }

    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

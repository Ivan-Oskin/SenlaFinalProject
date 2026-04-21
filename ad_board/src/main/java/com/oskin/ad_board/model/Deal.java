package com.oskin.ad_board.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "deals")
public class Deal implements IIdentified {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ad_id")
    private Ad ad;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id")
    private User buyer;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private StatusDeal status;
    @CreationTimestamp
    @Column(name = "created_date_time", updatable = false)
    private LocalDateTime createdDateTime;

    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public StatusDeal getStatus() {
        return status;
    }

    public void setStatus(StatusDeal status) {
        this.status = status;
    }

    public Deal() {
    }

    public Ad getAd() {
        return ad;
    }

    public void setAd(Ad ad) {
        this.ad = ad;
    }

    public User getBuyer() {
        return buyer;
    }

    public void setBuyer(User buyer) {
        this.buyer = buyer;
    }

    public Deal(Ad ad, User buyer, StatusDeal status) {
        this.ad = ad;
        this.buyer = buyer;
        this.status = status;
    }

    public Deal(Ad ad, User buyer, StatusDeal status, int id) {
        this.ad = ad;
        this.buyer = buyer;
        this.status = status;
        this.id = id;
    }
}

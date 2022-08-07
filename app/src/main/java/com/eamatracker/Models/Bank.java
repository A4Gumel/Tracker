package com.eamatracker.Models;

import java.util.Date;

public class Bank {

    // Nigerian Banks List
    public String name;
    public String slug;
    public String code;
    public String longcode;
    public Object gateway;
    public boolean pay_with_bank;
    public boolean active;
    public boolean is_deleted;
    public String country;
    public String currency;
    public String type;
    public int id;
    public Date createdAt;
    public Date updatedAt;

    public Bank() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLongcode() {
        return longcode;
    }

    public void setLongcode(String longcode) {
        this.longcode = longcode;
    }

    public Object getGateway() {
        return gateway;
    }

    public void setGateway(Object gateway) {
        this.gateway = gateway;
    }

    public boolean isPay_with_bank() {
        return pay_with_bank;
    }

    public void setPay_with_bank(boolean pay_with_bank) {
        this.pay_with_bank = pay_with_bank;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isIs_deleted() {
        return is_deleted;
    }

    public void setIs_deleted(boolean is_deleted) {
        this.is_deleted = is_deleted;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}

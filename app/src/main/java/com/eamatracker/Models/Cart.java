package com.eamatracker.Models;

public class Cart {

    private int id, quantity, items, discount, shipping_company;
    private Double shipping_amount;
    private String grade;

    public Cart() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public int getShipping_company() {
        return shipping_company;
    }

    public void setShipping_company(int shipping_company) {
        this.shipping_company = shipping_company;
    }

    public Double getShipping_amount() {
        return shipping_amount;
    }

    public void setShipping_amount(Double shipping_amount) {
        this.shipping_amount = shipping_amount;
    }

    public int getItems() {
        return items;
    }

    public void setItems(int items) {
        this.items = items;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }
}

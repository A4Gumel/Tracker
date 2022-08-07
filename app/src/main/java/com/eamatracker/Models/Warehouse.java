package com.eamatracker.Models;

import com.google.gson.annotations.SerializedName;

public class Warehouse {

    private String name;

    @SerializedName("warehouse_address")
    private Address warehouseAddress;

    public Warehouse() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Address getWarehouseAddress() {
        return warehouseAddress;
    }

    public void setWarehouseAddress(Address warehouseAddress) {
        this.warehouseAddress = warehouseAddress;
    }
}

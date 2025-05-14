package com.alten.shop.enums;

public enum InventoryStatus {


    INSTOCK("INSTOCK"),
    LOWSTOCK("LOWSTOCK"),
    OUTOFSTOCK("OUTOFSTOCK");

    private final String status;


    InventoryStatus(String status) {
        this.status = status;
    }

    
    public String getStatus() {
        return status;
    }
    

}

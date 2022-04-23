package com.dev334.aircache.model;

public class Item {
    private String itemID;
    private String name;
    private String category;
    private String image;
    private String ownerName;
    private String OwnerID;
    private String LockerID;
    private String rentPrice;
    private String securityMoney;

    Item(){
        //empty Constructor
    }

    public String getItemID() {
        return itemID;
    }

    public void setItemID(String itemID) {
        this.itemID = itemID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getOwnerID() {
        return OwnerID;
    }

    public void setOwnerID(String ownerID) {
        OwnerID = ownerID;
    }

    public String getLockerID() {
        return LockerID;
    }

    public void setLockerID(String lockerID) {
        this.LockerID = lockerID;
    }

    public String getRentPrice() {
        return rentPrice;
    }

    public void setRentPrice(String rentPrice) {
        this.rentPrice = rentPrice;
    }

    public String getSecurityMoney() {
        return securityMoney;
    }

    public void setSecurityMoney(String securityMoney) {
        this.securityMoney = securityMoney;
    }
}

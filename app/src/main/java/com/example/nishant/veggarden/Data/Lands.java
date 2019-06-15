package com.example.nishant.veggarden.Data;

public class Lands {

    String landName, landArea, landAddress, landid;

    public Lands(String landName, String landArea, String landLocation, String landid) {
        this.landName = landName;
        this.landArea = landArea;
        this.landAddress = landLocation;
        this.landid = landid;
    }

    public String getLandName() {
        return landName;
    }

    public String getLandArea() {
        return landArea;
    }

    public String getLandAddress() {
        return landAddress;
    }

    public String getLandid() {
        return landid;
    }
}

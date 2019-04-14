package com.example.rupik.veggarden.Data;

public class Lands {

    String landName, landArea, landLocation;

    public Lands(String landName, String landArea, String landLocation) {
        this.landName = landName;
        this.landArea = landArea;
        this.landLocation = landLocation;
    }

    public String getLandName() {
        return landName;
    }

    public String getLandArea() {
        return landArea;
    }

    public String getLandLocation() {
        return landLocation;
    }
}

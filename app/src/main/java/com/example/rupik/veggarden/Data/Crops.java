package com.example.rupik.veggarden.Data;

public class Crops {

    String cropId, cropName, cropPrice, cropQuantity, cropLand, landName;

    public Crops(String cropId, String cropName, String cropPrice, String cropQuantity, String cropLand, String landName) {
        this.cropId = cropId;
        this.cropName = cropName;
        this.cropPrice = cropPrice;
        this.cropQuantity = cropQuantity;
        this.cropLand = cropLand;
        this.landName = landName;
    }

    public String getCropId() {
        return cropId;
    }

    public String getCropName() {
        return cropName;
    }

    public String getCropPrice() {
        return cropPrice;
    }

    public String getCropQuantity() {
        return cropQuantity;
    }

    public String getCropLand() {
        return cropLand;
    }

    public String getLandName() {
        return landName;
    }
}

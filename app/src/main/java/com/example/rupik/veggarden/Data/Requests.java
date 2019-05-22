package com.example.rupik.veggarden.Data;

public class Requests {

    String agreementid, buyerid, farmerid, cropid, duration, created_at, accepted, uemail, ucontact, uaddress, uname, cropname;

    public Requests(String agreementid, String buyerid, String farmerid, String cropid, String duration, String created_at, String accepted, String uemail, String ucontact, String uaddress, String uname, String cropname) {
        this.agreementid = agreementid;
        this.buyerid = buyerid;
        this.farmerid = farmerid;
        this.cropid = cropid;
        this.duration = duration;
        this.created_at = created_at;
        this.accepted = accepted;
        this.uemail = uemail;
        this.ucontact = ucontact;
        this.uaddress = uaddress;
        this.uname = uname;
        this.cropname = cropname;
    }

    public String getAgreementid() {
        return agreementid;
    }

    public String getBuyerid() {
        return buyerid;
    }

    public String getFarmerid() {
        return farmerid;
    }

    public String getCropid() {
        return cropid;
    }

    public String getDuration() {
        return duration;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getAccepted() {
        return accepted;
    }

    public String getUemail() {
        return uemail;
    }

    public String getUcontact() {
        return ucontact;
    }

    public String getUaddress() {
        return uaddress;
    }

    public String getUname() {
        return uname;
    }

    public String getCropname() {
        return cropname;
    }
}

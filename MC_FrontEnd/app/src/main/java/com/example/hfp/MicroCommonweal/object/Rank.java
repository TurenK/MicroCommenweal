package com.example.hfp.MicroCommonweal.object;

public class Rank {

    private String rank;
    private String avatar;
    private  String donatenumber;
    private String uid;
    private String uName;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setRank(String rank){ this.rank = rank; }
    public String getRank(){ return rank; }
    public void setAvatar(String avatar){this.avatar = avatar;}
    public String getAvatar(){return  avatar;}
    public void setDonatenumber(String donatenumber){this.donatenumber = donatenumber;}
    public String getDonatenumber(){return donatenumber;}

    public void setuName(String uName) {
        this.uName = uName;
    }

    public String getuName() {
        return uName;
    }
}

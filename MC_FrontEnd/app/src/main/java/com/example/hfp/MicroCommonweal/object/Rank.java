package com.example.hfp.MicroCommonweal.object;

public class Rank {

    private String rank;
    private  int avatar;
    private  String donatenumber;
    private String uid;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setRank(String rank){ this.rank = rank; }
    public String getRank(){ return rank; }
    public void setAvatar(int avatar){this.avatar = avatar;}
    public int getAvatar(){return  avatar;}
    public void setDonatenumber(String donatenumber){this.donatenumber = donatenumber;}
    public String getDonatenumber(){return donatenumber;}

}

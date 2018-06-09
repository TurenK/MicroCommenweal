package com.example.hfp.MicroCommonweal.object;

public class Charity {
    private String aID;
    private String name;
    private  String imagepath;
    private  String peoplenum;
    private  String status;

    public Charity(){
    }

    public void setaID(String aID) {
        this.aID = aID;
    }

    public String getaID() {
        return aID;
    }

    public void setName(String name){
        this.name = name;
    }
    public String getName(){

        return name;
    }

    public void setPeoplenum(String peoplenum){
        this.peoplenum = peoplenum;
    }
    public String getPeoplenum(){

        return  peoplenum;
    }

    public void setStatus(String status){
        this.status = status;
    }
    public String getStatus(){

        return  status;
    }

    public String getImagepath() {
        return imagepath;
    }

    public void setImagepath(String imagepath) {
        this.imagepath = imagepath;
    }
}

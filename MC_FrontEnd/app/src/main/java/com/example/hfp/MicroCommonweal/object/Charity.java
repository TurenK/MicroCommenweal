package com.example.hfp.MicroCommonweal.object;

public class Charity {
    private String aID;
    private String name;
    private  String imagepath;
    private  String peoplenum;
    private  String status;
    private String commentscore;
    private String description;
    private int actcom;

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

    public String getCommentscore() {
        return commentscore;
    }

    public void setCommentscore(String commentscore) {
        this.commentscore = commentscore;
    }

    public int getActcom() {
        return actcom;
    }

    public void setActcom(int actcom) {
        this.actcom = actcom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

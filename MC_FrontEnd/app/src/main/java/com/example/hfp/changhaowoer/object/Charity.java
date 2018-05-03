package com.example.hfp.changhaowoer.object;

public class Charity {
    private String name;
    private  int iamgeId;
    private  String peoplenum;
    private  String status;

    public Charity(String name ,int iamgeId,String peoplenum,String status){
        this.name = name;
        this.iamgeId = iamgeId;
        this.peoplenum = peoplenum;
        this.status = status;
    }

    public String getName(){
        return name;
    }

    public int getIamgeId(){
        return  iamgeId;
    }

    public String getPeoplenum(){
        return  peoplenum;
    }
    public String getStatus(){
        return  status;
    }
}

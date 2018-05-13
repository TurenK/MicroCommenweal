package com.example.hfp.MicroCommonweal.object;

public class Message {
    private String str_addresser;
    private  int avatarId;
    private  String str_message;
    private  int pointId;

    public Message(String str_addresser , int avatarId, String str_message, int pointId){
        this.str_addresser = str_addresser;
        this.avatarId = avatarId;
        this.str_message = str_message;
        this.pointId = pointId;
    }

    public String getAddresser(){
        return str_addresser;
    }

    public int getAvatarId(){
        return  avatarId;
    }
    public int getPointId(){
        return  pointId;
    }
    public String getMessage(){
        return  str_message;
    }

}

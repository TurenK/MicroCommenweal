package com.example.hfp.MicroCommonweal.object;

public class UserInfo {
    public static int CHARITY_USER = 1;
    public static int CHARITY_ORG = 2;
    private String uId;
    private String uName;
    private String uPhone;
    private String uAge;
    private String uAvatar;
    private String uLabel;
    private String uAttention;
    private String uAddr;
    private String uMail;
    private String uIntro;
    private String uGender;
    private int type;

    private static UserInfo userInfo = null;

    public static UserInfo getUserInfo(){
        if (userInfo == null){
            userInfo = new UserInfo();
        }
        return userInfo;
    }

    public void setuAge(String uAge) {
        this.uAge = uAge;
    }

    public void setuAttention(String uAttention) {
        this.uAttention = uAttention;
    }

    public void setuAvatar(String uAvatar) {
        this.uAvatar = uAvatar;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public void setuLable(String uLabel) {
        this.uLabel = uLabel;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }

    public void setuPhone(String uPhone) {
        this.uPhone = uPhone;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setuAddr(String uAddr) {
        this.uAddr = uAddr;
    }

    public void setuMail(String uMail) {
        this.uMail = uMail;
    }

    public void setuIntro(String uIntro) {
        this.uIntro = uIntro;
    }

    public String getuAge() {
        return uAge;
    }

    public String getuAttention() {
        return uAttention;
    }

    public String getuAvatar() {
        return uAvatar;
    }

    public String getuId() {
        return uId;
    }

    public String getuLabel() {
        return uLabel;
    }

    public String getuName() {
        return uName;
    }

    public String getuPhone() {
        return uPhone;
    }

    public int getType() {
        return type;
    }

    public String getuAddr() {
        return uAddr;
    }

    public String getuMail() {
        return uMail;
    }

    public String getuIntro() {
        return uIntro;
    }

    public String getuGender() {
        return uGender;
    }

    public void setuGender(String uGender) {
        this.uGender = uGender;
    }
}

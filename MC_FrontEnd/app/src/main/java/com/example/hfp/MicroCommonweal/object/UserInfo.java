package com.example.hfp.MicroCommonweal.object;

public class UserInfo {
    private String uId;
    private String uName;
    private String uPhone;
    private String uAge;
    private String uAvatar;
    private String uLabel;
    private String uAttention;

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
}

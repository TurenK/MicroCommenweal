package com.example.hfp.MicroCommonweal.object;

public class Personal {
    private String avatorurl;
    private String committext;
    private int grade;
    private String uid;
    private String uname;

    public String getAvatorurl() {
        return avatorurl;
    }

    public void setAvatorurl(String avatorurl) {
        this.avatorurl = avatorurl;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public int getGrade() {
        return grade;
    }

    public String getCommittext() {
        return committext;
    }

    public void setCommittext(String committext) {
        this.committext = committext;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }
}

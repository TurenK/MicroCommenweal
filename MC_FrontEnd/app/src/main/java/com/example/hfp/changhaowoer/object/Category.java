package com.example.hfp.changhaowoer.object;

public class Category {
    private int image;
    private String cotegory_name;

    public Category(int image,String cotegory_name){
        this.image = image;
        this.cotegory_name = cotegory_name;
    }

    public int getImage(){return image;}

    public String getCotegory_name(){return cotegory_name;}
}

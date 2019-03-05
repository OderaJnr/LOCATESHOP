package com.example.user.model;

public class Products
{
    private String pid,pname,date,time,price,description,category,image;


    public Products()
    {

    }

    public Products(String pid, String pname, String date, String time, String price, String description, String category, String image) {
        this.pid = pid;
        this.pname = pname;
        this.date = date;
        this.time = time;
        this.price = price;
        this.description = description;
        this.category = category;
        this.image = image;
    }


    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}

package com.comptech.login.khuranas.Model;

public class Cart
{
    private String pname,  price, image, pid,Quantity;

    Cart()
    {

    }

    public Cart(String pname, String price, String image, String pid, String quantity) {
        this.pname = pname;
        this.price = price;
        this.image = image;
        this.pid = pid;
        Quantity = quantity;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }
}

package com.example.vegetablesfruit;

public class ContentData {

    public String Name,image , price, quantity;

    public ContentData(){}

    public ContentData(String Name, String image, String price , String quantity) {
        this.Name = Name;
        this.image = image;
        this.price = price;
        this.quantity = quantity;

    }

    public String getNames() {
        return Name;
    }

    public void setNames(String name) {
        Name = name;
    }

    public String getImages() {
        return image;
    }

    public void setImages(String image) {
        this.image = image;
    }

    public String getPrices() {
        return price;
    }

    public void setPrices(String price) {
        this.price = price;
    }

    public String getQuantitys() {
        return quantity;
    }

    public void setQuantitys(String quantity) {
        this.quantity = quantity;
    }
}

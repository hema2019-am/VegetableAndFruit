package com.example.vegetablesfruit;

public class ContentData {

    public String Name,image, price, quantity;


    public ContentData(){}

    public ContentData(String Name, String image, String prices, String quantity) {
        this.Name = Name;
        this.image = image;
        this.price = prices;
        this.quantity = quantity;


    }



    public String getNamess() {
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


    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}

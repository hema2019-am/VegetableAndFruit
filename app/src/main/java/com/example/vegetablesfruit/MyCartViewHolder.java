package com.example.vegetablesfruit;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyCartViewHolder extends RecyclerView.ViewHolder {

    View cartview;
    TextView itemName, itemPrice, itemQuantity;
    public MyCartViewHolder(@NonNull View itemView) {
        super(itemView);
        cartview = itemView;
        itemName = cartview.findViewById(R.id.cartItemName);
        itemPrice = cartview.findViewById(R.id.cartItemPrice);
        itemQuantity = cartview.findViewById(R.id.cartItemQuantity);


    }

    public void setCartItemName(String name){
        itemName = itemView.findViewById(R.id.cartItemName);
        itemName.setText("Name: " + name);
    }

    public void setCartItemPrice(String price){
        itemPrice = itemView.findViewById(R.id.cartItemPrice);
        itemPrice.setText("Price: " + price);
    }

    public void setCartItemQuantity(String Quantity){
        itemQuantity = itemView.findViewById(R.id.cartItemQuantity);
        itemQuantity.setText("Quantity " + Quantity);
    }
}

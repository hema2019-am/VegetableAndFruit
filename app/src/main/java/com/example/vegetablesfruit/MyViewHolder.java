package com.example.vegetablesfruit;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

public class MyViewHolder extends RecyclerView.ViewHolder {

    TextView fruitNamess, price;
    ImageView imagePic;
    View v;
    EditText quantity;

    Button btn_update;


    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        v = itemView;

        fruitNamess = v.findViewById(R.id.fruitName);
        quantity = v.findViewById(R.id.edt_quantity);
        price = v.findViewById(R.id.price);
        imagePic = v.findViewById(R.id.imagePic);
        btn_update = v.findViewById(R.id.btn_update);


    }

    public void setFruitName(String fruitNames) {
        fruitNamess =  v.findViewById(R.id.fruitName);
        fruitNamess.setText(fruitNames);
    }

    public void setQuantity(String quants) {
        quantity = v.findViewById(R.id.edt_quantity);
        quantity.setText(quants);
    }

    public void setPrice(String prices) {
        price = v.findViewById(R.id.price);
        price.setText("Prices: " + prices);
    }

    public void setImagePic(String imagePics, Context context) {
        imagePic = v.findViewById(R.id.imagePic);
        Picasso.with(context).load(imagePics).placeholder(R.mipmap.ic_launcher).into(imagePic);
    }
}

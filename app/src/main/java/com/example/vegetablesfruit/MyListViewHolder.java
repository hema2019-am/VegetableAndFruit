package com.example.vegetablesfruit;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

public class MyListViewHolder extends RecyclerView.ViewHolder {
    View view;
    TextView list_item_name, list_item_price;
    ImageView list_item_pic, move_arrow_list;
    public MyListViewHolder(@NonNull View itemView) {
        super(itemView);
        view = itemView;


        list_item_name = view.findViewById(R.id.ListItemName);
        list_item_price = view.findViewById(R.id.ListItemPrice);
        list_item_pic = view.findViewById(R.id.ListPic);
        move_arrow_list = view.findViewById(R.id.move_arrow_list);

    }

    public void setList_item_name(String list_item_names){
        list_item_name = view.findViewById(R.id.ListItemName);
        list_item_name.setText(list_item_names);
    }

    public void setList_item_price(String list_item_prices){
        list_item_price = view.findViewById(R.id.ListItemPrice);
        list_item_price.setText("Price: " + list_item_prices);
    }

    public void setList_item_pic(String imagePics, Context context) {
        list_item_pic = view.findViewById(R.id.ListPic);
        Picasso.with(context).load(imagePics).placeholder(R.mipmap.ic_launcher).into(list_item_pic);
    }
}

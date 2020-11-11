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

    TextView catogoriesTypes;
    ImageView imagePic, move_arrow;
    View v;
    EditText quantity;





    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        v = itemView;

        catogoriesTypes = v.findViewById(R.id.types);

        imagePic = v.findViewById(R.id.imagePic);
        move_arrow = v.findViewById(R.id.move_arrow);



    }

    public void setNames(String Types) {
        catogoriesTypes =  v.findViewById(R.id.types);
        catogoriesTypes.setText(Types);
    }



    public void setImagePic(String imagePics, Context context) {
        imagePic = v.findViewById(R.id.imagePic);
        Picasso.with(context).load(imagePics).placeholder(R.mipmap.ic_launcher).into(imagePic);
    }
}

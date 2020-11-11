package com.example.vegetablesfruit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class ListItemData extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    TextView txt_name, txt_price, txt_category;
    Spinner spinner_quantity;
    Button btn_add_to_mart;

    String listName,listCatogroy;
    int listPrice;

    String[] quants = {"1","2","3","4","5","6","7","8","9","10"};
    int finalItemPrice;

    DatabaseReference db;
    int quant;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_item_data);


        txt_name = findViewById(R.id.txt_name);
        txt_price = findViewById(R.id.txt_price);
        txt_category = findViewById(R.id.txt_category);
        spinner_quantity = findViewById(R.id.spinner_quantity);
        btn_add_to_mart = findViewById(R.id.cart);

        listName = getIntent().getStringExtra("item_name");
        listCatogroy = getIntent().getStringExtra("category");
        listPrice = Integer.parseInt(getIntent().getStringExtra("price"));

        txt_name.setText("Name: " + listName);
        txt_category.setText("Category: "+ listCatogroy);
        txt_price.setText("Price: "+ Integer.toString(listPrice) + " rs");

        final ArrayAdapter<String> Class = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, quants);
        Class.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_quantity.setAdapter(Class);
        spinner_quantity.setOnItemSelectedListener(this);




        btn_add_to_mart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
FirebaseUser cureentUser = FirebaseAuth.getInstance().getCurrentUser();

                db = FirebaseDatabase.getInstance().getReference().child("cart").child(cureentUser.getUid()).child(listName);
                Map userCart = new HashMap<>();
                userCart.put("Name", listName);
                userCart.put("price", Integer.toString(finalItemPrice) );
                userCart.put("quantity",Integer.toString(quant) );

                db.updateChildren(userCart).addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        Toast.makeText(ListItemData.this, "Added to cart", Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        quant = Integer.parseInt(quants[position]);
        finalItemPrice = quant * listPrice;
        txt_price.setText("Price: " + Integer.toString(finalItemPrice)+" rs");
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}

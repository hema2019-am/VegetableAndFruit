package com.example.vegetablesfruit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.HashMap;

public class MainScreen extends AppCompatActivity {

   DatabaseReference mDatabase, mUser;

   RecyclerView mRecyclerFruit;

    private FirebaseRecyclerAdapter<ContentData, MyViewHolder> adapter;
    FirebaseRecyclerOptions<ContentData> options;
    Toolbar toolbar;

    ProgressDialog mProgress;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        mRecyclerFruit = findViewById(R.id.recyclerView);
        mRecyclerFruit.setLayoutManager(new LinearLayoutManager(this));






        toolbar = findViewById(R.id.mainUserAppBar);
        setSupportActionBar(toolbar);

        toolbar.setTitleTextColor(Color.BLACK);
        getSupportActionBar().setTitle("List");
        mDatabase = FirebaseDatabase.getInstance().getReference().child("categories");

//        cart = findViewById(R.id.tool_cart);
//        cart.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(MainScreen.this, "carted", Toast.LENGTH_SHORT).show();
//            }
//        });





        Query query = mDatabase;

        options = new FirebaseRecyclerOptions.Builder<ContentData>().setQuery(query,ContentData.class).setLifecycleOwner(this).build();
        adapter = new FirebaseRecyclerAdapter<ContentData, MyViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final MyViewHolder holder, int position, @NonNull final ContentData model) {
              holder.setNames(model.getNamess());

              holder.setImagePic(model.getImages(),getApplicationContext());


              final String catogeryName = getRef(position).getKey();
              holder.move_arrow.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {

                      Intent intent = new Intent(getApplicationContext(),List.class);
                      intent.putExtra("name", catogeryName);
                      startActivity(intent);
                  }
              });




            }

            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view =  LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_row, parent, false);

                return new MyViewHolder(view);
            }


        };


        mRecyclerFruit.setAdapter(adapter);



    }


    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menus_list, menu);

        MenuItem getItem = menu.findItem(R.id.logout);
        if (getItem != null) {
            Button button = (Button) getItem.getActionView();
            button.setText("Logout");
            button.setBackgroundColor(Color.WHITE);
            button.setMinHeight(0);
            button.setMinimumHeight(0);

            button.setHeight(80);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent logout = new Intent(getApplicationContext(), MainActivity.class);
                    FirebaseAuth.getInstance().signOut();
                    startActivity(logout);
                }
            });
            //Set a ClickListener, the text,
            //the background color or something like that
        }

        MenuItem get = menu.findItem(R.id.cart);
        if(get != null){
            Button button = (Button) get.getActionView();
            button.setText("Cart");
            button.setBackgroundColor(Color.WHITE);
            button.setMinHeight(0);
            button.setMinimumHeight(0);

            button.setHeight(80);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent cart = new Intent(getApplicationContext(), Cart.class);
                    startActivity(cart);
                }
            });
        }

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();




    }


    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}

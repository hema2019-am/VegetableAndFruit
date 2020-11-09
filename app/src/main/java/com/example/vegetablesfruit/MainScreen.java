package com.example.vegetablesfruit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.HashMap;

public class MainScreen extends AppCompatActivity {

   DatabaseReference mDatabase, mUser;
   FirebaseDatabase database;
   String fruitName , quanitiy, price;
   String fruitPic;
   RecyclerView mRecyclerFruit;

    private FirebaseRecyclerAdapter<ContentData, MyViewHolder> adapter;
    FirebaseRecyclerOptions<ContentData> options;
    Toolbar toolbar;


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
        mDatabase = FirebaseDatabase.getInstance().getReference().child("data");



        Query query = mDatabase;

        options = new FirebaseRecyclerOptions.Builder<ContentData>().setQuery(query,ContentData.class).setLifecycleOwner(this).build();
        adapter = new FirebaseRecyclerAdapter<ContentData, MyViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final MyViewHolder holder, int position, @NonNull final ContentData model) {
              holder.setFruitName(model.getNames());
              holder.setPrice(model.getPrices());
              holder.setQuantity(model.getQuantitys());
              holder.setImagePic(model.getImages(),getApplicationContext());

              holder.btn_update.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {

                      if (ConnectingReceview.checkConnection(getApplicationContext())) {
                          // Its Available...
                          FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

                          HashMap<String, String> userPickUp = new HashMap<>();
                          userPickUp.put("prices",model.getPrices());
                          userPickUp.put("quantity",holder.quantity.getText().toString());


                          String uid = currentUser.getUid();
                          mUser = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child(model.getNames());
                          mUser.setValue(userPickUp).addOnCompleteListener(new OnCompleteListener<Void>() {
                              @Override
                              public void onComplete(@NonNull Task<Void> task) {
                                  if(task.isSuccessful()){
                                      Toast.makeText(MainScreen.this, "updated", Toast.LENGTH_SHORT).show();
                                  }
                              }
                          });
                      } else {
                          // Not Available...
                          Toast.makeText(getApplicationContext(), "Connect to internet", Toast.LENGTH_SHORT).show();
                      }




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
        menuInflater.inflate(R.menu.menus, menu);

        MenuItem getItem = menu.findItem(R.id.get_item);
        if (getItem != null) {
            Button button = (Button) getItem.getActionView();
            button.setText("Logout");
            button.setBackgroundColor(Color.WHITE);
            button.setMinHeight(0);
            button.setMinimumHeight(0);

            button.setHeight(100);
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

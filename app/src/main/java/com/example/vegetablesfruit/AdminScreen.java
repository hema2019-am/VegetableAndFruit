package com.example.vegetablesfruit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.DialogInterface;
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
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.HashMap;
import java.util.Map;

public class AdminScreen extends AppCompatActivity {

    RecyclerView mRecyclerFruit;
    DatabaseReference mRef, mAdmin, mDeleteRef;

    private FirebaseRecyclerAdapter<ContentData, MyViewHolder> adapter;
    FirebaseRecyclerOptions<ContentData> options;


    Button btn_add;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_screen);

        btn_add = findViewById(R.id.btn_add);

        toolbar = findViewById(R.id.mainAdminAppBar);
        setSupportActionBar(toolbar);

        toolbar.setTitleTextColor(Color.BLACK);
        getSupportActionBar().setTitle("List");


        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddList.class);
                startActivity(intent);
            }
        });


        mRecyclerFruit = findViewById(R.id.recyclerView_admin);
        mRecyclerFruit.setLayoutManager(new LinearLayoutManager(this));

        mRef = FirebaseDatabase.getInstance().getReference().child("data");
        Query query = mRef;


        options = new FirebaseRecyclerOptions.Builder<ContentData>().setQuery(query, ContentData.class).build();
        adapter = new FirebaseRecyclerAdapter<ContentData, MyViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final MyViewHolder holder, int position, @NonNull final ContentData model) {
                holder.setFruitName(model.getNames());
                holder.setPrice(model.getPrices());
                holder.setQuantity(model.getQuantitys());
                holder.setImagePic(model.getImages(), getApplicationContext());

                if (ConnectingReceview.checkConnection(getApplicationContext())) {
                    // Its Available...
                    holder.v.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Snackbar snackbar
                                    = Snackbar
                                    .make(
                                            v,
                                            "do you want to delete the item?",
                                            Snackbar.LENGTH_LONG)
                                    .setAction(
                                            "Ok",

                                            // If the Undo button
                                            // is pressed, show
                                            // the message using Toast
                                            new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view)
                                                {
                                                    mDeleteRef = FirebaseDatabase.getInstance().getReference().child("data").child(model.getNames());
                                                    mDeleteRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if(task.isSuccessful()){
                                                                Toast.makeText(AdminScreen.this, "deleted", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });

                                                }
                                            });

                            snackbar.show();

                        }
                    });
                } else {
                    // Not Available...
                    Toast.makeText(getApplicationContext(), "Connect to internet", Toast.LENGTH_SHORT).show();
                }


                holder.btn_update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (ConnectingReceview.checkConnection(getApplicationContext())) {
                            // Its Available...
                            Map AdminPickUp = new HashMap<>();

                            AdminPickUp.put("quantity", holder.quantity.getText().toString());


                            mAdmin = FirebaseDatabase.getInstance().getReference().child("data").child(model.getNames());
                            mAdmin.updateChildren(AdminPickUp).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getApplicationContext(), "updated", Toast.LENGTH_SHORT).show();
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
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_row, parent, false);

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

                    if (ConnectingReceview.checkConnection(getApplicationContext())) {
                        // Its Available...
                        Intent logout = new Intent(getApplicationContext(), AdminActivity.class);
                        FirebaseAuth.getInstance().signOut();
                        startActivity(logout);
                    } else {
                        // Not Available...
                        Toast.makeText(getApplicationContext(), "Connect to internet", Toast.LENGTH_SHORT).show();
                    }

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

package com.example.vegetablesfruit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.Objects;

public class List extends AppCompatActivity {

    RecyclerView listRecyclerView;
    private FirebaseRecyclerAdapter<ContentData, MyListViewHolder> adapter;
    FirebaseRecyclerOptions<ContentData> options;

    DatabaseReference mListRef, mDeleteRef;
    String catogeryNames;
    int admin =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        catogeryNames = getIntent().getStringExtra("name");
        listRecyclerView = findViewById(R.id.listRecyclerView);
        admin = getIntent().getIntExtra("admin",0);
        listRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mListRef = FirebaseDatabase.getInstance().getReference().child("mart").child(catogeryNames);

        Query query = mListRef;

        options = new FirebaseRecyclerOptions.Builder<ContentData>().setQuery(query, ContentData.class).setLifecycleOwner(this).build();
        adapter = new FirebaseRecyclerAdapter<ContentData, MyListViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MyListViewHolder holder, int position, @NonNull final ContentData model) {

                holder.setList_item_name(model.getNamess());
                holder.setList_item_price(model.getPrice());
                holder.setList_item_pic(model.getImages(), getApplicationContext());
                holder.move_arrow_list.setVisibility(View.VISIBLE);
                holder.move_arrow_list.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent listIntent = new Intent(getApplicationContext(),ListItemData.class);
                        listIntent.putExtra("item_name",model.getNamess());
                        listIntent.putExtra("category",catogeryNames);
                        listIntent.putExtra("price",model.getPrice());
                        startActivity(listIntent);
                    }
                });
                if(admin==1){
                    holder.move_arrow_list.setVisibility(View.INVISIBLE);
                    if (ConnectingReceview.checkConnection(getApplicationContext())) {
                        // Its Available...
                        holder.view.setOnClickListener(new View.OnClickListener() {
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

                                                new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view)
                                                    {
                                                        mDeleteRef = FirebaseDatabase.getInstance().getReference().child("mart").child(catogeryNames).child(model.getNamess());
                                                        mDeleteRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if(task.isSuccessful()){
                                                                    Toast.makeText(getApplicationContext(), "deleted", Toast.LENGTH_SHORT).show();
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

                }

            }

            @NonNull
            @Override
            public MyListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_list_data, parent, false);

                return new MyListViewHolder(view);
            }

        };

        listRecyclerView.setAdapter(adapter);


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

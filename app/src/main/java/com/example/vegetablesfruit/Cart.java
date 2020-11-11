package com.example.vegetablesfruit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class Cart extends AppCompatActivity {
    DatabaseReference mDatabase, mUser;

    RecyclerView mRecyclerFruit;

    Button btn_order;
    FirebaseUser currentUser;

    private FirebaseRecyclerAdapter<ContentData, MyCartViewHolder> adapter;
    FirebaseRecyclerOptions<ContentData> options;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        mRecyclerFruit = findViewById(R.id.cartRecycler);
        mRecyclerFruit.setLayoutManager(new LinearLayoutManager(this));

        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        btn_order = findViewById(R.id.btn_order);
        btn_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(Cart.this, "order Placed", Toast.LENGTH_SHORT).show();
            }
        });

        mDatabase = FirebaseDatabase.getInstance().getReference().child("cart").child(currentUser.getUid());

        Query query = mDatabase;

        options = new FirebaseRecyclerOptions.Builder<ContentData>().setQuery(query,ContentData.class).setLifecycleOwner(this).build();
        adapter = new FirebaseRecyclerAdapter<ContentData, MyCartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MyCartViewHolder holder, int position, @NonNull ContentData model) {
             holder.setCartItemName(model.getNamess());
             holder.setCartItemPrice(model.getPrice());
             holder.setCartItemPrice(model.getQuantity());
            }

            @NonNull
            @Override
            public MyCartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view =  LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_layout, parent, false);

                return new MyCartViewHolder(view);
            }
        };

        mRecyclerFruit.setAdapter(adapter);

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

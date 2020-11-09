package com.example.vegetablesfruit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private Toolbar mToolbarAddUpdates;
    TextView userRegistration;

    FirebaseAuth mAuth;
    EditText edt_email, edt_pass;

    String userEmail, userPass;
    Button btn_user_login;

    ProgressDialog mProgress;

    DatabaseReference databaseReference ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mToolbarAddUpdates = findViewById(R.id.mainAppBar);

        setSupportActionBar(mToolbarAddUpdates);

        mToolbarAddUpdates.setTitleTextColor(Color.BLACK);
        getSupportActionBar().setTitle("Users Login");

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");

       mAuth = FirebaseAuth.getInstance();
        edt_email = findViewById(R.id.edt_user_login_email);
        edt_pass = findViewById(R.id.edt_user_login_password);

        userRegistration = findViewById(R.id.userRegistration);
        userRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent userRegistration = new Intent(getApplicationContext(), UserRegistration.class);
                startActivity(userRegistration);
            }
        });

        btn_user_login = findViewById(R.id.btn_user_login);
        btn_user_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                userEmail = edt_email.getText().toString();
                userPass = edt_pass.getText().toString();


                if (ConnectingReceview.checkConnection(getApplicationContext())) {
                    // Its Available...

                    if(!userEmail.isEmpty() && !userPass.isEmpty()){
                        Toast.makeText(MainActivity.this, "please wait", Toast.LENGTH_SHORT).show();
                        loginUser(userEmail,userPass);

                        edt_pass.setText("");
                        edt_email.setText("");
                    }else {
                        Toast.makeText(MainActivity.this, "empty fields", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    // Not Available...
                    Toast.makeText(getApplicationContext(), "Connect to internet", Toast.LENGTH_SHORT).show();
                }



            }
        });


    }






    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menus, menu);

        MenuItem getItem = menu.findItem(R.id.get_item);
        if (getItem != null) {
            Button button = (Button) getItem.getActionView();
            button.setText("Admin");
            button.setBackgroundColor(Color.WHITE);
            button.setMinHeight(0);
            button.setMinimumHeight(0);

            button.setHeight(100);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent AdminIntent = new Intent(getApplicationContext(), AdminActivity.class);
                    startActivity(AdminIntent);
                }
            });
            //Set a ClickListener, the text,
            //the background color or something like that
        }

        return super.onCreateOptionsMenu(menu);
    }

    private void loginUser(final String mEmail, String password) {

        /**
         * user is login if he is the admin.
         */


        mAuth.signInWithEmailAndPassword(mEmail, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    userEmail = edt_email.getText().toString();
                    userPass = edt_pass.getText().toString();

                    HashMap<String, String> UserDetails = new HashMap<>();

                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

                    String uid = currentUser.getUid();

                    databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);

                    UserDetails.put("email",userEmail);

                    Intent mainScreen = new Intent(getApplicationContext(), MainScreen.class );
                    mainScreen.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(mainScreen);
                    finish();





                } else {
                    // If sign in fails, display a message to the user.

                    Toast.makeText(getApplicationContext(), "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

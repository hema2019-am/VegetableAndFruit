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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class UserRegistration extends AppCompatActivity {
    private Toolbar mToolbarAddUpdates;

    FirebaseAuth mAuth;
    Button btn_user_regestration;

    EditText edt_userReg_email, edt_userReg_Pass, edt_userReg_conPass;
    String mail, pass, confirmPass;

    ProgressDialog mProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);


        mToolbarAddUpdates = findViewById(R.id.userAppBar);

        setSupportActionBar(mToolbarAddUpdates);
        mAuth = FirebaseAuth.getInstance();

        mToolbarAddUpdates.setTitleTextColor(Color.BLACK);
        getSupportActionBar().setTitle("Users Registration");

        edt_userReg_email = findViewById(R.id. edt_user_registration_email);
        edt_userReg_Pass = findViewById(R.id.edt_user_registration_password);
        edt_userReg_conPass = findViewById(R.id.edt_user_registration_ConfirmPassword);

        btn_user_regestration = findViewById(R.id.btn_user_Register);
        btn_user_regestration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                mail = edt_userReg_email.getText().toString();
                pass = edt_userReg_Pass.getText().toString();
                confirmPass = edt_userReg_conPass.getText().toString();
                if(!mail.isEmpty() && !pass.isEmpty() && !confirmPass.isEmpty()){
                    if(pass.equals(confirmPass)){

                        if (ConnectingReceview.checkConnection(getApplicationContext())) {
                            // Its Available...

                            registerUser(mail,pass);
                            Toast.makeText(UserRegistration.this, "please wait", Toast.LENGTH_SHORT).show();
                            edt_userReg_email.setText("");
                            edt_userReg_conPass.setText("");
                            edt_userReg_Pass.setText("");
                        } else {
                            // Not Available...
                            Toast.makeText(getApplicationContext(), "Connect to internet", Toast.LENGTH_SHORT).show();
                        }

                    }else {
                        Toast.makeText(UserRegistration.this, "Password and confirm Password doesn't match", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(UserRegistration.this, "empty fields", Toast.LENGTH_SHORT).show();
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


    private void registerUser(String displayEmail, String displayPassword) {

        mAuth.createUserWithEmailAndPassword(displayEmail, displayPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Toast.makeText(UserRegistration.this, "registered", Toast.LENGTH_SHORT).show();

Intent registeedIntent = new Intent(getApplicationContext(), MainScreen.class);
startActivity(registeedIntent);




                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(UserRegistration.this, "already present", Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });
    }
}

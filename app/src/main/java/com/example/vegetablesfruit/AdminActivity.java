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

public class AdminActivity extends AppCompatActivity {


    EditText edt_admin_email, edt_adminPass;
    Button btn_adminLogin;
    String adminEmail, adminPass;

    FirebaseAuth mAuth;

  Toolbar mToolbar;

  ProgressDialog mProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        edt_admin_email = findViewById(R.id.edt_admin_email);
        edt_adminPass = findViewById(R.id.edt_admin_password);

        mAuth = FirebaseAuth.getInstance();

        mToolbar = findViewById(R.id.adminAppBar);

        setSupportActionBar(mToolbar);
        mAuth = FirebaseAuth.getInstance();

        mProgress = new ProgressDialog(this);
        mProgress.setCanceledOnTouchOutside(false);
        mProgress.setTitle("Login...");

        mToolbar.setTitleTextColor(Color.BLACK);
        getSupportActionBar().setTitle("Admin Login");

        btn_adminLogin = findViewById(R.id.btn_admin_login);
        btn_adminLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



mProgress.show();

                adminEmail = edt_admin_email.getText().toString();
                adminPass = edt_adminPass.getText().toString();
                if (ConnectingReceview.checkConnection(getApplicationContext())) {
                    // Its Available...

                    if(!adminPass.isEmpty() && !adminEmail.isEmpty()){
                        Toast.makeText(AdminActivity.this, "Plase wait", Toast.LENGTH_SHORT).show();
                        loginAdmin(adminEmail,adminPass);
                        edt_admin_email.setText("");
                        edt_adminPass.setText("");
                    }else {
                        mProgress.hide();
                        Toast.makeText(getApplicationContext(), "empty fields", Toast.LENGTH_SHORT).show();
                    }



                } else {
                    mProgress.hide();
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
            button.setText("Users");
            button.setBackgroundColor(Color.WHITE);
            button.setMinHeight(0);
            button.setMinimumHeight(0);

            button.setHeight(100);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent AdminIntent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(AdminIntent);
                }
            });
            //Set a ClickListener, the text,
            //the background color or something like that
        }

        return super.onCreateOptionsMenu(menu);
    }


    private void loginAdmin(final String mEmail, String password) {

        /**
         * user is login if he is the admin.
         */

        mAuth.signInWithEmailAndPassword(mEmail, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
mProgress.dismiss();
                    Intent mainScreen = new Intent(getApplicationContext(), AdminScreen.class );
                    startActivity(mainScreen);





                } else {
                    // If sign in fails, display a message to the user.
mProgress.hide();
                    Toast.makeText(getApplicationContext(), "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

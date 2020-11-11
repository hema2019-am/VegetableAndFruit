package com.example.vegetablesfruit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import id.zelory.compressor.Compressor;

public class AddList extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    EditText edt_name, edt_price, edt_quantity;
    Button btn_addList;
    String name, price, quantity;
    ImageView addPic;

    DatabaseReference mRef;
    StorageReference mStudentStorageRef;
    Bitmap thumb_bitmap;

    byte[] thumb_bite;
    int Gallery_pick = 1;

    String imageUrl;

    ProgressDialog mProgress;


    Spinner category;

    String[] catgeory = {"LeafyVegetables", "Fruits"};
    String catogryName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_list);


        edt_name = findViewById(R.id.edt_addName);
        edt_price = findViewById(R.id.edt_addPrice);

        btn_addList = findViewById(R.id.btn_add_item);
        addPic = findViewById(R.id.addPic);

        mProgress = new ProgressDialog(this);
        mProgress.setCanceledOnTouchOutside(false);
        mProgress.setTitle("Adding...");

        category = findViewById(R.id.spinner_category);

        final ArrayAdapter<String> Class = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, catgeory);
        Class.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category.setAdapter(Class);
        category.setOnItemSelectedListener(this);

        mStudentStorageRef = FirebaseStorage.getInstance().getReference();
        addPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(galleryIntent, "Select Image"), Gallery_pick);
            }
        });


        btn_addList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                mProgress.show();

                name = edt_name.getText().toString();
                price = edt_price.getText().toString();




                mRef = FirebaseDatabase.getInstance().getReference().child("mart").child(catogryName).child(name);
                final StorageReference thumb_filepath = mStudentStorageRef.child("/fruit_pic").child(name + ".jpg");
                HashMap<String, String> addItem = new HashMap<>();
                addItem.put("Name", name);
                addItem.put("price", price);


                mRef.setValue(addItem).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            try{
                                UploadTask uploadTask = thumb_filepath.putBytes(thumb_bite);
                                uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            thumb_filepath.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Uri> task) {
                                                    imageUrl = task.getResult().toString();
                                                    Map images = new HashMap<>();
                                                    images.put("image", imageUrl);
                                                    mRef.updateChildren(images).addOnCompleteListener(new OnCompleteListener() {
                                                        @Override
                                                        public void onComplete(@NonNull Task task) {
                                                            if (task.isSuccessful()) {
                                                                mProgress.dismiss();
                                                                Toast.makeText(AddList.this, "please wait", Toast.LENGTH_SHORT).show();
                                                                Toast.makeText(AddList.this, "images uploaded", Toast.LENGTH_SHORT).show();
                                                                addPic.setImageResource(R.mipmap.ic_launcher);
                                                            } else {
                                                                Toast.makeText(AddList.this, "error images", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });
                                                }
                                            });
                                        }
                                    }
                                });
                            }catch (Exception e){

                            }

                            edt_name.setText("");
                            edt_price.setText("");
                            edt_quantity.setText("");
                            Toast.makeText(AddList.this, "data added", Toast.LENGTH_SHORT).show();
                        } else {
                            mProgress.hide();
                            Toast.makeText(AddList.this, " data " +
                                    "not added", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Gallery_pick && resultCode == RESULT_OK) {
            Uri ImageUri = data.getData();
            CropImage.activity(ImageUri)
                    .setAspectRatio(1, 1)
                    .start(this);


            //Toast.makeText(SettingsActivity.this, ImageUri, Toast.LENGTH_SHORT).show();
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {


                Uri resultUri = result.getUri();

                File thum_file = new File(resultUri.getPath());


                thumb_bitmap = null;
                try {
                    thumb_bitmap = new Compressor(this).setMaxHeight(200).setMaxWidth(200).setQuality(75).compressToBitmap(thum_file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                thumb_bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                thumb_bite = baos.toByteArray();

                Picasso.with(getApplicationContext()).load(thum_file).placeholder(R.mipmap.ic_launcher).into(addPic);


            }
        }


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        catogryName = catgeory[position];
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}

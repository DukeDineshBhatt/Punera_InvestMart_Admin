package com.puneragroups.punerainvestmartadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class ChangeImageActivity extends AppCompatActivity {

    private Toolbar toolbar;
    ProgressBar progressBar;

    private Button btnSelect, post;
    String pre_url;

    private ImageView imageView;

    // Uri indicates, where the image will be picked from
    private Uri filePath;

    // request code
    private final int PICK_IMAGE_REQUEST = 22;

    private DatabaseReference mFeedsDatabase;

    FirebaseStorage storage;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_image);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Select Image");
        toolbar.setTitleTextColor(Color.WHITE);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        btnSelect = findViewById(R.id.btnChoose);
        post = findViewById(R.id.post);

        imageView = findViewById(R.id.imgView);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        mFeedsDatabase = FirebaseDatabase.getInstance().getReference().child("Graph");

        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectImage();
            }
        });

        mFeedsDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                pre_url = snapshot.child("img").getValue().toString();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                uploadImage();



            }
        });

    }


    private void uploadImage() {
        if (filePath != null) {

            // Code for showing progressDialog while uploading
            ProgressDialog progressDialog
                    = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            StorageReference storageReference1 = FirebaseStorage.getInstance().getReferenceFromUrl(pre_url);

            storageReference1.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {


                    // Defining the child of storageReference
                    StorageReference ref
                            = storageReference
                            .child(
                                    "graph/"
                                            + UUID.randomUUID().toString());

                    // adding listeners on upload
                    // or failure of image
                    ref.putFile(filePath)
                            .addOnSuccessListener(
                                    new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                        @Override
                                        public void onSuccess(
                                                UploadTask.TaskSnapshot taskSnapshot) {


                                            ref.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Uri> task) {

                                                    String profileImageUrl = task.getResult().toString();

                                                    mFeedsDatabase.child("img").setValue(profileImageUrl).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {

                                                            if (task.isSuccessful()) {

                                                                progressDialog.dismiss();
                                                                Toast
                                                                        .makeText(ChangeImageActivity.this,
                                                                                "Uploaded Successfully!!",
                                                                                Toast.LENGTH_SHORT)
                                                                        .show();
                                                                finish();

                                                            }else {

                                                                progressDialog.dismiss();
                                                                Toast
                                                                        .makeText(ChangeImageActivity.this,
                                                                                "Try Again!!",
                                                                                Toast.LENGTH_SHORT)
                                                                        .show();
                                                            }
                                                        }
                                                    });

                                                }
                                            });

                                        }
                                    })

                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                    // Error, Image not uploaded
                                    progressDialog.dismiss();
                                    Toast
                                            .makeText(ChangeImageActivity.this,
                                                    "Failed " + e.getMessage(),
                                                    Toast.LENGTH_SHORT)
                                            .show();
                                }
                            })
                            .addOnProgressListener(
                                    new OnProgressListener<UploadTask.TaskSnapshot>() {

                                        // Progress Listener for loading
                                        // percentage on the dialog box
                                        @Override
                                        public void onProgress(
                                                UploadTask.TaskSnapshot taskSnapshot) {
                                            double progress
                                                    = (100.0
                                                    * taskSnapshot.getBytesTransferred()
                                                    / taskSnapshot.getTotalByteCount());
                                            progressDialog.setMessage(
                                                    "Uploaded "
                                                            + (int) progress + "%");
                                        }
                                    });



                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {

                    Toast.makeText(ChangeImageActivity.this, "Try again!!", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    //Log.e("firebasestorage", "onFailure: did not delete file");
                }
            });


        }
    }


    private void SelectImage() {

        // Defining Implicit Intent to mobile gallery
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(
                        intent,
                        "Select Image from here..."),
                PICK_IMAGE_REQUEST);
    }

    // Override onActivityResult method
    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data) {

        super.onActivityResult(requestCode,
                resultCode,
                data);

        // checking request code and result code
        // if request code is PICK_IMAGE_REQUEST and
        // resultCode is RESULT_OK
        // then set image in the image view
        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {

            // Get the Uri of data
            filePath = data.getData();
            try {

                // Setting image on image view using Bitmap
                Bitmap bitmap = MediaStore
                        .Images
                        .Media
                        .getBitmap(
                                getContentResolver(),
                                filePath);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
    }

}
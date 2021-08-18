package com.puneragroups.punerainvestmartadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    ProgressBar progressbar;
    int flags;
    EditText editTextUserid, editTextPassword;
    Button btn_continue;
    int randomNumber;
    String base;
    EditText type;
    List<String> listItems;
    DatabaseReference mOtpdatabase, mUserDatabase;
    String txtType, message, authorization;
    String userid, pass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        editTextUserid = findViewById(R.id.userid);
        //type = findViewById(R.id.type);
        editTextPassword = findViewById(R.id.password);
        btn_continue = findViewById(R.id.buttonContinue);
        progressbar = findViewById(R.id.progressbar);

        listItems = new ArrayList<String>();
        listItems.add("Accountant");
        listItems.add("Manager");

        FirebaseApp.initializeApp(this);
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        mUserDatabase = database.getReference("Admin");

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        /*type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity
                        .this);
                builder.setTitle("Select Type");
                builder.setCancelable(true);
                builder.setSingleChoiceItems(listItems.toArray(new String[listItems.size()]), -1, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        //Toast.makeText(getApplicationContext(), items[item], Toast.LENGTH_SHORT).show();
                        txtType = listItems.get(item).toString();

                    }
                });

                builder.setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                type.setText(txtType);
                            }
                        });
                builder.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //Toast.makeText(MainActivity.this, "Fail", Toast.LENGTH_SHORT).show();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();

            }
        });*/

        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressbar.setVisibility(View.VISIBLE);

                userid = editTextUserid.getText().toString().trim();
                pass = editTextPassword.getText().toString().trim();
                //txtType = type.getText().toString().trim();

                /*if (txtType.isEmpty()) {

                    progressbar.setVisibility(View.GONE);
                    type.setError("select type");
                    type.requestFocus();
                    return;

                }else*/ if (userid.isEmpty() || userid.length() < 6) {

                    progressbar.setVisibility(View.GONE);
                    editTextUserid.setError("Enter a valid User Id");
                    editTextUserid.requestFocus();
                    return;

                } else if (pass.isEmpty() || pass.length() < 6) {

                    progressbar.setVisibility(View.GONE);
                    editTextPassword.setError("Enter a valid Password");
                    editTextPassword.requestFocus();
                    return;
                } else {

                    /*if (txtType.equals("Accountant")) {

                        mUserDatabase.child("Accountant").child("Login").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if (dataSnapshot.child("userid").getValue().toString().trim().equals(userid) &&
                                        dataSnapshot.child("password").getValue().toString().trim().equals(pass)) {

                                    SharedPreferences mPrefs = getSharedPreferences("myAppPrefs", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = mPrefs.edit();
                                    editor.putBoolean("is_logged_before", true); //this line will do trick
                                    editor.commit();

                                    Intent intent = new Intent(LoginActivity.this, AccountantMainActivity.class);
                                    startActivity(intent);
                                    finish();

                                }else {

                                    progressbar.setVisibility(View.GONE);
                                    Toast.makeText(LoginActivity.this, "Wrong Credentials! Try Again.", Toast.LENGTH_SHORT).show();
                                }


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                progressbar.setVisibility(View.GONE);
                            }
                        });

                    } else if (txtType.equals("Manager")) {*/

                        mUserDatabase.child("Manager").child("Login").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if (dataSnapshot.child("userid").getValue().toString().trim().equals(userid) &&
                                        dataSnapshot.child("password").getValue().toString().trim().equals(pass)) {

                                    SharedPreferences mPrefs = getSharedPreferences("myAppPrefs", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = mPrefs.edit();
                                    editor.putBoolean("is_logged_before", true); //this line will do trick
                                    editor.commit();

                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();

                                }else {

                                    progressbar.setVisibility(View.GONE);
                                    Toast.makeText(LoginActivity.this, "Wrong Credentials! Try Again.", Toast.LENGTH_SHORT).show();
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                progressbar.setVisibility(View.GONE);
                            }
                        });

                    //}


                }


            }
        });

    }

}
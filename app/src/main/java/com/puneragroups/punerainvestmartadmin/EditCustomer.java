package com.puneragroups.punerainvestmartadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class EditCustomer extends AppCompatActivity {

    String user_id;
    private Toolbar toolbar;
    ProgressBar progressBar;
    Button save;
    private DatabaseReference mUserDatabase;
    EditText name, alt_number, email, house_no, area, city, pincode, state;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_customer);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Edit User Info");
        toolbar.setTitleTextColor(Color.WHITE);

        /*getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);*/

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_baseline_arrow_back_24));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        user_id = getIntent().getStringExtra("user_id");

        HashMap<String, Object> map = new HashMap<>();
        HashMap<String, Object> map1 = new HashMap<>();

        name = findViewById(R.id.name);
        save = findViewById(R.id.save);
        alt_number = findViewById(R.id.alt_no);
        email = findViewById(R.id.email);
        house_no = findViewById(R.id.house_no);
        area = findViewById(R.id.area);
        city = findViewById(R.id.city);
        pincode = findViewById(R.id.pincode);
        state = findViewById(R.id.state);

        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        mUserDatabase.child(user_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                progressBar.setVisibility(View.VISIBLE);

                name.setText(snapshot.child("name").getValue().toString());
                alt_number.setText(snapshot.child("alt_number").getValue().toString());
                email.setText(snapshot.child("email").getValue().toString());
                house_no.setText(snapshot.child("Address").child("house_no").getValue().toString());
                area.setText(snapshot.child("Address").child("area").getValue().toString());
                city.setText(snapshot.child("Address").child("city").getValue().toString());
                pincode.setText(snapshot.child("Address").child("pincode").getValue().toString());
                state.setText(snapshot.child("Address").child("state").getValue().toString());

                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);
            }
        });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressBar.setVisibility(View.VISIBLE);

                String nameTxt = name.getText().toString().trim();
                String altnoTxt = alt_number.getText().toString().trim();
                String emailTxt = email.getText().toString().trim();
                String areaTxt = area.getText().toString().trim();
                String house_noTxt = house_no.getText().toString().trim();
                String cityTxt = city.getText().toString().trim();
                String stateTxt = state.getText().toString().trim();
                String pincodeTxt = pincode.getText().toString().trim();

                if (nameTxt.length() > 0) {
                    if (altnoTxt.length() > 0 && altnoTxt.length() == 10) {
                        if (emailTxt.length() > 0) {
                            if (house_noTxt.length() > 0) {
                                if (areaTxt.length() > 0) {
                                    if (cityTxt.length() > 0) {
                                        if (pincodeTxt.length() > 0) {
                                            if (stateTxt.length() > 0) {

                                                //chicken sandwich 2, fanta 1, 4 plate chole 30*4
                                                map.put("name", nameTxt);
                                                map.put("alt_number", altnoTxt);
                                                map.put("email", emailTxt);

                                                map1.put("house_no", house_noTxt);
                                                map1.put("area", areaTxt);
                                                map1.put("city", cityTxt);
                                                map1.put("pincode", pincodeTxt);
                                                map1.put("state", stateTxt);


                                                mUserDatabase.child(user_id).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {

                                                        if (task.isSuccessful()) {

                                                            mUserDatabase.child(user_id).child("Address").updateChildren(map1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull @NotNull Task<Void> task) {

                                                                    if (task.isSuccessful()) {

                                                                        Toast.makeText(EditCustomer.this, "successful. ", Toast.LENGTH_SHORT).show();
                                                                        progressBar.setVisibility(View.GONE);
                                                                        finish();

                                                                    } else {


                                                                        Toast.makeText(EditCustomer.this, "Something went wrong. Try again. ", Toast.LENGTH_SHORT).show();
                                                                        progressBar.setVisibility(View.GONE);

                                                                    }

                                                                }
                                                            });


                                                        } else {

                                                            Toast.makeText(EditCustomer.this, "Something went wrong. Try again. ", Toast.LENGTH_SHORT).show();
                                                            progressBar.setVisibility(View.GONE);
                                                        }

                                                    }
                                                });




                                            } else {

                                                progressBar.setVisibility(View.GONE);
                                                state.setError("Enter State");
                                                return;
                                            }
                                        } else {

                                            progressBar.setVisibility(View.GONE);
                                            pincode.setError("Enter Pincode");
                                            return;
                                        }
                                    } else {

                                        progressBar.setVisibility(View.GONE);
                                        city.setError("Enter City");
                                        return;
                                    }
                                } else {

                                    progressBar.setVisibility(View.GONE);
                                    area.setError("Enter Area");
                                    return;
                                }
                            } else {

                                progressBar.setVisibility(View.GONE);
                                house_no.setError("Enter House No");
                                return;
                            }
                        } else {

                            progressBar.setVisibility(View.GONE);
                            email.setError("Enter Email");
                            return;
                        }
                    } else {

                        progressBar.setVisibility(View.GONE);
                        alt_number.setError("Enter Valid Number");
                        return;
                    }
                } else {

                    progressBar.setVisibility(View.GONE);
                    name.setError("Enter name");
                    return;
                }

            }
        });
    }
}
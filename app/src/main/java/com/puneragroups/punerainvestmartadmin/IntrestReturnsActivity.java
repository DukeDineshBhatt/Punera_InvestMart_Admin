package com.puneragroups.punerainvestmartadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;


public class IntrestReturnsActivity extends AppCompatActivity {

    int month, year;
    private Toolbar toolbar;
    ProgressBar progressBar;
    ImageView nodata;
    String user_id;
    TextView amount,ref_no,date;
    LinearLayout layout;
    private DatabaseReference mUserDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intrest_returns);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Returns");
        toolbar.setTitleTextColor(Color.WHITE);

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_baseline_arrow_back_24));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });

        nodata = findViewById(R.id.nodata);
        layout = findViewById(R.id.layout);
        date = findViewById(R.id.date);
        amount = findViewById(R.id.amount);
        ref_no = findViewById(R.id.ref_no);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        Intent mIntent = getIntent();
        month = mIntent.getIntExtra("month", 0);
        year = mIntent.getIntExtra("year", 0);
        user_id = getIntent().getStringExtra("user_id");

        progressBar.setVisibility(View.VISIBLE);
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id).child("Returns");

        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                if (snapshot.hasChild(month + "-" + year)) {

                    nodata.setVisibility(View.GONE);
                    layout.setVisibility(View.VISIBLE);

                    date.setText(snapshot.child(month + "-" + year).child("date").getValue().toString());
                    amount.setText(snapshot.child(month + "-" + year).child("amount").getValue().toString());
                    ref_no.setText(snapshot.child(month + "-" + year).child("ref_no").getValue().toString());

                    progressBar.setVisibility(View.GONE);

                } else {

                    nodata.setVisibility(View.VISIBLE);
                    layout.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);

                }


            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);
            }
        });


    }
}
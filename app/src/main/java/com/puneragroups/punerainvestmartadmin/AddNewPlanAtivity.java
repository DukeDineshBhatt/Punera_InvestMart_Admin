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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class AddNewPlanAtivity extends AppCompatActivity {

    private Toolbar toolbar;
    ProgressBar progressBar;
    EditText plan, percentage;
    Button save;
    private DatabaseReference mPlasDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_plan_ativity);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add Plan");
        toolbar.setTitleTextColor(Color.WHITE);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        plan = (EditText) findViewById(R.id.plan);
        percentage = (EditText) findViewById(R.id.percentage);
        save = (Button) findViewById(R.id.save);

        mPlasDatabase = FirebaseDatabase.getInstance().getReference().child("Admin").child("Plans");


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressBar.setVisibility(View.VISIBLE);

                String planName = plan.getText().toString().trim();
                String percent = percentage.getText().toString().trim();

                if (planName.isEmpty()) {

                    progressBar.setVisibility(View.GONE);
                    plan.setError("Enter plan Name");
                    return;

                } else if (percent.isEmpty()) {

                    progressBar.setVisibility(View.GONE);
                    percentage.setError("Enter percentage");
                    return;
                } else {

                    HashMap<String, Object> map = new HashMap<>();
                    map.put("Name", planName);
                    map.put("percent", percent);

                    mPlasDatabase.child(planName).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<Void> task) {

                            if (task.isSuccessful()) {

                                Toast.makeText(AddNewPlanAtivity.this, "Successfully Done", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {

                                Toast.makeText(AddNewPlanAtivity.this, "Something Wrong! Try Again.", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

                }

            }
        });

    }
}
package com.puneragroups.punerainvestmartadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

public class EditPlansActivity extends AppCompatActivity {

    String plan_name;
    private Toolbar toolbar;
    EditText percent;
    Button save, delete;
    ProgressBar progressBar;
    TextView plan;
    private DatabaseReference mPlasDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_plans);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setTitleTextColor(Color.WHITE);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        plan_name = getIntent().getStringExtra("plan_name");
        getSupportActionBar().setTitle(plan_name);

        save = findViewById(R.id.save);
        delete = findViewById(R.id.delete);
        plan = findViewById(R.id.plan);
        percent = findViewById(R.id.percent);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        mPlasDatabase = FirebaseDatabase.getInstance().getReference().child("Admin").child("Plans");

        mPlasDatabase.child(plan_name).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                percent.setText(snapshot.child("percent").getValue().toString());

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        plan.setText(plan_name);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressBar.setVisibility(View.VISIBLE);

                String newPercent = percent.getText().toString().trim();

                if (newPercent.isEmpty()) {

                    progressBar.setVisibility(View.GONE);
                    percent.setError("Enter percentage");
                    return;

                } else {


                    mPlasDatabase.child(plan_name).child("percent").setValue(newPercent).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<Void> task) {

                            if (task.isSuccessful()) {

                                Toast.makeText(EditPlansActivity.this, "Successfully Done", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {

                                Toast.makeText(EditPlansActivity.this, "Something Wrong! Try Again.", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

                }


            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder dialog = new AlertDialog.Builder(EditPlansActivity.this);
                dialog.setCancelable(false);
                dialog.setTitle("Delete Plan");
                dialog.setMessage("Are you sure you want to delete this plan?");
                dialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //Action for "Delete".
                        mPlasDatabase.child(plan_name).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task<Void> task) {

                                if (task.isSuccessful()) {

                                    Toast.makeText(EditPlansActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {

                                    Toast.makeText(EditPlansActivity.this, "Something wrong! Try Again", Toast.LENGTH_SHORT).show();

                                }

                            }
                        });

                    }
                })
                        .setNegativeButton("Cancel ", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Action for "Cancel".
                            }
                        });

                final AlertDialog alert = dialog.create();
                alert.show();

            }
        });
    }
}
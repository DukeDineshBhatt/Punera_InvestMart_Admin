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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class AddMarketIndices extends AppCompatActivity {

    private Toolbar toolbar;
    ProgressBar progressBar;
    RadioGroup rGroup;
    Button save;
    EditText name, value, fluc;
    String rbtn="up";
    private DatabaseReference mMarketDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_market_indices);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Market Indices");
        toolbar.setTitleTextColor(Color.WHITE);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        rGroup = (RadioGroup) findViewById(R.id.radioGroup1);
        save = (Button) findViewById(R.id.save);
        name = (EditText) findViewById(R.id.name);
        value = (EditText) findViewById(R.id.value);
        fluc = (EditText) findViewById(R.id.fluc);


        mMarketDatabase = FirebaseDatabase.getInstance().getReference().child("Market_indices");
        RadioButton up = findViewById(R.id.up);
        RadioButton down = findViewById(R.id.down);

        rGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // This will get the radiobutton that has changed in its check state
                // RadioButton checkedRadioButton = (RadioButton) group.findViewById(checkedId);
                // This puts the value (true/false) into the variable
                boolean isChecked = up.isChecked();
                // If the radiobutton that has changed in check state is now checked...
                if (isChecked) {

                    rbtn = "up";

                }else {

                    rbtn = "down";
                }
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressBar.setVisibility(View.VISIBLE);

                String nameTxt = name.getText().toString().trim();
                String valueTxt = value.getText().toString().trim();
                String flucTxt = fluc.getText().toString().trim();


                if (nameTxt.length() > 0) {
                    if (valueTxt.length() > 0) {
                        if (flucTxt.length() > 0) {

                            HashMap<String, Object> map = new HashMap<>();
                            map.put("Name", nameTxt);
                            map.put("Value", valueTxt);
                            map.put("fluc", flucTxt);
                            map.put("State", rbtn);

                            mMarketDatabase.child(nameTxt).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()){

                                        progressBar.setVisibility(View.GONE);

                                        finish();
                                        Toast.makeText(AddMarketIndices.this, "Saved Successfully.", Toast.LENGTH_SHORT).show();

                                    }else {

                                        progressBar.setVisibility(View.GONE);

                                        Toast.makeText(AddMarketIndices.this, "Try Again!!.", Toast.LENGTH_SHORT).show();

                                    }

                                }
                            });



                        } else {

                            progressBar.setVisibility(View.GONE);
                            fluc.setError("Enter valid Fluctuation");
                            return;
                        }
                    } else {

                        progressBar.setVisibility(View.GONE);
                        value.setError("Enter valid value");
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
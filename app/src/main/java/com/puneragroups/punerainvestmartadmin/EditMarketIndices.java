package com.puneragroups.punerainvestmartadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class EditMarketIndices extends AppCompatActivity {

    private Toolbar toolbar;
    ProgressBar progressBar;
    String intent_name;
    RadioGroup rGroup;
    Button save;
    EditText name, value, fluc;
    String rbtn;
    private DatabaseReference mMarketDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_market_indices);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Market Indices");
        toolbar.setTitleTextColor(Color.WHITE);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        rGroup = (RadioGroup) findViewById(R.id.radioGroup1);
        save = (Button) findViewById(R.id.save);
        name = (EditText) findViewById(R.id.name);
        value = (EditText) findViewById(R.id.value);
        fluc = (EditText) findViewById(R.id.fluc);

        intent_name = getIntent().getStringExtra("name");

        mMarketDatabase = FirebaseDatabase.getInstance().getReference().child("Market_indices");
        RadioButton up = findViewById(R.id.up);
        RadioButton down = findViewById(R.id.down);


        mMarketDatabase.child(intent_name).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                name.setText(snapshot.child("Name").getValue().toString());
                value.setText(snapshot.child("Value").getValue().toString());
                fluc.setText(snapshot.child("fluc").getValue().toString());

                rbtn = snapshot.child("State").getValue().toString();

                if (snapshot.child("State").getValue().toString().equals("up")) {

                    up.setChecked(true);

                } else {

                    down.setChecked(true);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        rGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // This will get the radiobutton that has changed in its check state
                // RadioButton checkedRadioButton = (RadioButton) group.findViewById(checkedId);
                // This puts the value (true/false) into the variable
                boolean isChecked = up.isChecked();
                // If the radiobutton that has changed in check state is now checked...
                if (isChecked) {

                    rbtn = "up";

                } else {

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

                            mMarketDatabase.child(intent_name).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()) {

                                        progressBar.setVisibility(View.GONE);

                                        finish();
                                        Toast.makeText(EditMarketIndices.this, "Saved Successfully.", Toast.LENGTH_SHORT).show();

                                    } else {

                                        progressBar.setVisibility(View.GONE);

                                        Toast.makeText(EditMarketIndices.this, "Try Again!!.", Toast.LENGTH_SHORT).show();

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.delete_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_favorite) {


            new AlertDialog.Builder(EditMarketIndices.this)
                    .setTitle("Delete")
                    .setMessage("are you sure want to Delete?")
                    .setNegativeButton(android.R.string.no, null)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface arg0, int arg1) {
                            EditMarketIndices.super.onBackPressed();

                            mMarketDatabase.child(intent_name).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()){

                                        Toast.makeText(EditMarketIndices.this, "Deleted.", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(EditMarketIndices.this, MarketIndices.class);
                                        startActivity(intent);
                                        finish();

                                    }else{

                                        Toast.makeText(EditMarketIndices.this, "Try Again!!.", Toast.LENGTH_SHORT).show();

                                    }

                                }
                            });



                        }
                    }).create().show();


            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
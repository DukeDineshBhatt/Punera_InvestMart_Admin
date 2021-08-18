package com.puneragroups.punerainvestmartadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class AddNewCustomer extends AppCompatActivity {

    private Toolbar toolbar;
    EditText name, mobile, alt_number, email, revenue, plan, percent, house_no, ship_house_no, area, ship_area, city, ship_city, pincode, ship_pincode, state, ship_state;
    LinearLayout shipping_layout;
    ProgressBar progressbar;
    CheckBox checkbox;
    Button save;
    DatabaseReference mPlanDatabase;
    List<String> listItems;
    boolean myIsChecked = false;
    String autoPercent, plan_txt, ship_areaTxt, ship_cityTxt, ship_pincodeTxt, ship_stateTxt;
    private DatabaseReference mUserDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_customer);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add Customer");
        toolbar.setTitleTextColor(Color.WHITE);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        name = findViewById(R.id.name);
        progressbar = findViewById(R.id.progressbar);
        save = findViewById(R.id.save);
        mobile = findViewById(R.id.mobile);
        alt_number = findViewById(R.id.alt_no);
        email = findViewById(R.id.email);
        revenue = findViewById(R.id.revenue);
        house_no = findViewById(R.id.house_no);
        area = findViewById(R.id.area);
        city = findViewById(R.id.city);
        pincode = findViewById(R.id.pincode);
        state = findViewById(R.id.state);
        plan = findViewById(R.id.plan);
        percent = findViewById(R.id.percent);

        HashMap<String, Object> map = new HashMap<>();
        HashMap<String, Object> map1 = new HashMap<>();
        listItems = new ArrayList<String>();

        FirebaseApp.initializeApp(this);
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        mPlanDatabase = database.getReference("Admin").child("Plans");

        mPlanDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    listItems.add(postSnapshot.child("Name").getValue().toString());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        plan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(AddNewCustomer.this);
                builder.setTitle("Select Plan");
                builder.setCancelable(false);
                builder.setSingleChoiceItems(listItems.toArray(new String[listItems.size()]), -1, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {

                        plan_txt = listItems.get(item).toString();

                    }
                });

                builder.setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                plan.setText(plan_txt);
                                mPlanDatabase.child(plan_txt).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                                        autoPercent = snapshot.child("percent").getValue().toString();
                                        percent.setText(autoPercent);


                                    }

                                    @Override
                                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                    }
                                });

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
        });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String nameTxt = name.getText().toString().trim();
                String mobileTxt = mobile.getText().toString().trim();
                String altTxt = alt_number.getText().toString().trim();
                String emailTxt = email.getText().toString().trim();
                String revenueTxt = revenue.getText().toString().trim();
                String planTxt = plan.getText().toString().trim();
                String houseTxt = house_no.getText().toString().trim();
                String areaTxt = area.getText().toString().trim();
                String cityTxt = city.getText().toString().trim();
                String pincodeTxt = pincode.getText().toString().trim();
                String stateTxt = state.getText().toString().trim();
                String percentTxt = percent.getText().toString().trim();

               /* if (myIsChecked) {

                    ship_houseTxt = houseTxt;
                    ship_areaTxt = areaTxt;
                    ship_cityTxt = cityTxt;
                    ship_pincodeTxt = pincodeTxt;
                    ship_stateTxt = stateTxt;
                } else {

                    ship_houseTxt = ship_house_no.getText().toString().trim();
                    ship_areaTxt = ship_area.getText().toString().trim();
                    ship_cityTxt = ship_city.getText().toString().trim();
                    ship_pincodeTxt = ship_pincode.getText().toString().trim();
                    ship_stateTxt = ship_state.getText().toString().trim();

                }*/

                progressbar.setVisibility(View.VISIBLE);

                if (nameTxt.length() > 0) {
                    if (mobileTxt.length() > 0 && mobileTxt.length() == 10) {
                        if (altTxt.length() > 0 && altTxt.length() == 10) {
                            if (emailTxt.length() > 0) {

                                if (revenueTxt.length() > 0) {
                                    if (planTxt.length() > 0) {
                                        if (percentTxt.length() > 0) {
                                            if (houseTxt.length() > 0) {
                                                if (areaTxt.length() > 0) {
                                                    if (cityTxt.length() > 0) {
                                                        if (pincodeTxt.length() > 0) {
                                                            if (stateTxt.length() > 0) {
                                                                mUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                                        for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                                                                            if (postSnapshot.hasChild(mobileTxt)) {

                                                                                Toast.makeText(AddNewCustomer.this, "This Mobile Number is Already Registered.!", Toast.LENGTH_LONG).show();

                                                                                progressbar.setVisibility(View.GONE);
                                                                                break;

                                                                            } else {

                                                                                String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

                                                                                map.put("name", nameTxt);
                                                                                map.put("mobile", mobileTxt);
                                                                                map.put("alt_number", altTxt);
                                                                                map.put("email", emailTxt);
                                                                                map.put("revenue", revenueTxt);
                                                                                map.put("creationDate", date);
                                                                                map.put("plan", planTxt);
                                                                                map.put("percent", percentTxt);

                                                                                map1.put("house_no", houseTxt);
                                                                                map1.put("area", areaTxt);
                                                                                map1.put("city", cityTxt);
                                                                                map1.put("pincode", pincodeTxt);
                                                                                map1.put("state", stateTxt);


                                                                                mUserDatabase.child(mobileTxt).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                    @Override
                                                                                    public void onComplete(@NonNull Task<Void> task) {

                                                                                        if (task.isSuccessful()) {

                                                                                            mUserDatabase.child(mobileTxt).child("Address").updateChildren(map1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                @Override
                                                                                                public void onComplete(@NonNull @NotNull Task<Void> task) {

                                                                                                    if (task.isSuccessful()) {

                                                                                                        Toast.makeText(AddNewCustomer.this, "successful. ", Toast.LENGTH_SHORT).show();
                                                                                                        progressbar.setVisibility(View.GONE);
                                                                                                        finish();

                                                                                                    } else {


                                                                                                        Toast.makeText(AddNewCustomer.this, "Something went wrong. Try again. ", Toast.LENGTH_SHORT).show();
                                                                                                        progressbar.setVisibility(View.GONE);

                                                                                                    }

                                                                                                }
                                                                                            });


                                                                                        } else {

                                                                                            Toast.makeText(AddNewCustomer.this, "Something went wrong. Try again. ", Toast.LENGTH_SHORT).show();
                                                                                            progressbar.setVisibility(View.GONE);
                                                                                        }

                                                                                    }
                                                                                });


                                                                            }


                                                                        }

                                                                    }

                                                                    @Override
                                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                                    }
                                                                });

                                                            } else {

                                                                progressbar.setVisibility(View.GONE);
                                                                state.setError("Enter state");
                                                                return;
                                                            }
                                                        } else {

                                                            progressbar.setVisibility(View.GONE);
                                                            pincode.setError("Enter pincode");
                                                            return;
                                                        }
                                                    } else {

                                                        progressbar.setVisibility(View.GONE);
                                                        city.setError("Enter city");
                                                        return;
                                                    }
                                                } else {

                                                    progressbar.setVisibility(View.GONE);
                                                    area.setError("Enter area");
                                                    return;
                                                }
                                            } else {

                                                progressbar.setVisibility(View.GONE);
                                                house_no.setError("Enter house no");
                                                return;
                                            }
                                        } else {

                                            progressbar.setVisibility(View.GONE);
                                            percent.setError("Enter Percent");
                                            return;
                                        }


                                    } else {

                                        progressbar.setVisibility(View.GONE);
                                        plan.setError("Enter plan");
                                        return;
                                    }

                                } else {

                                    progressbar.setVisibility(View.GONE);
                                    revenue.setError("Enter revenue");
                                    return;
                                }

                            } else {

                                progressbar.setVisibility(View.GONE);
                                email.setError("Enter email");
                                return;
                            }
                        } else {

                            progressbar.setVisibility(View.GONE);
                            alt_number.setError("Enter alt nuber");
                            return;
                        }

                    } else {

                        progressbar.setVisibility(View.GONE);
                        mobile.setError("Enter valid mobile");
                        return;
                    }

                } else {

                    progressbar.setVisibility(View.GONE);
                    name.setError("Enter name");
                    return;
                }

            }
        });

    }
}
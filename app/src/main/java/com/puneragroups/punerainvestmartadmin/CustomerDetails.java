package com.puneragroups.punerainvestmartadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class CustomerDetails extends AppCompatActivity {

    private Toolbar toolbar;
    String user_id, plan_name;
    ProgressBar progressBar;
    private DatabaseReference mUserDatabase;
    TextView name, mobile, alt_no, email, enroll, house_no, area, city, pincode, state, plan, revenue, percent;
    Button edit_percent, intrest_return, deposit;
    int mMonth, mYear, dMonth, dYear;
    ImageView img_edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_details);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("User");
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

        user_id = getIntent().getStringExtra("user_id");
        plan_name = getIntent().getStringExtra("plan_name");

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        name = findViewById(R.id.name);
        mobile = findViewById(R.id.mobile);
        alt_no = findViewById(R.id.alt_no);
        email = findViewById(R.id.email);
        enroll = findViewById(R.id.enroll);
        house_no = findViewById(R.id.house_no);
        area = findViewById(R.id.area);
        city = findViewById(R.id.city);
        pincode = findViewById(R.id.pincode);
        state = findViewById(R.id.state);
        plan = findViewById(R.id.plan);
        revenue = findViewById(R.id.revenue);
        percent = findViewById(R.id.percent);
        edit_percent = findViewById(R.id.edit_percent);
        intrest_return = findViewById(R.id.intrest_return);
        deposit = findViewById(R.id.deposit);
        img_edit = findViewById(R.id.edit);

        progressBar.setVisibility(View.VISIBLE);


        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);

        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                name.setText(snapshot.child("name").getValue().toString());
                mobile.setText(snapshot.child("mobile").getValue().toString());
                email.setText(snapshot.child("email").getValue().toString());
                alt_no.setText(snapshot.child("alt_number").getValue().toString());
                enroll.setText(snapshot.child("creationDate").getValue().toString());
                plan.setText(snapshot.child("plan").getValue().toString());
                percent.setText(snapshot.child("percent").getValue().toString() + " %");
                revenue.setText(snapshot.child("revenue").getValue().toString());
                house_no.setText(snapshot.child("Address").child("house_no").getValue().toString());
                area.setText(snapshot.child("Address").child("area").getValue().toString());
                city.setText(snapshot.child("Address").child("city").getValue().toString());
                pincode.setText(snapshot.child("Address").child("pincode").getValue().toString());
                state.setText(snapshot.child("Address").child("state").getValue().toString());


                progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);
            }
        });

        img_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(CustomerDetails.this, EditCustomer.class);
                intent.putExtra("user_id", user_id);
                startActivity(intent);

            }
        });

        edit_percent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder alert = new AlertDialog.Builder(CustomerDetails.this);

                EditText edittext = new EditText(CustomerDetails.this);
                alert.setMessage("Enter Percentage");
                alert.setView(edittext);

                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        String s = edittext.getText().toString();

                        if (s.isEmpty()) {

                            Toast.makeText(CustomerDetails.this, "Please type percentage.", Toast.LENGTH_SHORT).show();


                        } else {

                            mUserDatabase.child("percent").setValue(s).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<Void> task) {

                                    if (task.isSuccessful()) {

                                        Toast.makeText(CustomerDetails.this, "successful.", Toast.LENGTH_SHORT).show();
                                    } else {

                                        Toast.makeText(CustomerDetails.this, "Try again!.", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });
                        }


                    }
                });


                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // what ever you want to do with No option.
                    }
                });

                alert.show();

            }
        });

        intrest_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Calendar c = Calendar.getInstance();
                int cyear = c.get(Calendar.YEAR);
                int cmonth = c.get(Calendar.MONTH);
                int cday = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        CustomerDetails.this,
                        android.R.style.Theme_Holo_Dialog,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                //pick_expire_date.setText((monthOfYear + 1)+"/"+year);
                                mMonth = monthOfYear + 1;
                                mYear = year;

                                Intent intent = new Intent(CustomerDetails.this, IntrestReturnsActivity.class);
                                intent.putExtra("month", mMonth);
                                intent.putExtra("year", mYear);
                                intent.putExtra("user_id", user_id);
                                startActivity(intent);

                            }
                        },
                        cyear, cmonth, cday);
                datePickerDialog.setTitle("View Interest return");
                datePickerDialog.setMessage("Select Month And Year");
                datePickerDialog.getDatePicker().findViewById(getResources().getIdentifier("day", "id", "android")).setVisibility(View.GONE);
                datePickerDialog.show();


            }
        });

        deposit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String myFormat = "dd-MM-yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                final Calendar myCalendar = Calendar.getInstance();

                int cyear = myCalendar.get(Calendar.YEAR);
                int cmonth = myCalendar.get(Calendar.MONTH);
                int cday = myCalendar.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        CustomerDetails.this,
                        android.R.style.Theme_Holo_Dialog,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                //pick_expire_date.setText((monthOfYear + 1)+"/"+year);
                                dMonth = monthOfYear + 1;
                                dYear = year;

                                Intent intent = new Intent(CustomerDetails.this, DepositActivity.class);
                                intent.putExtra("month", dMonth);
                                intent.putExtra("year", dYear);
                                intent.putExtra("user_id", user_id);
                                startActivity(intent);

                            }
                        },
                        cyear, cmonth, cday);
                datePickerDialog.setTitle("Deposit");
                datePickerDialog.setMessage("Select Month And Year");
                datePickerDialog.getDatePicker().findViewById(getResources().getIdentifier("day", "id", "android")).setVisibility(View.GONE);
                datePickerDialog.show();


            }
        });

    }

}
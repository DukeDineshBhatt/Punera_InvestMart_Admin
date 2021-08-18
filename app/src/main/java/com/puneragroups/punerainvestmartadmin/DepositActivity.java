package com.puneragroups.punerainvestmartadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import com.puneragroups.punerainvestmartadmin.NOtificationPOJO.Data;
import com.puneragroups.punerainvestmartadmin.NOtificationPOJO.DataBean;
import com.puneragroups.punerainvestmartadmin.NOtificationPOJO.ResultBean;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class DepositActivity extends AppCompatActivity {

    private Toolbar toolbar;
    String user_id;
    int month, year;
    ProgressBar progressBar;
    private DatabaseReference mUserDatabase;
    LinearLayout layout, formLayout;
    EditText et_ref_no, et_amount, et_date, msg;
    TextView date, ref_no, amount;
    Button save;
    String user_token, msgTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposit);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Deposit");
        toolbar.setTitleTextColor(Color.WHITE);

        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_baseline_arrow_back_24));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });

        Intent mIntent = getIntent();
        month = mIntent.getIntExtra("month", 0);
        year = mIntent.getIntExtra("year", 0);
        user_id = getIntent().getStringExtra("user_id");

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        layout = (LinearLayout) findViewById(R.id.layout);
        formLayout = (LinearLayout) findViewById(R.id.formLayout);
        et_ref_no = findViewById(R.id.et_ref_no);
        et_amount = findViewById(R.id.et_amount);
        et_date = findViewById(R.id.et_date);
        date = findViewById(R.id.date);
        ref_no = findViewById(R.id.ref_no);
        amount = findViewById(R.id.amount);
        save = findViewById(R.id.save);
        msg = findViewById(R.id.msg);


        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id).child("Returns");

        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                if (snapshot.hasChild(month + "-" + year)) {

                    formLayout.setVisibility(View.GONE);
                    layout.setVisibility(View.VISIBLE);

                    date.setText(snapshot.child(month + "-" + year).child("date").getValue().toString());
                    amount.setText(snapshot.child(month + "-" + year).child("amount").getValue().toString());
                    ref_no.setText(snapshot.child(month + "-" + year).child("ref_no").getValue().toString());

                    progressBar.setVisibility(View.GONE);

                } else {

                    formLayout.setVisibility(View.VISIBLE);
                    layout.setVisibility(View.GONE);

                    Calendar date = Calendar.getInstance();
                    String dayToday = android.text.format.DateFormat.format("d", date).toString();
                    et_date.setText(dayToday + "-" + month + "-" + year);

                    FirebaseDatabase.getInstance().getReference().child("Users").child(user_id).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                            int percent = Integer.parseInt(snapshot.child("percent").getValue().toString());
                            int revenue = Integer.parseInt(snapshot.child("revenue").getValue().toString());

                            int f = (int) (revenue * (percent / 100.0f));

                            et_amount.setText(String.valueOf(f));

                            msg.setText("You have been credited interest return amount of INR " + String.valueOf(f) + " .");
                        }

                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                        }
                    });


                    progressBar.setVisibility(View.GONE);

                }


            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);
            }
        });

        FirebaseDatabase.getInstance().getReference().child("Users").child(user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                user_token = snapshot.child("token").getValue().toString();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        et_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*new DatePickerDialog(DepositActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();*/

                final Calendar myCalendar = Calendar.getInstance();

                int cyear = myCalendar.get(Calendar.YEAR);
                int cmonth = myCalendar.get(Calendar.MONTH);
                int cday = myCalendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        DepositActivity.this,
                        android.R.style.Theme_Holo_Dialog,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                int dDate = dayOfMonth;

                                et_date.setText(dDate + "-" + month + "-" + year);


                            }
                        },
                        cyear, cmonth, cday);
                datePickerDialog.setTitle("Deposit");
                datePickerDialog.setMessage("Select Date of deposit");
                datePickerDialog.getDatePicker().findViewById(getResources().getIdentifier("month", "id", "android")).setVisibility(View.GONE);
                datePickerDialog.getDatePicker().findViewById(getResources().getIdentifier("year", "id", "android")).setVisibility(View.GONE);
                datePickerDialog.show();


            }
        });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressBar.setVisibility(View.VISIBLE);

                String ref_no = et_ref_no.getText().toString().trim();
                String amount = et_amount.getText().toString().trim();
                msgTxt = msg.getText().toString().trim();

                if (ref_no.isEmpty()) {

                    progressBar.setVisibility(View.GONE);
                    et_ref_no.setError("Enter Ref No");
                    return;

                } else if (amount.isEmpty()) {

                    progressBar.setVisibility(View.GONE);
                    et_amount.setError("Enter Amount");
                    return;
                } else if (msgTxt.isEmpty()) {

                    progressBar.setVisibility(View.GONE);
                    msg.setError("Enter Message");
                    return;
                } else {

                    String child = String.valueOf(month + "-" + year);

                    HashMap<String, Object> map = new HashMap<>();
                    map.put("amount", amount);
                    map.put("date", et_date.getText().toString());
                    map.put("ref_no", ref_no);

                    HashMap<String, Object> map1 = new HashMap<>();
                    map1.put("text", msgTxt);
                    map1.put("date", et_date.getText().toString());


                    mUserDatabase.child(child).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<Void> task) {

                            if (task.isSuccessful()) {

                                FirebaseDatabase.getInstance().getReference().child("Users").child(user_id).child("Notifications").child(child).updateChildren(map1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if (task.isSuccessful()) {

                                            sendNotification();
                                            progressBar.setVisibility(View.GONE);
                                            finish();

                                        } else {


                                            Toast.makeText(DepositActivity.this, "Try again!.", Toast.LENGTH_SHORT).show();
                                            progressBar.setVisibility(View.GONE);


                                        }

                                    }
                                });


                            } else {

                                Toast.makeText(DepositActivity.this, "Try again!.", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                            }


                        }
                    });
                }

            }
        });
    }

    public void sendNotification() {

        Bean b = (Bean) getApplicationContext();

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.level(HttpLoggingInterceptor.Level.HEADERS);
        logging.level(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder().writeTimeout(1000, TimeUnit.SECONDS).readTimeout(1000, TimeUnit.SECONDS).connectTimeout(1000, TimeUnit.SECONDS).addInterceptor(logging).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(b.notificationBaseurl)
                .client(client)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        AllApiIneterface cr = retrofit.create(AllApiIneterface.class);

        Data body1 = new Data();
        body1.setBody(msgTxt);
        body1.setTitle("Punera InvestMart Returns");
        body1.setIcon("https://firebasestorage.googleapis.com/v0/b/punerainvestmart-89fc5.appspot.com/o/logo.png?alt=media&token=544d36a5-285b-4375-9c3f-aa684b7efac5");

        DataBean body = new DataBean();
        body.setTo(user_token);
        body.setData(body1);

        Call<ResultBean> call = cr.sendNotification(body);
        call.enqueue(new Callback<ResultBean>() {
            @Override
            public void onResponse(@com.google.firebase.database.annotations.NotNull Call<ResultBean> call, @com.google.firebase.database.annotations.NotNull Response<ResultBean> response) {

                //Toast.makeText(CabBookingSuccess.this, response.body().toString(), Toast.LENGTH_LONG).show();

                if (response.body().getSuccess() == 1) {

                    Toast.makeText(DepositActivity.this, "Deposited Successfully.", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(DepositActivity.this, "Please try again", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onFailure(Call<ResultBean> call, Throwable t) {

                Toast.makeText(DepositActivity.this, "Something wrong", Toast.LENGTH_SHORT).show();
                Log.d("DDDD", " " + t.toString());

            }
        });


    }

    private void updateLabel() {

        String myFormat = "dd-MM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        et_date.setText(sdf.format(myCalendar.getTime()));
    }

    final Calendar myCalendar = Calendar.getInstance();


}
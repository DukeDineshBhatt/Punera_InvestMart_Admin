package com.puneragroups.punerainvestmartadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

public class CustomersListActivity extends AppCompatActivity {

    private LinearLayoutManager linearLayoutManager;
    private RecyclerView recyclerView;
    private myadapter adapter;
    ProgressBar progressBar;
    TextView txt_no_order;
    private Toolbar toolbar;
    String plan_name, name;
    private DatabaseReference mPlasDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customers_list);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Users");
        toolbar.setTitleTextColor(Color.WHITE);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);


        plan_name = getIntent().getStringExtra("plan_name");


        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        recyclerView = (RecyclerView) findViewById(R.id.upload_list);
        txt_no_order = (TextView) findViewById(R.id.txt_no_orders);

        plan_name = getIntent().getStringExtra("plan_name");

        progressBar.setVisibility(View.VISIBLE);

        mPlasDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        mPlasDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.hasChildren()) {

                    txt_no_order.setVisibility(View.GONE);

                    linearLayoutManager = new LinearLayoutManager(CustomersListActivity.this);
                    recyclerView.setLayoutManager(linearLayoutManager);

                    FirebaseRecyclerOptions<ListSetGet1> options =
                            new FirebaseRecyclerOptions.Builder<ListSetGet1>()
                                    .setQuery(FirebaseDatabase.getInstance().getReference().child("Users"), ListSetGet1.class)
                                    .build();


                    adapter = new myadapter(options);
                    adapter.startListening();
                    recyclerView.setAdapter(adapter);

                    progressBar.setVisibility(View.GONE);

                } else {

                    txt_no_order.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);

                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public class myadapter extends FirebaseRecyclerAdapter<ListSetGet1, myadapter.myviewholder> {
        public myadapter(@NonNull FirebaseRecyclerOptions<ListSetGet1> options) {
            super(options);
        }

        @Override
        protected void onBindViewHolder(@NonNull myadapter.myviewholder holder, final int position, @NonNull ListSetGet1 model) {

            final String Id = getRef(position).getKey();


            mPlasDatabase.child(Id).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {


                    if (snapshot.child("plan").getValue().toString().equals(plan_name)) {


                        name = snapshot.child("name").getValue().toString();
                        holder.name.setText(name + "  " + Id);

                    } else {

                        holder.itemView.setVisibility(View.GONE);

                        holder.itemView.getLayoutParams().height=0;                    }

                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(CustomersListActivity.this, CustomerDetails.class);
                    intent.putExtra("user_id", Id);
                    intent.putExtra("plan_name", plan_name);
                    startActivity(intent);

                }
            });
        }

        @NonNull
        @Override
        public myadapter.myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.customers_list, parent, false);
            return new myadapter.myviewholder(view);
        }

        class myviewholder extends RecyclerView.ViewHolder {

            TextView name, status;
            LinearLayout layout;

            public myviewholder(@NonNull View itemView) {
                super(itemView);
                name = (TextView) itemView.findViewById(R.id.name);
                layout = (LinearLayout) itemView.findViewById(R.id.layout);

            }
        }
    }
}
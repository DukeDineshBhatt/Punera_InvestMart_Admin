package com.puneragroups.punerainvestmartadmin;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Customers extends ManagerBaseActivity {


    private static final String SELECTED_ITEM = "arg_selected_item";
    int mSelectedItem;
    FloatingActionButton fab;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView recyclerView;
    private myadapter adapter;
    ProgressBar progressBar;
    TextView txt_no_order;
    private DatabaseReference mPlasDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        fab = findViewById(R.id.fab);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        recyclerView = (RecyclerView) findViewById(R.id.upload_list);
        txt_no_order = (TextView) findViewById(R.id.txt_no_orders);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Customers.this, AddNewCustomer.class);
                startActivity(intent);

            }
        });

        progressBar.setVisibility(View.VISIBLE);

        mPlasDatabase = FirebaseDatabase.getInstance().getReference().child("Admin").child("Plans");

        mPlasDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.hasChildren()) {

                    txt_no_order.setVisibility(View.GONE);

                    linearLayoutManager = new LinearLayoutManager(Customers.this);
                    recyclerView.setLayoutManager(linearLayoutManager);

                    FirebaseRecyclerOptions<ListSetGet1> options =
                            new FirebaseRecyclerOptions.Builder<ListSetGet1>()
                                    .setQuery(FirebaseDatabase.getInstance().getReference().child("Admin").child("Plans"), ListSetGet1.class)
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
            holder.name.setText(Id+" Users");

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(Customers.this, CustomersListActivity.class);
                    intent.putExtra("plan_name", Id);
                    startActivity(intent);

                }
            });
        }

        @NonNull
        @Override
        public myadapter.myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.plans_list1, parent, false);
            return new myadapter.myviewholder(view);
        }

        class myviewholder extends RecyclerView.ViewHolder {

            TextView name, status;

            public myviewholder(@NonNull View itemView) {
                super(itemView);
                name = (TextView) itemView.findViewById(R.id.name);

            }
        }
    }


    @Override
    int getContentViewId() {
        return R.layout.activity_customers;
    }

    @Override
    int getNavigationMenuItemId() {
        return R.id.customers;
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(SELECTED_ITEM, mSelectedItem);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(Customers.this, MainActivity.class);
        startActivity(intent);
        finish();


    }
}
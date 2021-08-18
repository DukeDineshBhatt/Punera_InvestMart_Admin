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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class MarketIndices extends AppCompatActivity {

    private Toolbar toolbar;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView recyclerView;
    private myadapter1 adapter;
    ProgressBar progressBar;
    private DatabaseReference mMarketDatabase;
    FloatingActionButton fab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market_indices2);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("market Indices");
        toolbar.setTitleTextColor(Color.WHITE);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        recyclerView = (RecyclerView) findViewById(R.id.upload_list);

        fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MarketIndices.this, AddMarketIndices.class);
                startActivity(intent);

            }
        });


        linearLayoutManager = new LinearLayoutManager(MarketIndices.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setNestedScrollingEnabled(false);

        FirebaseRecyclerOptions<MarketDataSetGet> options1 =
                new FirebaseRecyclerOptions.Builder<MarketDataSetGet>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Market_indices"), MarketDataSetGet.class)
                        .build();


        adapter = new myadapter1(options1);
        adapter.startListening();
        recyclerView.setAdapter(adapter);


    }

    public class myadapter1 extends FirebaseRecyclerAdapter<MarketDataSetGet, myadapter1.myviewholder> {
        public myadapter1(@NonNull FirebaseRecyclerOptions<MarketDataSetGet> options) {
            super(options);
        }

        @Override
        protected void onBindViewHolder(@NonNull myadapter1.myviewholder holder, final int position, @NonNull MarketDataSetGet model) {

            final String Id = getRef(position).getKey();


            if (model.getState().equals("up")){

                holder.fluctuation.setText(String.valueOf(model.getFluc()));
            }else {
                holder.fluctuation.setText(String.valueOf(model.getFluc()));
                holder.fluctuation.setTextColor(Color.parseColor("#ff0000"));
                holder.fluctuation.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_arrow_downward_24, 0, 0, 0);
            }

            holder.name.setText(model.getName().toString());
            holder.value.setText(String.valueOf(model.getValue()));

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(MarketIndices.this,EditMarketIndices.class);
                    intent.putExtra("name",Id);
                    startActivity(intent);

                }
            });


        }

        @NonNull
        @Override
        public myadapter1.myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.market_list, parent, false);
            return new myadapter1.myviewholder(view);
        }

        class myviewholder extends RecyclerView.ViewHolder {

            TextView name,value,fluctuation;

            public myviewholder(@NonNull View itemView) {
                super(itemView);

                name = (TextView) itemView.findViewById(R.id.name);
                value = (TextView) itemView.findViewById(R.id.value);
                fluctuation = (TextView) itemView.findViewById(R.id.fluctuation);




            }
        }
    }

}
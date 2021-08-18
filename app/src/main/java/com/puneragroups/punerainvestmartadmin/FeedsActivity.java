package com.puneragroups.punerainvestmartadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class FeedsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView recyclerView;
    private myadapter adapter;
    ProgressBar progressBar;
    TextView txt_no_order;
    private DatabaseReference mFeedsDatabase;
    FloatingActionButton fab;
    FirebaseStorage storage;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feeds);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("News Feeds");
        toolbar.setTitleTextColor(Color.WHITE);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        recyclerView = (RecyclerView) findViewById(R.id.upload_list);
        txt_no_order = (TextView) findViewById(R.id.txt_no_orders);

        fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(FeedsActivity.this, AddFeedActivity.class);
                startActivity(intent);

            }
        });

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        mFeedsDatabase = FirebaseDatabase.getInstance().getReference().child("Feed");

        mFeedsDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.hasChildren()) {

                    progressBar.setVisibility(View.VISIBLE);

                    txt_no_order.setVisibility(View.GONE);

                    linearLayoutManager = new LinearLayoutManager(FeedsActivity.this);
                    recyclerView.setLayoutManager(linearLayoutManager);

                    FirebaseRecyclerOptions<FeedsList> options =
                            new FirebaseRecyclerOptions.Builder<FeedsList>()
                                    .setQuery(FirebaseDatabase.getInstance().getReference().child("Feed"), FeedsList.class)
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

            Intent intent = new Intent(FeedsActivity.this, AddFeedActivity.class);
            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public class myadapter extends FirebaseRecyclerAdapter<FeedsList, myadapter.myviewholder> {
        public myadapter(@NonNull FirebaseRecyclerOptions<FeedsList> options) {
            super(options);
        }

        @Override
        protected void onBindViewHolder(@NonNull myadapter.myviewholder holder, final int position, @NonNull FeedsList model) {

            final String Id = getRef(position).getKey();

            Glide.with(getApplicationContext()).
                    load(model.getImg())
                    .placeholder(R.drawable.loading)
                    .into(holder.img);

            holder.des.setText(model.getDes());


            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    new AlertDialog.Builder(FeedsActivity.this)
                            .setTitle("Delete")
                            .setMessage("are you sure want to Delete this feed?")
                            .setNegativeButton(android.R.string.no, null)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface arg0, int arg1) {
                                    FeedsActivity.super.onBackPressed();

                                    ProgressDialog progressDialog
                                            = new ProgressDialog(FeedsActivity.this);
                                    progressDialog.setTitle("Deleting...");
                                    progressDialog.setCancelable(false);
                                    progressDialog.show();

                                    StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(model.getImg());

                                    storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                            mFeedsDatabase.child(Id).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                    if (task.isSuccessful()) {

                                                        Toast.makeText(FeedsActivity.this, "Deleted Successfully.", Toast.LENGTH_SHORT).show();
                                                        progressDialog.dismiss();
                                                        // Log.e("firebasestorage", "onSuccess: deleted file");

                                                    } else {

                                                        Toast.makeText(FeedsActivity.this, "Try again!!", Toast.LENGTH_SHORT).show();
                                                        progressDialog.dismiss();
                                                    }

                                                }
                                            });


                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception exception) {

                                            Toast.makeText(FeedsActivity.this, "Try again!!", Toast.LENGTH_SHORT).show();
                                            progressDialog.dismiss();
                                            //Log.e("firebasestorage", "onFailure: did not delete file");
                                        }
                                    });


                                }
                            }).create().show();

                }
            });

        }

        @NonNull
        @Override
        public myadapter.myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feeds_list, parent, false);
            return new myadapter.myviewholder(view);
        }

        class myviewholder extends RecyclerView.ViewHolder {

            TextView des;
            ImageView img;
            Button delete;

            public myviewholder(@NonNull View itemView) {
                super(itemView);

                des = (TextView) itemView.findViewById(R.id.des);
                img = (ImageView) itemView.findViewById(R.id.img);
                delete = (Button) itemView.findViewById(R.id.delete);


            }
        }
    }
}
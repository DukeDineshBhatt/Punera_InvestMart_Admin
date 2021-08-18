package com.puneragroups.punerainvestmartadmin;

import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends ManagerBaseActivity {

    CardView plans, customers,feeds,market;
    public static final String PREFS_NAME = "MyPrefsFile";
    private static final String SELECTED_ITEM = "arg_selected_item";
    int mSelectedItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences settings = getSharedPreferences(MainActivity.PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("hasLoggedIn", true);
        editor.commit();

        plans = findViewById(R.id.plans);
        customers = findViewById(R.id.customers);
        feeds = findViewById(R.id.feeds);
        market = findViewById(R.id.market);


        plans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, PlansActivity.class);
                startActivity(intent);

            }
        });

        customers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, Customers.class);
                startActivity(intent);

            }
        });

        market.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, MarketIndices.class);
                startActivity(intent);

            }
        });

        feeds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, FeedsActivity.class);
                startActivity(intent);

            }
        });


    }


    @Override
    int getContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    int getNavigationMenuItemId() {
        return R.id.homee;
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(SELECTED_ITEM, mSelectedItem);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        finishAffinity();


    }


}
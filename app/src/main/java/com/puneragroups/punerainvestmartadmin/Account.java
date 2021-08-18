package com.puneragroups.punerainvestmartadmin;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Account extends ManagerBaseActivity {

    public static final String PREFS_NAME = "MyPrefsFile";
    private static final String SELECTED_ITEM = "arg_selected_item";
    int mSelectedItem;
    Button logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        logout = findViewById(R.id.logout);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(Account.this)
                        .setTitle("Log Out")
                        .setMessage("are you sure want to logout?")
                        .setNegativeButton(android.R.string.no, null)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface arg0, int arg1) {
                                Account.super.onBackPressed();

                                SharedPreferences settings = getSharedPreferences(MainActivity.PREFS_NAME, 0);
                                SharedPreferences.Editor editor = settings.edit();
                                editor.putBoolean("hasLoggedIn", false);

                                editor.commit();

                                Intent intent = new Intent(Account.this, LoginActivity.class);
                                startActivity(intent);
                                finish();

                            }
                        }).create().show();

            }
        });




    }


    @Override
    int getContentViewId() {
        return R.layout.activity_account;
    }

    @Override
    int getNavigationMenuItemId() {
        return R.id.acount;
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(SELECTED_ITEM, mSelectedItem);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(Account.this, MainActivity.class);
        startActivity(intent);
        finish();


    }
}
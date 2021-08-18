package com.puneragroups.punerainvestmartadmin;

import android.os.Bundle;

public class AccountantMainActivity extends ManagerBaseActivity {

    public static final String PREFS_NAME = "MyPrefsFile";
    private static final String SELECTED_ITEM = "arg_selected_item";
    int mSelectedItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);





    }


    @Override
    int getContentViewId() {
        return R.layout.activity_accountant_main;
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
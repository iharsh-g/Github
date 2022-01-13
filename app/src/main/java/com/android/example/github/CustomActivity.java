package com.android.example.github;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.example.github.Adapters.CustomAdapter;
import com.android.example.github.Models.Utils;
import com.google.android.material.tabs.TabLayout;

public class CustomActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private CustomAdapter mCustomAdapter;
    private TabLayout mTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom);

        getSupportActionBar().hide();

        SharedPreferences sharedPreferences = getSharedPreferences("prefUser", MODE_PRIVATE);

        TextView userNameActionBar = findViewById(R.id.activity_custom_action_bar_name_tv);
        userNameActionBar.setText(sharedPreferences.getString("userName", "-1"));

        ImageView imageViewBack = findViewById(R.id.activity_custom_back_iv);
        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        mViewPager = findViewById(R.id.activity_custom_viewPager);
        mTabLayout = findViewById(R.id.activity_custom_tabs);

        if(!Utils.isNetworkAvailable(this)){
            MakeDialog();
        } else {
            mCustomAdapter = new CustomAdapter(this, getSupportFragmentManager());
            mViewPager.setAdapter(mCustomAdapter);

            mTabLayout.setupWithViewPager(mViewPager);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private void MakeDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("No Internet Connection");
        builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(!Utils.isNetworkAvailable(CustomActivity.this)){
                    MakeDialog();
                }else {
                    dialog.dismiss();
                    mCustomAdapter = new CustomAdapter(CustomActivity.this, getSupportFragmentManager());
                    mViewPager.setAdapter(mCustomAdapter);
                    mTabLayout.setupWithViewPager(mViewPager);
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });
        builder.create().show();
    }
}
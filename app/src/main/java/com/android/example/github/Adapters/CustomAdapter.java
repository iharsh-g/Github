package com.android.example.github.Adapters;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.android.example.github.Fragments.UserDetailsFragment;
import com.android.example.github.Fragments.UserGitFragment;

public class CustomAdapter extends FragmentPagerAdapter {

    private Context mContext;

    public CustomAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if(position == 0){
            return new UserDetailsFragment();
        } else {
            return new UserGitFragment();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if(position == 0){
            return "Repositories";
        } else {
            return "Git History";
        }
    }
}

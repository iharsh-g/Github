package com.android.example.github.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.example.github.CustomActivity;
import com.android.example.github.Models.UserSearch;
import com.android.example.github.R;
import com.bumptech.glide.Glide;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;

public class UserSearchAdapter extends RecyclerView.Adapter<UserSearchAdapter.UserSearchViewHolder> {
    private Activity mActivity;
    private ArrayList<UserSearch> mUserSearchList;
    private int lastPos = -1;

    public UserSearchAdapter(Activity activity, ArrayList<UserSearch> userSearchList) {
        this.mActivity = activity;
        this.mUserSearchList = userSearchList;
    }

    @NonNull
    @Override
    public UserSearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(mActivity).inflate(R.layout.list_item_user_search, parent, false);
        return new UserSearchViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull UserSearchViewHolder holder, int position) {
        UserSearch currentUser = mUserSearchList.get(position);

        Glide.with(mActivity).load(currentUser.getUserAvatar()).into(holder.mAvatar);
        holder.mName.setText(currentUser.getUserName());
        holder.mLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(currentUser.getUserGithubUrl()));
                mActivity.startActivity(intent);
            }
        });

        holder.mRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, CustomActivity.class);
                SharedPreferences sharedPreferences = mActivity.getSharedPreferences("prefUser", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("userName", currentUser.getUserName());
                editor.putString("userAvatar", currentUser.getUserAvatar());
                editor.apply();
                mActivity.startActivity(intent);
                mActivity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        Animation animation;
        if( holder.getAdapterPosition() > lastPos ){
            animation = AnimationUtils.loadAnimation(mActivity, R.anim.item_up_from_bottom);
        } else {
            animation = AnimationUtils.loadAnimation(mActivity, R.anim.item_down_from_top);
        }
        holder.itemView.setAnimation(animation);
        lastPos = holder.getAdapterPosition();
    }

    @Override
    public int getItemCount() {
        return mUserSearchList.size();
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull UserSearchViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.itemView.clearAnimation();
    }

    class UserSearchViewHolder extends RecyclerView.ViewHolder {
        private CircularImageView mAvatar;
        private TextView mName;
        private ImageView mLogo;
        private RelativeLayout mRl;
        public UserSearchViewHolder(@NonNull View itemView) {
            super(itemView);
            mAvatar = itemView.findViewById(R.id.user_search_civ);
            mName = itemView.findViewById(R.id.user_search_name_tv);
            mLogo = itemView.findViewById(R.id.user_search_logo_iv);
            mRl = itemView.findViewById(R.id.user_search_detailsRl);
        }
    }
}

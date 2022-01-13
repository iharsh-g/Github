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

import com.android.example.github.IssuesActivity;
import com.android.example.github.Models.UserRepos;
import com.android.example.github.R;

import java.util.ArrayList;

public class UserReposAdapter extends RecyclerView.Adapter<UserReposAdapter.UserReposViewHolder> {

    private ArrayList<UserRepos> mUserRepoList;
    private Activity mActivity;
    private int lastPos = -1;

    public UserReposAdapter(Activity activity, ArrayList<UserRepos> userRepoList) {
        this.mUserRepoList = userRepoList;
        this.mActivity = activity;
    }

    @NonNull
    @Override
    public UserReposViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(mActivity).inflate(R.layout.list_item_user_repo, parent, false);
        return new UserReposViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull UserReposViewHolder holder, int position) {
        UserRepos currentRepo = mUserRepoList.get(position);

        holder.mRepoName.setText(currentRepo.getRepoName());
        String repoDesc = currentRepo.getRepoDesc();
        String repoLang = currentRepo.getRepoLang();

        if(repoDesc == null){
            repoDesc = "No Description Available";
        }

        if(repoLang == null){
            repoLang = "No Languages Available";
        }

        holder.mRepoDesc.setText(repoDesc);
        holder.mRepoLang.setText(repoLang);
        holder.mIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(currentRepo.getRepoUrl()));
                mActivity.startActivity(intent);
            }
        });

        SharedPreferences sharedPreferences = mActivity.getSharedPreferences("prefUser", Context.MODE_PRIVATE);
        holder.mRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, IssuesActivity.class);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("repoName", currentRepo.getRepoName());
                editor.apply();
                mActivity.startActivity(intent);
                mActivity.overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_down);
            }
        });

        Animation animation;
        if( holder.getAdapterPosition() > lastPos ){
            animation = AnimationUtils.loadAnimation(mActivity, R.anim.item_right_to_left);
        } else {
            animation = AnimationUtils.loadAnimation(mActivity, R.anim.item_left_to_right);
        }
        holder.itemView.setAnimation(animation);
        lastPos = holder.getAdapterPosition();
    }

    @Override
    public int getItemCount() {
        return mUserRepoList.size();
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull UserReposViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.itemView.clearAnimation();
    }

    class UserReposViewHolder extends RecyclerView.ViewHolder {
        private TextView mRepoName, mRepoDesc, mRepoLang;
        private ImageView mIcon;
        private RelativeLayout mRl;

        public UserReposViewHolder(@NonNull View itemView) {
            super(itemView);
            mRepoName = itemView.findViewById(R.id.user_repo_name);
            mRepoDesc = itemView.findViewById(R.id.user_repo_desc);
            mRepoLang = itemView.findViewById(R.id.user_repo_lang);
            mIcon = itemView.findViewById(R.id.user_repo_url);
            mRl = itemView.findViewById(R.id.user_repo_rl);
        }
    }
}

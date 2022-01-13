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
import com.android.example.github.Models.RepoSearch;
import com.android.example.github.R;

import java.util.ArrayList;

public class RepoSearchAdapter extends RecyclerView.Adapter<RepoSearchAdapter.RepoSearchViewHolder> {

    private ArrayList<RepoSearch> mList;
    private Activity mActivity;
    private int lastPos = -1;

    public RepoSearchAdapter(ArrayList<RepoSearch> list, Activity activity) {
        this.mList = list;
        this.mActivity = activity;
    }

    @NonNull
    @Override
    public RepoSearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.list_item_repo_search, parent, false);
        return new RepoSearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RepoSearchViewHolder holder, int position) {
        RepoSearch currentRepo = mList.get(position);

        holder.mName.setText(currentRepo.getFullName());
        holder.mDesc.setText(currentRepo.getDesc());
        holder.mIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(currentRepo.getHtmlUrl()));
                mActivity.startActivity(intent);
            }
        });

        holder.mRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, IssuesActivity.class);
                SharedPreferences sharedPreferences = mActivity.getSharedPreferences("prefUser", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                String fN = currentRepo.getFullName();
                String userN = null, repoN = null;
                if(fN.contains("/")){
                    String[] part = fN.split("/");
                    userN = part[0];
                    repoN = part[1];
                }

                editor.putString("userName", userN);
                editor.putString("repoName", repoN);
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
        return mList.size();
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull RepoSearchViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.itemView.clearAnimation();
    }

    public class RepoSearchViewHolder extends RecyclerView.ViewHolder {

        private TextView mName, mDesc;
        private ImageView mIcon;
        private RelativeLayout mRl;

        public RepoSearchViewHolder(@NonNull View itemView) {
            super(itemView);
            mName = itemView.findViewById(R.id.repo_search_name);
            mDesc = itemView.findViewById(R.id.repo_search_desc);
            mIcon = itemView.findViewById(R.id.repo_search_url);
            mRl = itemView.findViewById(R.id.repo_search_rl);
        }
    }
}

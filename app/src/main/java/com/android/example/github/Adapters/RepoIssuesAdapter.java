package com.android.example.github.Adapters;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
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

import com.android.example.github.Models.RepoIssues;
import com.android.example.github.R;
import com.android.example.github.SpecificIssueActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class RepoIssuesAdapter extends RecyclerView.Adapter<RepoIssuesAdapter.RepoIssuesViewHolder> {

    private ArrayList<RepoIssues> mList;
    private Activity mActivity;
    private int lastPos = -1;

    public RepoIssuesAdapter(ArrayList<RepoIssues> list, Activity activity) {
        this.mList = list;
        this.mActivity = activity;
    }

    @NonNull
    @Override
    public RepoIssuesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.list_item_repo_issues, parent, false);
        return new RepoIssuesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RepoIssuesViewHolder holder, int position) {
        RepoIssues current = mList.get(position);

        holder.setDT(current.getCreatedTime());
        holder.mTitle.setText(current.getTitle());
        holder.mIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(current.getUrl()));
                mActivity.startActivity(intent);
            }
        });

        holder.mRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, SpecificIssueActivity.class);
                intent.putExtra("number", current.getNum());
                Log.e("Number", " "+ current.getNum());
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
    public void onViewDetachedFromWindow(@NonNull RepoIssuesViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.itemView.clearAnimation();
    }

    class RepoIssuesViewHolder extends RecyclerView.ViewHolder {

        private TextView mDate, mTime, mTitle;
        private ImageView mIcon;
        private RelativeLayout mRl;

        public RepoIssuesViewHolder(@NonNull View itemView) {
            super(itemView);
            mDate = itemView.findViewById(R.id.repo_issues_date);
            mTime = itemView.findViewById(R.id.repo_issues_time);
            mTitle = itemView.findViewById(R.id.repo_issues_Title);
            mIcon = itemView.findViewById(R.id.repo_issues_url);
            mRl = itemView.findViewById(R.id.repo_issues_rl);
        }

        public void setDT(String datetime){
            SimpleDateFormat readDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
            readDate.setTimeZone(TimeZone.getTimeZone("GMT"));

            Date date = null;
            try {
                date = readDate.parse(datetime);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            SimpleDateFormat writeDate = new SimpleDateFormat("dd MMMM, yyyy 'T' hh:mm aa", Locale.US);
            writeDate.setTimeZone(TimeZone.getTimeZone("GMT+05:30"));

            datetime = writeDate.format(date);

            String actualDate = "";
            String actualTime = "";

            if (datetime.contains(" T ")) {
                String[] parts = datetime.split(" T ");
                actualDate = parts[0];
                actualTime = parts[1];
            }

            mDate.setText(actualDate);
            mTime.setText(actualTime);
        }
    }
}

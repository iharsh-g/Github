package com.android.example.github.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.example.github.Models.UserGit;
import com.android.example.github.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class UserGitAdapter extends RecyclerView.Adapter<UserGitAdapter.UserGitViewHolder> {

    private Context mContext;
    private ArrayList<UserGit> mUserGitList;
    private int lastPos = -1;

    public UserGitAdapter(Context context, ArrayList<UserGit> list) {
        this.mContext = context;
        this.mUserGitList = list;
    }

    @NonNull
    @Override
    public UserGitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.list_item_user_git, parent, false);
        return new UserGitViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull UserGitViewHolder holder, int position) {
        UserGit current = mUserGitList.get(position);

        holder.mGitRepoName.setText(current.getGitRepoName());
        holder.setTD(current.getGitTime());
        holder.mGitMessage.setText(current.getGitMessage());

        Animation animation;
        if( holder.getAdapterPosition() > lastPos ){
            animation = AnimationUtils.loadAnimation(mContext, R.anim.item_up_from_bottom);
        } else {
            animation = AnimationUtils.loadAnimation(mContext, R.anim.item_down_from_top);
        }
        holder.itemView.setAnimation(animation);
        lastPos = holder.getAdapterPosition();
    }

    @Override
    public int getItemCount() {
        return mUserGitList.size();
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull UserGitViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.itemView.clearAnimation();
    }


    class UserGitViewHolder extends RecyclerView.ViewHolder {
        private TextView mGitRepoName, mGitMessage, mGitDate, mGitTime;

        public UserGitViewHolder(@NonNull View itemView) {
            super(itemView);
            mGitRepoName = itemView.findViewById(R.id.user_git_repo_name);
            mGitDate = itemView.findViewById(R.id.user_git_date);
            mGitTime = itemView.findViewById(R.id.user_git_time);
            mGitMessage = itemView.findViewById(R.id.user_git_message);
        }

        public void setTD(String datetime){
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

            mGitDate.setText(actualDate);
            mGitTime.setText(actualTime);
        }
    }
}

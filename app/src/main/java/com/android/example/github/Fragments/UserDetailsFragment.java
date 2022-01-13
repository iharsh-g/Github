package com.android.example.github.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.example.github.Adapters.UserReposAdapter;
import com.android.example.github.Models.UserRepos;
import com.android.example.github.R;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class UserDetailsFragment extends Fragment {

    private String mUserName;
    private ProgressBar mPb;
    private RecyclerView mRecyclerView;
    private ArrayList<UserRepos> mRepoList;
    private RequestQueue requestQueue;
    private UserReposAdapter mAdapter;

    public UserDetailsFragment(){    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_user_details, container, false);
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("prefUser", Context.MODE_PRIVATE);

        mUserName = sharedPreferences.getString("userName", "-1");

        mPb = view.findViewById(R.id.fragment_user_details_pb);
        mRecyclerView = view.findViewById(R.id.fragment_user_details_rv);
        mRepoList = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(getContext());
        mAdapter = new UserReposAdapter(getActivity(), mRepoList);

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mRecyclerView.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);


        parseJson();

        return view;
    }

    private void parseJson() {
        String url = "https://api.github.com/users/" + mUserName + "/repos";
        Log.e("UserDetailsAc", ""+ url);
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for( int i=0; i<response.length(); i++){
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                String repoName, repoDesc, repoLang, repoUrl;

                                repoName = jsonObject.getString("name");
                                repoUrl = jsonObject.getString("html_url");
                                repoDesc = jsonObject.getString("description");
                                repoLang = jsonObject.getString("language");

                                UserRepos userRepos = new UserRepos(repoName, repoUrl, repoDesc, repoLang);
                                mRepoList.add(userRepos);

                                mPb.setVisibility(View.GONE);
                                mRecyclerView.setVisibility(View.VISIBLE);
                                mAdapter.notifyDataSetChanged();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "There is some error", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(request);
    }
}

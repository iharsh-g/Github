package com.android.example.github.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.example.github.Adapters.UserGitAdapter;
import com.android.example.github.Adapters.UserReposAdapter;
import com.android.example.github.Models.UserGit;
import com.android.example.github.Models.UserRepos;
import com.android.example.github.R;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class UserGitFragment extends Fragment {

    private String mUserName;
    private RecyclerView mRecyclerView;
    private ArrayList<UserGit> mGitList;
    private RequestQueue requestQueue;
    private UserGitAdapter mAdapter;

    public UserGitFragment(){    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_user_git, container, false);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("prefUser", Context.MODE_PRIVATE);
        mUserName = sharedPreferences.getString("userName", "-1");

        mRecyclerView = view.findViewById(R.id.user_git_rv);
        mGitList = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(getContext());
        mAdapter = new UserGitAdapter(getContext(), mGitList);

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mRecyclerView.setVisibility(View.GONE);
        parseJson();
        return view;
    }

    private void parseJson() {
        String url = "https://api.github.com/users/" + mUserName + "/events";
        Log.e("UserGirF", ""+url);

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        mRecyclerView.setVisibility(View.VISIBLE);
                        for( int i=0; i<response.length(); i++ ){
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                String name, message, time;

                                String type = jsonObject.getString("type");

                                if(type.equals("PushEvent")){
                                    JSONObject repo = jsonObject.getJSONObject("repo");
                                    name = repo.getString("name");

                                    JSONObject payload = jsonObject.getJSONObject("payload");
                                    JSONArray commits = payload.getJSONArray("commits");
                                    JSONObject jsonObject1 = commits.getJSONObject(0);
                                    message = jsonObject1.getString("message");

                                    time = jsonObject.getString("created_at");

                                    UserGit userGit = new UserGit(name, message, time);
                                    mGitList.add(userGit);
                                    mAdapter.notifyDataSetChanged();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "No git History", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(request);
    }
}

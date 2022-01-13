package com.android.example.github;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.example.github.Adapters.RepoIssuesAdapter;
import com.android.example.github.Adapters.UserSearchAdapter;
import com.android.example.github.Models.RepoIssues;
import com.android.example.github.Models.UserSearch;
import com.android.example.github.Models.Utils;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class IssuesActivity extends AppCompatActivity {

    private ArrayList<RepoIssues> mList;
    private RequestQueue mQueue;
    private RepoIssuesAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private ProgressBar mPb;
    private SharedPreferences mSharedPreferences;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issues);
        mSharedPreferences = getSharedPreferences("prefUser", MODE_PRIVATE);

        mList = new ArrayList<>();
        mQueue = Volley.newRequestQueue(this);
        mAdapter = new RepoIssuesAdapter(mList, this);
        mRecyclerView = findViewById(R.id.activity_issues_rv);
        mPb = findViewById(R.id.activity_issues_pb);

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mRecyclerView.setVisibility(View.GONE);
        mPb.setVisibility(View.VISIBLE);

        ImageView imageView = findViewById(R.id.activity_issues_back_iv);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        TextView textViewActionBar = findViewById(R.id.activity_issues_action_bar_name_tv);
        textViewActionBar.setText(mSharedPreferences.getString("repoName", "-1") + "'s Issues");

        getSupportActionBar().hide();

        if(!Utils.isNetworkAvailable(this)){
            MakeDialog();
        } else {
            jsonParse();
        }
    }

    private void jsonParse() {
        String url = "https://api.github.com/repos/" + mSharedPreferences.getString("userName", "-1")
                      + "/" + mSharedPreferences.getString("repoName", "-1") + "/issues";
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for( int i=0; i<response.length(); i++){
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                String title, url, time;

                                url = jsonObject.getString("html_url");
                                int no = jsonObject.getInt("number");
                                title = jsonObject.getString("title");
                                time = jsonObject.getString("created_at");

                                RepoIssues repoIssues = new RepoIssues(title, url, time, no);
                                mList.add(repoIssues);
                                mAdapter.notifyDataSetChanged();

                                mPb.setVisibility(View.GONE);
                                mRecyclerView.setVisibility(View.VISIBLE);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(IssuesActivity.this, "SOme Problem Occurs", Toast.LENGTH_SHORT).show();
            }
        });
        mQueue.add(request);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_up);
    }

    private void MakeDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("No Internet Connection");
        builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(!Utils.isNetworkAvailable(IssuesActivity.this)){
                    MakeDialog();
                }else {
                    dialog.dismiss();
                    jsonParse();
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
package com.android.example.github;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.example.github.Adapters.RepoSearchAdapter;
import com.android.example.github.Models.RepoSearch;
import com.android.example.github.Models.Utils;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchRepositoryActivity extends AppCompatActivity {

    private ArrayList<RepoSearch> mList;
    private RequestQueue mQueue;
    private RepoSearchAdapter mAdapter;
    private TextInputEditText mEditText;
    private RecyclerView mRecyclerView;
    private ProgressBar mPb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_repository);

        mEditText = findViewById(R.id.activity_search_repo_editText);
        mRecyclerView = findViewById(R.id.activity_search_repo_rv);
        mPb = findViewById(R.id.activity_search_repo_pb);
        mQueue = Volley.newRequestQueue(this);

        mList = new ArrayList<>();
        mAdapter = new RepoSearchAdapter(mList, this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mRecyclerView.setVisibility(View.GONE);
        mPb.setVisibility(View.GONE);

        showKeyboard();

        mEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH && Utils.isNetworkAvailable(SearchRepositoryActivity.this)){
                    search();
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
                } else {
                    MakeDialog();
                }
                return true;
            }
        });

        TextInputLayout textInputLayout = findViewById(R.id.activity_search_repo_inputLayout);
        textInputLayout.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showKeyboard();
            }
        });

        getSupportActionBar().hide();
    }

    private void showKeyboard(){
        mEditText.setText("");
        mEditText.requestFocus();
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    private void search() {
        String q = mEditText.getText().toString().trim();
        if(q.equals("")){
            Toast.makeText(this, "Enter Repo Name", Toast.LENGTH_SHORT).show();
        } else {
            mList.clear();
            mAdapter.notifyDataSetChanged();
            mPb.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
            jsonParse(q);
        }
    }

    private void jsonParse(String q) {
        String url = "https://api.github.com/search/repositories?q=" + q;

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray items = response.getJSONArray("items");

                            for( int i=0; i<items.length(); i++ ) {
                                JSONObject jsonObject = items.getJSONObject(i);

                                String fullName, htmlUrl, desc, url;
                                fullName = jsonObject.getString("full_name");
                                htmlUrl = jsonObject.getString("html_url");
                                desc = jsonObject.getString("description");
                                url = jsonObject.getString("url");

                                RepoSearch repoSearch = new RepoSearch(fullName, htmlUrl, desc, url);
                                mList.add(repoSearch);

                                mRecyclerView.setVisibility(View.VISIBLE);
                                mPb.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SearchRepositoryActivity.this, "Pass Another Query!", Toast.LENGTH_SHORT).show();
            }
        });
        mQueue.add(req);
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
                if(!Utils.isNetworkAvailable(SearchRepositoryActivity.this)){
                    MakeDialog();
                }else {
                    dialog.dismiss();
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
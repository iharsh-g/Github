package com.android.example.github;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.example.github.Models.Utils;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class SpecificIssueActivity extends AppCompatActivity {

    private RequestQueue mQueue;
    private TextView mTitle;
    private TextView mBody;
    private TextView mTime, mDate;
    private String mUrl="";

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_issue);

        mQueue = Volley.newRequestQueue(this);
        TextView mNum = findViewById(R.id.specific_issue_number);
        ImageView mIcon = findViewById(R.id.specific_issue_url);
        mTitle = findViewById(R.id.specific_issue_title);
        mBody = findViewById(R.id.specific_issue_body);
        mDate = findViewById(R.id.specific_issue_date);
        mTime = findViewById(R.id.specific_issue_time);

        mNum.setText("# " + getIntent().getIntExtra("number", -1));
        mIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(mUrl));
                startActivity(intent);
            }
        });

        ImageView imageView = findViewById(R.id.activity_specific_issues_back_iv);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        TextView textViewActionBar = findViewById(R.id.activity_specific_issues_action_bar_name_tv);
        textViewActionBar.setText("Specific Issue");

        getSupportActionBar().hide();

        if(!Utils.isNetworkAvailable(this)){
            MakeDialog();
        } else {
            jsonParse();
        }
    }

    private void jsonParse() {
        SharedPreferences sharedPreferences = getSharedPreferences("prefUser", MODE_PRIVATE);
        String url = "https://api.github.com/repos/" + sharedPreferences.getString("userName", "-1")
                + "/" + sharedPreferences.getString("repoName", "-1") + "/issues/" + getIntent().getIntExtra("number", -1);

        Log.e("Specific", ""+url);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            mUrl = response.getString("html_url");
                            String title = response.getString("title");
                            String body = response.getString("body");
                            String time = response.getString("updated_at");

                            mTitle.setText(title);
                            mBody.setText(body);
                            setTimeDate(time);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        mQueue.add(request);
    }

    private void setTimeDate(String datetime) {
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
                if(!Utils.isNetworkAvailable(SpecificIssueActivity.this)){
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
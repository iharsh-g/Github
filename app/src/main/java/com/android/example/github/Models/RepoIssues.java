package com.android.example.github.Models;

public class RepoIssues {
    private String mTitle;
    private String mUrl;
    private String mCreatedTime;
    private int mNum;

    public RepoIssues(String title, String url, String cTime, int num) {
        this.mTitle = title;
        this.mUrl = url;
        this.mCreatedTime = cTime;
        this.mNum = num;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getUrl() {
        return mUrl;
    }

    public String getCreatedTime() {
        return mCreatedTime;
    }

    public int getNum() {
        return mNum;
    }
}

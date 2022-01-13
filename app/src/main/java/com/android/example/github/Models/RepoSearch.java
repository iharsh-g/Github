package com.android.example.github.Models;

public class RepoSearch {
    private String mFullName;
    private String mHtmlUrl;
    private String mDesc;
    private String mUrl;

    public RepoSearch(String fullName, String htmlUrl, String desc, String url) {
        this.mFullName = fullName;
        this.mHtmlUrl = htmlUrl;
        this.mDesc = desc;
        this.mUrl = url;
    }

    public String getFullName() {
        return mFullName;
    }

    public String getHtmlUrl() {
        return mHtmlUrl;
    }

    public String getDesc() {
        return mDesc;
    }

    public String getUrl() {
        return mUrl;
    }
}

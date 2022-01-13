package com.android.example.github.Models;

public class UserRepos {
    private String mRepoName;
    private String mRepoUrl;
    private String mRepoDesc;
    private String mRepoLang;

    public UserRepos(String repoName, String repoUrl, String repoDesc, String repoLang) {
        this.mRepoName = repoName;
        this.mRepoUrl = repoUrl;
        this.mRepoDesc = repoDesc;
        this.mRepoLang = repoLang;
    }

    public String getRepoName() {
        return mRepoName;
    }

    public String getRepoUrl() {
        return mRepoUrl;
    }

    public String getRepoDesc() {
        return mRepoDesc;
    }

    public String getRepoLang() {
        return mRepoLang;
    }
}

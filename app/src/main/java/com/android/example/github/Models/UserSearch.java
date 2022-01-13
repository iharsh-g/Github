package com.android.example.github.Models;

public class UserSearch {
    private String mUserName;
    private String mUserId;
    private String mUserAvatar;
    private String mUserGithubUrl;

    public UserSearch(String userName, String userId, String userAvatar, String userGithubUrl) {
        this.mUserName = userName;
        this.mUserId = userId;
        this.mUserAvatar = userAvatar;
        this.mUserGithubUrl = userGithubUrl;
    }

    public String getUserName() {
        return mUserName;
    }

    public String getUserId() {
        return mUserId;
    }

    public String getUserAvatar() {
        return mUserAvatar;
    }

    public String getUserGithubUrl() {
        return mUserGithubUrl;
    }
}

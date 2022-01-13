package com.android.example.github.Models;

public class UserGit {
    private String mGitRepoName;
    private String mGitMessage;
    private String mGitTime;

    public UserGit(String gitRepoName, String gitMessage, String gitTime) {
        this.mGitRepoName = gitRepoName;
        this.mGitMessage = gitMessage;
        this.mGitTime = gitTime;
    }

    public String getGitRepoName() {
        return mGitRepoName;
    }

    public String getGitMessage() {
        return mGitMessage;
    }

    public String getGitTime() {
        return mGitTime;
    }
}

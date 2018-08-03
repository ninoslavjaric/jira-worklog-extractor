package com.logic;

public class JiraThread extends Thread {
    private Jira jira;
    private int offset;
    private int limit;

    JiraThread(Jira jira, int offset, int limit) {
        this.jira = jira;
        this.offset = offset;
        this.limit = limit;
    }

    @Override
    public void run() {
        jira.issueListCall(offset, limit);
    }
}

package com.logic;

import com.Logger;
import org.json.simple.parser.ParseException;
import java.io.IOException;

public class JiraThread extends Thread {
    private final int ISSUE_LIST_LIMIT = 5;

    private Jira jira;
    private int offset;
    private int limit;

    private int issueListAttemptCounter = 0;

    JiraThread(Jira jira, int offset, int limit) {
        this.jira = jira;
        this.offset = offset;
        this.limit = limit;
    }

    @Override
    public void run() {
        while (++issueListAttemptCounter < ISSUE_LIST_LIMIT) {
            try {
                jira.issueListCall(offset, limit);
                break;
            } catch (IOException | ParseException e) {
                e.printStackTrace();
                if (ISSUE_LIST_LIMIT < issueListAttemptCounter) {
                    System.exit(2);
                }
                System.out.println();
                Logger.fail(String.format("Pulling issue chunk failed. Attempting again. %s iteration.", issueListAttemptCounter), false);
                System.out.println();
            }
        }
    }
}

package com.logic;

import com.Logger;
import com.model.Worklog;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import java.io.IOException;
import java.net.ConnectException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TreeMap;

public class WorklogThread extends Thread {
    private final int CALL_ATTEMPT_LIMIT = 5;

    private String url;
    private int callAttempt = 1;

    WorklogThread(String url) {
        this.url = url;
    }

    @Override
    public void run() {
        call();
    }

    private void call() {
        try {
            Object worklogs;
            TreeMap<String, ArrayList<Worklog>> stringWorklogHashMap;
            Object issue = Jira.getResponse(new URL(url));
            worklogs = issue;
            Worklog wl;
            ArrayList<Worklog> wls;

            for (String key : Worklog.getWorklogPath())  {
                worklogs = ((JSONObject) worklogs).get(key);
            }

            for (Object worklog : (JSONArray) worklogs) {
                String authorKey = (String)((JSONObject) ((JSONObject) worklog).get("author")).get("key");
                Date date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").parse((String) ((JSONObject) worklog).get("started"));
                String formattedDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
                wl = new Worklog(
                        (String) ((JSONObject) worklog).get("comment"),
                        Math.toIntExact((Long) ((JSONObject) worklog).get("timeSpentSeconds")),
                        (String) ((JSONObject) issue).get("key")
                );

                if ((stringWorklogHashMap = Worklog.getLogs().get(authorKey)) == null) {
                    stringWorklogHashMap = new TreeMap<>();
                    wls = new ArrayList<>();
                } else if((wls = stringWorklogHashMap.get(formattedDate)) == null) {
                    wls = new ArrayList<>();
                }

                wls.add(wl);
                stringWorklogHashMap.put(formattedDate, wls);
                Worklog.setLogs(authorKey, stringWorklogHashMap);
            }
        } catch (ConnectException e) {
            Logger.fail(String.format("\tCall to '%s' failed. Attempt number %d.", url, callAttempt));
            Logger.fail(e.getMessage());

            if (++callAttempt < CALL_ATTEMPT_LIMIT) {
                call();
            }
        } catch (IOException | ParseException | java.text.ParseException e) {
            e.printStackTrace();
            System.exit(2);
        }
    }
}

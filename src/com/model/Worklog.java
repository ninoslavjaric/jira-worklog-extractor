package com.model;

import com.logic.Serializer;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.TreeMap;

public class Worklog implements Serializable {
    private static ArrayList<String> keys = null;

    public static HashMap<String, TreeMap<String, ArrayList<Worklog>>> logs = new HashMap<>();

    private String desc;
    private int spent;
    private String issue;

    public Worklog(String desc, int spent, String issue) {
        this.desc = desc;
        this.spent = spent;
        this.issue = issue;
    }

    public static synchronized HashMap<String, TreeMap<String, ArrayList<Worklog>>> getLogs() {
        return logs;
    }

    public static synchronized HashMap<String, TreeMap<String, ArrayList<Worklog>>> fetchLogs() {
        if (logs.isEmpty()) {
            File folder = new File("resources/worklogs");
            File[] listOfFiles = folder.listFiles();
            for (File file : listOfFiles) {
                String name = file.getName().replace(".ser", "");
                Object map = Serializer.unserialize(file.getPath());
                logs.put(name, (TreeMap<String, ArrayList<Worklog>>) map);
            }
        }

        return logs;
    }

    public static synchronized void setLogs(HashMap<String, TreeMap<String, ArrayList<Worklog>>> logs) {
        Worklog.logs = logs;
    }
    public static synchronized void setLogs(String key, TreeMap<String, ArrayList<Worklog>> logs) {
        Worklog.logs.put(key, logs);
    }

    public static ArrayList<String> getWorklogPath() {
        if (keys == null) {
            keys = new ArrayList<>();

            keys.add("fields");
            keys.add("worklog");
            keys.add("worklogs");
        }

        return keys;
    }

    public String getDesc() {
        return desc;
    }

    public int getSpent() {
        return spent;
    }

    public String getIssue() {
        return issue;
    }
}

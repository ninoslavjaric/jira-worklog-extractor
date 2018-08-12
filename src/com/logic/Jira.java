package com.logic;

import com.Logger;
import com.model.Worklog;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;

public class Jira {
    private static String RESOURCE_DIR = "resources";
    private static String WORKLOGS_DIR = String.format("%s/%s", RESOURCE_DIR, "worklogs");
    private static String ISSUES_SERIALIZED = String.format("%s/%s", RESOURCE_DIR, "issues.ser");
    private HashMap<String, Worklog> worklogs = new HashMap<>();

    private static final String JIRA_DOMAIN = FileSystem.load("config/domain.local");
    private static
    final String initialResourcePattern = JIRA_DOMAIN + "/rest/api/2/search?jql=%s&startAt=%d&maxResults=%d";
    private static final String[] projects = FileSystem.load("config/projects.local").split("\n");

    private int issuePullStatus = 0;
    private int issuePullTotal = 0;

    private ArrayList<String> issues = new ArrayList<>();
    private int issueCallAttempt;

    private synchronized void updateIssuePullStatus(int increment) {
        issuePullStatus += increment;
        float percentage = (float) issuePullStatus*100 / issuePullTotal;
        Logger.info("\rIssues pull progress:\t" + percentage + "%", !(percentage < 100));
//        System.out.print("\rIssues pull progress:\t" + percentage + "%");
    }

    public Jira() {
        setIssuePullTotal();
        setIssues();
        setWorkLogs();
    }

    private void setWorkLogs() {
        ArrayList<WorklogThread> threads = new ArrayList<>();
        ArrayList<ArrayList<WorklogThread>> threadChunks = new ArrayList<>();
        int urlCounter = 0;
        for (String url : this.issues) {
            threads.add(new WorklogThread(url));
            if (++urlCounter % 50 == 0) {
                threadChunks.add(threads);
                threads = new ArrayList<>();
            }
        }
        if (threads.size() > 0) {
            threadChunks.add(threads);
        }

        urlCounter = 0;
        for (ArrayList<WorklogThread> threads1 : threadChunks) {
            Logger.info(String.format("\r%d out of %d", ++urlCounter, threadChunks.size()), (urlCounter == threadChunks.size()));
            try {
                for (Thread thread : threads1) {
                    thread.start();
                }
                for (Thread thread : threads1) {
                    thread.join();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.exit(2);
            }
        }

        for(Map.Entry<String, TreeMap<String, ArrayList<Worklog>>> entry : Worklog.getLogs().entrySet()) {
            String key = entry.getKey();
            TreeMap<String, ArrayList<Worklog>> value = entry.getValue();

            Serializer.serialize(value, String.format("%s/%s", WORKLOGS_DIR, key + ".ser"));
        }
    }

    static JSONObject getResponse(URL url) throws IOException, ParseException {
        HttpURLConnection connection  = (HttpURLConnection) url.openConnection();

        String userCredentials = FileSystem.load("config/user.local");
        String basicAuth = "Basic " + new String(Base64.getEncoder().encode(userCredentials.getBytes()));

        connection.setRequestMethod("GET");
        connection.setConnectTimeout(0);
        connection.setReadTimeout(0);
        connection.setRequestProperty("Authorization", basicAuth);

        InputStream is = connection.getInputStream();
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            response.append(line);
            response.append('\r');
        }
        rd.close();
        String result = response.toString();

        return (JSONObject) (new JSONParser().parse(result));
    }

    private void setIssuePullTotal() {
        try {
            String jql = String.format("project in (%s)", String.join(", ", projects));
            jql = URLEncoder.encode(jql, "UTF-8").replace("+", "%20");
            String resource = String.format(initialResourcePattern, jql, 0, 0);
            JSONObject jsonObject = getResponse(new URL(resource));
            Long total = (Long) jsonObject.get("total");
            this.issuePullTotal = Math.toIntExact(total);
        } catch ( ParseException | IOException e) {
            e.printStackTrace();
            System.exit(2);
        }
    }

    private void issueListCall() {
        int offset = 0;
        int limit = 50;
        ArrayList<JiraThread> threadPool = new ArrayList<>();
        JiraThread tmpThread;
        try {
            do {
                tmpThread = new JiraThread(this, offset, limit);
                tmpThread.start();
                threadPool.add(tmpThread);
                offset += limit;
            } while (offset < this.issuePullTotal);

            for (JiraThread jt : threadPool) {
                jt.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    void issueListCall(int offset, int limit) throws IOException, ParseException {
        String jql = String.format("project in (%s)", String.join(", ", projects));
        jql = URLEncoder.encode(jql, "UTF-8").replace("+", "%20");

        String resource = String.format(initialResourcePattern, jql, offset, limit);

        Object jsonObject = getResponse(new URL(resource));

        JSONArray issues = (JSONArray) ((JSONObject) jsonObject).get("issues");

        String url;
        JSONObject JSONobject;
        for (Object object : issues) {
            if (object instanceof JSONObject) {
                JSONobject = (JSONObject) object;
                url = (String) JSONobject.get("self");
                this.issues.add(url);
            }
        }

        updateIssuePullStatus(issues.size());
    }

    private void setIssues() {
        if ((this.issues = (ArrayList<String>) Serializer.unserialize(ISSUES_SERIALIZED, 1)) == null) {
            this.issues = new ArrayList<>();
            issueListCall();

            Logger.success("Issue list is gathered successfully.");
            Serializer.serialize(this.issues, ISSUES_SERIALIZED);
        }
    }

    public static void main() {
        Jira jr = new Jira();
    }
}
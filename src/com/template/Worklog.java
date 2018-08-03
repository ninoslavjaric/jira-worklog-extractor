package com.template;

import com.Logger;
import com.logic.FileSystem;
import com.template.tag.Tag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class Worklog extends Main {
    public Worklog(String username) {
        super(username);
    }

    protected Html getBody(Html node) {
        Html body = super.getBody();
        Html container = new Html(Tag.div).setClassName("container").appendTo(body);

        new Html(Tag.div).setClassName("row").appendTo(container)
                .addNode(
                        new Html(Tag.div).setClassName("col-sm-12")
                                .addNode(new Html("Worklogs | " + username, Tag.h1).setClassName("text-center")));

        container.addNode(node);

        new Html(Tag.script)
                .addAttribute("src", String.format("%s/%s", "../assets", "igniter.js"))
                .appendTo(body);

        return body;
    }

    @Override
    protected Html getBody() {
        Html row =  new Html(Tag.div).setClassName("row");
        Html table = new Html(Tag.table).setClassName("table").setId("table");

        Html thead = new Html(Tag.thead).appendTo(table);
        Html tbody = new Html(Tag.tbody).appendTo(table);
        Html theadRow = new Html(Tag.tr).appendTo(thead);

        row.addNode(new Html(Tag.div).setClassName("col-sm-12").addNode(table));

        theadRow
                .addNode(new Html("Date", Tag.th))
                .addNode(new Html("Issue", Tag.th))
                .addNode(new Html("Spent", Tag.th))
                .addNode(new Html("Comment", Tag.th).addAttribute("style", "width: 65%;"))
        ;

        Html trow, dateHtml, issue, spent, desc;

        for(Map.Entry<String, ArrayList<com.model.Worklog>> entry : com.model.Worklog.fetchLogs().get(username).entrySet()) {
            String date = entry.getKey();
            ArrayList<com.model.Worklog> logs = entry.getValue();

            for (com.model.Worklog log : logs) {
                dateHtml = new Html(date, Tag.td);
                issue = new Html(Tag.td)
                    .addNode(
                        new Html(log.getIssue(), Tag.a)
                                .addAttribute("href", String.format("https://fajira.futureads.com/browse/%s", log.getIssue()))
                    );
                spent = new Html(String.format("%d", log.getSpent()), Tag.td);
                desc = new Html(log.getDesc(), Tag.td);

                trow = new Html(Tag.tr)
                        .addNode(dateHtml)
                        .addNode(issue)
                        .addNode(spent)
                        .addNode(desc);
                tbody.addNode(trow);
            }

        }

        return getBody(table);
    }

    public static void main() {
        for (Map.Entry<String, TreeMap<String, ArrayList<com.model.Worklog>>> djese : com.model.Worklog.fetchLogs().entrySet()){
            String username = djese.getKey();
            FileSystem.save("html/user/" +username+ ".html", new Worklog(username).toString());
            Logger.info(String.format("%s log html is generated.", username), true);
        }
    }
}

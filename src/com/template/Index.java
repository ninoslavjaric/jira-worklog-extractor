package com.template;

import com.Logger;
import com.logic.FileSystem;
import com.template.tag.Tag;

public class Index extends Main {
    public Index() {
    }

    @Override
    protected Html getBody() {
        Html row =  new Html(Tag.div).setClassName("row");
        Html table = new Html(Tag.table).setClassName("table").setId("table");
        row.addNode(new Html(Tag.div).setClassName("col-sm-12").addNode(table));

        Html trow;
        for (int i = 0; i < 20; i++) {
            trow = new Html(Tag.tr).addNode(new Html(String.format("Row %d", i), Tag.td));
            table.addNode(trow);
        }

        return getBody(table);
    }

    protected Html getBody(Html node) {
        Html body = super.getBody();
        Html container = new Html(Tag.div).setClassName("container").appendTo(body);

        new Html(Tag.div).setClassName("row").appendTo(container)
                .addNode(
                        new Html(Tag.div).setClassName("col-sm-12")
                                .addNode(new Html("Worklogs", Tag.h1).setClassName("text-center")));

        container.addNode(node);

        return body;
    }
}

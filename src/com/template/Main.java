package com.template;

import com.template.tag.Tag;

public abstract class Main {
    protected String username = "";

    public Main() {
    }

    public Main(String username) {
        this.username = username;
    }


    protected Html getHead() {
        Html head = new Html(Tag.head);

        head.addNode(new Html("Pregled", Tag.title));
        new Html(Tag.link)
                .addAttribute("href", "https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css")
                .addAttribute("rel", "stylesheet").appendTo(head);

        new Html(Tag.script)
                .addAttribute("src", "https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js")
                .appendTo(head);

        new Html(Tag.script)
                .addAttribute("src", "https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js")
                .appendTo(head);

        new Html(Tag.script)
                .addAttribute("src", "https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.7.2/Chart.bundle.min.js")
                .appendTo(head);

        new Html(Tag.link)
                .addAttribute("href", "https://cdn.datatables.net/1.10.16/css/jquery.dataTables.css")
                .addAttribute("rel", "stylesheet").appendTo(head);

        new Html(Tag.script)
                .addAttribute("src", "https://cdn.datatables.net/1.10.16/js/jquery.dataTables.js")
                .appendTo(head);

        new Html(Tag.link)
                .addAttribute("href", "//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css")
                .addAttribute("rel", "stylesheet").appendTo(head);

        new Html(Tag.script)
                .addAttribute("src", "https://code.jquery.com/ui/1.12.1/jquery-ui.js")
                .appendTo(head);

        new Html(Tag.script)
                .addAttribute("src", "https://momentjs.com/downloads/moment.min.js")
                .appendTo(head);

        return head;
    }

    protected Html getHtml() {
        Html html = new Html(Tag.html);
        getHead().appendTo(html);
        getBody().appendTo(html);
        return html;
    }

    protected Html getBody() {
        return new Html(Tag.body);
    }

    @Override
    public String toString() {
        return getHtml().toString();
    }
}

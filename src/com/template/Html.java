package com.template;

import com.template.tag.Tag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Html {
    private Enum tag = Tag.td;

    private String content = "";
    private ArrayList<Html> nodes = new ArrayList<>();

    private String className = "";
    private String id = "";
    private HashMap<String, String> attributes = new HashMap<>();

    public Html(String object) {
        content = object;
    }

    public Html(ArrayList<Html> nodes) {
        this.nodes = nodes;
    }

    public Html(ArrayList<Html> nodes, Tag tag) {
        this.nodes = nodes;
        this.tag = tag;
    }

    public Html(Enum tag) {
        this.tag = tag;
    }

    public Html(String content, Tag tag) {
        this.content = content;
        this.tag = tag;
    }

    public String getClassName() {
        return className;
    }

    public Html setClassName(String className) {
        addAttribute("class", className);
        this.className = className;
        return this;
    }

    public String getId() {
        return id;
    }

    public Html setId(String id) {
        addAttribute("id", id);
        this.id = id;
        return this;
    }

    public String getContent() {
        if (content.isEmpty()) {
            content = "";

            for (Html node : getNodes()) {
                content = content.concat(node.toString());
            }
        }
        return content;
    }

    public ArrayList<Html> getNodes() {
        return nodes;
    }

    public Html setNodes(ArrayList<Html> nodes) {
        this.nodes = nodes;
        return this;
    }

    public Html addNode(Html node) {
        this.nodes.add(node);
        return this;
    }

    public Html addAttribute(String key, String value) {
        this.attributes.put(key, value);
        return this;
    }

    public String[] getAttributes() {
        String[] attrs = new String[this.attributes.size()];
        int counter = 0;
        for(Map.Entry<String, String> entry : this.attributes.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            attrs[counter++] = String.format("%s='%s'", key, value);
        }

        return attrs;
    }

    @Override
    public String toString() {
        String attributes = String.format(" %s", String.join(" ", getAttributes()));
        String html;

        if (tag.equals(Tag.link)) {
            html = String.format(
                "<%s%s/>", tag,
                attributes.trim().isEmpty() ? "" : attributes
            );
        } else {
            html = String.format(
                "<%s%s>%s</%s>", tag,
                attributes.trim().isEmpty() ? "" : attributes,
                this.getContent(), tag
            );
        }

        return html;
    }

    public Html appendTo(Html container) {
        container.addNode(this);
        return this;
    }
}

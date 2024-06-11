package com.wasmake.itemupgrade.config;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.HashMap;
import java.util.Map;

public class AbstractConfiguration {
    @JsonIgnore
    private String filePath;
    @JsonIgnore
    Map<String, String[]> comments = new HashMap<>();
    @JsonIgnore
    Map<String, String[]> header = new HashMap<>();
    @JsonIgnore
    String[] headings = new String[0];
    @JsonIgnore
    private IConfigurationLoader loader;

    public void addComment(String key, String... comment) {
        comments.put(key, comment);
    }

    public void addHeader(String key, String... header) {
        this.header.put(key, header);
    }

    public void addHeading(String... heading) {
        this.headings = heading;
    }

    public String[] getHeadings() {
        return headings;
    }
    public String[] getCommentsForField(String key) {
        return comments.get(key);
    }

    public String[] getHeaderForField(String key) {
        return header.get(key);
    }

    public void setLoader(IConfigurationLoader loader) {
        this.loader = loader;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
    public void save() {
        loader.save(this, filePath);
    }
}

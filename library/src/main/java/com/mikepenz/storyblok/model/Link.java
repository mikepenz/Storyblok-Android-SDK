package com.mikepenz.storyblok.model;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by mikepenz on 14/04/2017.
 */

public class Link extends Entity {
    private boolean isFolder;
    private long parentId;
    private boolean published;
    private int position;

    public Link(JSONObject story) {
        super(story);

        if (story.has("is_folder")) {
            this.isFolder = story.optBoolean("is_folder");
        }
        if (story.has("parent_id")) {
            this.parentId = story.optLong("parent_id");
        }
        if (story.has("published")) {
            this.published = story.optBoolean("published");
        }
        if (story.has("position")) {
            this.position = story.optInt("position");
        }
    }

    public static Map<String, Link> parseLinks(JSONObject result) {
        if (result != null && result.has("links")) {
            JSONObject jsonObject = result.optJSONObject("links");
            Map<String, Link> links = new HashMap<>(jsonObject.length());
            Iterator<String> i = jsonObject.keys();
            while (i.hasNext()) {
                String key = i.next();
                links.put(key, new Link(jsonObject.optJSONObject(key)));
            }
            return links;
        }
        return null;
    }

    public boolean isFolder() {
        return isFolder;
    }

    public long getParentId() {
        return parentId;
    }

    public boolean isPublished() {
        return published;
    }

    public int getPosition() {
        return position;
    }
}

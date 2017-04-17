package com.mikepenz.storyblok.model;

import org.json.JSONObject;

/**
 * Created by mikepenz on 14/04/2017.
 */

public class Entity {
    private long id;
    private String name;
    private String uuid;
    private String slug;
    private boolean isStartpage;

    public Entity(JSONObject story) {
        if (story.has("name")) {
            this.name = story.optString("name");
        }
        if (story.has("id")) {
            this.id = story.optLong("id");
        }
        if (story.has("uuid")) {
            this.uuid = story.optString("uuid");
        }
        if (story.has("slug")) {
            this.slug = story.optString("slug");
        }
        if (story.has("is_startpage")) {
            this.isStartpage = story.optBoolean("is_startpage");
        }
    }

    public String getName() {
        return name;
    }

    public long getId() {
        return id;
    }

    public String getUuid() {
        return uuid;
    }

    public String getSlug() {
        return slug;
    }

    public boolean isStartpage() {
        return isStartpage;
    }
}

package com.mikepenz.storyblok.model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mikepenz on 14/04/2017.
 */

public class Tag extends Entity {
    private int taggingsCount;

    public Tag(JSONObject story) {
        super(story);

        if (story.has("taggings_count")) {
            this.taggingsCount = story.optInt("taggings_count");
        }
    }

    public static List<Tag> parseTags(JSONObject result) {
        if (result != null && result.has("tags")) {
            JSONArray jsonArray = result.optJSONArray("tags");
            List<Tag> tags = new ArrayList<>(jsonArray.length());
            for (int i = 0; i < jsonArray.length(); i++) {
                tags.add(new Tag(jsonArray.optJSONObject(i)));
            }
            return tags;
        }
        return null;
    }

    public int getTaggingsCount() {
        return taggingsCount;
    }
}

package com.mikepenz.storyblok.model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mikepenz on 14/04/2017.
 */

public class Story extends Entity {
    private String createdAdt;
    private String publishedAt;
    private String fullSlug;
    private String sortByDate;
    private JSONObject content;
    private List<String> tagList;

    public Story(JSONObject story) {
        super(story);

        if (story.has("created_at")) {
            this.createdAdt = story.optString("created_at");
        }
        if (story.has("published_at")) {
            this.publishedAt = story.optString("published_at");
        }
        if (story.has("content")) {
            this.content = story.optJSONObject("content");
        }
        if (story.has("tag_list")) {
            List<String> tagList = new ArrayList<>();
            JSONArray array = story.optJSONArray("tag_list");
            for (int i = 0; i < array.length(); i++) {
                tagList.add(array.optString(i));
            }
            this.tagList = tagList;
        }
        if (story.has("full_slug")) {
            this.fullSlug = story.optString("full_slug");
        }
        if (story.has("sort_by_date")) {
            this.sortByDate = story.optString("sort_by_date");
        }
    }

    public static Story parseStory(JSONObject result) {
        if (result != null) {
            return new Story(result.optJSONObject("story"));
        }
        return null;
    }

    public static List<Story> parseStories(JSONObject result) {
        if (result != null && result.has("stories")) {
            JSONArray jsonArray = result.optJSONArray("stories");
            List<Story> stories = new ArrayList<>(jsonArray.length());
            for (int i = 0; i < jsonArray.length(); i++) {
                stories.add(new Story(jsonArray.optJSONObject(i)));
            }
            return stories;
        }
        return null;
    }

    public String getCreatedAdt() {
        return createdAdt;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public String getFullSlug() {
        return fullSlug;
    }

    public String getSortByDate() {
        return sortByDate;
    }

    public JSONObject getContent() {
        return content;
    }

    public List<String> getTagList() {
        return tagList;
    }
}

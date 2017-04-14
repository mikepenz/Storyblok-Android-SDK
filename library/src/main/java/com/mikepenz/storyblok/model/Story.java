package com.mikepenz.storyblok.model;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.mikepenz.storyblok.StoryBlok.ERROR_TAG;
import static com.mikepenz.storyblok.StoryBlok.ERROR_TEXT;

/**
 * Created by mikepenz on 14/04/2017.
 */

public class Story {
    private String name;
    private String createdAdt;
    private String publishedAt;
    private long id;
    private String uuid;
    private String slug;
    private String fullSlug;
    private String sortByDate;
    private boolean isStartpage;
    private JSONObject content;
    private JSONArray tagList;

    public Story(JSONObject story) throws JSONException {
        if (story.has("name")) {
            this.name = story.getString("name");
        }
        if (story.has("created_at")) {
            this.createdAdt = story.getString("created_at");
        }
        if (story.has("published_at")) {
            this.publishedAt = story.getString("published_at");
        }
        if (story.has("id")) {
            this.id = story.getLong("id");
        }
        if (story.has("uuid")) {
            this.uuid = story.getString("uuid");
        }
        if (story.has("content")) {
            this.content = story.getJSONObject("content");
        }
        if (story.has("tag_list")) {
            this.tagList = story.getJSONArray("tag_list");
        }
        if (story.has("slug")) {
            this.slug = story.getString("slug");
        }
        if (story.has("full_slug")) {
            this.fullSlug = story.getString("full_slug");
        }
        if (story.has("sort_by_date")) {
            this.sortByDate = story.getString("sort_by_date");
        }
        if (story.has("is_startpage")) {
            this.isStartpage = story.getBoolean("is_startpage");
        }
    }

    public static Story parseStory(JSONObject result) {
        if (result != null) {
            try {
                return new Story(result.getJSONObject("story"));
            } catch (JSONException e) {
                Log.e(ERROR_TAG, ERROR_TEXT);
            }
        }
        return null;
    }

    public static List<Story> parseStories(JSONObject result) {
        if (result != null && result.has("stories")) {
            JSONArray jsonArray;
            try {
                jsonArray = result.getJSONArray("stories");
            } catch (JSONException e) {
                Log.e(ERROR_TAG, ERROR_TEXT);
                return null;
            }
            List<Story> stories = new ArrayList<>(jsonArray.length());
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    stories.add(new Story(jsonArray.getJSONObject(i)));
                } catch (JSONException e) {
                    Log.e(ERROR_TAG, ERROR_TEXT);
                }
            }
            return stories;
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public String getCreatedAdt() {
        return createdAdt;
    }

    public String getPublishedAt() {
        return publishedAt;
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

    public String getFullSlug() {
        return fullSlug;
    }

    public String getSortByDate() {
        return sortByDate;
    }

    public boolean isStartpage() {
        return isStartpage;
    }

    public JSONObject getContent() {
        return content;
    }

    public JSONArray getTagList() {
        return tagList;
    }
}

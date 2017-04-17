package com.mikepenz.storyblok.model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mikepenz on 14/04/2017.
 */

public class Datasource extends Entity {
    private String value;

    public Datasource(JSONObject story) {
        super(story);

        if (story.has("value")) {
            this.value = story.optString("value");
        }
    }

    public static List<Datasource> parseDatasource(JSONObject result) {
        if (result != null && result.has("datasource_entries")) {
            JSONArray jsonArray = result.optJSONArray("datasource_entries");
            List<Datasource> datasources = new ArrayList<>(jsonArray.length());
            for (int i = 0; i < jsonArray.length(); i++) {
                datasources.add(new Datasource(jsonArray.optJSONObject(i)));
            }
            return datasources;
        }
        return null;
    }

    public String getValue() {
        return value;
    }
}

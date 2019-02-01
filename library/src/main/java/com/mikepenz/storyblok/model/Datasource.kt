package com.mikepenz.storyblok.model

import org.json.JSONObject
import java.util.*

/**
 * Created by mikepenz on 14/04/2017.
 */

class Datasource(story: JSONObject) : Entity(story) {
    var value: String? = null
        private set

    init {

        if (story.has("value")) {
            this.value = story.optString("value")
        }
    }

    companion object {

        fun parseDatasource(result: JSONObject?): List<Datasource>? {
            if (result != null && result.has("datasource_entries")) {
                val jsonArray = result.optJSONArray("datasource_entries")
                val datasources = ArrayList<Datasource>(jsonArray.length())
                for (i in 0 until jsonArray.length()) {
                    datasources.add(Datasource(jsonArray.optJSONObject(i)))
                }
                return datasources
            }
            return null
        }
    }
}

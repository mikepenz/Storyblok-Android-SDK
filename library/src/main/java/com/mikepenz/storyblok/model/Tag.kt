package com.mikepenz.storyblok.model

import org.json.JSONObject
import java.util.*

/**
 * Created by mikepenz on 14/04/2017.
 */

class Tag(story: JSONObject) : Entity(story) {
    var taggingsCount: Int = 0
        private set

    init {

        if (story.has("taggings_count")) {
            this.taggingsCount = story.optInt("taggings_count")
        }
    }

    companion object {

        fun parseTags(result: JSONObject?): List<Tag>? {
            if (result != null && result.has("tags")) {
                val jsonArray = result.optJSONArray("tags")
                val tags = ArrayList<Tag>(jsonArray.length())
                for (i in 0 until jsonArray.length()) {
                    tags.add(Tag(jsonArray.optJSONObject(i)))
                }
                return tags
            }
            return null
        }
    }
}

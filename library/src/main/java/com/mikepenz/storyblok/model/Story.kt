package com.mikepenz.storyblok.model

import org.json.JSONObject
import java.util.*

/**
 * Created by mikepenz on 14/04/2017.
 */

class Story(story: JSONObject) : Entity(story) {
    var createdAdt: String? = null
        private set
    var publishedAt: String? = null
        private set
    var fullSlug: String? = null
        private set
    var sortByDate: String? = null
        private set
    var content: JSONObject? = null
        private set
    var tagList: List<String>? = null
        private set

    init {

        if (story.has("created_at")) {
            this.createdAdt = story.optString("created_at")
        }
        if (story.has("published_at")) {
            this.publishedAt = story.optString("published_at")
        }
        if (story.has("content")) {
            this.content = story.optJSONObject("content")
        }
        if (story.has("tag_list")) {
            val tagList = ArrayList<String>()
            val array = story.optJSONArray("tag_list")
            for (i in 0 until array.length()) {
                tagList.add(array.optString(i))
            }
            this.tagList = tagList
        }
        if (story.has("full_slug")) {
            this.fullSlug = story.optString("full_slug")
        }
        if (story.has("sort_by_date")) {
            this.sortByDate = story.optString("sort_by_date")
        }
    }

    companion object {

        fun parseStory(result: JSONObject?): Story? {
            return if (result != null) {
                Story(result.optJSONObject("story"))
            } else null
        }

        fun parseStories(result: JSONObject?): List<Story>? {
            if (result != null && result.has("stories")) {
                val jsonArray = result.optJSONArray("stories")
                val stories = ArrayList<Story>(jsonArray.length())
                for (i in 0 until jsonArray.length()) {
                    stories.add(Story(jsonArray.optJSONObject(i)))
                }
                return stories
            }
            return null
        }
    }
}

package com.mikepenz.storyblok.model

import org.json.JSONObject
import java.util.*

/**
 * Created by mikepenz on 14/04/2017.
 */

class Link(story: JSONObject) : Entity(story) {
    var isFolder: Boolean = false
        private set
    var parentId: Long = 0
        private set
    var isPublished: Boolean = false
        private set
    var position: Int = 0
        private set

    init {

        if (story.has("is_folder")) {
            this.isFolder = story.optBoolean("is_folder")
        }
        if (story.has("parent_id")) {
            this.parentId = story.optLong("parent_id")
        }
        if (story.has("published")) {
            this.isPublished = story.optBoolean("published")
        }
        if (story.has("position")) {
            this.position = story.optInt("position")
        }
    }

    companion object {

        fun parseLinks(result: JSONObject?): Map<String, Link>? {
            if (result != null && result.has("links")) {
                val jsonObject = result.optJSONObject("links")
                val links = HashMap<String, Link>(jsonObject.length())
                val i = jsonObject.keys()
                while (i.hasNext()) {
                    val key = i.next()
                    links[key] = Link(jsonObject.optJSONObject(key))
                }
                return links
            }
            return null
        }
    }
}

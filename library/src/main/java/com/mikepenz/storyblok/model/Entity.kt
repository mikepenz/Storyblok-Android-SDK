package com.mikepenz.storyblok.model

import org.json.JSONObject

/**
 * Created by mikepenz on 14/04/2017.
 */

open class Entity(story: JSONObject) {
    var id: Long = 0
        private set
    var name: String? = null
        private set
    var uuid: String? = null
        private set
    var slug: String? = null
        private set
    var isStartpage: Boolean = false
        private set

    init {
        if (story.has("name")) {
            this.name = story.optString("name")
        }
        if (story.has("id")) {
            this.id = story.optLong("id")
        }
        if (story.has("uuid")) {
            this.uuid = story.optString("uuid")
        }
        if (story.has("slug")) {
            this.slug = story.optString("slug")
        }
        if (story.has("is_startpage")) {
            this.isStartpage = story.optBoolean("is_startpage")
        }
    }
}

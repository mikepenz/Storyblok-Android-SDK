package com.mikepenz.storyblok

import android.util.Log
import com.mikepenz.storyblok.cache.Cache
import com.mikepenz.storyblok.model.*
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

/**
 * Created by mikepenz on 14/04/2017.
 */

class Storyblok private constructor(token: String) {

    private val client = OkHttpClient()

    private var token: String? = null
    private var cache: Cache? = null
    private var editMode = false
    private var apiProtocol = API_PROTOCOL
    private var apiEndpoint = API_ENDPOINT
    private var apiVersion = API_VERSION

    private val requestBuild: Request.Builder
        get() = Request.Builder()

    init {
        this.token = token
    }

    fun withCache(cache: Cache): Storyblok {
        this.cache = cache
        return this
    }

    fun withToken(token: String): Storyblok {
        this.token = token
        return this
    }

    fun withApiProtocol(apiProtocol: String): Storyblok {
        this.apiProtocol = apiProtocol
        return this
    }

    fun withApiEndpoint(apiEndpoint: String): Storyblok {
        this.apiEndpoint = apiEndpoint
        return this
    }

    fun withApiVersion(apiVersion: String): Storyblok {
        this.apiVersion = apiVersion
        return this
    }

    fun withEditMode(editMode: Boolean): Storyblok {
        this.editMode = editMode
        return this
    }

    fun getStory(slug: String, successCallback: SuccessCallback<Story>, errorCallback: ErrorCallback?) {
        val cacheKey = buildCacheKey(ENDPOINT_STORIES, "slug", slug)
        reCacheOnPublish(cacheKey)

        client.newCall(buildRequest(buildUrl(ENDPOINT_STORIES).addPathSegments(slug))).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                errorCallback?.onFailure(e, null)
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                if (response.code() >= 300) {
                    errorCallback?.onFailure(null, response.body().string())
                } else {
                    successCallback.onResponse(Result(response.headers(), Story.parseStory(toJsonObjectAndCache(cacheKey, response.body().string()))))
                }
            }
        })
    }

    fun getStories(startsWith: String?, withTag: String?, sortBy: String?, perPage: Int?, page: Int?, successCallback: SuccessCallback<List<Story>>, errorCallback: ErrorCallback?) {
        val cacheKey = buildCacheKey(ENDPOINT_STORIES, "starts_with", startsWith, "with_tag", withTag, "sort_by", sortBy, "per_page", perPage.toString(), "page", page.toString())
        reCacheOnPublish(cacheKey)

        client.newCall(
                buildRequest(
                        buildUrl(ENDPOINT_STORIES)
                                .addQueryParameter("starts_with", startsWith)
                                .addQueryParameter("with_tag", withTag)
                                .addQueryParameter("sort_by", sortBy)
                                .addQueryParameter("per_page", perPage?.toString())
                                .addQueryParameter("page", page?.toString())
                                .addQueryParameter("cache_version", cache?.cacheVersion?.toString())
                )
        ).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                errorCallback?.onFailure(e, null)
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                if (response.code() >= 300) {
                    errorCallback?.onFailure(null, response.body().string())
                } else {
                    successCallback.onResponse(Result(response.headers(), Story.parseStories(toJsonObjectAndCache(cacheKey, response.body().string()))))
                }
            }
        })
    }

    fun getTags(startsWith: String?, successCallback: SuccessCallback<List<Tag>>, errorCallback: ErrorCallback?) {
        val cacheKey = buildCacheKey(ENDPOINT_TAGS, "starts_with", startsWith)
        reCacheOnPublish(cacheKey)

        client.newCall(buildRequest(buildUrl(ENDPOINT_TAGS).addQueryParameter("starts_with", startsWith))).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                errorCallback?.onFailure(e, null)
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                if (response.code() >= 300) {
                    errorCallback?.onFailure(null, response.body().string())
                } else {
                    successCallback.onResponse(Result(response.headers(), Tag.parseTags(toJsonObjectAndCache(cacheKey, response.body().string()))))
                }
            }
        })
    }

    fun getLinks(successCallback: SuccessCallback<Map<String, Link>>, errorCallback: ErrorCallback?) {
        val cacheKey = buildCacheKey(ENDPOINT_LINKS)
        reCacheOnPublish(cacheKey)

        client.newCall(buildRequest(buildUrl(ENDPOINT_LINKS))).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                errorCallback?.onFailure(e, null)
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                if (response.code() >= 300) {
                    errorCallback?.onFailure(null, response.body().string())
                } else {
                    successCallback.onResponse(Result(response.headers(), Link.parseLinks(toJsonObjectAndCache(cacheKey, response.body().string()))))
                }
            }
        })
    }

    fun getDatasource(datasource: String?, successCallback: SuccessCallback<List<Datasource>>, errorCallback: ErrorCallback?) {
        val cacheKey = buildCacheKey(ENDPOINT_DATASOURCE, "datasource", datasource)
        reCacheOnPublish(cacheKey)

        client.newCall(buildRequest(buildUrl(ENDPOINT_DATASOURCE).addQueryParameter("datasource", datasource))).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                errorCallback?.onFailure(e, null)
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                if (response.code() >= 300) {
                    errorCallback?.onFailure(null, response.body().string())
                } else {
                    successCallback.onResponse(Result(response.headers(), Datasource.parseDatasource(toJsonObjectAndCache(cacheKey, response.body().string()))))
                }
            }
        })
    }

    private fun toJsonObjectAndCache(key: String, result: String?): JSONObject? {
        try {
            val jsonObject = JSONObject(result)
            cache?.save(jsonObject, key)
            return jsonObject
        } catch (e: JSONException) {
            Log.e(ERROR_TAG, ERROR_TEXT)
        }

        return null
    }

    private fun buildCacheKey(vararg `val`: String?): String {
        if (cache == null) {
            return ""
        }
        val res = StringBuilder()
        for (v in `val`) {
            res.append(v)
        }
        return res.toString()
    }

    private fun reCacheOnPublish(key: String) {
        if (cache == null) {
            return
        }

        val storyblokPublished = "" // FIXME
        val item = cache?.load(key)
        if (item != null) {
            try {
                if (item.has("story") && item.getJSONObject("story").get("id") === storyblokPublished) {
                    cache?.delete(key)
                }
            } catch (e: JSONException) {
                Log.e(ERROR_TAG, ERROR_TEXT)
            }

            // Always refresh cache of links
            cache?.delete(ENDPOINT_LINKS)
            setCacheVersion()
        }
    }

    private fun setCacheVersion() {
        cache?.withCacheVersion(System.currentTimeMillis())
    }

    private fun buildRequest(url: HttpUrl.Builder): Request {
        return requestBuild
                .url(url.build())
                .header("User-Agent", SDK_USER_AGENT)
                .build()
    }

    private fun buildUrl(method: String): HttpUrl.Builder {
        return HttpUrl.Builder()
                .scheme(apiProtocol)
                .host(apiEndpoint)
                .addPathSegment(apiVersion)
                .addPathSegment("cdn")
                .addPathSegment(method)
                .addQueryParameter("token", token)
                .addQueryParameter("version", if (editMode) VERSION_DRAFT else VERSION_PUBLISHED)
    }

    interface SuccessCallback<Model> {
        fun onResponse(result: Result<Model>)
    }

    interface ErrorCallback {
        fun onFailure(exception: IOException?, response: String?)
    }

    companion object {
        const val ERROR_TAG = "Storyblok"
        const val ERROR_TEXT = "Sorry for the inconvience. Something broken was sent by the server"

        private const val API_PROTOCOL = "https"
        private const val API_ENDPOINT = "api.storyblok.com"
        private const val API_VERSION = "v1"

        private const val SDK_VERSION = "0.1"
        private const val SDK_USER_AGENT = "storyblok-sdk-android/$SDK_VERSION"

        private const val VERSION_PUBLISHED = "published"
        private const val VERSION_DRAFT = "draft"

        private const val ENDPOINT_STORIES = "stories"
        private const val ENDPOINT_LINKS = "links"
        private const val ENDPOINT_TAGS = "tags"
        private const val ENDPOINT_DATASOURCE = "datasource_entries"

        private var SINGLETON: Storyblok? = null

        fun init(token: String): Storyblok {
            if (SINGLETON == null) {
                SINGLETON = Storyblok(token)
            }
            return SINGLETON as Storyblok
        }
    }
}

package com.mikepenz.storyblok;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.mikepenz.storyblok.cache.Cache;
import com.mikepenz.storyblok.model.Datasource;
import com.mikepenz.storyblok.model.Link;
import com.mikepenz.storyblok.model.Result;
import com.mikepenz.storyblok.model.Story;
import com.mikepenz.storyblok.model.Tag;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by mikepenz on 14/04/2017.
 */

public class Storyblok {
    public static final String ERROR_TAG = "Storyblok";
    public static final String ERROR_TEXT = "Sorry for the inconvience. Something broken was sent by the server";

    private static final String API_PROTOCOL = "https";
    private static final String API_ENDPOINT = "api.storyblok.com";
    private static final String API_VERSION = "v1";

    private static final String SDK_VERSION = "0.1";
    private static final String SDK_USER_AGENT = "storyblok-sdk-android" + "/" + SDK_VERSION;

    private static final String VERSION_PUBLISHED = "published";
    private static final String VERSION_DRAFT = "draft";

    private static final String ENDPOINT_STORIES = "stories";
    private static final String ENDPOINT_LINKS = "links";
    private static final String ENDPOINT_TAGS = "tags";
    private static final String ENDPOINT_DATASOURCE = "datasource_entries";

    private static Storyblok SINGLETON = null;

    private OkHttpClient client = new OkHttpClient();

    private String token = null;
    private Cache cache = null;
    private boolean editMode = false;
    private String apiProtocol = API_PROTOCOL;
    private String apiEndpoint = API_ENDPOINT;
    private String apiVersion = API_VERSION;

    private Storyblok(String token) {
        this.token = token;
    }

    public static Storyblok init(String token) {
        if (SINGLETON == null) {
            SINGLETON = new Storyblok(token);
        }
        return SINGLETON;
    }

    public Storyblok withCache(Cache cache) {
        this.cache = cache;
        return this;
    }

    public Storyblok withToken(String token) {
        this.token = token;
        return this;
    }

    public Storyblok withApiProtocol(String apiProtocol) {
        this.apiProtocol = apiProtocol;
        return this;
    }

    public Storyblok withApiEndpoint(String apiEndpoint) {
        this.apiEndpoint = apiEndpoint;
        return this;
    }

    public Storyblok withApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
        return this;
    }

    public Storyblok withEditMode(boolean editMode) {
        this.editMode = editMode;
        return this;
    }

    public void getStory(@NonNull String slug, @NonNull final SuccessCallback<Story> successCallback, @Nullable final ErrorCallback errorCallback) {
        final String cacheKey = buildCacheKey(ENDPOINT_STORIES, "slug", slug);
        reCacheOnPublish(cacheKey);

        client.newCall(buildRequest(buildUrl(ENDPOINT_STORIES).addPathSegments(slug))).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (errorCallback != null) errorCallback.onFailure(e, null);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.code() >= 300) {
                    if (errorCallback != null)
                        errorCallback.onFailure(null, response.body().string());
                } else {
                    successCallback.onResponse(new Result<>(response.headers(), Story.parseStory(toJsonObjectAndCache(cacheKey, response.body().string()))));
                }
            }
        });
    }

    public void getStories(@Nullable String startsWith, @Nullable String withTag, @Nullable String sortBy, @Nullable Integer perPage, @Nullable Integer page, @NonNull final SuccessCallback<List<Story>> successCallback, @Nullable final ErrorCallback errorCallback) {
        final String cacheKey = buildCacheKey(ENDPOINT_STORIES, "starts_with", startsWith, "with_tag", withTag, "sort_by", sortBy, "per_page", String.valueOf(perPage), "page", String.valueOf(page));
        reCacheOnPublish(cacheKey);

        client.newCall(
                buildRequest(
                        buildUrl(ENDPOINT_STORIES)
                                .addQueryParameter("starts_with", startsWith)
                                .addQueryParameter("with_tag", withTag)
                                .addQueryParameter("sort_by", sortBy)
                                .addQueryParameter("per_page", perPage != null ? String.valueOf(perPage) : null)
                                .addQueryParameter("page", page != null ? String.valueOf(page) : null)
                                .addQueryParameter("cache_version", cache != null ? String.valueOf(cache.getCacheVersion()) : null)
                )
        ).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (errorCallback != null) errorCallback.onFailure(e, null);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.code() >= 300) {
                    if (errorCallback != null)
                        errorCallback.onFailure(null, response.body().string());
                } else {
                    successCallback.onResponse(new Result<>(response.headers(), Story.parseStories(toJsonObjectAndCache(cacheKey, response.body().string()))));
                }
            }
        });
    }

    public void getTags(@Nullable String startsWith, @NonNull final SuccessCallback<List<Tag>> successCallback, @Nullable final ErrorCallback errorCallback) {
        final String cacheKey = buildCacheKey(ENDPOINT_TAGS, "starts_with", startsWith);
        reCacheOnPublish(cacheKey);

        client.newCall(buildRequest(buildUrl(ENDPOINT_TAGS).addQueryParameter("starts_with", startsWith))).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (errorCallback != null) errorCallback.onFailure(e, null);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.code() >= 300) {
                    if (errorCallback != null)
                        errorCallback.onFailure(null, response.body().string());
                } else {
                    successCallback.onResponse(new Result<>(response.headers(), Tag.parseTags(toJsonObjectAndCache(cacheKey, response.body().string()))));
                }
            }
        });
    }

    public void getLinks(@NonNull final SuccessCallback<Map<String, Link>> successCallback, @Nullable final ErrorCallback errorCallback) {
        final String cacheKey = buildCacheKey(ENDPOINT_LINKS);
        reCacheOnPublish(cacheKey);

        client.newCall(buildRequest(buildUrl(ENDPOINT_LINKS))).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (errorCallback != null) errorCallback.onFailure(e, null);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.code() >= 300) {
                    if (errorCallback != null)
                        errorCallback.onFailure(null, response.body().string());
                } else {
                    successCallback.onResponse(new Result<>(response.headers(), Link.parseLinks(toJsonObjectAndCache(cacheKey, response.body().string()))));
                }
            }
        });
    }

    public void getDatasource(@Nullable String datasource, @NonNull final SuccessCallback<List<Datasource>> successCallback, @Nullable final ErrorCallback errorCallback) {
        final String cacheKey = buildCacheKey(ENDPOINT_DATASOURCE, "datasource", datasource);
        reCacheOnPublish(cacheKey);

        client.newCall(buildRequest(buildUrl(ENDPOINT_DATASOURCE).addQueryParameter("datasource", datasource))).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (errorCallback != null) errorCallback.onFailure(e, null);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.code() >= 300) {
                    if (errorCallback != null)
                        errorCallback.onFailure(null, response.body().string());
                } else {
                    successCallback.onResponse(new Result<>(response.headers(), Datasource.parseDatasource(toJsonObjectAndCache(cacheKey, response.body().string()))));
                }
            }
        });
    }

    private JSONObject toJsonObjectAndCache(@NonNull String key, @Nullable String result) {
        try {
            JSONObject jsonObject = new JSONObject(result);
            if (cache != null) {
                cache.save(jsonObject, key);
            }
            return jsonObject;
        } catch (JSONException e) {
            Log.e(ERROR_TAG, ERROR_TEXT);
        }
        return null;
    }

    private String buildCacheKey(String... val) {
        if (cache == null) {
            return "";
        }
        StringBuilder res = new StringBuilder();
        for (String v : val) {
            res.append(v);
        }
        return res.toString();
    }

    private void reCacheOnPublish(String key) {
        if (cache == null) {
            return;
        }

        //_storyblok_published ?!
        String _storyblok_published = "";
        JSONObject item = cache.load(key);
        if (item != null) {
            try {
                if (item.has("story") && item.getJSONObject("story").get("id") == _storyblok_published) {
                    cache.delete(key);
                }
            } catch (JSONException e) {
                Log.e(ERROR_TAG, ERROR_TEXT);
            }

            // Always refresh cache of links
            cache.delete(ENDPOINT_LINKS);
            setCacheVersion();
        }
    }

    private void setCacheVersion() {
        if (cache != null) {
            cache.withCacheVersion(System.currentTimeMillis());
        }
    }

    public Request.Builder getRequestBuild() {
        return new Request.Builder();
    }

    private Request buildRequest(HttpUrl.Builder url) {
        return getRequestBuild()
                .url(url.build())
                .header("User-Agent", SDK_USER_AGENT)
                .build();
    }

    private HttpUrl.Builder buildUrl(@NonNull String method) {
        return new HttpUrl.Builder()
                .scheme(apiProtocol)
                .host(apiEndpoint)
                .addPathSegment(apiVersion)
                .addPathSegment("cdn")
                .addPathSegment(method)
                .addQueryParameter("token", token)
                .addQueryParameter("version", editMode ? VERSION_DRAFT : VERSION_PUBLISHED);
    }

    public interface SuccessCallback<Model> {
        void onResponse(@Nullable Result<Model> result);
    }

    public interface ErrorCallback {
        void onFailure(@Nullable IOException exception, @Nullable String response);
    }
}

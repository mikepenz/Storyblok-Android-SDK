package com.mikepenz.storyblok.cache;

import org.json.JSONObject;

/**
 * Created by mikepenz on 14/04/2017.
 */

public interface Cache {
    long getCacheVersion();

    Cache withCacheVersion(long cacheVersion);

    Cache delete(String key);

    Cache save(JSONObject response, String key);

    JSONObject load(String key);
}

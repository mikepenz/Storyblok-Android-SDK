package com.mikepenz.storyblok.model;

import androidx.annotation.Nullable;

import okhttp3.Headers;

/**
 * Created by mikepenz on 14/04/2017.
 */

public class Result<Model> {
    private Headers header;
    private Model result;

    public Result(@Nullable Headers header, @Nullable Model result) {
        this.header = header;
        this.result = result;
    }

    @Nullable
    public Headers getHeader() {
        return header;
    }

    public void setHeader(@Nullable Headers header) {
        this.header = header;
    }

    @Nullable
    public Model getResult() {
        return result;
    }

    public void setResult(@Nullable Model result) {
        this.result = result;
    }
}

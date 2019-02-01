package com.mikepenz.storyblok.model

import okhttp3.Headers

/**
 * Created by mikepenz on 14/04/2017.
 */

class Result<Model>(var header: Headers?, var result: Model?)

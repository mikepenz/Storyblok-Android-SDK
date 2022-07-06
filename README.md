# Storyblok Kotlin Multiplatform SDK

Please find the new Kotlin multiplatform SDK available here: https://github.com/mikepenz/storyblok-mp-SDK


# Storyblok-Android-SDK [Deprecated]

This is the Storyblok Android client for easy access of the publishing api.

# More about Storyblok
- **WEBSITE** https://www.storyblok.com/
- **API DOC** https://www.storyblok.com/docs/api/content-delivery

# Include in your project
## Using Maven

```gradle
implementation "com.mikepenz:storyblok-android-sdk:1.0.0"
```

> Note starting with v1.0.0 this library uses kotlin. See older versions for java only.

## How to use
### Init client

```kotlin
val client = Storyblok.init("your-storyblok-token")
```

### Load a story
```kotlin
client.getStory("fullSlug", object : Storyblok.SuccessCallback<Story> {
    override fun onResponse(result: Result<Story>) {
        //on success
    }
}, object : Storyblok.ErrorCallback {
    override fun onFailure(exception: IOException?, response: String?) {
        // empty
    }
})
```

### Load a list of stories
```kotlin
client.getStories(startsWith, withTag, sortBy, perPage, page, object : Storyblok.SuccessCallback<List<Story>> {
    override fun onResponse(result: Result<List<Story>>) {
        //on success
    }
}, object : Storyblok.ErrorCallback {
    override fun onFailure(exception: IOException?, response: String?) {
        // empty
    }
})
```

### Load a list of tags
```kotlin
client.getTags(startsWith, object : Storyblok.SuccessCallback<List<Tag>> {
    override fun onResponse(result: Result<List<Tag>>) {
        //on success
    }
}, object : Storyblok.ErrorCallback {
    override fun onFailure(exception: IOException?, response: String?) {
        // empty
    }
})
```

### Load a map of links
```kotlin
client.getLinks(object : Storyblok.SuccessCallback<Map<String, Link>> {
    override fun onResponse(result: Result<Map<String, Link>>) {
        //on success
    }
}, object : Storyblok.ErrorCallback {
    override fun onFailure(exception: IOException?, response: String?) {
        // empty
    }
})
```

### Load a list of datasources
```kotlin
client.getDatasource(datasource, object : Storyblok.SuccessCallback<List<Datasource>> {
    override fun onResponse(result: Result<List<Datasource>>) {
        //on success
    }
}, object : Storyblok.ErrorCallback {
    override fun onFailure(exception: IOException?, response: String?) {
        // empty
    }
})
```


## Libs used in sample app:
Mike Penz:
- FastAdapter https://github.com/mikepenz/FastAdapter
- AboutLibraries https://github.com/mikepenz/AboutLibraries
- Android-Iconics https://github.com/mikepenz/Android-Iconics
- MaterialDrawer https://github.com/mikepenz/MaterialDrawer

# Developed By

* Mike Penz 
  * [mikepenz.com](http://mikepenz.com) - <mikepenz@gmail.com>
  * [paypal.me/mikepenz](http://paypal.me/mikepenz)

# Contributors

This free, open source software was also made possible by a group of volunteers that put many hours of hard work into it. See the [CONTRIBUTORS.md](CONTRIBUTORS.md) file for details.

# License

    Copyright 2019 Mike Penz

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

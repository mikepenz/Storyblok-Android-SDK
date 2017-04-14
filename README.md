# StoryBlok-Android-SDK

This is the Storyblok Android client for easy access of the publishing api.

# Include in your project
## Using Maven

```gradle
//not yet published
compile "com.mikepenz:storyblok-android-sdk:0.1.0@aar"
```

## How to use
### Init client

```java
StoryBlok client = StoryBlok.init("your-storyblok-token");
```

### Load a story
```java
client.getStory("full_slug", new StoryBlok.StoryblokCallback<Story>() {
    @Override
    public void onFailure(IOException exception) {
        //on error
    }

    @Override
    public void onResponse(final Result<Story> result) {
        //on success
    }
});
```

### Load a list of stories
```java
client.getStories(startsWith, withTag, sortBy, perPage, page, new StoryBlok.StoryblokCallback<List<Story>>() {
    @Override
        public void onFailure(IOException exception) {
            //on error
        }

        @Override
        public void onResponse(final Result<List<Story>> result) {
            //on success
        }
});
```


## Libs used in sample app:
Mike Penz:
- AboutLibraries https://github.com/mikepenz/AboutLibraries
- Android-Iconics https://github.com/mikepenz/Android-Iconics
- ItemAnimators https://github.com/mikepenz/ItemAnimators
- MaterialDrawer https://github.com/mikepenz/MaterialDrawer

# Developed By

* Mike Penz 
  * [mikepenz.com](http://mikepenz.com) - <mikepenz@gmail.com>
  * [paypal.me/mikepenz](http://paypal.me/mikepenz)

# Contributors

This free, open source software was also made possible by a group of volunteers that put many hours of hard work into it. See the [CONTRIBUTORS.md](CONTRIBUTORS.md) file for details.

# License

    Copyright 2017 Mike Penz

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

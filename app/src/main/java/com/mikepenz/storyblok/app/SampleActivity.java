package com.mikepenz.storyblok.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.mikepenz.aboutlibraries.Libs;
import com.mikepenz.aboutlibraries.LibsBuilder;
import com.mikepenz.fastadapter.commons.adapters.FastItemAdapter;
import com.mikepenz.material_design_iconic_typeface_library.MaterialDesignIconic;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.storyblok.Storyblok;
import com.mikepenz.storyblok.app.items.SimpleItem;
import com.mikepenz.storyblok.model.Datasource;
import com.mikepenz.storyblok.model.Link;
import com.mikepenz.storyblok.model.Result;
import com.mikepenz.storyblok.model.Story;
import com.mikepenz.storyblok.model.Tag;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class SampleActivity extends AppCompatActivity {

    //our rv
    RecyclerView mRecyclerView;
    //save our header or result
    private Drawer mResult = null;
    //save our FastAdapter
    private FastItemAdapter<SimpleItem> mFastAdapter;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        //create the activity
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // Handle Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Create the drawer
        mResult = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withHasStableIds(true)
                .withSavedInstance(savedInstanceState)
                .withShowDrawerOnFirstLaunch(true)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.open_source).withSelectable(false).withIdentifier(100).withIcon(MaterialDesignIconic.Icon.gmi_github)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if (drawerItem != null) {
                            Intent intent = null;
                            if (drawerItem.getIdentifier() == 100) {
                                intent = new LibsBuilder()
                                        .withFields(R.string.class.getFields())
                                        .withActivityTitle(getString(R.string.open_source))
                                        .withActivityStyle(Libs.ActivityStyle.LIGHT_DARK_TOOLBAR)
                                        .withAboutIconShown(true)
                                        .withVersionShown(true)
                                        .withAboutVersionShown(true)
                                        .intent(SampleActivity.this);
                            }
                            if (intent != null) {
                                SampleActivity.this.startActivity(intent);
                            }
                        }
                        return false;
                    }
                })
                .withSelectedItemByPosition(-1)
                .build();

        //create our FastAdapter which will manage everything
        mFastAdapter = new FastItemAdapter<>();

        //configure our fastAdapter
        //get our recyclerView and do basic setup
        mRecyclerView = (RecyclerView) findViewById(R.id.rv);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mFastAdapter);
        mFastAdapter.withSavedInstanceState(savedInstanceState);

        //init our Storyblok
        Storyblok client = Storyblok.init(BuildConfig.STORYBLOK_TOKEN);

        //get all stories
        client.getStories(null, null, null, 100, 0, new Storyblok.StoryblokCallback<List<Story>>() {
            @Override
            public void onFailure(IOException exception, String response) {
            }

            @Override
            public void onResponse(final Result<List<Story>> result) {
                Log.e("Storyblok sample", result.toString());
                if (result.getResult() != null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            for (Story story : result.getResult()) {
                                mFastAdapter.add(new SimpleItem().withName("getStories: " + story.getUuid()).withDescription(story.getName()));
                            }
                        }
                    });
                }
            }
        });

        //get story for a specific slug
        client.getStory("pictures/first-image-ever", new Storyblok.StoryblokCallback<Story>() {
            @Override
            public void onFailure(IOException exception, String response) {
            }

            @Override
            public void onResponse(final Result<Story> result) {
                Log.e("Storyblok sample", result.toString());
                if (result.getResult() != null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mFastAdapter.add(new SimpleItem().withName("getStory: " + result.getResult().getUuid()).withDescription(result.getResult().getName()));
                        }
                    });
                }
            }
        });

        //get tags
        client.getTags(null, new Storyblok.StoryblokCallback<List<Tag>>() {
            @Override
            public void onFailure(IOException exception, String response) {
            }

            @Override
            public void onResponse(final Result<List<Tag>> result) {
                Log.e("Storyblok sample", result.toString());
                if (result.getResult() != null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            for (Tag tag : result.getResult()) {
                                mFastAdapter.add(new SimpleItem().withName("getTags: " + tag.getName()).withDescription(tag.getTaggingsCount() + ""));
                            }
                        }
                    });
                }
            }
        });

        //get links
        client.getLinks(new Storyblok.StoryblokCallback<Map<String, Link>>() {
            @Override
            public void onFailure(IOException exception, String response) {
            }

            @Override
            public void onResponse(final Result<Map<String, Link>> result) {
                Log.e("Storyblok sample", result.toString());
                if (result.getResult() != null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            for (Map.Entry<String, Link> e : result.getResult().entrySet()) {
                                mFastAdapter.add(new SimpleItem().withName("getLinks: " + e.getKey()).withDescription(e.getValue().getName()));
                            }
                        }
                    });
                }
            }
        });

        //get datasources
        client.getDatasource(null, new Storyblok.StoryblokCallback<List<Datasource>>() {
            @Override
            public void onFailure(IOException exception, String response) {
            }

            @Override
            public void onResponse(final Result<List<Datasource>> result) {
                Log.e("Storyblok sample", result.toString());
                if (result.getResult() != null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            for (Datasource datasource : result.getResult()) {
                                mFastAdapter.add(new SimpleItem().withName("getDatasources: " + datasource.getId()).withDescription(datasource.getName()));
                            }
                        }
                    });
                }
            }
        });
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //add the values which need to be saved from the drawer to the bundle
        outState = mResult.saveInstanceState(outState);
        //add the values which need to be saved from the adapter to the bundel
        outState = mFastAdapter.saveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        //handle the back press :D close the drawer first and if the drawer is closed close the activity
        if (mResult != null && mResult.isDrawerOpen()) {
            mResult.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }
}

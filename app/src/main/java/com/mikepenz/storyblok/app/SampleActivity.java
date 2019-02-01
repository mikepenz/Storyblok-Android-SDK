package com.mikepenz.storyblok.app;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.adapters.ItemAdapter;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.storyblok.Storyblok;
import com.mikepenz.storyblok.app.items.SimpleItem;
import com.mikepenz.storyblok.model.Datasource;
import com.mikepenz.storyblok.model.Link;
import com.mikepenz.storyblok.model.Story;
import com.mikepenz.storyblok.model.Tag;

import java.util.Map;

public class SampleActivity extends AppCompatActivity {

    //our rv
    RecyclerView mRecyclerView;
    //save our header or result
    private Drawer mResult = null;
    //save our FastAdapter
    private FastAdapter<SimpleItem> mFastAdapter;
    private ItemAdapter<SimpleItem> mItemAdapter;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        //create the activity
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // Handle Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Create the drawer
        /*
        // TODO enable drawer again with
        mResult = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withHasStableIds(true)
                .withSavedInstance(savedInstanceState)
                .withShowDrawerOnFirstLaunch(true)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.open_source).withSelectable(false).withIdentifier(100).withIcon(MaterialDesignIconic.Icon.gmi_github)
                )
                .withOnDrawerItemClickListener((view, position, drawerItem) -> {
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
                })
                .withSelectedItemByPosition(-1)
                .build();
                */

        //create our FastAdapter which will manage everything
        mItemAdapter = new ItemAdapter<>();
        mFastAdapter = FastAdapter.with(mItemAdapter);

        //configure our fastAdapter
        //get our recyclerView and do basic setup
        mRecyclerView = findViewById(R.id.rv);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mFastAdapter);
        mFastAdapter.withSavedInstanceState(savedInstanceState);

        //init our Storyblok
        Storyblok client = Storyblok.init(BuildConfig.STORYBLOK_TOKEN);

        //get all stories
        client.getStories(null, null, null, 100, 0, result -> {
            Log.e("Storyblok sample", result.toString());
            if (result.getResult() != null) {
                runOnUiThread(() -> {
                    for (Story story : result.getResult()) {
                        mItemAdapter.add(new SimpleItem().withName("getStories: " + story.getUuid()).withDescription(story.getName()));
                    }
                });
            }
        }, (exception, response) -> {
        });

        //get story for a specific slug
        client.getStory("pictures/first-image-ever", result -> {
            Log.e("Storyblok sample", result.toString());
            if (result.getResult() != null) {
                runOnUiThread(() -> mItemAdapter.add(new SimpleItem().withName("getStory: " + result.getResult().getUuid()).withDescription(result.getResult().getName())));
            }
        }, (exception, response) -> {
        });

        //get tags
        client.getTags(null, result -> {
            Log.e("Storyblok sample", result.toString());
            if (result.getResult() != null) {
                runOnUiThread(() -> {
                    for (Tag tag : result.getResult()) {
                        mItemAdapter.add(new SimpleItem().withName("getTags: " + tag.getName()).withDescription(tag.getTaggingsCount() + ""));
                    }
                });
            }
        }, (exception, response) -> {
        });

        //get links
        client.getLinks(result -> {
            Log.e("Storyblok sample", result.toString());
            if (result.getResult() != null) {
                runOnUiThread(() -> {
                    for (Map.Entry<String, Link> e : result.getResult().entrySet()) {
                        mItemAdapter.add(new SimpleItem().withName("getLinks: " + e.getKey()).withDescription(e.getValue().getName()));
                    }
                });
            }
        }, (exception, response) -> {
        });

        //get datasources
        client.getDatasource(null, result -> {
            Log.e("Storyblok sample", result.toString());
            if (result.getResult() != null) {
                runOnUiThread(() -> {
                    for (Datasource datasource : result.getResult()) {
                        mItemAdapter.add(new SimpleItem().withName("getDatasources: " + datasource.getId()).withDescription(datasource.getName()));
                    }
                });
            }
        }, (exception, response) -> {
        });
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //add the values which need to be saved from the drawer to the bundle
        if (mResult != null) {
            outState = mResult.saveInstanceState(outState);
        }
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

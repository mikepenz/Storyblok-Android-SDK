package com.mikepenz.storyblok.app

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.LinearLayoutManager
import com.mikepenz.aboutlibraries.Libs
import com.mikepenz.aboutlibraries.LibsBuilder
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.materialdrawer.AccountHeaderBuilder
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.storyblok.Storyblok
import com.mikepenz.storyblok.app.items.SimpleItem
import com.mikepenz.storyblok.model.*
import kotlinx.android.synthetic.main.activity_main.*
import java.io.IOException

class SampleActivity : AppCompatActivity() {

    //save our header or result
    private lateinit var result: Drawer
    //save our FastAdapter
    private lateinit var fastAdapter: FastAdapter<SimpleItem>
    private lateinit var itemAdapter: ItemAdapter<SimpleItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        //create the activity
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        // Handle Toolbar
        setSupportActionBar(toolbar)

        val header = AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.header_background)
                .build()

        //Create the drawer
        result = DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withAccountHeader(header)
                .withHasStableIds(true)
                .withSavedInstance(savedInstanceState)
                .withShowDrawerOnFirstLaunch(true)
                .addDrawerItems(
                        PrimaryDrawerItem().withName(R.string.storyblok).withSelectable(false).withIdentifier(10).withIcon(AppCompatResources.getDrawable(this, R.drawable.ico_storyblok)).withIconTintingEnabled(true)
                        //PrimaryDrawerItem().withName(R.string.open_source).withSelectable(false).withIdentifier(100).withIcon(MaterialDesignIconic.Icon.gmi_github)
                )
                .withOnDrawerItemClickListener { _, _, drawerItem ->
                    if (drawerItem?.identifier == 10L) {
                        startActivity(Intent(Intent.ACTION_VIEW).apply { data = Uri.parse("https://www.storyblok.com/") })
                    } else if (drawerItem?.identifier == 100L) {
                        val intent = LibsBuilder()
                                .withFields(R.string::class.java.fields)
                                .withActivityTitle(getString(R.string.open_source))
                                .withActivityStyle(Libs.ActivityStyle.LIGHT_DARK_TOOLBAR)
                                .withAboutIconShown(true)
                                .withVersionShown(true)
                                .withAboutVersionShown(true)
                                .intent(this@SampleActivity)
                        this@SampleActivity.startActivity(intent)
                    }
                    false
                }
                .withSelectedItemByPosition(-1)
                .build()

        //create our FastAdapter which will manage everything
        itemAdapter = ItemAdapter()
        fastAdapter = FastAdapter.with(itemAdapter)

        //configure our fastAdapter
        //get our recyclerView and do basic setup
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = fastAdapter
        fastAdapter.withSavedInstanceState(savedInstanceState)

        //init our Storyblok
        val client = Storyblok.init(BuildConfig.STORYBLOK_TOKEN)

        //get all stories
        client.getStories(null, null, null, 100, 0, object : Storyblok.SuccessCallback<List<Story>> {
            override fun onResponse(result: Result<List<Story>>) {
                Log.e("Storyblok sample", result.toString())

                result.result?.let {
                    runOnUiThread {
                        for (story in it) {
                            itemAdapter.add(SimpleItem().withName("getStories: " + story.uuid).withDescription(story.name))
                        }
                    }
                }
            }
        }, object : Storyblok.ErrorCallback {
            override fun onFailure(exception: IOException?, response: String?) {
                // empty
            }
        })

        //get story for a specific slug
        client.getStory("pictures/first-image-ever", object : Storyblok.SuccessCallback<Story> {
            override fun onResponse(result: Result<Story>) {
                Log.e("Storyblok sample", result.toString())
                result.result?.let {
                    runOnUiThread { itemAdapter.add(SimpleItem().withName("getStory: " + it.uuid).withDescription(it.name)) }
                }
            }
        }, object : Storyblok.ErrorCallback {
            override fun onFailure(exception: IOException?, response: String?) {
                // empty
            }
        })

        //get tags
        client.getTags(null, object : Storyblok.SuccessCallback<List<Tag>> {
            override fun onResponse(result: Result<List<Tag>>) {
                Log.e("Storyblok sample", result.toString())
                result.result?.let {
                    runOnUiThread {
                        for (tag in it) {
                            itemAdapter.add(SimpleItem().withName("getTags: " + tag.name).withDescription(tag.taggingsCount.toString()))
                        }
                    }
                }
            }
        }, object : Storyblok.ErrorCallback {
            override fun onFailure(exception: IOException?, response: String?) {
                // empty
            }
        })

        //get links
        client.getLinks(object : Storyblok.SuccessCallback<Map<String, Link>> {
            override fun onResponse(result: Result<Map<String, Link>>) {
                Log.e("Storyblok sample", result.toString())
                result.result?.let {
                    runOnUiThread {
                        for ((key, value) in it.entries) {
                            itemAdapter.add(SimpleItem().withName("getLinks: $key").withDescription(value.name))
                        }
                    }
                }
            }
        }, object : Storyblok.ErrorCallback {
            override fun onFailure(exception: IOException?, response: String?) {
                // empty
            }
        })

        //get datasources
        client.getDatasource(null, object : Storyblok.SuccessCallback<List<Datasource>> {
            override fun onResponse(result: Result<List<Datasource>>) {
                Log.e("Storyblok sample", result.toString())
                result.result?.let {
                    runOnUiThread {
                        for (datasource in it) {
                            itemAdapter.add(SimpleItem().withName("getDatasources: " + datasource.id).withDescription(datasource.name))
                        }
                    }
                }
            }
        }, object : Storyblok.ErrorCallback {
            override fun onFailure(exception: IOException?, response: String?) {
                // empty
            }
        })
    }


    override fun onSaveInstanceState(state: Bundle?) {
        var outState = state
        //add the values which need to be saved from the drawer to the bundle
        outState = result.saveInstanceState(outState)
        //add the values which need to be saved from the adapter to the bundel
        outState = fastAdapter.saveInstanceState(outState)
        super.onSaveInstanceState(outState)
    }

    override fun onBackPressed() {
        //handle the back press :D close the drawer first and if the drawer is closed close the activity
        if (result.isDrawerOpen) {
            result.closeDrawer()
        } else {
            super.onBackPressed()
        }
    }
}

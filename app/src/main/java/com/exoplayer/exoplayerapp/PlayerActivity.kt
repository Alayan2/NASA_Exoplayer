/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
* limitations under the License.
 */
package com.exoplayer.exoplayerapp

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.media3.common.MediaItem
import androidx.media3.common.util.Util
import androidx.media3.exoplayer.ExoPlayer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.exoplayer.exoplayerapp.databinding.ActivityPlayerBinding
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


//TODO: Add intents for new activity and send url string
/**
 * A fullscreen activity to play audio or video streams.
 */
class PlayerActivity : AppCompatActivity() {

    private var player: ExoPlayer? = null
    private var playWhenReady = true
    private var currentItem = 0
    private var playbackPosition = 0L
    lateinit var url: String
    lateinit var keyword: String
    lateinit var desc: String
    lateinit var title: String
    var movieList: Collection? = null
    lateinit var recyclerView: RecyclerView
    lateinit var recyclerAdapter: RecyclerAdapter

    //lazy(..) is a kotlin delegate for lazy initializing a value the first time it is used.
    private val viewBinding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityPlayerBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)
        url = intent.getStringExtra("url").toString()
        keyword = intent.getStringExtra("keywords").toString()
        desc = intent.getStringExtra("desc").toString()
        title = intent.getStringExtra("title").toString()

        var open = false;

        //setting the video player height programmitically by screen width
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)

        var width = displayMetrics.widthPixels
        var height = displayMetrics.heightPixels

        val videoHeight = 148
        val videoWidth = 264
        val screenWidth = width
        viewBinding.videoView.layoutParams.height =
            (videoHeight.toFloat() / videoWidth.toFloat() * screenWidth.toFloat()).toInt()
        viewBinding.videoView.setLayoutParams(viewBinding.videoView.layoutParams)

        viewBinding.exoDescription.setText(desc)

        if (viewBinding.exoDescription.text.length > 300) {

            viewBinding.seeMore.visibility = View.VISIBLE;

            val seeMore = viewBinding.seeMore as TextView
            seeMore.setOnClickListener {
               if(!open) {
                   viewBinding.exoDescription.maxLines = 30;
                   viewBinding.seeMore.setText("Collapse");
                   seeMore.setOnClickListener {
                       viewBinding.exoDescription.maxLines = 5;
                       viewBinding.seeMore.setText("See More");
                       open = false;
                   }
               }
            }
        }

        viewBinding.exoTitle.setText(title)

        val layoutManager = LinearLayoutManager(this)
        viewBinding.recyclerview.setLayoutManager(layoutManager)
        recyclerAdapter = RecyclerAdapter(applicationContext, movieList)
        viewBinding.recyclerview.setAdapter(recyclerAdapter)

        val okHttpClient = OkHttpClient().newBuilder().addInterceptor { chain ->
            val originalRequest = chain.request()
            val builder = originalRequest.newBuilder()
            val newRequest = builder.build()
            chain.proceed(newRequest)
        }.build()

        val apiService = ApiClient.getClient(okHttpClient).create(
            ApiInterface::class.java
        )

        Log.d("Playerkeywords!!!!!!!!!", keyword)
        val call = apiService.getMovies(keyword, "video")

        call.enqueue(object : Callback<Collection?> {
            override fun onResponse(call: Call<Collection?>, response: Response<Collection?>) {
                if (response.isSuccessful) {
                    movieList = response.body()!!
                    Log.d("TAG",
                        "Response = we did it johnny"
                    )
                    recyclerAdapter.setMovieList(movieList)
                }
            }



            override fun onFailure(call: Call<Collection?>, t: Throwable) {
                Log.d("TAG", "Response = $t")
            }
        })
    }

    private fun initializePlayer() {
        player = ExoPlayer.Builder(this)
                .build()
                .also { exoPlayer ->
                    viewBinding.videoView.player = exoPlayer
                    viewBinding.videoView.setShutterBackgroundColor(Color.TRANSPARENT);
                    viewBinding.videoView.setKeepContentOnPlayerReset(true);

                    val mediaItem =
                        MediaItem.fromUri(url)//the content to play sourced from strings.xml
                    exoPlayer.setMediaItem(mediaItem)
                    exoPlayer.playWhenReady =
                        playWhenReady //playWhenReady tells the player whether to start playing as soon as all resources for playback have been acquired.
                    exoPlayer.seekTo(
                        currentItem,
                        playbackPosition
                    ) //seekTo tells the player to seek to a certain position within a specific media item.
                    exoPlayer.prepare() //prepare tells the player to acquire all the resources required for playback
                }
    }

    public override fun onStart() {
        super.onStart()
        if (Util.SDK_INT > 23) {
            initializePlayer()
        }
    }

    public override fun onResume() {
        super.onResume()
        hideSystemUi()
        if ((Util.SDK_INT <= 23 || player == null)) {
            initializePlayer()
        }
    }

    @SuppressLint("InlinedApi")
    private fun hideSystemUi() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, viewBinding.videoView).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    public override fun onPause() {
        super.onPause()
        if (Util.SDK_INT <= 23) {
            releasePlayer()
        }
    }


    public override fun onStop() {
        super.onStop()
        if (Util.SDK_INT > 23) {
            releasePlayer()
        }
    }

    private fun releasePlayer() {
        player?.let { exoPlayer ->
            playbackPosition = exoPlayer.currentPosition
            currentItem = exoPlayer.currentMediaItemIndex
            playWhenReady = exoPlayer.playWhenReady
            exoPlayer.release()
        }
        player = null
    }
}
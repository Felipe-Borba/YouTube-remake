package com.example.youtuberemake

import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.youtuberemake.databinding.ActivityMainBinding
import com.google.gson.GsonBuilder
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var videoAdapter: VideoAdapter
    private lateinit var binding: ActivityMainBinding

    private lateinit var view_layer: View
    private lateinit var motion_container: MotionLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        view_layer = findViewById(R.id.view_layer)
        motion_container = findViewById(R.id.motion_container)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = ""

        val videos = mutableListOf<Video>()
        videoAdapter = VideoAdapter(videos) { video ->
            showOverlayView(video)
        }
        view_layer.alpha = 0f

        binding.rvMain.layoutManager = LinearLayoutManager(this)
        binding.rvMain.adapter = videoAdapter

        CoroutineScope(Dispatchers.IO).launch {
            val res = async { getVideo() }
            val listVideo = res.await()
            withContext(Dispatchers.Main) {
                listVideo?.let {
                    videos.clear()
                    videos.addAll(listVideo.data)
                    videoAdapter.notifyDataSetChanged()
                    binding.motionContainer.removeView(binding.progressRecycler)
//                    binding.progressRecycler.visibility = View.GONE
                }
            }
        }


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun showOverlayView(video: Video) {
        view_layer.animate().apply {
            duration = 400
            alpha(0.5f)
        }

        motion_container.setTransitionListener(object : MotionLayout.TransitionListener {
            override fun onTransitionTrigger(
                motionLayout: MotionLayout?,
                triggerId: Int,
                positive: Boolean,
                progress: Float
            ) {
//                println("Transition Trigger $triggerId $positive $progress")
            }

            override fun onTransitionStarted(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int
            ) {
//                println("Transition Started $startId $endId")
            }

            override fun onTransitionChange(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int,
                progress: Float
            ) {
                if (progress > 0.5f)
                    view_layer.alpha = 1.0f - progress
                else
                    view_layer.alpha = 0.5f
            }

            override fun onTransitionCompleted(
                motionLayout: MotionLayout?,
                currentId: Int
            ) {
//                println("Transition Completed $currentId")
            }
        })

        findViewById<ImageView>(R.id.video_player).visibility = View.GONE

        val detailAdapter = VideoDetailAdapter(videos())
        val rv_similar = findViewById<RecyclerView>(R.id.rv_similar)
        rv_similar.layoutManager = LinearLayoutManager(this)
        rv_similar.adapter = detailAdapter

        findViewById<TextView>(R.id.content_channel).text = video.publisher.name
        findViewById<TextView>(R.id.content_title).text = video.title
        Picasso.get().load(video.publisher.pictureProfileUrl).into(findViewById<ImageView>(R.id.img_channel))

        detailAdapter.notifyDataSetChanged()
    }

    private fun getVideo(): ListVideo? {
        val client = OkHttpClient.Builder()
            .build()

        val request = Request.Builder()
            .get()
            .url("https://tiagoaguiar.co/api/youtube-videos")
            .build()

        return try {
            //TODO connection pending forever issue
            val response = client.newCall(request).execute()

            if (response.isSuccessful) {
                GsonBuilder().create()
                    .fromJson(response.body()?.string(), ListVideo::class.java)
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
}
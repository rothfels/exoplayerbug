package com.example.exoplayerbug

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import com.example.exoplayerbug.ui.theme.ExoplayerbugTheme
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout.RESIZE_MODE_ZOOM
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import java.io.File

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ExoplayerbugTheme {
                Surface(color = MaterialTheme.colors.background) {
                    val brokenUrl = "https://user-images.githubusercontent.com/1095573/117829374-8f257f00-b227-11eb-8660-c3bfa8e52dc4.mp4"
                    val workingUrl = "https://user-images.githubusercontent.com/1095573/117829535-b4b28880-b227-11eb-9a27-da2aec3f669f.mp4"
                    VideoPlayer(url = brokenUrl)
                }
            }
        }
    }
}


@Composable
fun VideoPlayer(url: String) {
    val context = LocalContext.current

    val exoPlayer = remember {
        SimpleExoPlayer.Builder(context).build()
    }

    LaunchedEffect(url) {
        val dataSourceFactory: DataSource.Factory = DefaultDataSourceFactory(context,
            Util.getUserAgent(context, context.packageName))


        val source = ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(Uri.parse(url))

        exoPlayer.prepare(source)
    }

    DisposableEffect(url) {
        object : DisposableEffectResult {
            override fun dispose() {
                exoPlayer.stop()
            }
        }
    }

    AndroidView(
        { context ->
            PlayerView(context).apply {
                useController = false
                exoPlayer.playWhenReady = true
                resizeMode = RESIZE_MODE_ZOOM
                player = exoPlayer
            }
        }
    )
}
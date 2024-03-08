package com.example.youtuberemake

import java.text.SimpleDateFormat
import java.util.*

data class Video(
    val id: String,
    val thumbnailUrl: String,
    val title: String,
    val publishedAt: Date,
    val viewsCount: Long,
    val viewsCountLabel: String,
    val duration: Int,
    val videoUrl: String,
    val publisher: Publisher
)

data class Publisher(
    val id: String,
    val name: String,
    val pictureProfileUrl: String
)

data class ListVideo(
    val status: Int,
    val data: List<Video>
)
//{"status": 200, "data": [{"id": "UVpKBHO2fMg", "thumbnailUrl": "https://img.youtube.com/vi/UVpKBHO2fMg/maxresdefault.jpg", "title": "Entrevista com Marlon Wayans | The Noite (14/08/19)", "viewsCount": 742497, "publishedAt": "2019-08-15", "viewsCountLabel": "7M", "duration": 1886, "videoUrl": "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4", "publisher": {"id": "sbtthenoite", "name": "The Noite com Danilo Gentili", "pictureProfileUrl": "https://yt3.ggpht.com/a/AGF-l7_3BYlSlp94WOjGe1UECUCdb73qRJVFH_t9Tw=s48-c-k-c0xffffffff-no-rj-mo"}}, {"id": "PlYUZU0H5go", "thumbnailUrl": "https://img.youtube.com/vi/cuau8E6t2QU/maxresdefault.jpg", "title": "Relembrando Steve Jobs", "viewsCount": 1703, "publishedAt": "2014-08-15", "viewsCountLabel": "1k", "duration": 194, "videoUrl": "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4", "publisher": {"id": "UCrWWMZ6GVOM5zqYAUI44XXg", "name": "Tiago Aguiar", "pictureProfileUrl": "https://yt3.ggpht.com/ytc/AKedOLT2VtZ3n30tTpDyjAoZGl44EfHhajN1Zy5LYm3iiA=s88-c-k-c0x00ffffff-no-rj"}}, {"id": "-y1HhAlAOTs", "thumbnailUrl": "https://img.youtube.com/vi/-y1HhAlAOTs/maxresdefault.jpg", "title": "MARRIAGE STORY Official Trailer (2019) Scarlett Johansson, Adam Driver Netflix Movie HD", "viewsCount": 1136717, "publishedAt": "2019-08-21", "viewsCountLabel": "1M", "duration": 160, "videoUrl": "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4", "publisher": {"id": "movietrailers", "name": "ONE Media", "pictureProfileUrl": "https://yt3.ggpht.com/a/AGF-l7_0zGH3p2Yu7hMMZVAjwS7H8Ct6qu_vNmGj=s48-c-k-c0xffffffff-no-rj-mo"}}]}

class VideoBuilder {
    var id: String = ""
    var thumbnailUrl: String = ""
    var title: String = ""
    var publishedAt: Date = Date()
    var viewsCount: Long = 0
    var viewsCountLabel: String = ""
    var duration: Int = 0
    var videoUrl: String = ""
    var publisher: Publisher = PublisherBuilder().build()

    fun build(): Video = Video(
        id, thumbnailUrl, title, publishedAt, viewsCount, viewsCountLabel, duration, videoUrl, publisher
    )

    fun publisher(block: PublisherBuilder.() -> Unit): Publisher = PublisherBuilder().apply(block).build()
}

class PublisherBuilder {
    var id: String = ""
    var name: String = ""
    var pictureProfileUrl: String = ""

    fun build(): Publisher = Publisher(id, name, pictureProfileUrl)
}


// DSL
fun video(block: VideoBuilder.() -> Unit): Video = VideoBuilder().apply(block).build()


fun videos(): List<Video> {
    return arrayListOf(video {
        id = "UVpKBHO2fMg"
        thumbnailUrl = "https://img.youtube.com/vi/UVpKBHO2fMg/maxresdefault.jpg"
        title = "Entrevista com Marlon Wayans | The Noite (14/08/19)"
        publishedAt = "2019-08-15".toDate()
        viewsCount = 742_497
        duration = 1886
        publisher {
            id = "sbtthenoite"
            name = "The Noite com Danilo Gentili"
            pictureProfileUrl =
                "https://yt3.ggpht.com/a/AGF-l7_3BYlSlp94WOjGe1UECUCdb73qRJVFH_t9Tw=s48-c-k-c0xffffffff-no-rj-mo"
        }
    }, video {
        id = "PlYUZU0H5go"
        thumbnailUrl = "https://img.youtube.com/vi/PlYUZU0H5go/maxresdefault.jpg"
        title = "LAST CHRISTMAS Official Trailer (2019) Emilia Clarke Movie"
        publishedAt = "2019-08-28".toDate()
        viewsCount = 5_468_366
        duration = 194
        publisher {
            id = "UCpJN7kiUkDrH11p0GQhLyFw"
            name = "Movie Trailer Source"
            pictureProfileUrl =
                "https://yt3.ggpht.com/a/AGF-l7_Qmltcncwt0z_XzAzjxnuE5gVV9uj7zThg2w=s48-c-k-c0xffffffff-no-rj-mo"
        }
    })
}

fun Date.formatted(): String = SimpleDateFormat("d MMM yyyy", Locale("pt", "BR")).format(this)

fun String.toDate(): Date = SimpleDateFormat("yyyy-mm-dd", Locale("pt", "BR")).parse(this)

fun Long.formatTime(): String {
    println(this)
    val minutes = this / 1000 / 60
    val seconds = this / 1000 % 60
    return String.format("%02d:%02d", minutes, seconds)
}
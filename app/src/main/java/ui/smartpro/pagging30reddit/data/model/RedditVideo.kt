package ui.smartpro.pagging30reddit.data.model

data class RedditVideo(
    val bitrate_kbps: Int,
    val dash_url: String,
    val duration: Int,
    val fallback_url: String,
    val height: Int,
    val hls_url: String,
    val is_gif: Boolean,
    val scrubber_media_url: String,
    val transcoding_status: String,
    val width: Int
)
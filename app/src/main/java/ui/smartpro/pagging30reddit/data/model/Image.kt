package ui.smartpro.pagging30reddit.data.model

data class Image(
    val id: String,
    val resolutions: List<Resolution>,
    val source: Source,
    val variants: Variants
)
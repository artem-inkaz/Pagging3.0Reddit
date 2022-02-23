package ui.smartpro.pagging30reddit.data.model

data class Data(
    val after: String,
    val before: String,
    val children: List<Children>,
    val dist: Int,
    val geo_filter: Any,
    val modhash: String
)
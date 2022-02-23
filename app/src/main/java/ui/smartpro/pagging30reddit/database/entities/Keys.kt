package ui.smartpro.pagging30reddit.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "keys")
data class Keys(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val after: String?,
    val before: String?
)

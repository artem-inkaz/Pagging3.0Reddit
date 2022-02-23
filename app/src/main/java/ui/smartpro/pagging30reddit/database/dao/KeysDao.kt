package ui.smartpro.pagging30reddit.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ui.smartpro.pagging30reddit.database.entities.Keys

@Dao
interface KeysDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveRedditKeys(redditKey: Keys)

    @Query("SELECT * FROM keys ORDER BY id DESC")
    suspend fun getRedditKeys(): List<Keys>
}
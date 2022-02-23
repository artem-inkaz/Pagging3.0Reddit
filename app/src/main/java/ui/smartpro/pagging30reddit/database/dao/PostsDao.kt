package ui.smartpro.pagging30reddit.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ui.smartpro.pagging30reddit.database.entities.Posts

@Dao
interface PostsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun savePosts(redditPosts: List<Posts>)

    @Query("SELECT * FROM posts")
    fun getPosts(): PagingSource<Int, Posts>
}
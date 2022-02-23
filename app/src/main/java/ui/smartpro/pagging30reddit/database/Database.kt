package ui.smartpro.pagging30reddit.database

import androidx.room.Database
import androidx.room.RoomDatabase
import ui.smartpro.pagging30reddit.database.dao.KeysDao
import ui.smartpro.pagging30reddit.database.dao.PostsDao

@Database(entities = [],
version =1,
exportSchema = false)
abstract class Database:RoomDatabase() {

    abstract fun keysDao():  KeysDao
    abstract fun postsDao(): PostsDao
}
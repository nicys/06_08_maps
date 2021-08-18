package ru.netology.nmedia.ui.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.netology.nmedia.ui.MarkerEntity
import ru.netology.nmedia.ui.dao.MarkerDao

@Database(entities = [MarkerEntity::class], version = 1, exportSchema = false)

abstract class AppDb: RoomDatabase() {
    abstract fun markerDao(): MarkerDao

    companion object{
        @Volatile
        private var instance: AppDb? = null

        fun getInstance(context: Context): AppDb {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context, AppDb::class.java, "app.db")
                .build()
    }
}
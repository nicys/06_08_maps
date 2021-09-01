package ru.netology.nmedia.ui.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.netology.nmedia.ui.dao.MarkerDao
import ru.netology.nmedia.ui.entity.MarkerEntity

@Database(entities = [MarkerEntity::class], version = 1, exportSchema = false)
abstract class AppDb : RoomDatabase() {

    abstract fun markerDao(): MarkerDao

    companion object {
        @Volatile
        private var instance: AppDb? = null

        fun getInstance(context: Context): AppDb {
            return instance ?: synchronized(this) {
                instance ?: buildDataBase(context).also { instance = it}
            }
        }

        private fun buildDataBase(context: Context) =
            Room.databaseBuilder(context, AppDb::class.java, "app.db")
                .build()
    }
}
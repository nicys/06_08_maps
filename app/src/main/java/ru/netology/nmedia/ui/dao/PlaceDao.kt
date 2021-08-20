package ru.netology.nmedia.ui.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.netology.nmedia.ui.entity.PlacesEntity

@Dao
interface PlaceDao {

    @Query("SELECT * FROM PlacesEntity ORDER BY id DESC")
    fun getAll(): LiveData<List<PlacesEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(marker: PlacesEntity)

    @Query("DELETE FROM PlacesEntity WHERE id = :id")
    fun deleteById(id: Short)
}
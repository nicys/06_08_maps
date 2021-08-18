package ru.netology.nmedia.ui.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.netology.nmedia.ui.MarkerEntity

@Dao
interface MarkerDao {

    @Query("SELECT * FROM MarkerEntity ORDER BY ID DESC")
    fun getAll(): LiveData<List<MarkerEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(markerEntity: MarkerEntity)

    @Query("DELETE FROM MarkerEntity WHERE id=:id")
    fun removedById(id: Short)
}
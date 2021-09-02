@file:Suppress("AndroidUnresolvedRoomSqlReference")

package ru.netology.nmedia.ui.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.netology.nmedia.ui.entity.MarkerEntity

@Dao
interface MarkerDao {

    @Query("SELECT * FROM MarkerEntity ORDER BY id DESC")
    fun getAll(): Flow<List<MarkerEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(marker: MarkerEntity)

    @Query("DELETE FROM MarkerEntity where id = :id")
    suspend fun removedById(id: Int)
}
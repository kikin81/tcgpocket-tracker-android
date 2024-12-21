package us.kikin.android.ptp.data.source.local

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CardDao {
    @Query("SELECT * FROM Cards")
    fun getAll(): List<CardEntity>

    @Query("SELECT * FROM Cards")
    fun observeAll(): Flow<List<CardEntity>>
}

package us.kikin.android.ptp.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [CardEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cardDao(): CardDao
}

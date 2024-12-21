package us.kikin.android.ptp.di

import android.content.Context
import androidx.room.Room
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import us.kikin.android.ptp.data.CardRepository
import us.kikin.android.ptp.data.DefaultCardRepository
import us.kikin.android.ptp.data.source.local.AppDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun bindCardRepository(repository: DefaultCardRepository): CardRepository
}

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "pokemon_cards.db"
        )
            .createFromAsset("database/pokemon_cards.db")
            .build()
    }

    @Provides
    fun provideCardDao(database: AppDatabase) = database.cardDao()
}

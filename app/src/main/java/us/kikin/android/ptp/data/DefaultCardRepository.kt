package us.kikin.android.ptp.data

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import us.kikin.android.ptp.data.source.local.CardDao
import us.kikin.android.ptp.di.DefaultDispatcher
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultCardRepository @Inject constructor(
    private val localDataSource: CardDao,
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher,
) : CardRepository {

    override fun getCardsStream(): Flow<List<Card>> {
        return localDataSource.observeAll().map { tasks ->
            withContext(dispatcher) {
                tasks.toDomain()
            }
        }
    }

    override fun getCards(): List<Card> {
        return localDataSource.getAll().toDomain()
    }

    override fun getCardByIdStream(cardId: String): Flow<Card> {
        return localDataSource.observeById(cardId).map { it.toDomain() }
    }
}

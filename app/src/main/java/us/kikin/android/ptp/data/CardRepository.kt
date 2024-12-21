package us.kikin.android.ptp.data

import kotlinx.coroutines.flow.Flow

interface CardRepository {

    fun getCardsStream(): Flow<List<Card>>

    fun getCards(): List<Card>

    fun getCardByIdStream(cardId: String): Flow<Card>
}

package us.kikin.android.ptp.data

import kotlinx.coroutines.flow.Flow

interface CardRepository {

    fun getCardsStream(): Flow<List<Card>>

    fun getCards(): List<Card>
}

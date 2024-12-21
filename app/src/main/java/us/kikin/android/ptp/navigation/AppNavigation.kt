package us.kikin.android.ptp.navigation

import kotlinx.serialization.Serializable
import us.kikin.android.ptp.cards.CardsFilterType

@Serializable
data class CardListDestination(
    val filterType: CardsFilterType = CardsFilterType.ALL_CARDS,
    val userMessage: Int? = null
)

@Serializable
data class CardDetailDestination(
    val cardId: String,
    val userMessage: Int? = null
)

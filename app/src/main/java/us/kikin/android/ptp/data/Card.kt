package us.kikin.android.ptp.data

/**
 * Immutable model class for a Pokemon card.
 */
data class Card(
    val id: String,
    val name: String,
    val hp: String? = null,
    val image: String? = null,
    val cardType: String? = null,
    val evolutionType: String? = null,
    val weakness: String? = null,
    val retreat: Int? = null,
    val rarity: String? = null,
    val pack: String? = null,
    val artist: String? = null,
    val craftingCost: Int? = null,
    val setDetails: String? = null,
)

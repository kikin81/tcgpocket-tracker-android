package us.kikin.android.ptp.data

/**
 * Immutable model class for a Pokemon card.
 */
data class Card(
    val id: String,
    val name: String,
    val hp: String? = null,
    val image: String? = null,
)

package us.kikin.android.ptp.data

import us.kikin.android.ptp.data.source.local.CardEntity

fun Card.toLocal() = CardEntity(
    id = id,
    name = name,
    hp = hp,
    image = image,
    cardType = null,
    evolutionType = null,
    weakness = null,
    retreat = null,
    rarity = null,
    fullart = null,
    ex = null,
    setDetails = null,
    pack = null,
    artist = null,
    craftingCost = null
)

fun CardEntity.toDomain() = Card(
    id = id,
    name = name,
    hp = hp,
    image = image
)

fun List<Card>.toLocal() = map(Card::toLocal)

fun List<CardEntity>.toDomain() = map(CardEntity::toDomain)

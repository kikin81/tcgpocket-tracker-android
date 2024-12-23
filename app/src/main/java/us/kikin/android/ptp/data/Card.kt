/*
 * Copyright 2024
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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

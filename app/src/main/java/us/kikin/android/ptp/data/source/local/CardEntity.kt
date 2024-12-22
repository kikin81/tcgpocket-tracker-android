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

package us.kikin.android.ptp.data.source.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Cards")
data class CardEntity(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "hp") val hp: String?,
    @ColumnInfo(name = "image") val image: String?,
    @ColumnInfo(name = "card_type") val cardType: String?,
    @ColumnInfo(name = "evolution_type") val evolutionType: String?,
    @ColumnInfo(name = "weakness") val weakness: String?,
    @ColumnInfo(name = "retreat") val retreat: Int?,
    @ColumnInfo(name = "rarity") val rarity: String?,
    @ColumnInfo(name = "fullart") val fullart: String?,
    @ColumnInfo(name = "ex") val ex: String?,
    @ColumnInfo(name = "set_details") val setDetails: String?,
    @ColumnInfo(name = "pack") val pack: String?,
    @ColumnInfo(name = "artist") val artist: String?,
    @ColumnInfo(name = "crafting_cost") val craftingCost: Int?,
)

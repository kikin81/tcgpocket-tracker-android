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

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UserCollectionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(userCollectionEntity: UserCollectionEntity)

    @Query("SELECT copies FROM UserCollection WHERE card_id = :cardId")
    suspend fun getCardCopies(cardId: String): Int?

    @Query("SELECT copies FROM UserCollection WHERE card_id = :cardId")
    fun observeCardCopies(cardId: String): Flow<Int>

    @Delete
    suspend fun delete(userCollectionEntity: UserCollectionEntity)

    @Query("DELETE FROM UserCollection WHERE card_id = :cardId")
    suspend fun deleteByCardId(cardId: String)
}

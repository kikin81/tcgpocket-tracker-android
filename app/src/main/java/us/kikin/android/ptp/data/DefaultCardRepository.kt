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

import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import us.kikin.android.ptp.data.source.local.CardDao
import us.kikin.android.ptp.data.source.local.UserCollectionDao
import us.kikin.android.ptp.data.source.local.UserCollectionEntity
import us.kikin.android.ptp.di.DefaultDispatcher

@Singleton
class DefaultCardRepository
@Inject
constructor(
    private val localDataSource: CardDao,
    private val userCollectionDao: UserCollectionDao,
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

    override suspend fun getCardCopies(cardId: String): Int {
        return userCollectionDao.getCardCopies(cardId) ?: 0
    }

    override suspend fun addCardToCollection(cardId: String, copies: Int) {
        userCollectionDao.upsert(UserCollectionEntity(cardId, copies))
    }
}

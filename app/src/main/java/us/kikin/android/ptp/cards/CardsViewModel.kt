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

package us.kikin.android.ptp.cards

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import us.kikin.android.ptp.R
import us.kikin.android.ptp.data.Card
import us.kikin.android.ptp.data.CardRepository
import us.kikin.android.ptp.util.Async
import us.kikin.android.ptp.util.WhileUiSubscribed

data class CardsUiState(
    val cards: List<Card> = emptyList(),
    val isLoading: Boolean = false,
    val filter: CardsFilterType = CardsFilterType.All,
    val userMessage: Int? = null,
)

@HiltViewModel
class CardsViewModel
@Inject
constructor(
    private val cardRepository: CardRepository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val _savedFilterType =
        savedStateHandle.getStateFlow(CARDS_FILTER_SAVED_STATE_KEY, CardsFilterType.All)
    private val _filterUiInfo = _savedFilterType
    private val _isLoading = MutableStateFlow(false)
    private val _userMessage: MutableStateFlow<Int?> = MutableStateFlow(null)
    private val _filteredCardsAsync =
        combine(cardRepository.getCardsStream(), _savedFilterType) { cards, type ->
            filterCards(cards, type)
        }
            .map { Async.Success(it) }
            .catch<Async<List<Card>>> { emit(Async.Error(R.string.loading_cards_error)) }

    val uiState: StateFlow<CardsUiState> =
        combine(
            _filterUiInfo,
            _isLoading,
            _userMessage,
            _filteredCardsAsync,
        ) { filterUiInfo, isLoading, userMessage, cardsAsync ->
            when (cardsAsync) {
                Async.Loading -> {
                    CardsUiState(isLoading = true)
                }

                is Async.Error -> {
                    CardsUiState(userMessage = cardsAsync.errorMessage)
                }

                is Async.Success -> {
                    CardsUiState(
                        cards = cardsAsync.data,
                        filter = filterUiInfo,
                        isLoading = isLoading,
                        userMessage = userMessage,
                    )
                }
            }
        }
            .stateIn(
                scope = viewModelScope,
                started = WhileUiSubscribed,
                initialValue = CardsUiState(isLoading = true),
            )

    private fun filterCards(cards: List<Card>, type: CardsFilterType): List<Card> {
        return when (type) {
            CardsFilterType.All -> cards
            CardsFilterType.GeneticApex -> cards.filter {
                it.setDetails == CardsFilterType.GeneticApex.setId
            }
            CardsFilterType.PromoA -> cards.filter {
                it.setDetails == CardsFilterType.PromoA.setId
            }
            CardsFilterType.MythicalIslands -> cards.filter {
                it.setDetails == CardsFilterType.MythicalIslands.setId
            }
        }
    }

    fun snackbarMessageShown() {
        _userMessage.value = null
    }

    fun setFiltering(requestType: CardsFilterType) {
        savedStateHandle[CARDS_FILTER_SAVED_STATE_KEY] = requestType
    }
}

const val CARDS_FILTER_SAVED_STATE_KEY = "CARDS_FILTER_SAVED_STATE_KEY"

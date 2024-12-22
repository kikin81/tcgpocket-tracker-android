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

package us.kikin.android.ptp.carddetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import us.kikin.android.ptp.R
import us.kikin.android.ptp.data.Card
import us.kikin.android.ptp.data.CardRepository
import us.kikin.android.ptp.navigation.CardDetailDestination
import us.kikin.android.ptp.util.Async
import us.kikin.android.ptp.util.WhileUiSubscribed
import javax.inject.Inject

data class CardDetailsUiState(
    val card: Card? = null,
    val isLoading: Boolean = false,
    val userMessage: Int? = null,
    val cardCount: Int? = null,
)

@HiltViewModel
class CardDetailViewModel @Inject constructor(
    private val cardRepository: CardRepository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private var _cardState =
        MutableStateFlow(savedStateHandle.toRoute<CardDetailDestination>().cardId)
    private val _cardCopies = MutableStateFlow(0)
    val cardCopies: StateFlow<Int> = _cardCopies
    private val _isLoading = MutableStateFlow(false)
    private val _userMessage: MutableStateFlow<Int?> = MutableStateFlow(null)
    private val _cardDetailAsync =
        cardRepository.getCardByIdStream(_cardState.value)
            .map { handleCard(it) }
            .catch { emit(Async.Error(R.string.loading_card_detail_error)) }
    private val _cardCopiesAsync =
        cardRepository.getCardCopiesStream(_cardState.value)
            .map { handleCardCount(it) }
            .catch { emit(Async.Error(R.string.loading_card_detail_error)) }
    val uiState: StateFlow<CardDetailsUiState> =
        combine(
            _isLoading,
            _userMessage,
            _cardDetailAsync,
            _cardCopiesAsync,
        ) { isLoading, userMessage, cardDetailAsync, cardCountAsync ->
            when {
                cardDetailAsync is Async.Loading || cardCountAsync is Async.Loading -> {
                    CardDetailsUiState(isLoading = true)
                }

                cardDetailAsync is Async.Error -> {
                    CardDetailsUiState(userMessage = cardDetailAsync.errorMessage)
                }

                cardCountAsync is Async.Error -> {
                    CardDetailsUiState(userMessage = cardCountAsync.errorMessage)
                }

                cardDetailAsync is Async.Success && cardCountAsync is Async.Success -> {
                    CardDetailsUiState(
                        card = cardDetailAsync.data,
                        cardCount = cardCountAsync.data,
                    )
                }

                else -> {
                    CardDetailsUiState(userMessage = R.string.loading_card_detail_error)
                }
            }
        }
            .stateIn(
                scope = viewModelScope,
                started = WhileUiSubscribed,
                initialValue = CardDetailsUiState(isLoading = true),
            )

    private fun handleCard(card: Card?): Async<Card?> {
        if (card == null) {
            return Async.Error(R.string.card_detail_not_found)
        }
        return Async.Success(card)
    }

    private fun handleCardCount(count: Int?): Async<Int> {
        if (count == null) {
            return Async.Error(R.string.card_detail_not_found)
        }
        return Async.Success(count)
    }

    fun snackbarMessageShown() {
        _userMessage.value = null
    }

    fun incrementCardCount(cardId: String?, cardCount: Int) {
        if (cardId == null) {
            return
        }
        viewModelScope.launch {
            cardRepository.addCardToCollection(cardId, cardCount)
            _cardCopies.value = cardCount
        }
    }
}

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
)

@HiltViewModel
class CardDetailViewModel
    @Inject
    constructor(
        private val cardRepository: CardRepository,
        private val savedStateHandle: SavedStateHandle,
    ) : ViewModel() {
        private var _cardState =
            MutableStateFlow(savedStateHandle.toRoute<CardDetailDestination>().cardId)
        private val _isLoading = MutableStateFlow(false)
        private val _userMessage: MutableStateFlow<Int?> = MutableStateFlow(null)
        private val _cardDetailAsync =
            cardRepository.getCardByIdStream(_cardState.value)
                .map { handleCard(it) }
                .catch { emit(Async.Error(R.string.loading_card_detail_error)) }
        val uiState: StateFlow<CardDetailsUiState> =
            combine(
                _isLoading,
                _userMessage,
                _cardDetailAsync,
            ) { isLoading, userMessage, cardDetailAsync ->
                when (cardDetailAsync) {
                    Async.Loading -> {
                        CardDetailsUiState(isLoading = true)
                    }

                    is Async.Error -> {
                        CardDetailsUiState(
                            userMessage = cardDetailAsync.errorMessage,
                        )
                    }

                    is Async.Success -> {
                        CardDetailsUiState(card = cardDetailAsync.data, isLoading = isLoading)
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

        fun snackbarMessageShown() {
            _userMessage.value = null
        }
    }

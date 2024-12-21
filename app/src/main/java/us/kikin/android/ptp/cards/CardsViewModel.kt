package us.kikin.android.ptp.cards

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import us.kikin.android.ptp.data.Card
import us.kikin.android.ptp.data.CardRepository
import us.kikin.android.ptp.util.Async
import javax.inject.Inject
import us.kikin.android.ptp.R
import us.kikin.android.ptp.util.WhileUiSubscribed

data class CardsUiState(
    val cards: List<Card> = emptyList(),
    val isLoading: Boolean = false,
    val userMessage: Int? = null
)

@HiltViewModel
class CardsViewModel @Inject constructor(
    private val cardRepository: CardRepository,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {

    private val _savedFilterType =
        savedStateHandle.getStateFlow(CARDS_FILTER_SAVED_STATE_KEY, CardsFilterType.ALL_CARDS)
    private val _isLoading = MutableStateFlow(false)
    private val _userMessage: MutableStateFlow<Int?> = MutableStateFlow(null)
    private val _filteredCardsAsync =
        combine(cardRepository.getCardsStream(), _savedFilterType) { cards, type ->
            filterCards(cards, type)
        }
            .map { Async.Success(it) }
            .catch<Async<List<Card>>> { emit(Async.Error(R.string.loading_cards_error)) }

    val uiState: StateFlow<CardsUiState> = combine(
        _isLoading, _userMessage, _filteredCardsAsync
    ) { isLoading, userMessage, cardsAsync ->
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
                    isLoading = isLoading,
                    userMessage = userMessage
                )
            }
        }
    }
        .stateIn(
            scope = viewModelScope,
            started = WhileUiSubscribed,
            initialValue = CardsUiState(isLoading = true)
        )


    private fun filterCards(cards: List<Card>, type: CardsFilterType): List<Card> {
        return when (type) {
            CardsFilterType.ALL_CARDS -> cards
        }
    }

    fun snackbarMessageShown() {
        _userMessage.value = null
    }

    private fun showSnackbarMessage(message: Int) {
        _userMessage.value = message
    }
}

const val CARDS_FILTER_SAVED_STATE_KEY = "CARDS_FILTER_SAVED_STATE_KEY"

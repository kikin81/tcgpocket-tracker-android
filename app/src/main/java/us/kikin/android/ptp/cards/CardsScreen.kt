package us.kikin.android.ptp.cards

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import us.kikin.android.ptp.data.Card
import androidx.compose.material3.Card as MaterialCard

@Composable
fun CardsScreen(
    modifier: Modifier = Modifier,
    @StringRes userMessage: Int? = null,
    viewModel: CardsViewModel = hiltViewModel(),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        CardsContent(
            cards = uiState.cards.toPersistentList(),
            isLoading = uiState.isLoading,
            userMessage = userMessage,
            modifier = Modifier.padding(paddingValues)
        )

        // Check for user messages to display on the screen
        uiState.userMessage?.let { message ->
            val snackbarText = stringResource(message)
            LaunchedEffect(snackbarHostState, viewModel, message, snackbarText) {
                snackbarHostState.showSnackbar(snackbarText)
                viewModel.snackbarMessageShown()
            }
        }
    }
}

@Composable
private fun CardsContent(
    cards: ImmutableList<Card>,
    isLoading: Boolean,
    userMessage: Int?,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        if (isLoading) {
            CircularProgressIndicator()
        } else {
            CardsList(cards = cards)
        }
    }
}

@Composable
private fun CardsList(
    cards: ImmutableList<Card>
) {
    // Display the list of cards
    LazyColumn {
        items(cards) { card ->
            CardItem(card = card)
        }
    }
}

@Composable
private fun CardItem(card: Card) {
    // Display the card item
    MaterialCard {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Display the card information
            Text(text = card.name)
            Text(text = "HP: ${card.hp}")
        }
    }
}

@Composable
@Preview
private fun CardsContentPreview() {
    CardsContent(
        cards = persistentListOf(
            Card(
                id = "A1001",
                name = "Bulbasaur",
                image = "https://example.com/image1.jpg",
                hp = "70",
            ),
        ),
        isLoading = false,
        userMessage = null
    )
}
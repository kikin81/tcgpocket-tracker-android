package us.kikin.android.ptp.cards

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.annotation.ExperimentalCoilApi
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePreviewHandler
import coil3.compose.LocalAsyncImagePreviewHandler
import coil3.test.FakeImage
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import us.kikin.android.ptp.data.Card
import androidx.compose.material3.Card as MaterialCard

@Composable
fun CardsScreen(
    onCardClick: (Card) -> Unit,
    modifier: Modifier = Modifier,
    @StringRes userMessage: Int? = null,
    viewModel: CardsViewModel = hiltViewModel(),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        CardsContent(
            cards = uiState.cards.toPersistentList(),
            onCardClick = onCardClick,
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
    onCardClick: (Card) -> Unit,
    userMessage: Int?,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        if (isLoading) {
            CircularProgressIndicator()
        } else {
            CardsList(
                cards = cards,
                onCardClick = onCardClick,
            )
        }
    }
}

@Composable
private fun CardsList(
    cards: ImmutableList<Card>,
    onCardClick: (Card) -> Unit,
) {
    // Display the list of cards
    LazyVerticalGrid(
        columns = GridCells.Adaptive(120.dp),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(cards) { card ->
            CardItem(
                card = card,
                onCardClick = onCardClick,
            )
        }
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
private fun CardItem(
    card: Card,
    onCardClick: (Card) -> Unit,
    modifier: Modifier = Modifier
) {
    MaterialCard(
        modifier = modifier
            .fillMaxWidth(),
        onClick = { onCardClick(card) },
    ) {
        Column {
            card.image?.let { imageUrl ->
                val previewHandler = AsyncImagePreviewHandler {
                    FakeImage(color = Color.Black.hashCode())
                }
                CompositionLocalProvider(LocalAsyncImagePreviewHandler provides previewHandler) {
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = card.name,
                        modifier = Modifier.aspectRatio(3f / 4f)
                    )
                }
            }
            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = card.name,
                    style = MaterialTheme.typography.titleSmall
                )
            }
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
            ),
            Card(
                id = "A1002",
                name = "Ivysaur",
                image = "https://example.com/image1.jpg",
            ),
            Card(
                id = "A1003",
                name = "Venosaur",
                image = "https://example.com/image1.jpg",
            ),
            Card(
                id = "A1004",
                name = "Charmander",
                image = "https://example.com/image1.jpg",
            ),
            Card(
                id = "A1005",
                name = "Charmeleon",
                image = "https://example.com/image1.jpg",
            ),
            Card(
                id = "A1006",
                name = "Charizard",
                image = "https://example.com/image1.jpg",
            ),
        ),
        isLoading = false,
        userMessage = null,
        onCardClick = {},
    )
}
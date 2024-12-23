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
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.material3.Card as MaterialCard
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
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
import us.kikin.android.ptp.R
import us.kikin.android.ptp.data.Card
import us.kikin.android.ptp.icons.PtpIcons
import us.kikin.android.ptp.icons.rounded.FilterList
import us.kikin.android.ptp.ui.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardsScreen(
    onCardClick: (Card) -> Unit,
    modifier: Modifier = Modifier,
    @StringRes userMessage: Int? = null,
    viewModel: CardsViewModel = hiltViewModel(),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
) {
    val scaffoldBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }
    AppTheme {
        Scaffold(
            modifier = modifier
                .fillMaxSize()
                .nestedScroll(scaffoldBehavior.nestedScrollConnection),
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = stringResource(id = R.string.app_name),
                            style = MaterialTheme.typography.titleLarge,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    },
                    actions = {
                        IconButton(onClick = { showBottomSheet = true }) {
                            Icon(
                                imageVector = PtpIcons.Rounded.FilterList,
                                contentDescription = stringResource(R.string.card_list_filters),
                            )
                        }
                    },
                )
            },
            snackbarHost = { SnackbarHost(snackbarHostState) },
        ) { paddingValues ->
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            CardsContent(
                cards = uiState.cards.toPersistentList(),
                onCardClick = onCardClick,
                isLoading = uiState.isLoading,
                userMessage = userMessage,
                modifier = Modifier.padding(paddingValues),
            )

            // Check for user messages to display on the screen
            uiState.userMessage?.let { message ->
                val snackbarText = stringResource(message)
                LaunchedEffect(snackbarHostState, viewModel, message, snackbarText) {
                    snackbarHostState.showSnackbar(snackbarText)
                    viewModel.snackbarMessageShown()
                }
            }

            if (showBottomSheet) {
                ModalBottomSheet(
                    onDismissRequest = {
                        showBottomSheet = false
                    },
                    sheetState = sheetState,
                ) {
                    FilterContent(
                        currentFilter = uiState.filter,
                        viewModel::setFiltering,
                    )
                }
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
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize(),
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterContent(
    currentFilter: CardsFilterType,
    onFilterChange: (CardsFilterType) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 24.dp),
    ) {
        // dropdown for set?
        val options = CardsFilterType.entries
        var expanded by remember { mutableStateOf(false) }
        val textFieldState = rememberTextFieldState(currentFilter.name)
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it },
            modifier = Modifier.fillMaxWidth(),
        ) {
            TextField(
                modifier = Modifier
                    .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                    .fillMaxWidth(),
                state = textFieldState,
                readOnly = true,
                lineLimits = TextFieldLineLimits.SingleLine,
                label = {
                    Text("Set")
                },
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
            ) {
                options.forEach { option ->
                    val label = stringResource(option.titleResId)
                    DropdownMenuItem(
                        text = { Text(label, style = MaterialTheme.typography.bodyLarge) },
                        onClick = {
                            textFieldState.setTextAndPlaceCursorAtEnd(label)
                            expanded = false
                            onFilterChange(option)
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                    )
                }
            }
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
    modifier: Modifier = Modifier,
) {
    MaterialCard(
        modifier =
        modifier
            .fillMaxWidth(),
        onClick = { onCardClick(card) },
    ) {
        Column {
            card.image?.let { imageUrl ->
                val previewHandler =
                    AsyncImagePreviewHandler {
                        FakeImage(color = Color.Black.hashCode())
                    }
                CompositionLocalProvider(LocalAsyncImagePreviewHandler provides previewHandler) {
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = card.name,
                        modifier = Modifier.aspectRatio(3f / 4f),
                    )
                }
            }
            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = card.name,
                    style = MaterialTheme.typography.titleSmall,
                )
            }
        }
    }
}

@Composable
@Preview
private fun CardsContentPreview() {
    CardsContent(
        cards =
        persistentListOf(
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

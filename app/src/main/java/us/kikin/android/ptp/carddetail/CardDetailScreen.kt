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

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import us.kikin.android.ptp.data.Card
import us.kikin.android.ptp.ui.theme.PtpTheme

@Composable
fun CardDetailScreen(
    modifier: Modifier = Modifier,
    @StringRes userMessage: Int? = null,
    viewModel: CardDetailViewModel = hiltViewModel(),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
) {
    PtpTheme {
        Scaffold(
            modifier = modifier.fillMaxSize(),
            snackbarHost = { SnackbarHost(snackbarHostState) },
        ) { paddingValues ->
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            Column(modifier = Modifier.padding(paddingValues)) {
                CardDetailContent(
                    card = uiState.card,
                    userMessage = userMessage,
                    isLoading = uiState.isLoading,
                    modifier = Modifier.padding(paddingValues),
                )
            }

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
}

@Composable
fun CardDetailContent(
    card: Card?,
    isLoading: Boolean,
    userMessage: Int?,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        if (isLoading) {
            CircularProgressIndicator()
        } else {
            if (card != null) {
                CardDetail(card)
            } else {
                // Handle error state
                Text("Error loading card details")
            }
        }
    }
}

@Composable
fun CardDetail(
    card: Card,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
        modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(state = rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        AsyncImage(
            model = card.image,
            contentDescription = card.name,
            modifier = Modifier.fillMaxWidth(),
        )

        Text(
            text = card.name,
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.headlineMedium,
        )

        card.rarity?.let {
            Text(
                text = "Rarity: $it",
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.bodyMedium,
            )
        }
        card.hp?.let {
            Text(
                text = "HP: $it",
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.bodyMedium,
            )
        }
        card.artist?.let {
            Text(
                text = "Illustrator: $it",
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.bodyMedium,
            )
        }
        Row {
            card.setDetails?.let {
                Text(
                    text = "Set: $it",
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
            card.pack?.let {
                Text(
                    text = "Pack: $it",
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    }
}

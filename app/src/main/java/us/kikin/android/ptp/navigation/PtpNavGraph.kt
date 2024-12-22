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

package us.kikin.android.ptp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import us.kikin.android.ptp.carddetail.CardDetailScreen
import us.kikin.android.ptp.cards.CardsScreen

@Composable
fun SafePtpNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: CardListDestination = CardListDestination(),
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        composable<CardListDestination> { backStackEntry ->
            val destination: CardListDestination = backStackEntry.toRoute()
            CardsScreen(
                userMessage = destination.userMessage,
                onCardClick = { card ->
                    navController.navigate(CardDetailDestination(card.id))
                },
            )
        }
        composable<CardDetailDestination> { backStackEntry ->
            val destination: CardDetailDestination = backStackEntry.toRoute()
            CardDetailScreen(
                userMessage = destination.userMessage,
            )
        }
    }
}

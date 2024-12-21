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
    startDestination: CardListDestination = CardListDestination()
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable<CardListDestination> { backStackEntry ->
            val destination: CardListDestination = backStackEntry.toRoute()
            CardsScreen(
                userMessage = destination.userMessage,
                onCardClick = { card ->
                    navController.navigate(CardDetailDestination(card.id))
                }
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
package us.kikin.android.ptp.navigation

import us.kikin.android.ptp.navigation.AppDestinationArgs.USER_MESSAGE_ARG
import us.kikin.android.ptp.navigation.AppScreens.CARDS_SCREEN

object AppScreens {
    const val CARDS_SCREEN = "cards"
}

object AppDestinationArgs {
    const val USER_MESSAGE_ARG = "userMessage"
}

object AppDestinations {
    const val CARDS_ROUTE = "$CARDS_SCREEN?$USER_MESSAGE_ARG={$USER_MESSAGE_ARG}"
}

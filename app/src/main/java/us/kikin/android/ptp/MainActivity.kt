package us.kikin.android.ptp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import us.kikin.android.ptp.navigation.SafePtpNavGraph
import us.kikin.android.ptp.ui.theme.PtpTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PtpTheme {
                SafePtpNavGraph()
            }
        }
    }
}

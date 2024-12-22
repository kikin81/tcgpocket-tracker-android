package us.kikin.android.ptp.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import us.kikin.android.ptp.icons.PtpIcons
import us.kikin.android.ptp.icons.rounded.Remove
import us.kikin.android.ptp.ui.theme.PtpTheme

@Composable
fun Stepper(
    value: Int,
    onValueChange: (Int) -> Unit,
    valueProgression: IntProgression,
    modifier: Modifier = Modifier,
) {
    // Ensure the value respects the progression
    val clampedValue = value.coerceIn(valueProgression.first, valueProgression.last)

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        // Decrease Button
        FilledIconButton(
            onClick = {
                val newValue = clampedValue - valueProgression.step
                if (newValue >= valueProgression.first) onValueChange(newValue)
            },
            enabled = clampedValue > valueProgression.first, // Disable if at min
        ) {
            Icon(PtpIcons.Rounded.Remove, contentDescription = "Decrease value")
        }

        // Display Current Value
        Text(
            text = clampedValue.toString(),
            style = MaterialTheme.typography.displaySmall,
            modifier = Modifier.widthIn(min = 48.dp),
            textAlign = TextAlign.Center,
        )

        // Increase Button
        FilledIconButton(
            onClick = {
                val newValue = clampedValue + valueProgression.step
                if (newValue <= valueProgression.last) onValueChange(newValue)
            },
            enabled = clampedValue < valueProgression.last, // Disable if at max
        ) {
            Icon(Icons.Rounded.Add, contentDescription = "Increase value")
        }
    }
}

@Composable
@Preview
internal fun StepperPreview() {
    PtpTheme {
        var value by remember { mutableIntStateOf(0) }
        Stepper(
            value = value,
            onValueChange = { value = it},
            valueProgression = 0..10,
        )
    }
}

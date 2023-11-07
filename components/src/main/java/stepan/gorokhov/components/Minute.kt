package stepan.gorokhov.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun Minute(seconds: Int) {
    Text(
        text = "${(seconds / 60)}:${
            (seconds % 60).toString().padStart(2, '0')
        }", style = MaterialTheme.typography.labelSmall
    )
}
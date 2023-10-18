package stepan.gorokhov.music.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun LoadingScreen(modifier: Modifier = Modifier){
    Box(modifier = modifier) {
        CircularProgressIndicator(
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.align(
                Alignment.Center
            )
        )
    }
}


@Composable
@Preview
private fun LoadingScreenPreview(){
    Scaffold {
        LoadingScreen(Modifier.padding(it).fillMaxSize())
    }
}
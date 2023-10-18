package stepan.gorokhov.music.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import stepan.gorokhov.music.domain.models.Artist
import stepan.gorokhov.music.domain.models.Track
import stepan.gorokhov.music.ui.theme.MusicTheme

@Composable
fun TrackItem(track: Track, onSelect: (Track) -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.clickable { onSelect(track) },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(Modifier.weight(1f)){
            AsyncImage(
                model = track.image,
                contentDescription = null,
                modifier = Modifier
                    .padding(end = 16.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .size(56.dp)
            )
            Column {
                Text(
                    text = track.name,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = track.artists[0].name,
                    style = MaterialTheme.typography.labelSmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        Minute(seconds = track.duration)
    }
}

@Composable
@Preview
private fun TrackItemPreview() {
    MusicTheme {
        Row {
            TrackItem(
                track = Track(
                    "Hello",
                    artists = listOf(Artist("Morgenshtern")),
                    isLiked = true,
                    url = "",
                    image = ""
                ), onSelect = {}, modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
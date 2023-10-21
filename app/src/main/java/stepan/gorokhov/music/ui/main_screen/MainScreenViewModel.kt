package stepan.gorokhov.music.ui.main_screen

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.StateFlow
import stepan.gorokhov.domain.models.Track
import stepan.gorokhov.domain.repositories.TrackRepository
import javax.inject.Inject

class MainScreenViewModel @Inject constructor(repository: TrackRepository):ViewModel() {
    val track: StateFlow<Track?> = repository.currentTrack
}
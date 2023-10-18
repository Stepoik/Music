package stepan.gorokhov.music.ui.search_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import stepan.gorokhov.music.domain.models.Playlist
import stepan.gorokhov.music.domain.models.Track
import stepan.gorokhov.music.domain.repositories.TrackRepository
import javax.inject.Inject

sealed class SearchState{
    object Loading:SearchState()
    data class SearchResult(val playlist: Playlist):SearchState()
}
class SearchScreenViewModel @Inject constructor(private val repository: TrackRepository):ViewModel() {
    private val _searchValue = MutableStateFlow("")
    val searchValue: StateFlow<String> get() = _searchValue
    private val _searchState = MutableStateFlow<SearchState>(SearchState.SearchResult(Playlist(listOf())))
    val searchState:StateFlow<SearchState> get() = _searchState
    private val currentTrack = repository.currentTrack
    private val isPlaying = repository.isPlaying

    private val searchScope = CoroutineScope(Dispatchers.Main)
    fun search(searchValue:String){
        _searchValue.value = searchValue
        searchScope.coroutineContext.cancelChildren()
        searchScope.launch {
            _searchState.value = SearchState.Loading
            val searchResult = repository.getTracksByName(searchValue)
            if (isActive){
                searchResult.onSuccess {
                    _searchState.value = SearchState.SearchResult(it)
                }
            }
        }
    }
    fun selectTrack(track: Track, playlist: Playlist){
        if (currentTrack.value == track && isPlaying.value){
            repository.stop()
        }
        else if (currentTrack.value == track){
            viewModelScope.launch {
                repository.play()
            }
        }
        else{
            viewModelScope.launch {
                repository.play(track, playlist)
            }
        }
    }
}
package stepan.gorokhov.search_screen.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import stepan.gorokhov.domain.models.Playlist
import stepan.gorokhov.domain.models.Track
import stepan.gorokhov.domain.repositories.SearchRepository
import stepan.gorokhov.domain.repositories.TrackRepository
import javax.inject.Inject

internal sealed class SearchState{
    object Loading: SearchState()
    data class SearchResult(val playlist: Playlist): SearchState()
}
internal class SearchScreenViewModel @Inject constructor(private val repository: TrackRepository, private val searchRepository: SearchRepository):ViewModel() {
    private val _searchValue = MutableStateFlow("")
    val searchValue: StateFlow<String> get() = _searchValue
    private val _searchState = MutableStateFlow<SearchState>(
        SearchState.SearchResult(
            Playlist(
                listOf()
            )
        )
    )
    val searchState:StateFlow<SearchState> get() = _searchState
    private val currentTrack = repository.currentTrack
    private val isPlaying = repository.isPlaying

    private val searchScope = CoroutineScope(Dispatchers.Main)
    init {
        viewModelScope.launch {
            searchRepository.foundTracks.collect{
                _searchState.value = SearchState.SearchResult(it)
            }
        }
    }
    fun search(searchValue:String){
        _searchValue.value = searchValue
        searchScope.coroutineContext.cancelChildren()
        searchScope.launch {
            _searchState.value = SearchState.Loading
            searchRepository.getTracksByName(searchValue)
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
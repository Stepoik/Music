package stepan.gorokhov.domain.models

enum class ReplayState {
    NoReplay,
    ReplayPlaylist,
    ReplayTrack;
    fun next(): ReplayState {
        return when (this){
            NoReplay ->{
                ReplayPlaylist
            }
            ReplayPlaylist ->{
                ReplayTrack
            }
            ReplayTrack ->{
                NoReplay
            }
        }
    }
}
package com.sarabyeet.portalworlds.arch

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sarabyeet.portalworlds.domain.models.Episode
import kotlinx.coroutines.launch

class EpisodeDetailsViewModel : ViewModel() {
    private val repository = EpisodesRepository()

    private var _episodeDetailsLiveData = MutableLiveData<Episode?>()
    val episodeDetailsLiveData: LiveData<Episode?> = _episodeDetailsLiveData

    fun getEpisodeDetails(episodeId: Int) = viewModelScope.launch {
        val episode = repository.getEpisodesById(episodeId)

        _episodeDetailsLiveData.postValue(episode)
    }
}
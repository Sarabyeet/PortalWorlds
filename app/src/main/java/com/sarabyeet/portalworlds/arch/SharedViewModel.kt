package com.sarabyeet.portalworlds.arch

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sarabyeet.portalworlds.domain.models.Character
import com.sarabyeet.portalworlds.network.PortalWorldsCache
import kotlinx.coroutines.launch
import kotlin.random.Random

class SharedViewModel : ViewModel() {

    private var _getCharacterByIdLiveData = MutableLiveData<Character?>()
    val getCharacterByIdLiveData: LiveData<Character?> = _getCharacterByIdLiveData

    private var _getRandomCharacterLiveData = MutableLiveData<Character?>()
    val getRandomCharacterLiveData: LiveData<Character?> = _getRandomCharacterLiveData

    private val repository = SharedRepository()

    fun fetchCharacter(characterId: Int) = viewModelScope.launch {
        val character = repository.getCharacterById(characterId)
        _getCharacterByIdLiveData.postValue(character)
    }


    fun randomCharacter() = viewModelScope.launch {
        val response = repository.getRandomCharacter()
        _getRandomCharacterLiveData.postValue(response)
    }


}
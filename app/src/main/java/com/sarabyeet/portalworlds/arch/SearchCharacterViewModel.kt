package com.sarabyeet.portalworlds.arch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.sarabyeet.portalworlds.paging.SearchCharacterPageSource
import com.sarabyeet.portalworlds.util.Constants
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class SearchCharacterViewModel : ViewModel() {

    private var currentUserSearch: String = ""
    private var pagingSource: SearchCharacterPageSource? = null
    get() {
        if (field == null || field?.invalid == true){
            field = SearchCharacterPageSource(currentUserSearch) {
                viewModelScope.launch {
                    searchChannel.send(it)
                }
            }
        }
        return field
    }

    private val searchChannel = Channel<Event.LocalException>()
    val searchEventFlow = searchChannel.receiveAsFlow()

    val flow = Pager(
        PagingConfig(
            Constants.PAGE_SIZE,
            Constants.PREFETCH_DISTANCE,
            false
        )
    ) {
        pagingSource!!
    }.flow.cachedIn(viewModelScope)

    fun searchCharacter(userSearch:String) {
        currentUserSearch = userSearch
        pagingSource?.invalidate()
    }
}
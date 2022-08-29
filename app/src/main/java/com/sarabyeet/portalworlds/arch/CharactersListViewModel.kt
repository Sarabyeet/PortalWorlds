package com.sarabyeet.portalworlds.arch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.sarabyeet.portalworlds.network.response.GetCharacterByIdResponse
import com.sarabyeet.portalworlds.paging.CharactersPageSource
import com.sarabyeet.portalworlds.util.Constants
import kotlinx.coroutines.flow.Flow

class CharactersListViewModel: ViewModel() {
    val charactersListFlow : Flow<PagingData<GetCharacterByIdResponse>> = Pager(
        // Configure how data is loaded by passing additional properties to
        // PagingConfig, such as prefetchDistance.
        PagingConfig(pageSize = Constants.PAGE_SIZE, prefetchDistance = Constants.PREFETCH_DISTANCE, enablePlaceholders = false)
    ) {
        CharactersPageSource()
    }.flow
        .cachedIn(viewModelScope)
}
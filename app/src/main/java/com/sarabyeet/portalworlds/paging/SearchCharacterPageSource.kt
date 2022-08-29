package com.sarabyeet.portalworlds.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.sarabyeet.portalworlds.arch.Event
import com.sarabyeet.portalworlds.domain.mapers.CharacterMapper
import com.sarabyeet.portalworlds.domain.models.Character
import com.sarabyeet.portalworlds.network.NetworkLayer

class SearchCharacterPageSource(
    private val userSearch: String,
    private val localExceptionCallback: (Event.LocalException) -> Unit,
) : PagingSource<Int, Character>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Character> {
        if (userSearch.isEmpty()) {
            val exception = Event.LocalException.EmptySearch
            localExceptionCallback(exception)
            return LoadResult.Error(exception)
        }

        val pageNumber = params.key ?: 1
        val previousKey = if (pageNumber == 1) null else pageNumber - 1
        val request = NetworkLayer.api.getCharactersPage(userSearch, pageNumber)

        if (request.data?.code() == 404) {
            val exception = Event.LocalException.NoResults
            localExceptionCallback(exception)
            return LoadResult.Error(exception)
        }
        request.exception?.let {
            return LoadResult.Error(it)
        }

        return LoadResult.Page(
            data = request.bodyNullable?.results?.map {
                CharacterMapper.buildFrom(it)
            } ?: emptyList(),
            previousKey,
            getPageIndexFromNext(request.bodyNullable?.info?.next)
        )
    }

    override fun getRefreshKey(state: PagingState<Int, Character>): Int? {
        // Try to find the page key of the closest page to anchorPosition, from
        // either the prevKey or the nextKey, but you need to handle nullability
        // here:
        //  * prevKey == null -> anchorPage is the first page.
        //  * nextKey == null -> anchorPage is the last page.
        //  * both prevKey and nextKey null -> anchorPage is the initial page, so
        //    just return null.
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    private fun getPageIndexFromNext(next: String?): Int? {
        if (next == null) {
            return null
        }

        val remainder = next.substringAfter("page=")
        val finalIndex = if (remainder.contains('&')) {
            remainder.indexOfFirst { it == '&' }
        } else {
            remainder.length
        }

        return remainder.substring(0, finalIndex).toIntOrNull()
    }
}
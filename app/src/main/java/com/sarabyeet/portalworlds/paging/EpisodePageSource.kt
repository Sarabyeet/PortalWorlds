package com.sarabyeet.portalworlds.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.sarabyeet.portalworlds.arch.EpisodesRepository
import com.sarabyeet.portalworlds.domain.mapers.EpisodeMapper
import com.sarabyeet.portalworlds.domain.models.Episode
import com.sarabyeet.portalworlds.network.NetworkLayer
import com.sarabyeet.portalworlds.ui.episodes.EpisodesUiModel

class EpisodePageSource(
    val repository: EpisodesRepository,
) : PagingSource<Int, EpisodesUiModel>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, EpisodesUiModel> {
        return try {
            val pageNumber = params.key ?: 1
            val previousKey = if (pageNumber == 1) null else pageNumber - 1

            val request = NetworkLayer.api.getEpisodesPage(pageNumber)

            LoadResult.Page(
                data = request.body.results.map { response ->
                    EpisodesUiModel.Item(EpisodeMapper.buildFrom(response)) },
                prevKey = previousKey,
                nextKey = getPageIndexFromNext(request.body.info.next)
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, EpisodesUiModel>): Int? {
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
        return next?.split("?page=")?.get(1)?.toInt()
    }
}
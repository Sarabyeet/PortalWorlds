package com.sarabyeet.portalworlds.network.response

data class GetEpisodesPageResponse(
    val info: PageInfo = PageInfo(),
    val results: List<GetEpisodeByIdResponse> = listOf(),
)
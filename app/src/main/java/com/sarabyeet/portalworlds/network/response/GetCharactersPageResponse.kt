package com.sarabyeet.portalworlds.network.response

data class GetCharactersPageResponse(
    val info: PageInfo = PageInfo(),
    val results: List<GetCharacterByIdResponse> = emptyList()
)
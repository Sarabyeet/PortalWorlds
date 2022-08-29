package com.sarabyeet.portalworlds.domain.mapers

import com.sarabyeet.portalworlds.domain.models.Character
import com.sarabyeet.portalworlds.network.response.GetCharacterByIdResponse
import com.sarabyeet.portalworlds.network.response.GetEpisodeByIdResponse

object CharacterMapper {
    fun buildFrom(
        response: GetCharacterByIdResponse,
        episodes: List<GetEpisodeByIdResponse> = emptyList(),
    ): Character {
        return Character(
            episodeList = episodes.map {
                EpisodeMapper.buildFrom(it)
            },
            gender = response.gender,
            id = response.id,
            image = response.image,
            location = Character.Location(
                name = response.location.name,
                url = response.origin.url
            ),
            name = response.name,
            origin = Character.Origin(
                name = response.origin.name,
                url = response.origin.url
            ),
            species = response.species,
            status = response.status
        )
    }
}
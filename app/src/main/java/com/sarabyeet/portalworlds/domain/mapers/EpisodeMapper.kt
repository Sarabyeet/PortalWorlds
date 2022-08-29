package com.sarabyeet.portalworlds.domain.mapers

import com.sarabyeet.portalworlds.domain.models.Character
import com.sarabyeet.portalworlds.domain.models.Episode
import com.sarabyeet.portalworlds.network.response.GetCharacterByIdResponse
import com.sarabyeet.portalworlds.network.response.GetEpisodeByIdResponse

object EpisodeMapper {
    fun buildFrom(
        response: GetEpisodeByIdResponse,
        networkCharacters: List<GetCharacterByIdResponse> = emptyList(),
    ): Episode {
        return Episode(
            id = response.id,
            name = response.name,
            airDate = response.air_date,
            seasonNumber = getSeasonFromEpisodeString(response.episode),
            episodeNumber = getEpisodeFromEpisodeString(response.episode),
            episode = response.episode,
            characterList = networkCharacters.map {
                CharacterMapper.buildFrom(it)
            }
        )
    }

    private fun getEpisodeFromEpisodeString(episode: String): Int {
        val startIndex = episode.indexOfFirst { it.equals('e', true) }
        if (startIndex == -1) {
            return 0
        }
        return episode.substring(startIndex + 1).toIntOrNull() ?: 0
    }

    private fun getSeasonFromEpisodeString(episode: String): Int {
        val endIndex = episode.indexOfFirst { it.equals('e', true) }
        if (endIndex == -1) {
            return 0
        }
        return episode.substring(1, endIndex).toIntOrNull() ?: 0
    }
}
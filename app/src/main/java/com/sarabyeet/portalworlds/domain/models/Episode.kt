package com.sarabyeet.portalworlds.domain.models

data class Episode(
    val id: Int,
    val name: String,
    val airDate: String,
    val seasonNumber: Int,
    val episodeNumber: Int,
    val episode: String,
    val characterList: List<Character>
) {
    fun getFormattedSeasonTruncated(): String {
        return "S.$seasonNumber E.$episodeNumber"
    }

    fun getFormattedSeason(): String {
        return "Season.$seasonNumber Episode.$episodeNumber"
    }
}

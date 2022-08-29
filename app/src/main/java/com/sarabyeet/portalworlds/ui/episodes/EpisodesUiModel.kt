package com.sarabyeet.portalworlds.ui.episodes

import com.sarabyeet.portalworlds.domain.models.Episode

sealed class EpisodesUiModel {
    class Item (val episode: Episode): EpisodesUiModel()
    class Header (val title: String): EpisodesUiModel()
}
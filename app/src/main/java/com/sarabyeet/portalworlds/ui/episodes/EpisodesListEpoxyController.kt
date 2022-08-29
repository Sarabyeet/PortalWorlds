package com.sarabyeet.portalworlds.ui.episodes

import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.paging3.PagingDataEpoxyController
import com.sarabyeet.portalworlds.R
import com.sarabyeet.portalworlds.databinding.ModelCharacterListTitleBinding
import com.sarabyeet.portalworlds.databinding.ModelEpisodeListItemBinding
import com.sarabyeet.portalworlds.domain.models.Episode
import com.sarabyeet.travelapp.ui.epoxy.ViewBindingKotlinModel

class EpisodesListEpoxyController(
    val onClick: (Int) -> Unit
) : PagingDataEpoxyController<EpisodesUiModel>() {
    override fun buildItemModel(currentPosition: Int, item: EpisodesUiModel?): EpoxyModel<*> {
        return when (item!!) {
            is EpisodesUiModel.Item -> {
                val episode = (item as EpisodesUiModel.Item).episode
                EpisodeListItemModel(
                    episode = episode,
                    onClick = { episodeId ->
                        onClick.invoke(episodeId)
                    }
                ).id("episode-${episode.id}")
            }
            is EpisodesUiModel.Header -> {
                val headerText = (item as EpisodesUiModel.Header).title
                EpisodeHeaderItemModel(headerText).id("header-$headerText")
            }
        }
    }

    data class EpisodeListItemModel(
        val episode: Episode,
        val onClick: (Int) -> Unit,
    ) : ViewBindingKotlinModel<ModelEpisodeListItemBinding>(R.layout.model_episode_list_item) {
        override fun ModelEpisodeListItemBinding.bind() {
            episodeNameTextView.text = episode.name
            airDateTextView.text = episode.airDate
            episodeTextView.text = episode.getFormattedSeasonTruncated()

            root.setOnClickListener {
                onClick.invoke(episode.id)
            }
        }
    }
    data class EpisodeHeaderItemModel(
        val title: String,
    ) : ViewBindingKotlinModel<ModelCharacterListTitleBinding>(R.layout.model_character_list_title) {
        override fun ModelCharacterListTitleBinding.bind() {
            titleTextView.text = title
        }
    }
}
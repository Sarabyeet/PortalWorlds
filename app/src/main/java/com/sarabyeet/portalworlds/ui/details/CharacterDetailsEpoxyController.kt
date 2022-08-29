package com.sarabyeet.portalworlds.ui.details

import androidx.core.content.ContextCompat
import com.airbnb.epoxy.CarouselModel_
import com.airbnb.epoxy.EpoxyController
import com.sarabyeet.portalworlds.R
import com.sarabyeet.portalworlds.databinding.ModelCharacterDetailsDataBinding
import com.sarabyeet.portalworlds.databinding.ModelCharacterDetailsImageBinding
import com.sarabyeet.portalworlds.databinding.ModelCharacterDetailsTitleBinding
import com.sarabyeet.portalworlds.databinding.ModelEpisodeCarouselItemBinding
import com.sarabyeet.portalworlds.domain.models.Character
import com.sarabyeet.portalworlds.domain.models.Episode
import com.sarabyeet.portalworlds.epoxy.LoadingEpoxyModel
import com.sarabyeet.travelapp.ui.epoxy.ViewBindingKotlinModel
import com.squareup.picasso.Picasso

class CharacterDetailsEpoxyController(
    private val onEpisodeClick: (Int) -> Unit
) : EpoxyController() {

    private var isLoading: Boolean = true
        set(value) {
            field = value
            if (field) {
                requestModelBuild()
            }
        }

    var character: Character? = null
        set(value) {
            field = value
            field?.let {
                isLoading = false
                requestModelBuild()
            }
        }

    override fun buildModels() {
        if (isLoading) {
            LoadingEpoxyModel().id("loading-state").addTo(this)
            return
        }

        if (character == null){
            // todo error state
            return
        }

        // add header model
        HeaderEpoxyModel(
            name = character!!.name,
            status = character!!.status,
            gender = character!!.gender
        ).id("header").addTo(this)

        // add image model
        CharacterImageEpoxyModel(
            imageUrl = character!!.image
        ).id("character-image").addTo(this)

        // add episode carousel
        CharacterDetailsEpoxyModel(
            label = "Total episodes : ",
            labelDetails = character!!.episodeList.size.toString()
        ).id("episodes-details").addTo(this)
        if (character!!.episodeList.isNotEmpty()){
            val items = character!!.episodeList.map {
                EpisodeCarouselEpoxyModel(it, character!!.status, onEpisodeClick).id(it.id)
            }
            CarouselModel_()
                .models(items)
                .numViewsToShowOnScreen(1.25F)
                .id("episode_carousel")
                .addTo(this)
        }

        // add details model
        CharacterDetailsEpoxyModel(
            label = "Species : ",
            labelDetails = character!!.species
        ).id("species-details").addTo(this)

        CharacterDetailsEpoxyModel(
            label = "Origin : ",
            labelDetails = character!!.origin.name
        ).id("origin-details").addTo(this)

        CharacterDetailsEpoxyModel(
            label = "Last known location : ",
            labelDetails = character!!.location.name
        ).id("location-details").addTo(this)
    }

    data class HeaderEpoxyModel(
        val name: String,
        val status: String,
        val gender: String,
    ) : ViewBindingKotlinModel<ModelCharacterDetailsTitleBinding>(R.layout.model_character_details_title) {
        override fun ModelCharacterDetailsTitleBinding.bind() {
            titleTextView.text = name
            statusTextView.text = status
            val statusImage = when {
                status.equals("alive", true) -> R.drawable.green_circle
                status.equals("dead", true) -> R.drawable.red_circle
                else -> R.drawable.gray_circle
            }
            statusImageView.setImageResource(statusImage)
            val genderImage = when {
                gender.equals("male", true) -> R.drawable.ic_male_24
                gender.equals("female", true) -> R.drawable.ic_female_24
                else -> R.drawable.ic_question_mark_24
            }
            genderImageView.setImageResource(genderImage)
        }
    }

    data class CharacterImageEpoxyModel(
        val imageUrl: String,
    ) : ViewBindingKotlinModel<ModelCharacterDetailsImageBinding>(R.layout.model_character_details_image) {
        override fun ModelCharacterDetailsImageBinding.bind() {
            Picasso.get().load(imageUrl).into(headerImage)
        }
    }

    data class CharacterDetailsEpoxyModel(
        val label: String,
        val labelDetails: String,
    ) : ViewBindingKotlinModel<ModelCharacterDetailsDataBinding>(R.layout.model_character_details_data) {
        override fun ModelCharacterDetailsDataBinding.bind() {
            labelTextView.text = label
            labelDetailsTextView.text = labelDetails
        }
    }

    data class EpisodeCarouselEpoxyModel(
        val episode: Episode,
        val isAlive: String,
        val onEpisodeClick: (Int) -> Unit
    ): ViewBindingKotlinModel<ModelEpisodeCarouselItemBinding>(R.layout.model_episode_carousel_item){
        override fun ModelEpisodeCarouselItemBinding.bind() {
            episodeTextView.text = episode.getFormattedSeasonTruncated()
            episodeNameTextView.text = episode.name
            airDateTextView.text = episode.airDate

            val color = when {
                isAlive.equals("Alive", true) -> R.color.green
                isAlive.equals("dead", true) -> R.color.red
                else -> R.color.gray
            }

            card.strokeColor = ContextCompat.getColor(root.context, color)
            border.setBackgroundResource(color)

            card.setOnClickListener {
                onEpisodeClick.invoke(episode.id)
            }
        }
    }
}
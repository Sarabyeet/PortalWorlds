package com.sarabyeet.portalworlds.ui.episodes.details

import com.airbnb.epoxy.EpoxyController
import com.sarabyeet.portalworlds.R
import com.sarabyeet.portalworlds.databinding.ModelEpisodeDetailsCharacterBinding
import com.sarabyeet.portalworlds.domain.models.Character
import com.sarabyeet.travelapp.ui.epoxy.ViewBindingKotlinModel
import com.squareup.picasso.Picasso

class EpisodeDetailsEpoxyController(
    private val character: List<Character>,
    private val onCharacterClick: (Int) -> Unit
) : EpoxyController() {
    override fun buildModels() {
        character.forEach {
            EpisodeDetailsCharacterModel(it.name, it.image,it.id, onCharacterClick).id("episode-character-${it.id}").addTo(this)
        }
    }

    data class EpisodeDetailsCharacterModel(
        val name: String,
        val imageUrl: String,
        val characterId: Int,
        val onCharacterClick: (Int) -> Unit
    ) : ViewBindingKotlinModel<ModelEpisodeDetailsCharacterBinding>(R.layout.model_episode_details_character) {
        override fun ModelEpisodeDetailsCharacterBinding.bind() {
            characterText.text = name
            Picasso.get().load(imageUrl).into(characterImage)
            root.setOnClickListener {
                onCharacterClick.invoke(characterId)
            }
        }
    }
}
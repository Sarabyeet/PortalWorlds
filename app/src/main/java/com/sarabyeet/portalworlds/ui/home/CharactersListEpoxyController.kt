package com.sarabyeet.portalworlds.ui.home

import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.paging3.PagingDataEpoxyController
import com.sarabyeet.portalworlds.R
import com.sarabyeet.portalworlds.databinding.ModelCharacterListTitleBinding
import com.sarabyeet.portalworlds.databinding.ModelCharactersListBinding
import com.sarabyeet.portalworlds.epoxy.LoadingEpoxyModel
import com.sarabyeet.portalworlds.network.response.GetCharacterByIdResponse
import com.sarabyeet.travelapp.ui.epoxy.ViewBindingKotlinModel
import com.squareup.picasso.Picasso
import java.util.*


class CharactersListEpoxyController(private val onClick: (Int) -> Unit) :
    PagingDataEpoxyController<GetCharacterByIdResponse>() {
    override fun buildItemModel(
        currentPosition: Int,
        item: GetCharacterByIdResponse?,
    ): EpoxyModel<*> {
        return CharactersListEpoxy(item!!.image,
            item.name,
            onClick,
            item.id).id("character - ${item.id}")
    }

    override fun addModels(models: List<EpoxyModel<*>>) {
        if (models.isEmpty()) {
            LoadingEpoxyModel().id("loading").addTo(this)
            return
        }

        CharactersListTitleEpoxy("Main Family").id("main-family-header").addTo(this)
        super.addModels(models.subList(0, 5))

        (models.subList(5, models.size) as List<CharactersListEpoxy>).groupBy {
            it.name[0].uppercase(Locale.US)
        }.forEach { mapEntry ->
            val character = mapEntry.key.uppercase(Locale.US)
            CharactersListTitleEpoxy(character)
                .id(character)
                .addTo(this)
            super.addModels(mapEntry.value)
        }
    }

    data class CharactersListEpoxy(
        val image: String,
        val name: String,
        val onClick: (Int) -> Unit,
        val id: Int,
    ) : ViewBindingKotlinModel<ModelCharactersListBinding>(R.layout.model_characters_list) {
        override fun ModelCharactersListBinding.bind() {
            Picasso.get().load(image).into(charactersImage)
            charactersText.text = name
            root.setOnClickListener {
                onClick.invoke(id)
            }
        }
    }

    data class CharactersListTitleEpoxy(
        val title: String,
    ) : ViewBindingKotlinModel<ModelCharacterListTitleBinding>(R.layout.model_character_list_title) {
        override fun ModelCharacterListTitleBinding.bind() {
            titleTextView.text = title
        }
        override fun getSpanSize(totalSpanCount: Int, position: Int, itemCount: Int): Int {
            return totalSpanCount
        }
    }
}
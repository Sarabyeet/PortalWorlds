package com.sarabyeet.portalworlds.ui.search

import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.paging3.PagingDataEpoxyController
import com.sarabyeet.portalworlds.R
import com.sarabyeet.portalworlds.arch.Event
import com.sarabyeet.portalworlds.databinding.ModelCharactersListBinding
import com.sarabyeet.portalworlds.databinding.ModelLocalExceptionBinding
import com.sarabyeet.portalworlds.domain.models.Character
import com.sarabyeet.portalworlds.epoxy.LoadingEpoxyModel
import com.sarabyeet.travelapp.ui.epoxy.ViewBindingKotlinModel
import com.squareup.picasso.Picasso

class SearchCharacterEpoxyController(
    private val onCharacterClick: (Int) -> Unit,
) : PagingDataEpoxyController<Character>() {

    var localException: Event.LocalException?= null
    set(value) {
        field = value
        if (localException != null) {
            requestModelBuild()
        }
    }

    override fun buildItemModel(currentPosition: Int, item: Character?): EpoxyModel<*> {
        return CharactersListEpoxy(
            item!!.image,
            item.name,
            onClick = {
                onCharacterClick(it)
            },
            item.id
        ).id("item.id-${item.id}")
    }

    override fun addModels(models: List<EpoxyModel<*>>) {
        localException?.let {
            LocalExceptionModel(it).id("error_state").addTo(this)
            return
        }
        if (models.isEmpty()){
            LoadingEpoxyModel().id("loading").addTo(this)
            return
        }
        super.addModels(models)
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

    data class LocalExceptionModel(
        val localException: Event.LocalException
    ) : ViewBindingKotlinModel<ModelLocalExceptionBinding>(R.layout.model_local_exception){
        override fun ModelLocalExceptionBinding.bind() {
            errorTitleTextView.text = localException.title
            errorDescriptionTextView.text = localException.description
        }

        override fun getSpanSize(totalSpanCount: Int, position: Int, itemCount: Int): Int {
            return totalSpanCount
        }
    }
}
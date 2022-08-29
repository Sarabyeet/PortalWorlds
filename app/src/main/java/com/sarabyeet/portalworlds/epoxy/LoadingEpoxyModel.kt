package com.sarabyeet.portalworlds.epoxy

import com.sarabyeet.portalworlds.R
import com.sarabyeet.portalworlds.databinding.ModelLoadingBinding
import com.sarabyeet.travelapp.ui.epoxy.ViewBindingKotlinModel

class LoadingEpoxyModel: ViewBindingKotlinModel<ModelLoadingBinding>(R.layout.model_loading) {
    override fun ModelLoadingBinding.bind() {
        // Nothing to do here
    }

    override fun getSpanSize(totalSpanCount: Int, position: Int, itemCount: Int): Int {
        return totalSpanCount
    }
}
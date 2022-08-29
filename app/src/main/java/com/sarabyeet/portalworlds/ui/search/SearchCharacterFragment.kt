package com.sarabyeet.portalworlds.ui.search

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.sarabyeet.portalworlds.NavGraphDirections
import com.sarabyeet.portalworlds.arch.SearchCharacterViewModel
import com.sarabyeet.portalworlds.databinding.FragmentSearchCharacterBinding
import com.sarabyeet.portalworlds.ui.BaseFragment
import com.sarabyeet.portalworlds.util.Constants
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SearchCharacterFragment : BaseFragment() {

    private var _binding: FragmentSearchCharacterBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SearchCharacterViewModel by viewModels()
    private var currentText = ""
    private val handler = Handler(Looper.getMainLooper())
    private val searchRunnable = Runnable {
        println(currentText)
        viewModel.searchCharacter(currentText)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSearchCharacterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val epoxyController = SearchCharacterEpoxyController { characterId ->
            val directions = NavGraphDirections.actionGlobalToCharacterDetailsFragment(characterId)
            findNavController().navigate(directions)
        }
        binding.epoxyRecyclerView.setControllerAndBuildModels(epoxyController)

        binding.searchEditText.doAfterTextChanged { editable ->
            editable?.let {
                currentText = it.toString()
            }
            handler.removeCallbacks(searchRunnable)
            handler.postDelayed(searchRunnable,Constants.SEARCH_CHARACTER_DELAY)
        }

        lifecycleScope.launch {
            viewModel.flow.collectLatest { pagingData ->
                epoxyController.localException = null
                epoxyController.submitData(pagingData)
            }
        }

        lifecycleScope.launch {
            viewModel.searchEventFlow.collectLatest { localException ->
                epoxyController.localException = localException
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
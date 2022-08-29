package com.sarabyeet.portalworlds.ui.episodes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.PagingData
import com.sarabyeet.portalworlds.NavGraphDirections
import com.sarabyeet.portalworlds.arch.EpisodesViewModel
import com.sarabyeet.portalworlds.databinding.FragmentEpisodesListBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class EpisodesListFragment : Fragment() {
    private var _binding: FragmentEpisodesListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: EpisodesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentEpisodesListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val controller = EpisodesListEpoxyController { episodeId ->
            val directions = NavGraphDirections.actionGlobalToEpisodeDetailsBottomSheetFragment(
                episodeId = episodeId
            )
            findNavController().navigate(directions)
        }

        lifecycleScope.launch {
            viewModel.episodesListFlow.collectLatest { pagingData: PagingData<EpisodesUiModel> ->
                controller.submitData(pagingData)
            }
        }
        binding.epoxyRecyclerView.setControllerAndBuildModels(controller)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
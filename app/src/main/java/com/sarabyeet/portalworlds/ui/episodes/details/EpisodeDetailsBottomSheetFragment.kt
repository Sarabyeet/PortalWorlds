package com.sarabyeet.portalworlds.ui.episodes.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sarabyeet.portalworlds.NavGraphDirections
import com.sarabyeet.portalworlds.R
import com.sarabyeet.portalworlds.arch.EpisodeDetailsViewModel
import com.sarabyeet.portalworlds.databinding.FragmentBottomSheetEpisodeDetailsBinding

class EpisodeDetailsBottomSheetFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentBottomSheetEpisodeDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: EpisodeDetailsViewModel by viewModels()

    private val safeArgs: EpisodeDetailsBottomSheetFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentBottomSheetEpisodeDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.episodeDetailsLiveData.observe(viewLifecycleOwner) { episode ->
            if (episode == null) {
                // Handle Error state
                return@observe
            }

            binding.loadingProgress.isGone = true

            binding.apply {
                charactersLabel.text = getString(R.string.characters)
                airDateTextView.text = episode.airDate
                episodeNameTextView.text = episode.name
                episodeTextView.text = episode.getFormattedSeason()
                epoxyRecyclerView.setControllerAndBuildModels(
                    EpisodeDetailsEpoxyController(episode.characterList) {
                        val directions = NavGraphDirections.actionGlobalToCharacterDetailsFragment(
                            it
                        )
                        findNavController().navigate(directions)
                    }
                )
            }
        }
        viewModel.getEpisodeDetails(safeArgs.episodeId)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
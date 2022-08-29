package com.sarabyeet.portalworlds.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.sarabyeet.portalworlds.NavGraphDirections
import com.sarabyeet.portalworlds.arch.SharedViewModel
import com.sarabyeet.portalworlds.databinding.FragmentCharacterDetailsBinding


class CharacterDetailsFragment : Fragment() {

    private var _binding: FragmentCharacterDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SharedViewModel by lazy {
        ViewModelProvider(this)[SharedViewModel::class.java]
    }

    private val safeArgs: CharacterDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentCharacterDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val controller = CharacterDetailsEpoxyController{ episodeId ->
            val directions = NavGraphDirections.actionGlobalToEpisodeDetailsBottomSheetFragment(episodeId)
            findNavController().navigate(directions)
        }
        viewModel.getCharacterByIdLiveData.observe(viewLifecycleOwner) { character ->
            if (character == null) {
                Toast.makeText(
                    requireActivity(),
                    "Unsuccessful network call!",
                    Toast.LENGTH_SHORT
                ).show()
                findNavController().navigateUp()
                return@observe
            }
            controller.character = character
        }
        val selectedCharacterId = safeArgs.characterId
        binding.epoxyRecyclerView.setControllerAndBuildModels(controller)
        viewModel.fetchCharacter(selectedCharacterId)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
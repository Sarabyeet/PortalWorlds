package com.sarabyeet.portalworlds.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.sarabyeet.portalworlds.NavGraphDirections
import com.sarabyeet.portalworlds.arch.CharactersListViewModel
import com.sarabyeet.portalworlds.databinding.FragmentCharactersListBinding
import kotlinx.coroutines.flow.collectLatest


class CharactersListFragment : Fragment() {

    private var _binding: FragmentCharactersListBinding? = null
    private val binding get() = _binding!!

    private val characterViewModel: CharactersListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentCharactersListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val controller = CharactersListEpoxyController(::onCharacterSelected)

        binding.epoxyRecyclerView.setControllerAndBuildModels(controller)

        lifecycleScope.launchWhenStarted {
            characterViewModel.charactersListFlow.collectLatest {
                controller.submitData(it)
            }
        }
    }

    private fun onCharacterSelected(id: Int) {
        val directions =
            NavGraphDirections.actionGlobalToCharacterDetailsFragment(id)
        findNavController().navigate(directions)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
package com.idplus.asteroideradar.main

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.idplus.asteroideradar.databinding.FragmentMainBinding
import com.idplus.asteroideradar.data.remote.model.Asteroid
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private lateinit var navController: NavController
    private lateinit var asteroidListAdapter: MainAsteroidAdapter
    private val viewModel by activityViewModels<MainViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val fragmentBinding = FragmentMainBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        setupRecyclerView()
        setupObservers()
    }

    private fun setupRecyclerView() {
        // the asteroid list adapter takes a click listener as a parameter
        // the click listener will trigger the view model method used to navigate to the next screen
        // and at the same time post the asteroid item selected by the user upon clicking
        asteroidListAdapter = MainAsteroidAdapter(MainAsteroidAdapter.AsteroidListener { asteroid ->
            viewModel.onAsteroidClicked(asteroid)
        })
        binding.rvAsteroidsList.adapter = asteroidListAdapter
    }

    private fun setupObservers() {

        // observe & submit the list of asteroids to the adapter whenever it changes
        viewModel.asteroids.observe(viewLifecycleOwner) { asteroids ->
            if (asteroids != null) {
                asteroidListAdapter.submitList(asteroids)
            }
        }

        viewModel.downloadInProgress.observe(viewLifecycleOwner) { isDownloading ->
            setDownloadingAsteroids(isDownloading)
        }

        // observe the asteroid item onClick event & navigate to details screen by passing asteroid
        viewModel.navigateToDetailFragment.observe(viewLifecycleOwner) { asteroid ->
            if(asteroid != null) {
                navigateToAsteroidDetails(asteroid)
                viewModel.doneNavigating()
            }
        }
    }

    /**
     * Navigates from this fragment to the detail fragment using the navigation component
     * and safe args plugin for sending the asteroid as an argument
     */
    private fun navigateToAsteroidDetails(asteroid: Asteroid) {
        navController.navigate(
            MainFragmentDirections.actionMainFragmentToDetailFragment(
                asteroid
            )
        )
    }

    private fun setDownloadingAsteroids(isDownloading: Boolean) {
        binding.progressBar.visibility = if(isDownloading) View.VISIBLE else View.GONE
        binding.rvAsteroidsList.visibility = if(isDownloading) View.GONE else View.VISIBLE
        binding.flPictureOfTheDay.visibility = if(isDownloading) View.GONE else View.VISIBLE
    }
}
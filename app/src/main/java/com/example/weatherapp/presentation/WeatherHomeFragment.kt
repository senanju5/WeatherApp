package com.example.weatherapp.presentation

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.weatherapp.databinding.FragmentWeatherHomeBinding
import com.example.weatherapp.presentation.viewmodel.WeatherViewModel
import com.example.weatherapp.utils.getWeatherQuery
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.launch

class WeatherHomeFragment : Fragment() {
    private lateinit var weatherViewModel: WeatherViewModel
    private lateinit var binding: FragmentWeatherHomeBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                // Permission granted, get the location
                getWeatherDetails()
            } else {
                // Permission denied
                Toast.makeText(requireContext(), "Location permission denied", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        weatherViewModel = ViewModelProvider(requireActivity())[WeatherViewModel::class.java]
        binding = FragmentWeatherHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requestPermission()
    }

    init {
        lifecycleScope.launch {
//            repeatOnLifecycle(Lifecycle.State.STARTED) {
//                weatherViewModel.geoCodeUIModel.collect { it ->
//                    Log.d("sen- GeoCode", it.toString())
//                    weatherViewModel.weatherUIModel.collect {
//                        Log.d("sen- Weather", it.toString())
//                    }
//                }
//
//            }
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                    weatherViewModel.weatherUIModel.collect {
                        Log.d("sen- Weather", it.toString())
                }

            }

        }
    }

    private fun requestPermission() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        // Check for location permission
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Request permission
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            // Permission already granted, get the location
            getWeatherDetails()
        }
    }

    private fun getWeatherDetails() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    // Use the location to get weather data
                    val latitude: Double = location.latitude
                    val longitude:Double = location.longitude
                    weatherViewModel.getWeatherDetails(getWeatherQuery(latitude, longitude))
                } else {
                    Toast.makeText(requireContext(), "Unable to get location", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}
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
import coil.load
import com.example.weatherapp.R
import com.example.weatherapp.data.model.WeatherUIDetails
import com.example.weatherapp.databinding.FragmentWeatherHomeBinding
import com.example.weatherapp.presentation.viewmodel.WeatherViewModel
import com.example.weatherapp.utils.getGeoCodeQuery
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
                // TODO:Permission denied check for shared preferencece lat long to show weather
                Toast.makeText(requireContext(), "Location permission denied", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        weatherViewModel = ViewModelProvider(requireActivity())[WeatherViewModel::class.java]
        binding = FragmentWeatherHomeBinding.inflate(inflater, container, false)
        binding.searchView.searchButton.setOnClickListener {
            val query = binding.searchView.cityEditTextView.text.toString()
            if (query.isNotEmpty()){
                weatherViewModel.getGeoCode(getGeoCodeQuery(query))
            } else {
                Toast.makeText(requireContext(), "please enter some value to search", Toast.LENGTH_SHORT).show()

            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requestPermission()
    }

    init {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                    weatherViewModel.weatherUIModel.collect {
                        Log.d("sen- Weather", it.toString())
                        if (it.weatherDetails!=null)  {
                            setWeatherDetails(it.weatherDetails)
                        } else {

                        }
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

    private fun setWeatherDetails (weatherUIDetails: WeatherUIDetails) {
        binding.weatherDetailsView.dateTimeTextView.text = weatherUIDetails.dateTime
        binding.weatherDetailsView.temperatureTextView.text = weatherUIDetails.temperature
        binding.weatherDetailsView.weatherConditionTextView.text = weatherUIDetails.weatherIconDesc
        binding.weatherDetailsView.cityTextView.text = weatherUIDetails.cityAndCountry
        binding.weatherDetailsView.humidityValue.text = weatherUIDetails.humidity
        binding.weatherDetailsView.pressureValue.text = weatherUIDetails.pressure
        binding.weatherDetailsView.visibilityValue.text = weatherUIDetails.visibility
        binding.weatherDetailsView.sunRaiseValue.text = weatherUIDetails.sunrise
        binding.weatherDetailsView.sunSetValue.text = weatherUIDetails.sunset
        binding.weatherDetailsView.weatherIcon.load(weatherUIDetails.weatherIcon){
            placeholder(R.drawable.ic_launcher_background) // Placeholder image
            error(R.drawable.ic_launcher_background)
        }



    }

}
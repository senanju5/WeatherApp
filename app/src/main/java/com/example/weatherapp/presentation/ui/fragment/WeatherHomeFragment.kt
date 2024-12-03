package com.example.weatherapp.presentation.ui.fragment

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import coil.load
import com.example.weatherapp.R
import com.example.weatherapp.data.model.WeatherUIDetails
import com.example.weatherapp.databinding.FragmentWeatherHomeBinding
import com.example.weatherapp.presentation.viewmodel.WeatherViewModel
import com.example.weatherapp.utils.CitiUtils.isValidStateOrCity
import com.example.weatherapp.utils.SharePreferenceUtils.KEY_LATITUDE
import com.example.weatherapp.utils.SharePreferenceUtils.KEY_LONGITUDE
import com.example.weatherapp.utils.SharePreferenceUtils.LOCATION_PREFS
import com.example.weatherapp.utils.getGeoCodeQuery
import com.example.weatherapp.utils.getWeatherQuery
import com.example.weatherapp.utils.hide
import com.example.weatherapp.utils.show
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class WeatherHomeFragment : Fragment() {
    private var currentLat: String = ""
    private var currentLon: String = ""
    private  val weatherViewModel by viewModels<WeatherViewModel>()
    private lateinit var binding: FragmentWeatherHomeBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    /**
     * Register the permissions callback, which handles the user's response to the
     */
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                getWeatherDetails()
            } else {
                getWeatherDetailsByLastLocation()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWeatherHomeBinding.inflate(inflater, container, false)
        binding.searchView.searchButton.setOnClickListener {
            val query = binding.searchView.cityEditTextView.text.toString()
            if (query.isNotEmpty()) {
                //TODO: InFuture this needs to be handled using GooGle API
                if (isValidStateOrCity(query)) {
                    weatherViewModel.getGeoCode(getGeoCodeQuery(query))
                } else {
                    Toast.makeText(
                        requireContext(),
                        "please enter valid city or state in USA",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            } else {
                Toast.makeText(
                    requireContext(),
                    "please enter some value to search",
                    Toast.LENGTH_SHORT
                ).show()

            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requestPermission()
    }


    init {
        /**
         * Observing the weather UI model
         */
      lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                weatherViewModel.weatherUIModel.collect {
                        if (it.isLoading) binding.progressLoader.show() else binding.progressLoader.hide()
                    if (it.weatherDetails != null) {
                        binding.weatherDetailsView.root.show()
                        setWeatherDetails(it.weatherDetails)
                    } else {
                        if (it.isError) {
                            binding.weatherErrorView.root.show()
                            binding.weatherDetailsView.root.hide()
                        }
                    }
                }

            }

        }
    }

    /**
     * requesting the location permission
     */
    private fun requestPermission() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        // Check for location permission
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Request permission
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            getWeatherDetails()
        }
    }

    /**
     * getting the weather details
     */
    private fun getWeatherDetails() {

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    // Use the location to get weather data
                    val latitude: String = location.latitude.toString()
                    val longitude: String = location.longitude.toString()
                    updateGeoCode(latitude, longitude)
                    weatherViewModel.getWeatherDetails(getWeatherQuery(latitude, longitude))
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Unable to get location Due to GPS lost ",
                        Toast.LENGTH_SHORT
                    ).show()
                    getWeatherDetailsByLastLocation()
                }
            }
        }
    }

    /**
     * setting the weather details
     */
    private fun setWeatherDetails(weatherUIDetails: WeatherUIDetails) {
        binding.weatherDetailsView.dateTimeTextView.text = weatherUIDetails.dateTime
        binding.weatherDetailsView.temperatureTextView.text = weatherUIDetails.temperature
        binding.weatherDetailsView.weatherConditionTextView.text = weatherUIDetails.weatherIconDesc
        binding.weatherDetailsView.cityTextView.text = weatherUIDetails.cityAndCountry
        binding.weatherDetailsView.humidityValue.text = weatherUIDetails.humidity
        binding.weatherDetailsView.pressureValue.text = weatherUIDetails.pressure
        binding.weatherDetailsView.visibilityValue.text = weatherUIDetails.visibility
        binding.weatherDetailsView.sunRaiseValue.text = weatherUIDetails.sunrise
        binding.weatherDetailsView.sunSetValue.text = weatherUIDetails.sunset
        binding.weatherDetailsView.weatherIcon.load(weatherUIDetails.weatherIcon) {
            placeholder(R.drawable.ic_launcher_background) // Placeholder image
            error(R.drawable.ic_launcher_background)
        }
        updateGeoCode(weatherUIDetails.lat, weatherUIDetails.lon)
    }

    /**
     * saving the location
     */
    override fun onStop() {
        super.onStop()
        saveLocation(currentLat, currentLon)
    }

    /**
     * saving the location
     */
    override fun onPause() {
        super.onPause()
        saveLocation(currentLat, currentLon)
    }

    /**
     * updating the last used geo code
     */
    private fun updateGeoCode(lat: String, lon: String) {
        currentLat = lat
        currentLon = lon
    }

    /**
     * saving the location into shared preference
     */
    private fun saveLocation(latitude: String, longitude: String) {
        val sharedPref =
            requireActivity().getSharedPreferences(LOCATION_PREFS, Context.MODE_PRIVATE)
        if (latitude.isNotEmpty() && longitude.isNotEmpty()) {
            val editor = sharedPref.edit()
            editor.putString(KEY_LATITUDE, latitude)
            editor.putString(KEY_LONGITUDE, longitude)
            editor.apply()
        }
    }

    /**
     * getting the last used location from shared preference
     */
    private fun getLastLocation(): Pair<String, String>? {
        val sharedPref =
            requireActivity().getSharedPreferences(LOCATION_PREFS, Context.MODE_PRIVATE)
        val lat = sharedPref.getString(KEY_LATITUDE, null)
        val lon = sharedPref.getString(KEY_LONGITUDE, null)

        return if (lat != null && lon != null) {
            Pair(lat, lon)
        } else {
            null
        }
    }

    private fun getWeatherDetailsByLastLocation() {
        val location = getLastLocation()
        if (location == null) {
            Toast.makeText(
                requireContext(),
                "Please enter the values manually to get the weather",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            location.let {
                val latitude = it.first
                val longitude = it.second
                // Use the latitude and longitude
                updateGeoCode(latitude, longitude)
                weatherViewModel.getWeatherDetails(getWeatherQuery(latitude, longitude))
            }
        }
    }
}
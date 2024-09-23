package com.example.weatherapp.presentation

import android.Manifest
import android.content.Context
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
    private var currentLat: String = ""
    private var currentLon: String = ""
    private lateinit var weatherViewModel: WeatherViewModel
    private lateinit var binding: FragmentWeatherHomeBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                getWeatherDetails()
            } else {
                val location = getLastLocation()
                if (location == null) {
                    Toast.makeText(requireContext(), "Please enter the values manually to get the weather", Toast.LENGTH_SHORT).show()
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
                        binding.progressLoader.visibility =if (it.isLoading) View.VISIBLE else View.GONE
                        if (it.weatherDetails!=null)  {
                            binding.weatherDetailsView.root.visibility = View.VISIBLE
                            setWeatherDetails(it.weatherDetails)
                        } else {
                            if(it.isError) {
                                binding.weatherErrorView.root.visibility = View.VISIBLE
                                binding.weatherDetailsView.root.visibility = View.GONE
                            }
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
                    val latitude: String = location.latitude.toString()
                    val longitude:String = location.longitude.toString()
                    updateGeoCode(latitude, longitude)
                    weatherViewModel.getWeatherDetails(getWeatherQuery(latitude, longitude))
                } else {
                    Toast.makeText(requireContext(), "Unable to get location Due to GPS lost", Toast.LENGTH_SHORT).show()
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
        updateGeoCode(weatherUIDetails.lat, weatherUIDetails.lon)
    }

    override fun onStop() {
        super.onStop()
        saveLocation(currentLat, currentLon)
    }

    override fun onPause() {
       super.onPause()
        saveLocation(currentLat, currentLon)
    }

    private fun updateGeoCode(lat: String, lon: String) {
        currentLat = lat
        currentLon = lon
    }

    private fun saveLocation(latitude: String, longitude: String) {
        val sharedPref = requireActivity().getSharedPreferences("location_prefs", Context.MODE_PRIVATE)
        if(latitude.isNotEmpty() && longitude.isNotEmpty()){
            val editor = sharedPref.edit()
            editor.putString("latitude", latitude)
            editor.putString("longitude", longitude)
            editor.apply() // Save the data asynchronously
        }
    }

    fun getLastLocation(): Pair<String, String>? {
        val sharedPref = requireActivity().getSharedPreferences("location_prefs", Context.MODE_PRIVATE)
        val lat = sharedPref.getString("latitude", null)
        val lon = sharedPref.getString("longitude", null)

        return if (lat != null && lon != null) {
            Pair(lat, lon)
        } else {
            null
        }
    }

}
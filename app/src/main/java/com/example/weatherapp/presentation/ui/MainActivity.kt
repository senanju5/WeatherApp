package com.example.weatherapp.presentation.ui
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.weatherapp.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

//    init {
//        lifecycleScope.launch {
//            repeatOnLifecycle(Lifecycle.State.STARTED) {
//                weatherViewModel.geoCodeUIModel.collect { it ->
//                    Log.d("sen- GeoCode", it.toString())
//                    weatherViewModel.weatherUIModel.collect {
//                        Log.d("sen- Weather", it.toString())
//                        binding.cityTextView.text = it.weatherDetails?.cityAndCountry ?: "error"
//                    }
//                }
//
//            }
//
//        }
//    }
}
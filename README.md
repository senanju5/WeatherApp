# Weather App (MVVM architecture)
Welcome to the OpenWeatherMap Android App! This app provides real-time weather data and forecasts, powered by the OpenWeatherMap API. The app allows users to check current weather conditions, receive forecasts, and search weather information by city.

<img width="372" alt="Screenshot 2024-09-24 at 8 57 30 AM" src="https://github.com/user-attachments/assets/38fff088-a5eb-4f77-9b3c-09787cabd9b9">


# Features
Current Weather: Get real-time weather details such as temperature, humidity, wind speed, and more.
City Search: Search for weather data in any city worldwide.
Geolocation Support: Automatically detect and display the weather for your current location.

# Getting Started
# Prerequisites
Android Studio installed on your system. You can download it from here.
OpenWeatherMap API Key. Sign up for a free API key at OpenWeatherMap.
#Installation
# Clone the repository:
git clone https://github.com/senanju5/WeatherApp.git
Open the project in Android Studio:

# Launch Android Studio.
Select Open an existing project.
Navigate to the cloned repository directory and open it.

# Build the app:
In Android Studio, select Build > Make Project to build the app.

# Run the app:
Connect an Android device or use the Android Emulator.
Click the green "Run" button in Android Studio to launch the app on your device.

#A PI Integration
The app utilizes the OpenWeatherMap API to fetch weather data. The following endpoints are used:
# Current Weather Data:
Endpoint: /geo/1.0/direct
Parameters: q={city_name},  appid={API_key}
Endpoint: /data/2.5/weather
Parameters: lat={lat},  lon={lon}, appid={API_key}, units={units}

# The app requires the following permissions:

Access Fine Location: To fetch weather data for the user's current location.
Internet: To fetch data from the OpenWeatherMap API.
Make sure to declare the necessary permissions in the AndroidManifest.xml file:

<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
<uses-permission android:name="android.permission.INTERNET" />

# Usage
# Home Screen
Displays the current weather based on the user’s location.
City Search

# Troubleshooting
# Common Issues:
API Key Not Working: Ensure you are using a valid API key.
Location Permissions Not Granted: Check if the user has granted location access in their device settings.

# Debugging:
Use Android Studio’s Logcat to view log messages and debug errors.

Acknowledgments
Thanks to OpenWeatherMap for providing free weather data APIs.

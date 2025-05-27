# **🌦️ Weather App (Open-Meteo API)**

## 🗂️ Project Overview
This Weather App allows users to check the current weather for any city. It uses the Open-Meteo Geocoding API to find location coordinates and then fetches weather data using the Open-Meteo Weather Forecast API. This is a Java application with a modern Swing GUI. 
- **Technology Stack:** Java 11+, Swing, Gson library
- **Purpose:** Type a city name, fetch and display its current weather instantly
- **API Source:** [Open-Meteo Geocoding](https://open-meteo.com/en/docs/geocoding-api) and [Open-Meteo Weather](https://open-meteo.com/en/docs#current)

## App Features

- Search bar to enter a city name
- Display of temperature, wind speed, weather description, humidity, wind, precipitation
- Error message if city is not found/ empty field/ numbers only
- Supports only city searches not country searches
- Checked the code for privacy concerns and license conflicts
- Mobile Friendliness (Small Screens)
- To validate the code in the WeatherApp, created a separate test class called WeatherAppTest.java that calls my static method getWeatherForCity(String cityName) and checks its output. Since, it's   not a good practice to add test cases inside my Swing GUI app, I kept my tests separate for clarity and maintainability. (JUnit test classes: Run with the JUnit test runner)

## ⚙️ Installation Instructions

1. **Clone or Download the Repository**  
   git clone https://github.com/Thaksha/submission-weather-app.git
2. **Requirements**
   - Java 11 or higher
   - [gson-X.X.X.jar](https://github.com/google/gson)library (e.g., `gson-2.13.1.jar`)
   - junit-platform-console-standalone-1.9.3.jar

3. **Compile**
   ```sh
   javac -cp gson-2.13.1.jar WeatherApp.java 
   ```
  
4. **Run**
   ```sh
   java -cp ".;gson-2.13.1.jar" WeatherApp
   
   java -jar .\junit-platform-console-standalone-1.9.3.jar --class-path ".;gson-2.13.1.jar" --scan-class-path (for testing)
   ```
## 🚀 Usage Guide

1. **Launch** the application.
2. **Enter a city name** (e.g., `London`, `New York`) in the text field.
3. **Click** the **Get Weather** button.
4. **View the results** in the output area:
    - City name
    - Temperature in Celsius
    - Weather description
    - Humidity
    - Wind Speed
    - Precipitation

---

## 📝 Example Output

**Successful Query:**
```
{
  London
  Overcast
  🌡️ Temperature: 15.8 °C
  💨 Wind: 18.6 km/h
}
```

**Invalid City:**
```
{
  ❗ City "Qwertyuiop" not found.
}
```

---

## ✨ Features

- 🪟 Clean, intuitive Swing GUI
- 🌍 Real-time weather for any city worldwide
- 📝 Human-readable weather description (Open-Meteo code mapping)
- 💡 Error handling for:
  - Empty input
  - Invalid/non-existent city
  - Network/API errors
  - Simulated failures (for testing)
  - 🌐Full Unicode and special character support in city names

---

## ⚠️ Error Handling

- **Empty Input:** Prompts user to enter a city name.
- **Numeric Input:** Shows: “Please enter a valid city name (not a number).”
- **Country Name Input:** Shows: “Please enter a city, not a country name.”
- **City Not Found (API returns no results):** Shows: “City "X" not found.”
- **Network/API Errors:** Friendly error messages if the API is down or unreachable.
- **Malformed/Unavailable Data:** Handles unexpected API responses gracefully.
- **Simulated API Failures:** Enter `"simulate_api_failure"`, `"simulate_weather_api_failure"`, or `"simulate_malformed_response"` as the city name to trigger test error cases.
  
---

## 🌐 API Information

- **Geocoding API:**  
  `https://geocoding-api.open-meteo.com/v1/search?name={CITY}&count=1&language=en&format=json`

- **Weather API:**  
  `https://api.open-meteo.com/v1/forecast?latitude={LAT}&longitude={LON}&current_weather=true`

- **Weather Code Mapping:**  
  Weather codes are translated to descriptions, as documented in [Open-Meteo's API Docs](https://open-meteo.com/en/docs#weathercode).

---

## 🚧 Future Improvements

- [ ] Add weather forecast (multi-day)
- [ ] Allow switching temperature units (C/F)
- [ ] Show colourful weather icons/graphics
- [ ] Add search history and offline cache
- [ ] Enhance logging (user actions, error details)
- [ ] Localization/multi-language support
- [ ] Package as an installer or runnable JAR
- [ ] Expand test coverage with mocked API responses

---

## 📄 License

Gson (Apache-2.0 License) is used

## Third-party Libraries

- Gson library- Widely used, business-friendly
  - License: The Gson License (http://www.apache.org/licenses/LICENSE-2.0)

---

## 🙏 Acknowledgements

- [Open-Meteo](https://open-meteo.com/) for free weather and geocoding APIs
- The Gson License (http://www.apache.org/licenses/LICENSE-2.0) for using Gson
---

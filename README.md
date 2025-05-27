# **🌦️ Weather App (Open-Meteo API)**

## Project Overview
This Weather App allows users to check the current weather for any city. It uses the Open-Meteo Geocoding API to find location coordinates and then fetches weather data using the Open-Meteo Weather Forecast API. This is a Java application with a modern Swing GUI. 
- **Technology Stack:** Java 11+, Swing, Gson library
- **Purpose:** Type a city name, fetch and display its current weather instantly
- **API Source:** [Open-Meteo Geocoding](https://open-meteo.com/en/docs/geocoding-api) and [Open-Meteo Weather](https://open-meteo.com/en/docs#current)

## App Features

- Search bar to enter a city name
- Display of temperature, wind speed, weather description, humidity, wind, precipitation
- Error message if city is not found/ empty field/ numbers only
- Supports only city searches not country searches

## ⚙️ Installation Instructions

1. **Clone or Download the Repository**

2. **Requirements**
   - Java 11 or higher
   - [org.json](https://github.com/stleary/JSON) library (e.g., `json-20240303.jar`)

3. **Compile**
   ```sh
   javac -cp ".;json-20240303.jar" WeatherApp.java
   ```
   *(On Mac/Linux, use `:` instead of `;` in the classpath)*

4. **Run**
   ```sh
   java -cp ".;json-20240303.jar" WeatherApp
   ```

---


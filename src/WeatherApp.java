import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.http.*;
import java.net.URI;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * Simple Weather App using Gson, with emoji icons for weather and mobile-friendly layout.
 * Now includes a check to prevent using country names as input.
 */
public class WeatherApp extends JFrame {
    private JTextField cityInput;
    private JButton getWeatherButton;
    private JTextPane outputPane;

    // --- Country names list for exclusion ---
    private static final String[] COUNTRY_NAMES = {
        "Afghanistan", "Albania", "Algeria", "Andorra", "Angola", "Argentina", "Armenia",
        "Australia", "Austria", "Azerbaijan", "Bahamas", "Bahrain", "Bangladesh", "Barbados",
        "Belarus", "Belgium", "Belize", "Benin", "Bhutan", "Bolivia", "Bosnia", "Botswana",
        "Brazil", "Brunei", "Bulgaria", "Burkina Faso", "Burundi", "Cambodia", "Cameroon",
        "Canada", "Cape Verde", "Central African Republic", "Chad", "Chile", "China", "Colombia",
        "Comoros", "Congo", "Costa Rica", "Croatia", "Cuba", "Cyprus", "Czechia", "Denmark",
        "Djibouti", "Dominica", "Dominican Republic", "Ecuador", "Egypt", "El Salvador",
        "Equatorial Guinea", "Eritrea", "Estonia", "Eswatini", "Ethiopia", "Fiji", "Finland",
        "France", "Gabon", "Gambia", "Georgia", "Germany", "Ghana", "Greece", "Grenada",
        "Guatemala", "Guinea", "Guinea-Bissau", "Guyana", "Haiti", "Honduras", "Hungary",
        "Iceland", "India", "Indonesia", "Iran", "Iraq", "Ireland", "Israel", "Italy", "Jamaica",
        "Japan", "Jordan", "Kazakhstan", "Kenya", "Kiribati", "Kuwait", "Kyrgyzstan", "Laos",
        "Latvia", "Lebanon", "Lesotho", "Liberia", "Libya", "Liechtenstein", "Lithuania",
        "Luxembourg", "Madagascar", "Malawi", "Malaysia", "Maldives", "Mali", "Malta",
        "Marshall Islands", "Mauritania", "Mauritius", "Mexico", "Micronesia", "Moldova",
        "Monaco", "Mongolia", "Montenegro", "Morocco", "Mozambique", "Myanmar", "Namibia",
        "Nauru", "Nepal", "Netherlands", "New Zealand", "Nicaragua", "Niger", "Nigeria",
        "North Korea", "North Macedonia", "Norway", "Oman", "Pakistan", "Palau", "Panama",
        "Papua New Guinea", "Paraguay", "Peru", "Philippines", "Poland", "Portugal", "Qatar",
        "Romania", "Russia", "Rwanda", "Saint Kitts and Nevis", "Saint Lucia",
        "Saint Vincent and the Grenadines", "Samoa", "San Marino", "Sao Tome and Principe",
        "Saudi Arabia", "Senegal", "Serbia", "Seychelles", "Sierra Leone", "Singapore",
        "Slovakia", "Slovenia", "Solomon Islands", "Somalia", "South Africa", "South Korea",
        "South Sudan", "Spain", "Sri Lanka", "Sudan", "Suriname", "Sweden", "Switzerland",
        "Syria", "Taiwan", "Tajikistan", "Tanzania", "Thailand", "Timor-Leste", "Togo", "Tonga",
        "Trinidad and Tobago", "Tunisia", "Turkey", "Turkmenistan", "Tuvalu", "Uganda",
        "Ukraine", "United Arab Emirates", "United Kingdom", "United States", "Uruguay",
        "Uzbekistan", "Vanuatu", "Vatican City", "Venezuela", "Vietnam", "Yemen", "Zambia",
        "Zimbabwe"
    };
    // --- End Country names list ---

    public WeatherApp() {           
        setTitle("Simple Weather App");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Use BoxLayout Y_AXIS for mobile-friendly stacking
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        JLabel cityLabel = new JLabel("Enter city name:");
        cityLabel.setFont(new Font("Arial", Font.BOLD, 14));
        cityLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        cityInput = new JTextField(18);
        cityInput.setFont(new Font("Arial", Font.PLAIN, 14));
        cityInput.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        cityInput.setAlignmentX(Component.CENTER_ALIGNMENT);

        getWeatherButton = new JButton("\uD83C\uDF24 Get Weather"); // ☀️ Get Weather
        getWeatherButton.setFont(new Font("Arial", Font.BOLD, 14));
        getWeatherButton.setBackground(new Color(80, 170, 255));
        getWeatherButton.setForeground(Color.MAGENTA);
        getWeatherButton.setFocusPainted(false);
        getWeatherButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        getWeatherButton.setPreferredSize(new Dimension(180, 40));

        outputPane = new JTextPane();
        outputPane.setEditable(false);
        outputPane.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 12));
        outputPane.setBackground(new Color(245, 245, 245));
        outputPane.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Weather Output"),
                BorderFactory.createEmptyBorder(12, 12, 12, 12)
        ));

        JScrollPane scrollPane = new JScrollPane(outputPane);
        scrollPane.setPreferredSize(new Dimension(315, 185));
        scrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);

        mainPanel.add(cityLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        mainPanel.add(cityInput);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        mainPanel.add(getWeatherButton);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 16)));
        mainPanel.add(scrollPane);

        setContentPane(mainPanel);

        setMinimumSize(new Dimension(340, 350));
        setPreferredSize(new Dimension(350, 400));
        setResizable(false);
        pack();
        setLocationRelativeTo(null); // Center on screen

        getWeatherButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String city = cityInput.getText().trim();
                if (city.isEmpty()) {
                    setOutput("❗ <b>Please enter a city name.</b>");
                    return;
                }
                if (city.matches("\\d+")) {
                    setOutput("❗ <b>Please enter a valid city name (not a number).</b>");
                    return;
                }
                if (isCountryName(city)) {
                    setOutput("❗ <b>Please enter a city, not a country name.</b>");
                    return;
                }
                setOutput("<i>Fetching weather data...</i>");
                getWeatherButton.setEnabled(false);
                // Run API call in a new thread to avoid freezing UI
                new Thread(() -> {
                    JsonObject result = getWeatherForCity(city);
                    SwingUtilities.invokeLater(() -> {
                        setOutput(formatWeatherOutput(result));
                        getWeatherButton.setEnabled(true);
                    });
                }).start();
            }
        });
    }

    // --- Check if input is a country name (case-insensitive, trims whitespace) ---
    public static boolean isCountryName(String input) {
        if (input == null) return false;
        String trimmed = input.trim();
        for (String country : COUNTRY_NAMES) {
            if (country.equalsIgnoreCase(trimmed)) {
                return true;
            }
        }
        return false;
    }
    // ---

    public static JsonObject getWeatherForCity(String cityName) {
        HttpClient client = HttpClient.newHttpClient();
        Gson gson = new Gson();

        try {
            String geoUrl = "https://geocoding-api.open-meteo.com/v1/search?name=" +
                    java.net.URLEncoder.encode(cityName, java.nio.charset.StandardCharsets.UTF_8) +
                    "&count=1&language=en&format=json";

            HttpRequest geoRequest = HttpRequest.newBuilder()
                    .uri(URI.create(geoUrl))
                    .build();

            HttpResponse<String> geoResponse = client.send(geoRequest, HttpResponse.BodyHandlers.ofString());

            if (geoResponse.statusCode() != 200) {
                JsonObject error = new JsonObject();
                error.addProperty("error", "Could not fetch location data. Please try again later.");
                return error;
            }

            JsonObject geoJson = gson.fromJson(geoResponse.body(), JsonObject.class);
            JsonArray results = geoJson.has("results") ? geoJson.getAsJsonArray("results") : null;
            if (results == null || results.size() == 0) {
                JsonObject error = new JsonObject();
                error.addProperty("error", "City \"" + cityName + "\" not found.");
                return error;
            }

            JsonObject location = results.get(0).getAsJsonObject();
            double latitude = location.get("latitude").getAsDouble();
            double longitude = location.get("longitude").getAsDouble();
            String resolvedName = location.get("name").getAsString();

            String weatherUrl = "https://api.open-meteo.com/v1/forecast?latitude=" + latitude +
                    "&longitude=" + longitude +
                    "&current_weather=true" +
                    "&hourly=relative_humidity_2m,precipitation" +
                    "&timezone=auto";

            HttpRequest weatherRequest = HttpRequest.newBuilder()
                    .uri(URI.create(weatherUrl))
                    .build();

            HttpResponse<String> weatherResponse = client.send(weatherRequest, HttpResponse.BodyHandlers.ofString());

            if (weatherResponse.statusCode() != 200) {
                JsonObject error = new JsonObject();
                error.addProperty("error", "Could not fetch weather data. Please try again later.");
                return error;
            }

            JsonObject weatherJson = gson.fromJson(weatherResponse.body(), JsonObject.class);
            if (!weatherJson.has("current_weather")) {
                JsonObject error = new JsonObject();
                error.addProperty("error", "Weather data is unavailable for this location.");
                return error;
            }

            JsonObject currentWeather = weatherJson.getAsJsonObject("current_weather");
            double temperature = currentWeather.get("temperature").getAsDouble();
            int weatherCode = currentWeather.get("weathercode").getAsInt();
            double windspeed = currentWeather.has("windspeed") ? currentWeather.get("windspeed").getAsDouble()
                    : Double.NaN;

            // Get humidity and precipitation for current hour if available
            Double humidity = null;
            Double precipitation = null;
            if (weatherJson.has("hourly")) {
                JsonObject hourly = weatherJson.getAsJsonObject("hourly");
                JsonArray timeArray = hourly.has("time") ? hourly.getAsJsonArray("time") : null;
                JsonArray humidityArray = hourly.has("relative_humidity_2m")
                        ? hourly.getAsJsonArray("relative_humidity_2m")
                        : null;
                JsonArray precipitationArray = hourly.has("precipitation") ? hourly.getAsJsonArray("precipitation")
                        : null;

                if (timeArray != null && humidityArray != null && precipitationArray != null) {
                    String currentTime = currentWeather.has("time") ? currentWeather.get("time").getAsString() : null;
                    int currIdx = -1;
                    if (currentTime != null) {
                        for (int i = 0; i < timeArray.size(); i++) {
                            if (timeArray.get(i).getAsString().equals(currentTime)) {
                                currIdx = i;
                                break;
                            }
                        }
                    }
                    if (currIdx != -1) {
                        humidity = humidityArray.get(currIdx).isJsonNull() ? null
                                : humidityArray.get(currIdx).getAsDouble();
                        precipitation = precipitationArray.get(currIdx).isJsonNull() ? null
                                : precipitationArray.get(currIdx).getAsDouble();
                    }
                }
            }

            JsonObject result = new JsonObject();
            result.addProperty("city", resolvedName);
            result.addProperty("temperature_celsius", temperature);
            result.addProperty("weather_description", getWeatherDescription(weatherCode));
            result.addProperty("weather_emoji", getWeatherEmoji(weatherCode));
            if (!Double.isNaN(windspeed))
                result.addProperty("wind_speed_kmh", windspeed);
            if (humidity != null && !Double.isNaN(humidity))
                result.addProperty("humidity_percent", humidity);
            if (precipitation != null && !Double.isNaN(precipitation))
                result.addProperty("precipitation_mm", precipitation);
            return result;
        } catch (IOException e) {
            JsonObject error = new JsonObject();
            error.addProperty("error", "Network error occurred. Please check your connection.");
            return error;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            JsonObject error = new JsonObject();
            error.addProperty("error", "Request was interrupted.");
            return error;
        } catch (Exception e) {
            JsonObject error = new JsonObject();
            error.addProperty("error", "Unexpected error: " + e.getMessage());
            return error;
        }
    }

    /**
     * Formats the weather output with emojis and bold HTML for the JTextPane.
     */
    private static String formatWeatherOutput(JsonObject result) {
        if (result.has("error")) {
            return "<span style='color:#B22222;font-size:12px;'>❗ <b>" + escapeHtml(result.get("error").getAsString()) + "</b></span>";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("<span style='font-size:18px;'>").append(result.get("weather_emoji").getAsString()).append("</span><br>");
        sb.append("<b>").append(escapeHtml(result.get("city").getAsString())).append("</b><br>");
        sb.append("<span style='font-size:18px;'>")
          .append(result.get("weather_description").getAsString()).append("</span><br>");
        sb.append("<b>🌡️ Temperature:</b> ").append(result.get("temperature_celsius").getAsDouble()).append(" °C<br>");
        if (result.has("humidity_percent")) {
            sb.append("<b>💧 Humidity:</b> ").append(result.get("humidity_percent").getAsDouble()).append(" %<br>");
        }
        if (result.has("wind_speed_kmh")) {
            sb.append("<b>💨 Wind:</b> ").append(result.get("wind_speed_kmh").getAsDouble()).append(" km/h<br>");
        }
        if (result.has("precipitation_mm")) {
            sb.append("<b>🌧️ Precipitation:</b> ").append(result.get("precipitation_mm").getAsDouble()).append(" mm<br>");
        }
        return sb.toString();
    }

    // Utility for displaying HTML in JTextPane, including emojis
    private void setOutput(String html) {
        outputPane.setContentType("text/html");
        outputPane.setText("<html><body style='font-family:Segoe UI Emoji,Arial,sans-serif;font-size:17px;'>" + html + "</body></html>");
        outputPane.setCaretPosition(0);
    }

    // Maps Open-Meteo weather codes to string descriptions
    public static String getWeatherDescription(int code) {
        switch (code) {
            case 0: return "Clear sky";
            case 1: return "Mainly clear";
            case 2: return "Partly cloudy";
            case 3: return "Overcast";
            case 45: return "Fog";
            case 48: return "Depositing rime fog";
            case 51: return "Light drizzle";
            case 53: return "Moderate drizzle";
            case 55: return "Dense drizzle";
            case 56: return "Light freezing drizzle";
            case 57: return "Dense freezing drizzle";
            case 61: return "Slight rain";
            case 63: return "Moderate rain";
            case 65: return "Heavy rain";
            case 66: return "Light freezing rain";
            case 67: return "Heavy freezing rain";
            case 71: return "Slight snow fall";
            case 73: return "Moderate snow fall";
            case 75: return "Heavy snow fall";
            case 77: return "Snow grains";
            case 80: return "Slight rain showers";
            case 81: return "Moderate rain showers";
            case 82: return "Violent rain showers";
            case 85: return "Slight snow showers";
            case 86: return "Heavy snow showers";
            case 95: return "Thunderstorm";
            case 96: return "Thunderstorm with slight hail";
            case 99: return "Thunderstorm with heavy hail";
            default: return "Unknown";
        }
    }

    // Maps Open-Meteo weather codes to emojis
    public static String getWeatherEmoji(int code) {
        switch (code) {
            case 0: return "\u2600\uFE0F"; // ☀️
            case 1: return "\uD83C\uDF24"; // 🌤️ mainly clear
            case 2: return "\u26C5";       // ⛅ partly cloudy
            case 3: return "\u2601\uFE0F"; // ☁️ overcast
            case 45: case 48: return "\uD83C\uDF2B"; // 🌫️ fog
            case 51: case 53: case 55: case 56: case 57: return "\uD83D\uDCA7"; // 💧 drizzle
            case 61: case 63: case 65: case 66: case 67: return "\uD83C\uDF27"; // 🌧️ rain
            case 71: case 73: case 75: case 77: case 85: case 86: return "\u2744\uFE0F"; // ❄️ snow
            case 80: case 81: case 82: return "\uD83C\uDF26"; // 🌦️ rain showers
            case 95: return "\u26A1"; // ⚡ thunderstorm
            case 96: case 99: return "\uD83C\uDF29"; // 🌩️ thunderstorm with hail
            default: return "\u2753"; // ❓ unknown
        }
    }

    // Escape HTML special chars for city name etc.
    public static String escapeHtml(String s) {
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }

    public static void main(String[] args) {
        // Set up a simple look and feel for beginners
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }
        SwingUtilities.invokeLater(() -> {
            WeatherApp app = new WeatherApp();
            app.setVisible(true);
        });
    }
}
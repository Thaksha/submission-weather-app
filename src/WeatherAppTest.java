// import org.junit.jupiter.api.Test;
// import static org.junit.jupiter.api.Assertions.*;
// import java.util.Map;

// public class WeatherAppTest {
//     @Test
//     public void testValidCityName() {
//         Map<String, Object> res = WeatherApp.getWeatherForCity("London");
//         assertEquals("London", res.get("city"));
//         assertTrue(res.containsKey("temperature_celsius"));
//         assertTrue(res.containsKey("weather_description"));
//     }

//     @Test
//     public void testCityWithSpacesAndSpecialCharacters() {
//         Map<String, Object> res = WeatherApp.getWeatherForCity("New York");
//         assertEquals("New York", res.get("city"));
//         assertTrue(res.containsKey("temperature_celsius"));
//     }

//     @Test
//     public void testNonexistentCityName() {
//         Map<String, Object> res = WeatherApp.getWeatherForCity("Qwertyuiop");
//         assertTrue(res.containsKey("error"));
//     }

//     @Test
//     public void testEmptyInput() {
//         Map<String, Object> res = WeatherApp.getWeatherForCity("");
//         assertTrue(res.containsKey("error"));
//     }

//     @Test
//     public void testUnicodeCityName() {
//         Map<String, Object> res = WeatherApp.getWeatherForCity("北京市");
//         assertEquals("北京市", res.get("city"));
//     }
// }
// import org.junit.jupiter.api.Test;
// import static org.junit.jupiter.api.Assertions.*;
// import org.json.JSONObject;

// public class WeatherAppTest {
//     @Test
//     public void testValidCityName() {
//         JSONObject res = WeatherApp.getWeatherForCity("London");
//         assertEquals("London", res.getString("city"));
//         assertTrue(res.has("temperature_celsius"));
//         assertTrue(res.has("weather_description"));
//     }

//     @Test
//     public void testNonexistentCityName() {
//         JSONObject res = WeatherApp.getWeatherForCity("Qwertyuiop");
//         assertTrue(res.has("error"));
//     }

//     @Test
//     public void testEmptyInput() {
//         JSONObject res = WeatherApp.getWeatherForCity("");
//         assertTrue(res.has("error"));
//     }
// }
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class WeatherAppTest {

    private void printResult(String testName, String cityInput, JSONObject result) {
        System.out.println("=== " + testName + " ===");
        System.out.println("Input: \"" + cityInput + "\"");
        System.out.println("Output: " + result.toString(2));
        System.out.println();
    }

    @Test
    public void testValidCity() {
        JSONObject res = WeatherApp.getWeatherForCity("London");
        printResult("Valid city", "London", res);
        assertFalse(res.has("error"), "Should not have error for valid city");
        assertEquals("London", res.optString("city"));
        assertTrue(res.has("temperature_celsius"));
        assertTrue(res.has("weather_description"));
    }

    @Test
    public void testCityWithSpaces() {
        JSONObject res = WeatherApp.getWeatherForCity("New York");
        printResult("City with spaces", "New York", res);
        assertFalse(res.has("error"));
        assertEquals("New York", res.optString("city"));
    }

    @Test
    public void testCityWithNonAscii() {
        JSONObject res = WeatherApp.getWeatherForCity("北京市");
        printResult("City with non-ASCII", "北京市", res);
        assertFalse(res.has("error"));
        assertEquals("北京市", res.optString("city"));
    }

    @Test
    public void testAlternateCase() {
        JSONObject res = WeatherApp.getWeatherForCity("LoNDoN");
        printResult("Alternate case", "LoNDoN", res);
        assertFalse(res.has("error"));
        assertEquals("London", res.optString("city"));
    }

    @Test
    public void testEmptyInput() {
        JSONObject res = WeatherApp.getWeatherForCity("");
        printResult("Empty input", "", res);
        assertTrue(res.has("error"));
    }

    @Test
    public void testWhitespaceOnly() {
        JSONObject res = WeatherApp.getWeatherForCity("   ");
        printResult("Whitespace only", "   ", res);
        assertTrue(res.has("error"));
    }

    @Test
    public void testNonExistentCity() {
        JSONObject res = WeatherApp.getWeatherForCity("Qwertyuiop");
        printResult("Non-existent city", "Qwertyuiop", res);
        assertTrue(res.has("error"));
    }

    @Test
    public void testCityWithSpecialCharacters() {
        JSONObject res = WeatherApp.getWeatherForCity("St. John's");
        printResult("City with special characters", "St. John's", res);
        assertTrue(res.has("error") || "St. John's".equals(res.optString("city")));
    }

    @Test
    public void testCityWithNumbers() {
        JSONObject res = WeatherApp.getWeatherForCity("District 9");
        printResult("City with numbers", "District 9", res);
        assertTrue(res.has("error") || "District 9".equals(res.optString("city")));
    }

    @Test
    public void testAmbiguousCity() {
        JSONObject res = WeatherApp.getWeatherForCity("Springfield");
        printResult("Ambiguous city", "Springfield", res);
        assertTrue(res.has("error") || "Springfield".equalsIgnoreCase(res.optString("city")));
    }

    // Simulated error cases (if your app supports them)
    @Test
    public void testSimulateGeocodingApiFailure() {
        JSONObject res = WeatherApp.getWeatherForCity("simulate_api_failure");
        printResult("Simulate Geocoding API failure", "simulate_api_failure", res);
        assertTrue(res.has("error"));
    }

    @Test
    public void testSimulateWeatherApiFailure() {
        JSONObject res = WeatherApp.getWeatherForCity("simulate_weather_api_failure");
        printResult("Simulate Weather API failure", "simulate_weather_api_failure", res);
        assertTrue(res.has("error"));
    }

    @Test
    public void testSimulateMalformedApiResponse() {
        JSONObject res = WeatherApp.getWeatherForCity("simulate_malformed_response");
        printResult("Simulate Malformed API response", "simulate_malformed_response", res);
        assertTrue(res.has("error"));
    }
}
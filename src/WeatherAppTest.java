import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.google.gson.JsonObject;
import com.google.gson.GsonBuilder;

public class WeatherAppTest {

   private void printResult(String testName, String cityInput, JsonObject result) {
        System.out.println("Test: " + testName + " | Input: '" + cityInput + "'");
        System.out.println("Output: " +
            new GsonBuilder().setPrettyPrinting().create().toJson(result));
        System.out.println("------");
    }

    @Test
    public void testValidCity() {
        JsonObject res = WeatherApp.getWeatherForCity("London");
        printResult("Valid city", "London", res);
        assertFalse(res.has("error"), "Should not have error for valid city");
        assertEquals("London", res.get("city").getAsString());
        assertTrue(res.has("temperature_celsius"));
        assertTrue(res.has("weather_description"));
    }

    @Test
    public void testCityWithSpaces() {
        JsonObject res = WeatherApp.getWeatherForCity("New York");
        printResult("City with spaces", "New York", res);
        assertFalse(res.has("error"));
        assertEquals("New York", res.get("city").getAsString());
    }

    @Test
    public void testCityWithNonAscii() {
        JsonObject res = WeatherApp.getWeatherForCity("北京市");
        printResult("City with non-ASCII", "北京市", res);
        assertFalse(res.has("error"));
        assertEquals("北京市", res.get("city").getAsString());
    }

    @Test
    public void testAlternateCase() {
       JsonObject res = WeatherApp.getWeatherForCity("LoNDoN");
        printResult("Alternate case", "LoNDoN", res);
        assertFalse(res.has("error"));
        assertEquals("London", res.get("city").getAsString());
    }

    @Test
    public void testEmptyInput() {
        JsonObject res = WeatherApp.getWeatherForCity("");
        printResult("Empty input", "", res);
        assertTrue(res.has("error"));
    }

    @Test
    public void testWhitespaceOnly() {
        JsonObject res = WeatherApp.getWeatherForCity("   ");
        printResult("Whitespace only", "   ", res);
        assertTrue(res.has("error"));
    }

    @Test
    public void testNonExistentCity() {
        JsonObject res = WeatherApp.getWeatherForCity("Qwertyuiop");
        printResult("Non-existent city", "Qwertyuiop", res);
        assertTrue(res.has("error"));
    }

    @Test
    public void testCityWithSpecialCharacters() {
       JsonObject res = WeatherApp.getWeatherForCity("St. John's");
        printResult("City with special characters", "St. John's", res);
        assertTrue(res.has("error") || "St. John's".equals(res.get("city").getAsString()));
    }

    @Test
    public void testCityWithNumbers() {
        JsonObject res = WeatherApp.getWeatherForCity("District 9");
        printResult("City with numbers", "District 9", res);
        assertTrue(res.has("error") || "District 9".equals(res.get("city").getAsString()));
    }

    @Test
    public void testAmbiguousCity() {
        JsonObject res = WeatherApp.getWeatherForCity("Springfield");
        printResult("Ambiguous city", "Springfield", res);
        assertTrue(res.has("error") || "Springfield".equalsIgnoreCase(res.get("city").getAsString()));
    }

    // Simulated error cases (if your app supports them)
    @Test
    public void testSimulateGeocodingApiFailure() {
       JsonObject res = WeatherApp.getWeatherForCity("simulate_api_failure");
        printResult("Simulate Geocoding API failure", "simulate_api_failure", res);
        assertTrue(res.has("error"));
    }

    @Test
    public void testSimulateWeatherApiFailure() {
        JsonObject res = WeatherApp.getWeatherForCity("simulate_weather_api_failure");
        printResult("Simulate Weather API failure", "simulate_weather_api_failure", res);
        assertTrue(res.has("error"));
    }

    @Test
    public void testSimulateMalformedApiResponse() {
         JsonObject res = WeatherApp.getWeatherForCity("simulate_malformed_response");
        printResult("Simulate Malformed API response", "simulate_malformed_response", res);
        assertTrue(res.has("error"));
    }
}
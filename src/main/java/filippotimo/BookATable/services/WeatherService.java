package filippotimo.BookATable.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class WeatherService {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(WeatherService.class);
    private static final String WEATHER_API_URL = "https://api.openweathermap.org/data/2.5/weather";
    @Value("${openweather_key}")
    private String apiKey;

    public Map<String, Object> getWeatherForCity(String city) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String url = String.format("%s?q=%s&appid=%s&units=metric", WEATHER_API_URL, city, this.apiKey);

            @SuppressWarnings("unchecked")
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);

            if (response != null) {
                Map<String, Object> weatherData = new HashMap<>();

                @SuppressWarnings("unchecked")
                Map<String, Object> main = (Map<String, Object>) response.get("main");
                @SuppressWarnings("unchecked")
                java.util.List<Map<String, Object>> weather =
                        (java.util.List<Map<String, Object>>) response.get("weather");

                weatherData.put("temperature", main.get("temp"));
                weatherData.put("feels_like", main.get("feels_like"));
                weatherData.put("humidity", main.get("humidity"));
                weatherData.put("description", weather.get(0).get("description"));
                weatherData.put("city", response.get("name"));

                return weatherData;
            }
        } catch (Exception e) {
            log.error("Failed to fetch weather data for city: {}", city, e);
        }

        Map<String, Object> defaultWeather = new HashMap<>();
        defaultWeather.put("error", "Weather data not available");
        return defaultWeather;
    }

    public Map<String, Object> getWeatherByCoordinates(Double latitude, Double longitude) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String url = String.format("%s?lat=%s&lon=%s&appid=%s&units=metric",
                    WEATHER_API_URL, latitude, longitude, this.apiKey);

            @SuppressWarnings("unchecked")
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);

            if (response != null) {
                Map<String, Object> weatherData = new HashMap<>();

                @SuppressWarnings("unchecked")
                Map<String, Object> main = (Map<String, Object>) response.get("main");
                @SuppressWarnings("unchecked")
                java.util.List<Map<String, Object>> weather =
                        (java.util.List<Map<String, Object>>) response.get("weather");

                weatherData.put("temperature", main.get("temp"));
                weatherData.put("feels_like", main.get("feels_like"));
                weatherData.put("humidity", main.get("humidity"));
                weatherData.put("description", weather.get(0).get("description"));

                return weatherData;
            }
        } catch (Exception e) {
            log.error("Failed to fetch weather data for coordinates: {}, {}", latitude, longitude, e);
        }

        Map<String, Object> defaultWeather = new HashMap<>();
        defaultWeather.put("error", "Weather data not available");
        return defaultWeather;
    }

}

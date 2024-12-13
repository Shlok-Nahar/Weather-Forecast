package com.weatherapp.main;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URISyntaxException;
import java.util.Properties;
import java.util.Scanner;
import org.json.JSONObject;

public class WeatherApp {

    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/weather";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter city name: ");
        String city = scanner.nextLine();

        try {
            String apiKey = Config.getApiKey();
            String response = getWeatherData(city, apiKey);
            if (response != null) {
                parseAndDisplayWeather(response);
            } else {
                System.out.println("Unable to fetch weather data. Please try again.");
            }
        } catch (IOException | URISyntaxException e) {
            System.out.println("An error occurred: " + e.getMessage());
        }

        scanner.close();
    }

    private static String getWeatherData(String city, String apiKey) throws IOException, URISyntaxException {
        String urlString = BASE_URL + "?q=" + city + "&appid=" + apiKey + "&units=metric";
        URI uri = new URI(urlString);
        URL url = uri.toURL();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();
        if (responseCode == 200) { // HTTP OK
            try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                StringBuilder response = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                return response.toString();
            }
        } else {
            System.out.println("Error: HTTP response code " + responseCode);
            return null;
        }
    }

    private static void parseAndDisplayWeather(String response) {
        JSONObject json = new JSONObject(response);

        String cityName = json.getString("name");
        JSONObject main = json.getJSONObject("main");
        double temperature = main.getDouble("temp");
        int humidity = main.getInt("humidity");
        JSONObject weather = json.getJSONArray("weather").getJSONObject(0);
        String description = weather.getString("description");

        System.out.println("\nWeather in " + cityName + ":");
        System.out.println("Temperature: " + temperature + " °C");
        System.out.println("Humidity: " + humidity + "%");
        System.out.println("Description: " + description);
    }
}

class Config {
    public static String getApiKey() throws IOException {
        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream("config.properties")) {
            props.load(fis);
        }
        return props.getProperty("api.key");
    }
}

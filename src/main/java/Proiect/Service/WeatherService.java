package Proiect.Service;

import Proiect.Entity.Location;
import Proiect.Entity.Weather;
import Proiect.Repository.WeatherRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Scanner;


public class WeatherService implements WeatherRepository {

    private static final String WEATHERSTACK_API_KEY = "8ebca2f498ca14abd209c784636ad015";
    private static final String WEATHERSTACK_API_URL = "http://api.weatherstack.com/current";


    final SessionFactory sessionFactory = new Configuration()
            .configure("hibernate.cfg.xml")
            .addAnnotatedClass(Weather.class)
            .addAnnotatedClass(Location.class)
            .buildSessionFactory();


    @Override
    public void saveToDatabase(Weather weatherData) {

            try {
                Session session = sessionFactory.openSession();
                Transaction transaction = session.beginTransaction();
                session.persist(weatherData);
                transaction.commit();
                System.out.println("Weather data saved to the database.");
            } catch (Exception e) {
                e.printStackTrace();
            }

    }

    public Weather collectDataFromWeatherStack(String location, String date) throws IOException {
        try {String apiUrl = buildApiUrlWeatherStack(location);
            String jsonResponse = sendHttpRequest(apiUrl);
            return parseResponse(jsonResponse, date, location);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    private static String buildApiUrlWeatherStack(String location) {
        return String.format("%s?access_key=%s&query=%s", WEATHERSTACK_API_URL, WEATHERSTACK_API_KEY, location);
    }



    private static String sendHttpRequest(String apiUrl) throws IOException {
        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();

        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }

        reader.close();
        connection.disconnect();

        return response.toString();

    }

    private Weather parseResponse(String jsonResponse, String date, String location) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(jsonResponse);

        int temperature = rootNode.path("current").path("temperature").asInt();
        int pressure = rootNode.path("current").path("pressure").asInt();
        int humidity = rootNode.path("current").path("humidity").asInt();
        int windSpeed = rootNode.path("current").path("wind_speed").asInt();
        String windDirection = rootNode.path("current").path("wind_dir").asText();
        Weather weather = new Weather();
        weather.setDate(date);
        Location weatherLocation = getLocationByName(location);
        weather.setLocation(weatherLocation);
        weather.setCityName(location);
        return new Weather(location, weatherLocation, date, temperature, pressure, humidity, windSpeed, windDirection);
    }



    public void displayWeatherData(Weather weatherData) {
        if (weatherData != null) {
            System.out.println("Temperature: " + weatherData.getTemperature() + "°C");
            System.out.println("Pressure: " + weatherData.getPressure() + " hPa");
            System.out.println("Humidity: " + weatherData.getHumidity() + "%");
            System.out.println("Wind: " + weatherData.getWindSpeed() + " m/s " + weatherData.getWindDirection());
        } else {
            System.out.println("Failed to retrieve weather data.");
        }
    }

    public Location getLocationByName(String cityName) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Location WHERE cityName = :cityName", Location.class)
                    .setParameter("cityName", cityName)
                    .uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }




}

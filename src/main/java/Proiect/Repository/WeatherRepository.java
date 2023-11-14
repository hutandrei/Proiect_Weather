package Proiect.Repository;

import Proiect.Entity.Weather;

import java.io.IOException;

public interface WeatherRepository {
    public void saveToDatabase(Weather weatherData);
    public Weather collectDataFromWeatherStack(String location, String date) throws IOException;
    public void displayWeatherData(Weather weatherData);


}

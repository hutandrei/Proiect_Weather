package Proiect.Repository;

import Proiect.Entity.AvgWeather;

public interface AvgWeatherRepository{
    public void calculateAverage(String location, String date);

    void saveToDatabase(AvgWeather weatherData);
}

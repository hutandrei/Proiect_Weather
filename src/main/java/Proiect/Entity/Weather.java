package Proiect.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Weather{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;
    private String cityName;
    private String date;
    private int temperature;
    private String windDirection;
    private int windSpeed;
    private int pressure;
    private int humidity;

    public Weather(String cityName, Location location, String date, int temperature, int pressure, int humidity,  int windSpeed, String windDirection) {

        this.date = date;
        this.location=location;
        this.cityName = cityName;
        this.temperature = temperature;
        this.windDirection = windDirection;
        this.windSpeed = windSpeed;
        this.pressure = pressure;
        this.humidity = humidity;
    }

}

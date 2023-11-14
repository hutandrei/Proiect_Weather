package Proiect.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AvgWeather {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
//    @ManyToOne
//    @JoinColumn(name = "location_id", nullable = false)
//    private Location location;
    private String cityName;
    private String date;
    private double temperature;
    private String windDirection;
    private double windSpeed;
    private double pressure;
    private double humidity;
}

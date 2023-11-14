package Proiect;

import Proiect.Entity.Location;
import Proiect.Entity.Weather;
import Proiect.Service.AvgWeatherService;
import Proiect.Service.LocationService;
import Proiect.Service.WeatherService;

import java.io.IOException;
import java.util.Scanner;



public class Main {
    public static void main(String[] args) {

        LocationService locationService = new LocationService();
        WeatherService weatherService = new WeatherService();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("1. Add a new location to the database");
            System.out.println("2. Display all the locations added");
            System.out.println("3. Insert weather values");
            System.out.println("4. Retrieve average values");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");
            int nr = scanner.nextInt();
            scanner.nextLine();
            switch (nr) {
                case 1:
                    System.out.print("Enter City Name: ");
                    String cityName = scanner.nextLine();

                    System.out.print("Enter Region (optional): ");
                    String region = scanner.nextLine();

                    System.out.print("Enter Country Name: ");
                    String countryName = scanner.nextLine();

                    System.out.print("Enter Latitude: ");
                    double latitude = scanner.nextDouble();

                    System.out.print("Enter Longitude: ");
                    double longitude = scanner.nextDouble();

                    Location location = new Location(cityName, region, countryName, latitude, longitude);

                    locationService.addLocation(location);
                    System.out.println("Location added successfully!");

                    break;

                case 2:

                    locationService.getAllLocations();
                    break;
                case 3:
                    System.out.println("Enter date (YYYY-MM-DD): ");
                    String date = scanner.nextLine();

                    System.out.println("Enter location: ");
                    cityName = scanner.nextLine();

                    System.out.println("1. Download the information from internet");
                    System.out.println("2. Input the data manually");
                    int opt = scanner.nextInt();
                    switch (opt) {
                        case 1:
                            try {
                                Weather stackWeatherData = weatherService.collectDataFromWeatherStack(cityName, date);
                                weatherService.displayWeatherData(stackWeatherData);
                                weatherService.saveToDatabase(stackWeatherData);

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            break;
                        case 2:
                            Weather weather = new Weather();
                            System.out.println("Temperature: ");
                            weather.setTemperature(scanner.nextInt());
                            System.out.println("Pressure (usual range ~ 1000 hPa) :");
                            weather.setPressure(scanner.nextInt());
                            System.out.println("Humidity (range 1-100%) :");
                            weather.setHumidity(scanner.nextInt());
                            System.out.println("Wind speed: ");
                            weather.setWindSpeed(scanner.nextInt());
                            System.out.println("Wind direction: ");
                            weather.setWindDirection(scanner.next());
                            weather.setCityName(cityName);
                            weather.setDate(date);
                            weather.setLocation(weatherService.getLocationByName(cityName));
                            weatherService.saveToDatabase(weather);
                    }
                    break;

                case 4:
                    System.out.println("Enter location:");
                    cityName=scanner.nextLine();
                    System.out.println("Enter the date");
                    date=scanner.nextLine();
                    AvgWeatherService averageWeather = new AvgWeatherService();
                    averageWeather.calculateAverage(cityName,date);
                    break;

                case 5:
                    System.out.println("Exiting...");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");

            }
        }
    }
}

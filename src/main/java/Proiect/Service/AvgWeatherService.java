package Proiect.Service;

import Proiect.Entity.AvgWeather;
import Proiect.Entity.Location;
import Proiect.Entity.Weather;
import Proiect.Repository.AvgWeatherRepository;
import jakarta.persistence.EntityManager;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import java.util.List;

public class AvgWeatherService implements AvgWeatherRepository  {


    final SessionFactory sessionFactory = new Configuration()
            .configure("hibernate.cfg.xml")
            .addAnnotatedClass(Weather.class)
            .addAnnotatedClass(Location.class)
            .addAnnotatedClass(AvgWeather.class)
            .buildSessionFactory();
    private  EntityManager entityManager = sessionFactory.createEntityManager();



    @Override
    public void calculateAverage(String cityName, String date){

        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();

            // Calculate averages
            Query<Object[]> query = session.createQuery(
                    "SELECT AVG(w.humidity), AVG(w.pressure), AVG(w.temperature), AVG(w.windSpeed) " +
                            "FROM Weather w " +
                            "WHERE w.cityName = :cityName AND w.date = :date", Object[].class);
            query.setParameter("cityName", cityName);
            query.setParameter("date", date);

            List<Object[]> result = query.list();

            if (!result.isEmpty()) {
                Object[] averages = result.get(0);

                // Create and save AvgWeatherData entity
                AvgWeather avgWeatherData = new AvgWeather();
                avgWeatherData.setCityName(cityName);
                avgWeatherData.setDate(date);
                avgWeatherData.setHumidity((Double) averages[0]);
                avgWeatherData.setPressure((Double) averages[1]);
                avgWeatherData.setTemperature((Double) averages[2]);
                avgWeatherData.setWindSpeed((Double) averages[3]);

                session.save(avgWeatherData);
            }

            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void saveToDatabase(AvgWeather weatherData) {

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



    public List<Weather> weatherList(String cityName, String date) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("select w FROM Weather w WHERE w.cityName = :cityName AND w.date = :date ", Weather.class)
                    .setParameter("cityName", cityName)
                    .setParameter("date",date)
                    .getResultList();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }



}

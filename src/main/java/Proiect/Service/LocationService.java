package Proiect.Service;

import Proiect.Entity.Location;
import Proiect.Entity.Weather;
import Proiect.Repository.LocationRepository;

import jakarta.persistence.EntityManager;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.List;
import java.util.Scanner;

public class LocationService implements LocationRepository {

    public SessionFactory sessionFactory = new Configuration()
            .configure("hibernate.cfg.xml")
            .addAnnotatedClass(Location.class)
            .addAnnotatedClass(Weather.class)
            .buildSessionFactory();



    @Override
    public void addLocation(Location location) {

        EntityManager entityManager  = sessionFactory.createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(location);
        location.validate();
        entityManager.getTransaction().commit();
    }

    @Override
    public List<Location> getAllLocations() {
        EntityManager entityManager  = sessionFactory.createEntityManager();
        List<Location> locations =entityManager.createQuery("From Location", Location.class).getResultList();
        locations.forEach(loc-> System.out.println(loc.getId()+". "+loc.getCityName()+", "+loc.getCountryName()));
        return locations;
    }




}

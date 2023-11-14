package Proiect.Repository;

import Proiect.Entity.Location;

import java.util.List;

public interface LocationRepository {

    public void addLocation(Location location);
    List<Location> getAllLocations();


}

package citybike.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import citybike.model.Availability;
import citybike.model.Station;

@RestController
public class CitybikeRestController {
    @RequestMapping("/stations")
    public List<Station> bikeStations() {
        ArrayList<Station> stations = new ArrayList<>();
        stations.add(createStation("1", "title", -1, -1));
        stations.add(createStation("2", "title2", 22, 11));
        stations.add(createStation("3", "title3", 2, 3));
        return stations;
    }

    private Station createStation(String id, String title, int bikes, int locks) {
        Station station = new Station();
        station.setId(id);
        station.setTitle(title);
        Availability availability = new Availability();
        availability.setBikes(bikes);
        availability.setLocks(locks);
        if (bikes != -1) {
            station.setAvailability(availability);
        }
        return station;
    }
}

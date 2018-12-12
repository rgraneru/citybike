package citybike.controller;

import citybike.model.Station;
import citybike.service.CitybikeService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CitybikeRestController {
    private final CitybikeService citybikeService;

    public CitybikeRestController(CitybikeService citybikeService) {
        this.citybikeService = citybikeService;
    }

    @RequestMapping("/stations")
    public List<Station> bikeStations() {
        return citybikeService.getStationAvailabilityIncludingNames();
    }
}

package citybike.controller;

import citybike.model.Station;
import citybike.service.CitybikeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class CitybikeController {
    private final CitybikeService citybikeService;

    public CitybikeController(CitybikeService citybikeService) {
        this.citybikeService = citybikeService;
    }

    @GetMapping("/")
    public String availability(Model model) {
        List<Station> stationAvailabilityIncludingNames = citybikeService.getStationAvailabilityIncludingNames();
        model.addAttribute("stations", stationAvailabilityIncludingNames);
        return "citybikeInfo";
    }
}
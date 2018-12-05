package citybike.controller;

import citybike.model.Station;
import citybike.service.CitybikeService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class CitybikeController {
    @Value( "${Identifier}" )
    private String clientIdentifier;

    @Value( "${apiPath}" )
    private String apiPath;

    @GetMapping("/")
    public String availability(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model, CitybikeService citybikeService) {
        citybikeService.setClientIdentifier(clientIdentifier);
        citybikeService.setApiPath(apiPath);
        List<Station> stationAvailabilityIncludingNames = citybikeService.getStationAvailabilityIncludingNames();
        model.addAttribute("stations", stationAvailabilityIncludingNames);
        return "citybikeInfo";
    }
}
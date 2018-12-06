package citybike.service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import citybike.model.Station;
import citybike.model.Stations;

@Service
public class CitybikeService {

    private RestTemplate restTemplate;

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        restTemplate = builder.build();
        return restTemplate;
    }

    @Value( "${identifier}" )
    private String clientIdentifier;

    @Value( "${apiPath}" )
    private String apiPath;

    public List<Station> getStationAvailabilityIncludingNames() {
        //First get a list of IDs and StationNames
        List<Station> stationNames = getStationInfo("/stations");

        //Convert to map for easy access to title given id
        Map<String, String> tempNameMap = stationNames.stream()
                .collect(Collectors.toMap(Station::getId, Station::getTitle));

        //Get a list of stations and their availability removing those with no data
        List<Station> stationAvailability = getStationInfo("/stations/availability")
                .stream()
                .filter(station -> station.getNumberOfBikesAvailable() != -1)
                .collect(Collectors.toList());

        //Update the list with titles for each entry, matching id and putting in placeholder for those with no name
        stationAvailability.
                forEach(station -> station.setTitle(getTitleIfExistElseGetId(tempNameMap, station)));

        //alphabetic sorting by title
        stationAvailability.sort(Comparator.comparing(Station::getTitle));

        return stationAvailability;
    }

    private String getTitleIfExistElseGetId(Map<String, String> tempNameMap, Station station) {
        String title = tempNameMap.get(station.getId());
        if ("".equals(title) || title == null) {
            return "Uten navn. Id: " + station.getId();
        }
        else {
            return title;
        }
    }

    private List<Station> getStationInfo(String endPoint) {
        ResponseEntity<Stations> stations = restTemplate.exchange(apiPath + endPoint, HttpMethod.GET, getHttpEntityWithHeaders(), Stations.class);
        return stations.getBody().getStations();
    }

    private HttpEntity<String> getHttpEntityWithHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Client-Identifier", clientIdentifier);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        return new HttpEntity<>("parameters", headers);
    }
}

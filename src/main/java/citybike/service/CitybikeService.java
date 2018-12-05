package citybike.service;

import citybike.model.Station;
import citybike.model.Stations;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CitybikeService {
    private String clientIdentifier;
    private String apiPath;

    public List<Station> getStationAvailabilityIncludingNames() {
        RestTemplate restTemplate = new RestTemplateBuilder().build();
        //First get a list of IDs and StationNames
        List<Station> stationNames = getStationInfo(restTemplate, "/stations");

        //Convert to map for easy access to title given id
        Map<String, String> tempNameMap = stationNames.stream()
                .collect(Collectors.toMap(Station::getId, Station::getTitle));

        //Get a list of stations and their availability removing those with no data
        List<Station> stationAvailability = getStationInfo(restTemplate, "/stations/availability")
                .stream()
                .filter(station -> station.getNumberOfBikesAvailable() != -1)
                .collect(Collectors.toList());

        //Update the list with titles for each entry, matching id and putting in placeholder for those with no name
        stationAvailability.
                forEach(station -> station.setTitle(getTitleIfExistElseGetId(tempNameMap, station)));

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

    private List<Station> getStationInfo(RestTemplate restTemplate, String endPoint) {
        ResponseEntity<Stations> stations = restTemplate.exchange(apiPath + endPoint, HttpMethod.GET, getHttpEntityWithHeaders(), Stations.class);
        if (stations.getBody() == null) {
            throw new HttpClientErrorException(HttpStatus.UNPROCESSABLE_ENTITY, "No body on response");
        }
        return stations.getBody().getStations();
    }

    private HttpEntity<String> getHttpEntityWithHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Client-Identifier", clientIdentifier);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        return new HttpEntity<>("parameters", headers);
    }

    public void setClientIdentifier(String clientIdentifier) {
        this.clientIdentifier = clientIdentifier;
    }

    public void setApiPath(String apiPath) {
        this.apiPath = apiPath;
    }
}

package citybike.service;

import citybike.model.Station;
import citybike.model.Stations;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;

@RunWith(SpringRunner.class)
public class CitybikeServiceTest {

    @MockBean
    private RestTemplate restTemplate;

    @SpyBean
    private CitybikeService citybikeService;

    @Value( "${apiPath}" )
    private String apiPath;

    @Test
    public void getStationAvailabilityMergesTitleAndAvailabilityInfo() throws IOException {
        doReturn(new ResponseEntity<>(createStationsWithTitle(), HttpStatus.OK)).when(restTemplate).exchange(eq("${apiPath}/stations"), eq(HttpMethod.GET), ArgumentMatchers.any(), eq(Stations.class));
        doReturn(new ResponseEntity<>(createStationsAvailability(), HttpStatus.OK)).when(restTemplate).exchange(eq("${apiPath}/stations/availability"), eq(HttpMethod.GET), ArgumentMatchers.any(), eq(Stations.class));

        List<Station> stationAvailabilityIncludingNames = citybikeService.getStationAvailabilityIncludingNames();

        assertThat(stationAvailabilityIncludingNames, hasSize(4));
        assertThat(stationAvailabilityIncludingNames, everyItem(
                hasProperty("id", is(not(nullValue())))));
        assertThat(stationAvailabilityIncludingNames, everyItem(
                hasProperty("title", is(not(nullValue())))));
        assertThat(stationAvailabilityIncludingNames, everyItem(
                hasProperty("numberOfBikesAvailable", is(not(nullValue())))));
        assertThat(stationAvailabilityIncludingNames, everyItem(
                hasProperty("numberOfLocksAvailable", is(not(nullValue())))));

        //check one station to check if the id is correctly connected to the title and the availability of bikes and locks
        stationAvailabilityIncludingNames.stream()
                .filter(station -> station.getId().equals("173"))
                .forEach(station -> {
                    assertThat(station.getTitle(), is("title3"));
                    assertThat(station.getNumberOfBikesAvailable(), is(2L));
                    assertThat(station.getNumberOfLocksAvailable(), is(10L));
                });
    }

    @Test
    public void getStationAvailabilityCreatesTitleIfTitleIsMissing() throws IOException {
        doReturn(new ResponseEntity<>(createOneStationWithoutTitle(), HttpStatus.OK)).when(restTemplate).exchange(eq("${apiPath}/stations"), eq(HttpMethod.GET), ArgumentMatchers.any(), eq(Stations.class));
        doReturn(new ResponseEntity<>(createStationsAvailability(), HttpStatus.OK)).when(restTemplate).exchange(eq("${apiPath}/stations/availability"), eq(HttpMethod.GET), ArgumentMatchers.any(), eq(Stations.class));

        List<Station> stationAvailabilityIncludingNames = citybikeService.getStationAvailabilityIncludingNames();

        assertThat(stationAvailabilityIncludingNames, hasSize(4));
        assertThat(stationAvailabilityIncludingNames, everyItem(
                hasProperty("id", is(not(nullValue())))));
        assertThat(stationAvailabilityIncludingNames, everyItem(
                hasProperty("title", containsString("Uten navn. Id: "))));
    }

    private Stations createOneStationWithoutTitle() throws IOException {
        String jsonInString = "{\n" +
                "    \"stations\": [\n" +
                "        {\n" +
                "            \"id\": 177,\n" +
                "            \"title\": null\n" +
                "        }\n" +
                "     ]}";
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(jsonInString, Stations.class);
    }

    private Stations createStationsWithTitle() throws IOException {
        String jsonInString = "{\n" +
                "    \"stations\": [\n" +
                "        {\n" +
                "            \"id\": 177,\n" +
                "            \"title\": \"title1\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"id\": 178,\n" +
                "            \"title\": \"title2\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"id\": 173,\n" +
                "            \"title\": \"title3\"\n" +
                "        },\n" +
                "        {\n" +
                "            \"id\": 158,\n" +
                "            \"title\": \"title4\"\n" +
                "        }\n" +
                "     ]}";

        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(jsonInString, Stations.class);
    }

    private Stations createStationsAvailability() throws IOException {
        String jsonInString = "{\n" +
                "    \"stations\": [\n" +
                "        {\n" +
                "            \"id\": 177,\n" +
                "            \"availability\": {\n" +
                "                \"bikes\": 0,\n" +
                "                \"locks\": 29,\n" +
                "                \"overflow_capacity\": false\n" +
                "            }\n" +
                "        },\n" +
                "        {\n" +
                "            \"id\": 178,\n" +
                "            \"availability\": {\n" +
                "                \"bikes\": 12,\n" +
                "                \"locks\": 0,\n" +
                "                \"overflow_capacity\": false\n" +
                "            }\n" +
                "        },\n" +
                "        {\n" +
                "            \"id\": 173,\n" +
                "            \"availability\": {\n" +
                "                \"bikes\": 2,\n" +
                "                \"locks\": 10,\n" +
                "                \"overflow_capacity\": false\n" +
                "            }\n" +
                "        },\n" +
                "        {\n" +
                "            \"id\": 158,\n" +
                "            \"availability\": {\n" +
                "                \"bikes\": 4,\n" +
                "                \"locks\": 24,\n" +
                "                \"overflow_capacity\": false\n" +
                "            }\n" +
                "        }\n" +
                "     ]}";

        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(jsonInString, Stations.class);
    }
}
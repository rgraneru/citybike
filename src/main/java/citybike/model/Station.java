package citybike.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Station {
    private String title;
    private String id;
    private Availability availability;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAvailability(Availability availability) {
        this.availability = availability;
    }

    public long getNumberOfBikesAvailable() {
        return availability == null ? -1 : availability.getBikes();
    }

    public long getNumberOfLocksAvailable() {
        return availability == null ? -1 : availability.getLocks();
    }
}

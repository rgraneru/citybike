package citybike.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Availability {
    private long bikes;
    private long locks;

    public long getBikes() {
        return bikes;
    }

    public void setBikes(long bikes) {
        this.bikes = bikes;
    }

    public long getLocks() {
        return locks;
    }

    public void setLocks(long locks) {
        this.locks = locks;
    }
}

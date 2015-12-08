package com.booking.ooziezombie.json;

import com.booking.ooziezombie.json.Job;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Jobs {
    @JsonProperty
    private int total;
    @JsonProperty
    private List<Job> coordinatorjobs = new ArrayList<Job>();

    public List<Job> getCoordinatorjobs() {
        return coordinatorjobs;
    }

    public void setCoordinatorjobs(List<Job> coordinatorjobs) {
        this.coordinatorjobs = coordinatorjobs;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

}

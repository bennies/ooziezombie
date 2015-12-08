package com.booking.ooziezombie.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SystemStatus {
    private String systemMode;

    public String getSystemMode() {
        return systemMode;
    }

    public void setSystemMode(String systemMode) {
        this.systemMode = systemMode;
    }

    public String toString() {
        return "SystemStatus{systemMode='"+systemMode+"'}";
    }
}

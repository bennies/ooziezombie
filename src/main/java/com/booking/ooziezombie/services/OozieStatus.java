package com.booking.ooziezombie.services;

import com.booking.ooziezombie.json.SystemStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class OozieStatus {

    @Value("${oozie.host}")
    private String oozieHost;

    public String getStatus() {
        RestTemplate restTemplate = new RestTemplate();
        SystemStatus ss = restTemplate.getForObject(oozieHost + "oozie/v2/admin/status", SystemStatus.class);
        return ss.getSystemMode();
    }
}

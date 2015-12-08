package com.booking.ooziezombie.services;

import com.booking.ooziezombie.json.Job;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

@Configuration
public class CoordinatorState {
    private static final Logger log = LoggerFactory.getLogger(CoordinatorState.class);

    @Value("${oozie.host}")
    private String oozieHost;

    public void change(final Job job, final String state) throws URISyntaxException {
        log.info("Changing "+job.getCoordJobName()+" ( "+job.getCoordJobId()+" ) to "+state);
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_XML);
        HttpEntity<String> entity = new HttpEntity<>("", headers);
        URI uri = new URI(oozieHost + "oozie/v1/job/" + job.getCoordJobId() + "?action="+state.toLowerCase());
        restTemplate.put(uri, entity);
    }
}

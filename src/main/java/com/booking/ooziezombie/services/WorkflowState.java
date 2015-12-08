package com.booking.ooziezombie.services;

import com.booking.ooziezombie.json.Action;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

@Configuration
public class WorkflowState {
    private static final Logger log = LoggerFactory.getLogger(WorkflowState.class);

    @Value("${oozie.host}")
    private String oozieHost;

    /**
     * Currently only able to rerun an action of a coordinator.
     * @param action
     * @throws URISyntaxException
     */
    public void change(final Action action, final String state) throws URISyntaxException {
        RestTemplate restTemplate = new RestTemplate();
        log.info(state+":" + action.getId());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_XML);
        HttpEntity<String> entity = new HttpEntity<>("", headers);
        URI uri;
        if (state.equals("rerun")) {
            // reruns are a little different we let the action from a coordinator generate a new workflow.
            uri = new URI(oozieHost + "oozie/v1/job/" + action.getCoordJobId() + "?action=coord-rerun&type=action&scope=" + action.getActionNumber() + "-" + action.getActionNumber());
        } else {
            uri = new URI(oozieHost + "oozie/v1/job/" + action.getExternalId() + "?action=" + state.toLowerCase());
        }
        restTemplate.put(uri, entity);
    }

    /**
     * This returns the last 5minutes of log lines. Keep in mind it's the last 5 minutes so will normally always return
     * something even when a job stopped hours ago.
     * @param action
     * @return
     * @throws URISyntaxException
     */
    public String getLast5MinLog(final Action action) throws URISyntaxException {
        RestTemplate restTemplate = new RestTemplate();
        log.info("getLog for:" + action.getExternalId());
        URI uri = new URI(oozieHost + "/oozie/v1/job/"+action.getExternalId()+"?show=log&logfilter=recent=5m");
        return restTemplate.getForObject(uri, String.class);
    }

    /**
     * Take a logLine and return the last date we find in it.
     * @param logLine
     * @return the last date as calendar object
     */
    public Calendar getLastDateFromLog(final String logLine) {
        if (logLine==null) return null;
        String[] logLines = logLine.split("[\\r\\n]+");
        if (logLine.length()==0) {
            return null;
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS");
            String lastTimeStamp = logLines[logLines.length - 1].substring(0,23);
            Calendar cal = Calendar.getInstance();
            try {
                cal.setTime(sdf.parse(lastTimeStamp));
            } catch (ParseException e) {
                // if we can't get a date from the log not a big deal.
                log.error("lastDate:"+lastTimeStamp);
                return null;
            }
            return cal;
        }
    }
}

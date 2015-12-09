package com.booking.ooziezombie.services;

import com.booking.ooziezombie.Tools;
import com.booking.ooziezombie.json.Job;
import com.booking.ooziezombie.json.Jobs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.client.RestTemplate;

import java.net.ConnectException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Configuration
@EnableRetry
public class FailedCoordinatorSuspend {
    private static final Logger log = LoggerFactory.getLogger(FailedCoordinatorSuspend.class);

    @Value("${oozie.host}")
    private String oozieHost;
    @Value("${coordinator.suspended.kill.days}")
    private int killThisOld;
    @Autowired
    private CoordinatorState coordinatorState;

    @Retryable(ConnectException.class)
    public void run() throws URISyntaxException, ParseException {
        Calendar farInThePast = Calendar.getInstance();
        farInThePast.add(Calendar.DAY_OF_MONTH, killThisOld);
        Tools.removeTime(farInThePast);
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH);

        RestTemplate restTemplate = new RestTemplate();
        int offset=1, batchSize=50, totalRecords=2;
        while (offset<totalRecords) {
            Map<String, Object> params = new HashMap<>();
            params.put("batchSize",batchSize+"");
            String status = "SUSPENDED";
            URI uri = new URI(oozieHost + "oozie/v2/jobs?jobtype=coordinator&filter=status%3D"+status+"&len=" + batchSize + "&offset=" + offset);
            ResponseEntity<Jobs> runningJobs = restTemplate.getForEntity(uri, Jobs.class);
            totalRecords = runningJobs.getBody().getTotal();
            List<Job> jobs = runningJobs.getBody().getCoordinatorjobs();
            for (Job job: jobs) {
                cal.setTime(sdf.parse(job.getLastAction()));
                Tools.removeTime(cal);
                if(cal.before(farInThePast)) {
                    log.info("Suspended coordinator " + killThisOld + " days old: " + job.getCoordJobName() + " " + job.getLastAction());
                    coordinatorState.change(job, "kill");
                }
            }
            offset+=runningJobs.getBody().getCoordinatorjobs().size();
        }
        log.info("Jobs:" + totalRecords + " totalRecords:"+totalRecords+" offset:"+offset);
    }


}

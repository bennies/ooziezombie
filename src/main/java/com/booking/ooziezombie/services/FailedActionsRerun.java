package com.booking.ooziezombie.services;

import com.booking.ooziezombie.Tools;
import com.booking.ooziezombie.json.Action;
import com.booking.ooziezombie.json.Job;
import com.booking.ooziezombie.json.Jobs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
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
public class FailedActionsRerun {
    private static final Logger log = LoggerFactory.getLogger(FailedActionsRerun.class);

    @Value("${oozie.host}")
    private String oozieHost;
    @Value("${coordinator.fetch.pause.millis}")
    private long coordinatorFetchPause;
    @Value("${stuck.action.timeout.minutes}")
    private int stuckActionTimeoutMinutes;
    @Value("${coordinator.failedactionsrerun.batchsize}")
    private int batchSize;
    @Value("${coordinator.failedactionsrerun.exclude}")
    private Set excludeCoordinators;

    @Autowired
    private WorkflowState workflowState;

    @Retryable(maxAttempts=5, backoff=@Backoff(delay=100, maxDelay=500))
    public void run() throws URISyntaxException, ParseException {
        RestTemplate restTemplate = new RestTemplate();
        int offset = 1, batchSize = 50, totalRecords = 2;
        while (offset < totalRecords) {
            Map<String, Object> params = new HashMap<>();
            params.put("batchSize", batchSize + "");
            String status = "RUNNING";
            URI uri = new URI(oozieHost + "oozie/v2/jobs?jobtype=coordinator&filter=status%3D" + status + "&len=" + batchSize + "&offset=" + offset);
            ResponseEntity<Jobs> runningJobs = restTemplate.getForEntity(uri, Jobs.class);
            totalRecords = runningJobs.getBody().getTotal();
            walkCoordinators(runningJobs.getBody().getCoordinatorjobs());
            offset += runningJobs.getBody().getCoordinatorjobs().size();
        }
        log.info("Jobs:" + totalRecords + " totalRecords:" + totalRecords + " offset:" + offset);
    }

    public void walkCoordinators(final List<Job> jobs) throws URISyntaxException, ParseException {
        for (final Job job : jobs) {
            if (!excludeCoordinators.contains(job.getCoordJobName())) {
                RestTemplate restTemplate = new RestTemplate();
                URI uri = new URI(oozieHost + "oozie/v1/job/" + job.getCoordJobId() + "?show=info&order=desc&len=" + batchSize + "&offset=1");
                ResponseEntity<Job> runningJobs = restTemplate.getForEntity(uri, Job.class);
                CoordinatorStatus cs = walkActions(job, runningJobs.getBody().getActions());
                if (cs.failedKilledActions > 0 && cs.succeededActions == 0) {
                    log.info("Zombie coordinator:" + job.getCoordJobName()
                                    + " Status:" + job.getStatus()
                                    + " Id:" + job.getCoordJobId()
                                    + " failed/killed:" + cs.failedKilledActions
                                    + " running:" + cs.runningActions
                    );
                    // In theory we could decide to suspend at this point. Figure out how to deal with unsuspending without this script suspending it again within an hour.
                }
                try {
                    Thread.sleep(coordinatorFetchPause);
                } catch (InterruptedException e) {
                }
            }
        }
    }

    /**
     * Walk over a list of actions and find todays actions which have a killed/failed state and trigger a rerun.
     *
     * @param actions
     * @return
     * @throws ParseException
     * @throws URISyntaxException
     */
    public CoordinatorStatus walkActions(final Job job, final List<Action> actions) throws ParseException, URISyntaxException {
        log.info("Walking actions for:"+job.getCoordJobName());
        Calendar now = Tools.removeTime(Calendar.getInstance());
        Calendar weekAgo = Calendar.getInstance();
        weekAgo.add(Calendar.DAY_OF_MONTH, -8);
        Tools.removeTime(weekAgo);
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH);
        CoordinatorStatus cs = new CoordinatorStatus();
        for (final Action action : actions) {
            cal.setTime(sdf.parse(action.getNominalTime()));
            Tools.removeTime(cal);
            if (cal.after(weekAgo)) {
                if (action.getStatus().equals("KILLED") || action.getStatus().equals("FAILED")) {
                    cs.failedKilledActions++;
                    if (cal.getTime().equals(now.getTime())) {
                        log.info(job.getCoordJobName() + " ID:" + action.getId()
                                + " Status:" + action.getStatus()
                                + " Nominal time:" + action.getNominalTime()
                                + " Old workflow which died:" + action.getExternalId());
                        workflowState.change(action, "rerun");
                    }
                } else if (action.getStatus().equals("RUNNING")) {
                    cs.runningActions++;
                    // @TODO This doesn't seem to work it's the stdout logs which might contain info, rethink:
//                    Calendar timeAgo = Calendar.getInstance();
//                    timeAgo.add(Calendar.MINUTE, stuckActionTimeoutMinutes);
//                    Calendar lastDate = workflowState.getLastDateFromLog(workflowState.getLast5MinLog(action));
//                    if (lastDate!=null && lastDate.before(timeAgo)) {
//                        // no logs in the last 40min kill and rerun.
//                        log.info("No logs in the last "+stuckActionTimeoutMinutes+"min.. perhaps we should kill this:"+action.getExternalId());
//                        //workflowState.change(action, "kill");
//                        //workflowState.change(action, "rerun");
//                    }
                } else if (action.getStatus().equals("SUCCEEDED")) {
                    cs.succeededActions++;
                } else if (action.getStatus().equals("READY")) {
                    cs.readyActions++;
                }
            }
        }
        return cs;
    }

    private class CoordinatorStatus {
        int succeededActions=0;
        int runningActions=0;
        int failedKilledActions=0;
        int readyActions=0;
    }
}

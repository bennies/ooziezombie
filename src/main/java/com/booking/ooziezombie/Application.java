package com.booking.ooziezombie;

import com.booking.ooziezombie.services.FailedActionsRerun;
import com.booking.ooziezombie.services.SuspendedCoordinatorKill;
import com.booking.ooziezombie.services.OozieStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.stereotype.Component;

/**
 * https://oozie.apache.org/docs/4.2.0/WebServicesAPI.html
 * https://spring.io/guides/gs/consuming-rest/
 */
@SpringBootApplication
@Component
public class Application implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    @Value("${sleep.betweenruns.millis}")
    private long sleepBetweenRuns;

    @Autowired
    private FailedActionsRerun failedActionsRerun;
    @Autowired
    private SuspendedCoordinatorKill suspendedCoordinatorKill;
    @Autowired
    private OozieStatus oozieStatus;

    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }

    @Override
    public void run(String... strings) throws Exception {
        while(true) {
            if (oozieStatus.getStatus().equals("NORMAL")) {
                failedActionsRerun.run();
                suspendedCoordinatorKill.run();
            }
            try {
                log.info("Going to sleep for "+sleepBetweenRuns+"ms.");
                Thread.sleep(sleepBetweenRuns);
            } catch (InterruptedException e) { }
        }
    }


}

package com.bookin.ooziezombie.services;

import com.booking.ooziezombie.services.WorkflowState;
import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=WorkflowState.class, loader=AnnotationConfigContextLoader.class)
public class WorkflowStateTest {

    @Autowired
    WorkflowState workflowState;

    @Test
    public void testGetLastDateFromLog() {
        String logLine = "2015-12-04 20:05:19,844  INFO ActionStartXCommand:520 - USER[mapred] GROUP[-] TOKEN[] APP[events-hadoopify-fetch] JOB[0874810-151203201845854-oozie-oozi-W] ACTION[0874810-151203201845854-oozie-oozi-W@end] Start action [0874810-151203201845854-oozie-oozi-W@end] with user-retry state : userRetryCount [0], userRetryMax [0], userRetryInterval [10]\n" +
                "2015-12-04 21:00:19,844  INFO ActionStartXCommand:520 - USER[mapred] GROUP[-] TOKEN[] APP[events-hadoopify-fetch] JOB[0874810-151203201845854-oozie-oozi-W] ACTION[0874810-151203201845854-oozie-oozi-W@end] [***0874810-151203201845854-oozie-oozi-W@end***]Action status=DONE\n" +
                "2015-12-04 21:05:19,844  INFO ActionStartXCommand:520 - USER[mapred] GROUP[-] TOKEN[] APP[events-hadoopify-fetch] JOB[0874810-151203201845854-oozie-oozi-W] ACTION[0874810-151203201845854-oozie-oozi-W@end] [***0874810-151203201845854-oozie-oozi-W@end***]Action updated in DB!";

        Calendar cal = workflowState.getLastDateFromLog(logLine);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS");
        String strDate = sdf.format(cal.getTime());
        assertEquals("2015-12-04 21:05:19,844", strDate);
    }

}

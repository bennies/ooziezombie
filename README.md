# OozieZombie

This project should help with some of the oozie pains you might run into. When you have close to a thousand coordinators
running and you have a problem on the cluster the last thing you want to do is manually triggering a retry.
If this happens in a weekend you would want it to keep retrying until you find time to look at the issue.
For now this isn't incredibly modular/generic and might drastically change.

## Main services

### FailedActionsRerun
Look at each running coordinator and it's actions and decide to rerun them based on some criteria.

### FailedCoordinatorSuspend
Look at constantly failing coordinators and after some time decide to suspend them. Also look at suspended coordinators
and after some time decide to kill them.

## Build

### Prerequisite
Java 1.7+
Maven 3.2+

### How to create a jar
mvn package spring-boot:repackage

You now have a jar in the target dir which you can directly use:
java -jar target/OozieZombie-0.0.1.jar

### Start it like a deamon on the cli as your own user
nohup java -Xmx128m -jar OozieZombie-0.0.1.jar &

If you want to make some config changes on the machine you run this on create a file called application.properties and add properties there.


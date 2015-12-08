package com.booking.ooziezombie.json;

import com.booking.ooziezombie.json.Action;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Job {
    private int total;
    private String pauseTime;
    private String coordJobName;
    private String coordJobPath;
    private String timeZone;
    private String frequency;
    private String conf;
    private String endTime;
    private String executionPolicy;
    private String startTime;
    private String timeUnit;
    private String concurrency;
    private String coordJobId;
    private String lastAction;
    private String status;
    private String acl;
    private int mat_throttling;
    private int timeOut;
    private String nextMaterializedTime;
    private String bundleId;
    private String coordExternalId;
    private String group;
    private String user;
    private String consoleUrl;
    @JsonProperty
    private List<Action> actions = new ArrayList<Action>();


    public List<Action> getActions() {
        return actions;
    }

    public void setActions(List<Action> actions) {
        this.actions = actions;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getPauseTime() {
        return pauseTime;
    }

    public void setPauseTime(String pauseTime) {
        this.pauseTime = pauseTime;
    }

    public String getCoordJobName() {
        return coordJobName;
    }

    public void setCoordJobName(String coordJobName) {
        this.coordJobName = coordJobName;
    }

    public String getCoordJobPath() {
        return coordJobPath;
    }

    public void setCoordJobPath(String coordJobPath) {
        this.coordJobPath = coordJobPath;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getConf() {
        return conf;
    }

    public void setConf(String conf) {
        this.conf = conf;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getExecutionPolicy() {
        return executionPolicy;
    }

    public void setExecutionPolicy(String executionPolicy) {
        this.executionPolicy = executionPolicy;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getTimeUnit() {
        return timeUnit;
    }

    public void setTimeUnit(String timeUnit) {
        this.timeUnit = timeUnit;
    }

    public String getConcurrency() {
        return concurrency;
    }

    public void setConcurrency(String concurrency) {
        this.concurrency = concurrency;
    }

    public String getCoordJobId() {
        return coordJobId;
    }

    public void setCoordJobId(String coordJobId) {
        this.coordJobId = coordJobId;
    }

    public String getLastAction() {
        return lastAction;
    }

    public void setLastAction(String lastAction) {
        this.lastAction = lastAction;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAcl() {
        return acl;
    }

    public void setAcl(String acl) {
        this.acl = acl;
    }

    public int getMat_throttling() {
        return mat_throttling;
    }

    public void setMat_throttling(int mat_throttling) {
        this.mat_throttling = mat_throttling;
    }

    public int getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(int timeOut) {
        this.timeOut = timeOut;
    }

    public String getNextMaterializedTime() {
        return nextMaterializedTime;
    }

    public void setNextMaterializedTime(String nextMaterializedTime) {
        this.nextMaterializedTime = nextMaterializedTime;
    }

    public String getBundleId() {
        return bundleId;
    }

    public void setBundleId(String bundleId) {
        this.bundleId = bundleId;
    }

    public String getCoordExternalId() {
        return coordExternalId;
    }

    public void setCoordExternalId(String coordExternalId) {
        this.coordExternalId = coordExternalId;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getConsoleUrl() {
        return consoleUrl;
    }

    public void setConsoleUrl(String consoleUrl) {
        this.consoleUrl = consoleUrl;
    }

}

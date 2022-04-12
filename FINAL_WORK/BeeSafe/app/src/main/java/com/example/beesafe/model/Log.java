package com.example.beesafe.model;

import com.example.beesafe.utils.Operation;
import com.example.beesafe.utils.Outcome;
import com.example.beesafe.utils.RequestType;

import java.util.Comparator;

public class Log implements Comparable<Log>{
    private long timestamp;
    private String user;
    private String requestType;
    private String operation;
    private String outcome;
    private boolean isAdmin;

    public Log(long timestamp, String user, String requestType, String operation, String outcome, boolean isAdmin) {
        this.timestamp = timestamp;
        this.user = user;
        this.requestType = requestType;
        this.operation = operation;
        this.outcome = outcome;
        this.isAdmin = isAdmin;
    }

    @Override
    public String toString() {
        return "Log{" +
                "timestamp=" + timestamp +
                ", user='" + user + '\'' +
                ", requestType='" + requestType + '\'' +
                ", operation='" + operation + '\'' +
                ", outcome='" + outcome + '\'' +
                ", isAdmin=" + isAdmin +
                '}';
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getOutcome() {
        return outcome;
    }

    public void setOutcome(String outcome) {
        this.outcome = outcome;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    @Override
    public int compareTo(Log log) {
        long compareQuantity = log.getTimestamp();
        return (int) (this.timestamp - compareQuantity);
    }

    /*
    public static Comparator<Log> LogComparator = new Comparator<Log>() {
        @Override
        public int compare(Log log, Log t1) {
            long millis1 = log.getTimestamp();
            long millis2 = log.getTimestamp();

            return (int) (millis1-millis2);
        }
    };*/
}

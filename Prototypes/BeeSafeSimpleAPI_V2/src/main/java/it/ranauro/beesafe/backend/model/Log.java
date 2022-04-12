package it.ranauro.beesafe.backend.model;

import it.ranauro.beesafe.backend.utils.Operation;
import it.ranauro.beesafe.backend.utils.Outcome;
import it.ranauro.beesafe.backend.utils.RequestType;

public class Log {
    private long timestamp;
    private String user;
    private RequestType requestType;
    private Operation operation;
    private Outcome outcome;
    private boolean isAdmin;

    public Log(long timestamp, String user, RequestType requestType, Operation operation, Outcome outcome, boolean isAdmin) {
        this.timestamp = timestamp;
        this.user = user;
        this.requestType = requestType;
        this.operation = operation;
        this.outcome = outcome;
        this.isAdmin = isAdmin;
    }

    public Log() {
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

    public RequestType getRequestType() {
        return requestType;
    }

    public void setRequestType(RequestType requestType) {
        this.requestType = requestType;
    }

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    public Outcome getOutcome() {
        return outcome;
    }

    public void setOutcome(Outcome outcome) {
        this.outcome = outcome;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    @Override
    public String toString() {
        return "Log{" +
                "timestamp=" + timestamp +
                ", user='" + user + '\'' +
                ", requestType=" + requestType +
                ", operation=" + operation +
                ", outcome=" + outcome +
                ", isAdmin=" + isAdmin +
                '}';
    }
}


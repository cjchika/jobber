package com.cjchika.jobber.application.enums;

public enum Status {
    SUBMITTED("SUBMITTED"),
    INTERVIEW_SCHEDULED("INTERVIEW_SCHEDULED"),
    REJECTED("REJECTED"),
    ACCEPTED("ACCEPTED"),
    WITHDRAWN("WITHDRAWN");

    private final String value;

    Status(String value) {
        this.value = value;
    }

    public static Status fromValue(String value) {
        for (Status status : Status.values()) {
            if (status.value.equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid Status: " + value);
    }
}

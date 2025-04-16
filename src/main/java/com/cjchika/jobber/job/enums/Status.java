package com.cjchika.jobber.job.enums;

public enum Status {
    ACTIVE("ACTIVE"),
    CLOSED("CLOSED"),
    DRAFT("DRAFT");

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

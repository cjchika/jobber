package com.cjchika.jobber.job.enums;

public enum JobType {
    FULL_TIME("FULL_TIME"),
    PART_TIME("PART_TIME"),
    CONTRACT("CONTRACT"),
    INTERNSHIP("INTERNSHIP");

    private final String value;

    JobType(String value) {
        this.value = value;
    }

    public static JobType fromValue(String value) {
        for (JobType type : JobType.values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid JobType: " + value);
    }
}

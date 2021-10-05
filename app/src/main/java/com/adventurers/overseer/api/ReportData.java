package com.adventurers.overseer.api;

import com.google.gson.annotations.SerializedName;

public class ReportData {
    private int id;
    private ReportDataUser user;
    private String description;
    private int floodLevel;
    private String timestamp;

    public int getId() {
        return id;
    }

    public ReportDataUser getUser() {
        return user;
    }

    public String getDescription() {
        return description;
    }

    public int getFloodLevel() {
        return floodLevel;
    }

    public String getTimestamp() {
        return timestamp;
    }
}

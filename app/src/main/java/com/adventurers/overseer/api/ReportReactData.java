package com.adventurers.overseer.api;

public class ReportReactData {
    private int report;
    private int user;
    private boolean isPositive;

    public ReportReactData(int report, int user, boolean isPositive) {
        this.report = report;
        this.user = user;
        this.isPositive = isPositive;
    }

    public int getReport() {
        return report;
    }

    public int getUser() {
        return user;
    }

    public boolean isPositive() {
        return isPositive;
    }
}

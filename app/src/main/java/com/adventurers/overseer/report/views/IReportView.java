package com.adventurers.overseer.report.views;

public interface IReportView {
    void renderReportSuccess(String message);
    void renderReportProgressing(String message);
    void renderReportFailure(int code, String message);
}

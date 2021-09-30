package com.adventurers.overseer.report.views;

public interface IReportView {
    void renderReportSuccess();
    void renderReportProgressing();
    void renderReportFailure(int error_code, String message);
}

package com.adventurers.overseer.report.views;

public interface IReportView {
    void renderReportSuccess();
    void renderReportFailure();
    void renderReportProgressing();
}

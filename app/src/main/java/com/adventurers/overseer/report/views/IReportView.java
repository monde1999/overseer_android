package com.adventurers.overseer.report.views;

public interface IReportView {
    void renderReportFailure(int i, String str);

    void renderReportProgressing();

    void renderReportSuccess();
}


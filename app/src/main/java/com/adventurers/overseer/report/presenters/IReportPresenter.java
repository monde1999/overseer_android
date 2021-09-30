package com.adventurers.overseer.report.presenters;

public interface IReportPresenter {
    void presentReportFailure(int i, String str);

    void presentReportProgressing();

    void presentReportSuccess();
}


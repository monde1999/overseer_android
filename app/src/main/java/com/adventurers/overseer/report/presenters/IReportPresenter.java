package com.adventurers.overseer.report.presenters;

public interface IReportPresenter {
    void presentReportFailure(int errorCode, String errorMessage);
    void presentReportProgressing();
    void presentReportSuccess();
}


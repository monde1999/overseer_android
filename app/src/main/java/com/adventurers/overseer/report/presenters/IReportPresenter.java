package com.adventurers.overseer.report.presenters;

public interface IReportPresenter {
    void presentReportFailure(int code,String message);

    void presentReportProgressing(String message);

    void presentReportSuccess(String message);
}


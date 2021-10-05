package com.adventurers.overseer.report.interactors;


import com.adventurers.overseer.map.models.Location;
import com.adventurers.overseer.report.controllers.IReportController;
import com.adventurers.overseer.report.controllers.ReportController;
import com.adventurers.overseer.report.models.CreateReportData;
import com.adventurers.overseer.report.presenters.IReportPresenter;

import java.io.File;
import java.util.Date;
import java.util.List;

public class ReportInteractor{
    IReportController controller;
    IReportPresenter presenter;
    ReportController rc;
    CreateReportData createreportData;

    public ReportInteractor(IReportPresenter presenter)
    {
        this.presenter = presenter;
    }

    public void report(int UserID, Location location, Date datetime, List<File> picture, int floodLevel, String description) {
        this.rc = new ReportController(this);
        CreateReportData reportData = new CreateReportData(UserID, description, location, floodLevel, picture);
        this.createreportData = reportData;
        this.rc.addReportToDb(reportData);
    }
    public void feedbackReportSuccess(String message)
    {
        this.presenter.presentReportSuccess(message);
    }

    public void feedbackReportError(int code, String message) {
        this.presenter.presentReportFailure(code, message);
    }

    public void feedbackReportProgressing(String message)
    {
        this.presenter.presentReportProgressing(message);
    }
}


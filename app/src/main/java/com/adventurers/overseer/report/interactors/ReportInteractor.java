package com.adventurers.overseer.report.interactors;

import com.adventurers.overseer.report.controllers.IReportController;
import com.adventurers.overseer.report.controllers.ReportController;
import com.adventurers.overseer.report.presenters.IReportPresenter;
import com.adventurers.overseer.server.CreateReportData;

import java.io.File;
import java.util.Date;
import java.util.List;

public class ReportInteractor {
    IReportController controller;
    IReportPresenter presenter;
    ReportController rc;
    CreateReportData createreportData;

    public ReportInteractor(IReportPresenter presenter) {
        this.presenter = presenter;
    }

    public ReportInteractor(IReportController controller) {
        this.controller = controller;
    }

    public void report(int UserID, double locX, double locY, Date datetime, List<File> picture, int floodLevel, String description) {
        this.rc = new ReportController(this);
        CreateReportData reportData = new CreateReportData(UserID, description, locX, locY, floodLevel, picture);
        this.createreportData = reportData;
        this.rc.addReportToDb(reportData);
    }
    public void feedbackReportSuccess() {
        this.presenter.presentReportSuccess();
    }
    public void feedbackReportError(int errorCode, String Description) {
        this.presenter.presentReportFailure(errorCode, Description);
    }

    public void feedbackReportProgressing() {
        this.presenter.presentReportProgressing();
    }
}


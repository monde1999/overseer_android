package com.adventurers.overseer.report.presenters;


import com.adventurers.overseer.map.models.Location;
import com.adventurers.overseer.report.interactors.ReportInteractor;
import com.adventurers.overseer.report.views.IReportView;

import java.io.File;
import java.util.Date;
import java.util.List;

public class ReportPresenter implements IReportPresenter {
        ReportInteractor ri;
        IReportView rv;

public ReportPresenter(IReportView view) {
        this.rv = view;
        }

public void presentReportSuccess(String message) {
        this.rv.renderReportSuccess(message);
        }

public void presentReportFailure(int code, String message) {
        this.rv.renderReportFailure(code, message);
        }

public void report(int userID, Location location, Date datetime, int floodLevel, List<File> picture, String description, String token) {
        ReportInteractor reportInteractor = new ReportInteractor(this);
        this.ri = reportInteractor;
        reportInteractor.report(userID, location, datetime, picture, floodLevel, description, token);
        }

public void presentReportProgressing(String message) {
        this.rv.renderReportProgressing(message);
        }
}


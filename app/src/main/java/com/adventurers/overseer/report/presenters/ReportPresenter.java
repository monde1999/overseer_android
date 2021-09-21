package com.adventurers.overseer.report.presenters;

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

public void presentReportSuccess() {
        this.rv.renderReportSuccess();
        }

public void presentReportFailure(int errorCode, String errorMessage) {
        this.rv.renderReportFailure(errorCode, errorMessage);
        }

public void report(int userID, double locationX, double locationY, Date datetime, int floodLevel, List<File> picture, String description) {


        ReportInteractor reportInteractor = new ReportInteractor(this);
        this.ri = reportInteractor;
        reportInteractor.report(userID, locationX, locationY, datetime, picture, floodLevel, description);
        }

public void presentReportProgressing() {
        this.rv.renderReportProgressing();
        }
        }


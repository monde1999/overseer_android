package com.adventurers.overseer.report.presenters;

import com.adventurers.overseer.floodforecast.models.DateTime;
import com.adventurers.overseer.map.models.Location;

import java.io.File;
import java.util.List;

public class ReportPresenter implements IReportPresenter {
    @Override
    public void presentReportFailure(int errorCode, String errorMessage) {

    }

    @Override
    public void presentReportProgressing() {

    }

    @Override
    public void presentReportSuccess() {

    }

    public void report(int userId, Location location, DateTime time, int floodLevel, List<File> imageFiles, String description) {

    }
}

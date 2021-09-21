package com.adventurers.overseer.report.controllers;

import com.adventurers.overseer.server.CreateReportData;

public interface IReportController {
    void addReportToDb(CreateReportData reportData);
}


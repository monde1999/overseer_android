package com.adventurers.overseer.report.controllers;


import com.adventurers.overseer.report.server.CreateReportData;

public interface IReportController {
    void addReportToDb(CreateReportData reportData);

}


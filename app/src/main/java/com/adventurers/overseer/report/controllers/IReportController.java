package com.adventurers.overseer.report.controllers;


import com.adventurers.overseer.report.models.CreateReportData;

public interface IReportController {
    void addReportToDb(CreateReportData reportData, String token);

}


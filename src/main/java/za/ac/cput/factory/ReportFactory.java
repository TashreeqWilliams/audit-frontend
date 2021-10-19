package za.ac.cput.factory;

/*
 Factory for Report
 Author: Mlungisi Xakekile (215126505)
 Date: 04 June 2021
*/

import za.ac.cput.entity.Report;

public class ReportFactory {

    public static Report createReport(String reportId, String reportAuth, String reportDate){

        return new Report.Builder().
                setReportId(reportId).
                setReportAuth(reportAuth).
                setReportDate(reportDate).
                build();
    }
}

/*
 *
 * Copyright (c) 2001-2002. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved. *
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version. *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. *
 *
 * <OSCAR TEAM>
 *
 *  DepressionContinuityReporter.java
 *
 * Created on November 25, 2008, 11:28 AM
 *
 *
 *
 */

package oscar.oscarReport.reportByTemplate;

import java.sql.ResultSet;

import javax.servlet.http.HttpServletRequest;

import oscar.oscarDB.DBHandler;

/**
 *
 * @author rjonasz
 */
public class DepressionContinuityReporter implements Reporter{
    private String rsHtml = "";
    private String csv = "";
    /**
     * Creates a new instance of DepressionContinuityReporter
     */
    public DepressionContinuityReporter() {
    }
    
    public boolean generateReport( HttpServletRequest request ) {
        String templateId = request.getParameter("templateId");
        ReportObject curReport = (new ReportManager()).getReportTemplateNoParam(templateId);
        String diag_date_from = request.getParameter("diag_date_from");
        String diag_date_to = request.getParameter("diag_date_to");
        String visit_date_from = request.getParameter("visit_date_from");
        String visit_date_to = request.getParameter("visit_date_to");
        String strDxCodes = request.getParameter("dxCodes:list");
        String[] dxCodes = null;
        if( strDxCodes != null ) {
            dxCodes = strDxCodes.split(",");
        }
        
        
        if( diag_date_from == null ||  diag_date_to == null || visit_date_from == null || visit_date_to == null || dxCodes == null ) {
            rsHtml = "All dates must be set and at least one Dx Code must be set";
            request.setAttribute("errormsg", rsHtml);
            request.setAttribute("templateid", templateId);
            return false;
        }
        
        String cohortSQL = "select d.demographic_no, bi.service_date, bi.dx from demographic d, billing_on_cheader1 bc, billing_on_item bi where bc.demographic_no = d.demographic_no and bc.id = bi.ch1_id and bi.dx in (" + strDxCodes + ")" +
                " and bi.service_date >= '" + diag_date_from + "' and bi.service_date <= '" + diag_date_to + "' group by d.demographic_no,bi.dx order by d.demographic_no, bi.service_date";
    
        String apptSQL = "select a.appointment_date, concat(pAppt.first_name, ' ', pAppt.last_name), concat(pFam.first_name, ' ', pFam.last_name), bi.service_code, drugs.BN, concat(pDrug.first_name,' ',pDrug.last_name) from demographic d," +
                "appointment a left outer join drugs on drugs.demographic_no = a.demographic_no and drugs.rx_date = a.appointment_date and a.appointment_date >= '" + visit_date_from + "' and a.appointment_date <= '" + visit_date_to + 
                "' and a.demographic_no = ? left join provider pDrug on pDrug.provider_no = drugs.provider_no, billing_on_cheader1 bc, billing_on_item bi, provider pAppt, provider pFam where a.appointment_date >= '" + visit_date_from + "' and a.appointment_date <= '" + visit_date_to + "' and a.demographic_no = d.demographic_no" + 
                " and a.provider_no = pAppt.provider_no and d.provider_no = pFam.provider_no and bc.appointment_no = a.appointment_no and bi.ch1_id = bc.id and a.demographic_no = ?";

        ResultSet rs = null;        
        Boolean odd = new Boolean(true);
        try {
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            rs = db.GetSQL(cohortSQL);

            rsHtml = this.makeHTMLHeader();
            csv = this.makeCSVHeader();
            String curDemo = null;
            while(rs.next() ) {
                if( curDemo == null ) {
                    curDemo = rs.getString(1);
                }

                if( curDemo.equalsIgnoreCase(rs.getString(1))) {
                    this.addCodeEntry(rs, odd);
                }

                if( !curDemo.equalsIgnoreCase(rs.getString(1))) {
                    this.addAppt(db, apptSQL, curDemo, odd);
                    this.addCodeEntry(rs, odd);
                }
                curDemo = rs.getString(1);
            }
            if( curDemo != null ) {
                this.addAppt(db, apptSQL, curDemo, odd);
            }
            rsHtml += "</table>";
        }catch(Exception e) {
            e.printStackTrace();
            
        }
        
        String sql = cohortSQL + ";\n " + apptSQL;
        request.setAttribute("reportobject", curReport);
        request.setAttribute("resultsethtml", rsHtml);
        request.setAttribute("csv", csv);
        request.setAttribute("sql", sql);
        return true;
    }

    private void addCodeEntry(ResultSet rs, Boolean odd) throws Exception {
        rsHtml += "<tr class=\"";
        if( odd ) {
            rsHtml += "reportRow1\">";
        }
        else {
            rsHtml += "reportRow2\">";
        }
        odd = !odd;
        rsHtml += "<td>" + rs.getString(1) + "</td><td>" + rs.getString(2) + "</td><td>" + rs.getString(3) + "</td>";
        rsHtml += "<td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td>";
        rsHtml += "<td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td>";
        rsHtml += "</tr>";
        csv += rs.getString(1) + "," + rs.getString(2) + "," + rs.getString(3);
        csv += ", , , , , , \n";
    }

    private void addAppt(DBHandler db, String apptSQL, String curDemo, Boolean odd) throws Exception {
        ResultSet rs2 = null;
        String apptSQLwDemo;
        apptSQLwDemo = apptSQL.replaceAll("\\?", curDemo);
        rs2 = db.GetSQL(apptSQLwDemo);
        System.out.println(apptSQLwDemo);
        String rxName, rxPrescriber;
        while(rs2.next()) {
            rsHtml += "<tr class=\"";
            if( odd ) {
                rsHtml += "reportRow1\">";
            }
            else {
                rsHtml += "reportRow2\">";
            }
            odd = !odd;
            rsHtml += "<td>" + curDemo + "</td><td>&nbsp;</td><td>&nbsp;</td>";
            rsHtml += "<td>" + rs2.getString(1) + "</td><td>" + rs2.getString(2) + "</td><td>" + rs2.getString(3) + "</td>";

            rsHtml += "<td>" + rs2.getString(4) + "</td><td>" + rs2.getString(5)  + "</td><td>" + rs2.getString(6) + "</td>";
            rsHtml += "</tr>";

            csv += curDemo + ", , ";
            csv += "," + rs2.getString(1) + "," + rs2.getString(2) + "," + rs2.getString(3);
            rxName = rs2.getString(5) == null ? " ":rs2.getString(5);
            rxPrescriber = rs2.getString(6) == null ? " " : rs2.getString(6);
            csv += "," + rs2.getString(4) + "," + rxName + "," + rxPrescriber + "\n";
        }
    }

    private String makeHTMLHeader() {
        StringBuffer html = new StringBuffer("<table class=\"reportTable\">\n");
        html.append("<th class=\"reportHeader\">ID</th><th class=\"reportHeader\">Date of Code</th><th class=\"reportHeader\">Dx Code</th>"
                + "<th class=\"reportHeader\">Date of Visit</th><th class=\"reportHeader\">Provider Seen</th><th class=\"reportHeader\">MRP</th>"
                + "<th class=\"reportHeader\">Billing Code</th><th class=\"reportHeader\">Rx Name</th><th class=\"reportHeader\">Prescriber</th>");

        return html.toString();
    }

    private String makeCSVHeader() {
        String cvs = "ID,Date of Code,Dx Code,Date of Visit,Provider Seen,MRP,Billing Code,Rx Name,Prescriber\n";
        return cvs;
    }

    

}

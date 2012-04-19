/**
 *
 * Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * This software was written for
 * Centre for Research on Inner City Health, St. Michael's Hospital,
 * Toronto, Ontario, Canada
 */


package oscar.oscarReport.reportByTemplate;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.oscarehr.util.MiscUtils;

import oscar.oscarDB.DBHandler;

/**
 *
 * @author rjonasz
 */
public class DepressionContinuityReporter implements Reporter{
    private StringBuilder rsHtml = new StringBuilder();
    private StringBuilder csv = new StringBuilder();
    private HashMap<String,StringBuilder>demographics = new HashMap<String,StringBuilder>();
    private HashMap<String,StringBuilder>csvMap = new HashMap<String,StringBuilder>();
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
            rsHtml.append("All dates must be set and at least one Dx Code must be set");
            request.setAttribute("errormsg", rsHtml.toString());
            request.setAttribute("templateid", templateId);
            return false;
        }
        
        String cohortSQL = "select d.demographic_no, bi.service_date, bi.dx from demographic d, billing_on_cheader1 bc, billing_on_item bi where bc.demographic_no = d.demographic_no and bc.id = bi.ch1_id and bi.dx in (" + strDxCodes + ")" +
                " and bi.service_date >= '" + diag_date_from + "' and bi.service_date <= '" + diag_date_to + "' group by d.demographic_no,bi.dx order by d.demographic_no, bi.service_date";
    
        String apptSQL = "select a.appointment_date, concat(pAppt.first_name, ' ', pAppt.last_name), concat(pFam.first_name, ' ', pFam.last_name), bi.service_code, drugs.BN, concat(pDrug.first_name,' ',pDrug.last_name), a.demographic_no, drugs.GN, drugs.customName from demographic d," +
                    "appointment a left outer join drugs on drugs.demographic_no = a.demographic_no and drugs.rx_date = a.appointment_date and a.appointment_date >= '" + visit_date_from + "' and a.appointment_date <= '" + visit_date_to +
                    "' and a.demographic_no in (?) left join provider pDrug on pDrug.provider_no = drugs.provider_no, billing_on_cheader1 bc, billing_on_item bi, provider pAppt, provider pFam where a.appointment_date >= '" + visit_date_from + "' and a.appointment_date <= '" + visit_date_to + "' and a.demographic_no = d.demographic_no" +
                    " and a.provider_no = pAppt.provider_no and d.provider_no = pFam.provider_no and bc.appointment_no = a.appointment_no and bi.ch1_id = bc.id and a.demographic_no in (?) order by a.demographic_no, a.appointment_date";

        ResultSet rs = null;        
        Boolean odd = new Boolean(true);
        try {
            
            rs = DBHandler.GetSQL(cohortSQL);

            rsHtml = this.makeHTMLHeader();
            csv = this.makeCSVHeader();
            StringBuilder html = new StringBuilder();
            StringBuilder csvTmp = new StringBuilder();
            String curDemo = null;
            while(rs.next() ) {
                if( curDemo == null ) {
                    curDemo = rs.getString(1);
                }

                if( curDemo.equalsIgnoreCase(rs.getString(1))) {
                    html.append(this.addCodeEntry(rs, odd));
                    csvTmp.append(this.csvCodeEntry(rs));
                }

                if( !curDemo.equalsIgnoreCase(rs.getString(1))) {
                    demographics.put(curDemo, html);
                    csvMap.put(curDemo, csvTmp);
                    html = this.addCodeEntry(rs, odd);
                    csvTmp = new StringBuilder(this.csvCodeEntry(rs));
                }
                curDemo = rs.getString(1);
            }
            if( curDemo != null ) {
                demographics.put(curDemo, html);
                csvMap.put(curDemo, csvTmp);
            }

            this.addAppt(apptSQL, curDemo, odd);

            rsHtml.append("</table>");
        }catch(Exception e) {
            MiscUtils.getLogger().error("Error", e);
            
        }
        
        String sql = cohortSQL +  ";\n " + apptSQL;
        request.setAttribute("reportobject", curReport);
        request.setAttribute("resultsethtml", rsHtml.toString());
        request.setAttribute("csv", csv.toString());
        request.setAttribute("sql", sql);
        return true;
    }

    private StringBuilder addCodeEntry(ResultSet rs, Boolean odd) throws Exception {
         StringBuilder html = new StringBuilder("<tr class=\"");
        if( odd ) {
            html.append("reportRow1\">");
        }
        else {
            html.append("reportRow2\">");
        }
        odd = !odd;
        html.append("<td>" + rs.getString(1) + "</td><td>" + rs.getString(2) + "</td><td>" + rs.getString(3) + "</td>");
        html.append("<td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td>");
        html.append("<td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td>");
        html.append("</tr>");

        return html;
    }

    private String csvCodeEntry(ResultSet rs) throws Exception {
        String csvCode =  rs.getString(1) + "," + rs.getString(2) + "," + rs.getString(3) + ", , , , , , \n";
        return csvCode;
    }

    private void addAppt(String apptSQL, String curDemo, Boolean odd) throws Exception {
        ResultSet rs2 = null;

        Set<String>setDemo = demographics.keySet();
        Iterator<String>iter = setDemo.iterator();
        StringBuilder demos = new StringBuilder();
        while(iter.hasNext()) {
            demos.append(iter.next());
            if( iter.hasNext() ) {
                demos.append(",");
            }
        }

        String apptSQLwDemo;
        apptSQLwDemo = apptSQL.replaceAll("\\?", demos.toString());
        MiscUtils.getLogger().debug(apptSQLwDemo);
        rs2 = DBHandler.GetSQL(apptSQLwDemo);        
        String rxName, rxPrescriber, tmpDemo = "";
        while(rs2.next()) {
            if( !tmpDemo.equals(rs2.getString(7))) {
                MiscUtils.getLogger().debug(rs2.getString(7));
                rsHtml.append(demographics.get(rs2.getString(7)));
                csv.append(csvMap.get(rs2.getString(7)));
                tmpDemo = rs2.getString(7);
            }
            rsHtml.append("<tr class=\"");
            if( odd ) {
                rsHtml.append("reportRow1\">");
            }
            else {
                rsHtml.append("reportRow2\">");
            }
            odd = !odd;
            rsHtml.append("<td>" + rs2.getString(7) + "</td><td>&nbsp;</td><td>&nbsp;</td>");
            rsHtml.append("<td>" + rs2.getString(1) + "</td><td>" + rs2.getString(2) + "</td><td>" + rs2.getString(3) + "</td>");

            rxName = rs2.getString(5);
            if( rxName == null || rxName.equalsIgnoreCase("null") ) {
                rxName = rs2.getString(8);
                if( rxName == null || rxName.equalsIgnoreCase("null") ) {
                    rxName = rs2.getString(9);
                }
            }
            if( rxName == null ) {
                rxName = "";
            }

            rxPrescriber = rs2.getString(6) == null ? " " : rs2.getString(6);

            rsHtml.append("<td>" + rs2.getString(4) + "</td><td>" + rxName  + "</td><td>" + rxPrescriber + "</td>");
            rsHtml.append("</tr>");

            csv.append(curDemo + ", , ");
            csv.append("," + rs2.getString(1) + "," + rs2.getString(2) + "," + rs2.getString(3));                        
            csv.append("," + rs2.getString(4) + "," + rxName + "," + rxPrescriber + "\n");

        }

        if( curDemo != null && tmpDemo.equals("") ) {
            MiscUtils.getLogger().debug(curDemo);
            rsHtml.append(demographics.get(curDemo));
            csv.append(csvMap.get(curDemo));
        }
    }

    private StringBuilder makeHTMLHeader() {
        StringBuilder html = new StringBuilder("<table class=\"reportTable\">\n");
        html.append("<th class=\"reportHeader\">ID</th><th class=\"reportHeader\">Date of Code</th><th class=\"reportHeader\">Dx Code</th>"
                + "<th class=\"reportHeader\">Date of Visit</th><th class=\"reportHeader\">Provider Seen</th><th class=\"reportHeader\">MRP</th>"
                + "<th class=\"reportHeader\">Billing Code</th><th class=\"reportHeader\">Rx Name</th><th class=\"reportHeader\">Prescriber</th>");

        return html;
    }

    private StringBuilder makeCSVHeader() {
        StringBuilder cvs = new StringBuilder("ID,Date of Code,Dx Code,Date of Visit,Provider Seen,MRP,Billing Code,Rx Name,Prescriber\n");
        return cvs;
    }

    

}

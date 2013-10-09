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

import javax.servlet.http.HttpServletRequest;

import org.oscarehr.common.dao.DxresearchDAO;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.Dxresearch;
import org.oscarehr.common.model.Measurement;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.util.ConversionUtils;

/**
 *
 * @author rjonasz
 */
public class INRReporter implements Reporter {
    
    /** Creates a new instance of INRReporter */
    public INRReporter() {
    }
    
    public boolean generateReport( HttpServletRequest request) {
        String templateId = request.getParameter("templateId");
        ReportObject curReport = (new ReportManager()).getReportTemplateNoParam(templateId);
        String fromDate = request.getParameter("from_date");
        String toDate = request.getParameter("to_date");
        StringBuilder csvBody = new StringBuilder();
        StringBuilder csvHeader = new StringBuilder();
        StringBuilder csv = new StringBuilder();
        StringBuilder rsHtml = new StringBuilder();
        StringBuilder header = new StringBuilder();
        StringBuilder body = new StringBuilder();
        int numHeaders = 1;
        int curHeader = 0;
        ResultSet rs;
        long demographicNo;
        
        if( fromDate == null || toDate == null ) {
            rsHtml.append("from_date and to_date must be set in template");
            request.setAttribute("errormsg", rsHtml.toString());
            request.setAttribute("templateid", templateId);
            return false;
        }

        String cssRow1 = "reportRow1";
        String cssRow2 = "reportRow2";
        String cssCurrent = "";
        boolean firstRow = true;
         try {
            
        	 DxresearchDAO dao = SpringUtils.getBean(DxresearchDAO.class);
            demographicNo = -1;
            rsHtml.append("<table class=\"reportTable\">\n");
            header.append("<th class=\"reportHeader\">Last Name</th><th class=\"reportHeader\">First Name</th><th class=\"reportHeader\">DOB</th><th class=\"reportHeader\">MRP</th><th class=\"reportHeader\">INR</th><th class=\"reportHeader\">Observation Date</th><th class=\"reportHeader\">DX Code</th>");
            csvHeader.append("Last Name,First Name,DOB,MRP,INR,Observation Date,Dx Code");
            for(Object[] o : dao.getDataForInrReport(ConversionUtils.fromDateString(fromDate), ConversionUtils.fromDateString(toDate))) {
            	Demographic d = (Demographic) o[0];
            	Measurement m = (Measurement) o[1];
            	Dxresearch dx = (Dxresearch) o[2];
            	
            	if( demographicNo == d.getDemographicNo().longValue() ) {
                    ++curHeader;
                    if( curHeader > numHeaders ) {
                        numHeaders = curHeader;
                        header.append("<th class=\"reportHeader\">INR</th><th class=\"reportHeader\">Observation Date</th><th class=\"reportHeader\">Dx Code</th>");
                        csvHeader.append(",INR,Observation Date,Dx Code");
                    }
                    body.append("<td>" + m.getDataField() + "</td><td>" + ConversionUtils.toDateString(m.getDateObserved()) + "</td><td>" 
                    		+ dx.getDxresearchCode() + "</td>");
                    csvBody.append("," + m.getDataField() + "," + ConversionUtils.toDateString(m.getDateObserved()) + "," + dx.getDxresearchCode());
                }
                else {
                    if( firstRow ) {
                        firstRow = false;
                    }
                    else {                        
                        while( curHeader++ < numHeaders ) {                            
                            body.append("<td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td>");
                            csvBody.append(", , ,");
                        }
                        body.append("</tr>");
                        csvBody.append("\n");
                    }
                    
                    curHeader = 1;
                    demographicNo = d.getDemographicNo().longValue();                    
                    cssCurrent = cssCurrent.equals(cssRow2) ? cssRow1 : cssRow2;
                    body.append("<tr class=\"" + cssCurrent + "\">");
                    body.append("<td>" + d.getLastName() + "</td><td>" + d.getFirstName() + "</td><td>" + d.getBirthDayAsString() + "</td><td>" + d.getProviderNo() + "</td><td>" + m.getDataField() + "</td><td>" + ConversionUtils.toDateString(m.getDateObserved()) + "</td><td>" + dx.getDxresearchCode() + "</td>");                    
                    csvBody.append(d.getLastName() + "," + d.getFirstName() + "," + d.getBirthDayAsString() + "," + d.getProviderNo() + "," + m.getDataField() + "," + ConversionUtils.toDateString(m.getDateObserved()) + "," + dx.getDxresearchCode());
                    
                }
            }
            if( firstRow ) {
                rsHtml.append("</table><center><font color=\"red\">No Results</font></center>");
            }
            else {
                while( curHeader++ < numHeaders ) {
                    MiscUtils.getLogger().debug("Adding " + curHeader + " header");
                    body.append("<td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td>");
                    csvBody.append(", , ,");
                }
                rsHtml.append(header);
                rsHtml.append(body);
                rsHtml.append("</tr></table>");
                
                csvHeader.append("\n");
                csvBody.append("\n");
                csv.append(csvHeader);
                csv.append(csvBody);
            }            
                        
         }
         catch(Exception e) {
            MiscUtils.getLogger().error("Error", e);            
        }

        request.setAttribute("csv", csv.toString());
        String sql = "select demographic.demographic_no, last_name,first_name,concat(date_of_birth,'-',month_of_birth,'-',year_of_birth) as DOB, demographic.provider_no, dataField as INR, measurements.dateObserved, dx.dxresearch_code from demographic, measurements, dxresearch dx where measurements.demographicNo = dx.demographic_no and dx.status != 'D' and dx.coding_system = 'icd9' and (dx.dxresearch_code = '42731' or dx.dxresearch_code = 'V5861' or dx.dxresearch_code = 'V1251') and measurements.demographicNo = demographic.demographic_no and type = 'INR'   and measurements.dateObserved >= '" + fromDate + "' and measurements.dateObserved <= '" + toDate + "' order by demographic.last_name,demographic.first_name, measurements.dateObserved";
        request.setAttribute("sql", sql);
        request.setAttribute("reportobject", curReport);
        request.setAttribute("resultsethtml", rsHtml.toString());        
        
        return true;
    }
    
}

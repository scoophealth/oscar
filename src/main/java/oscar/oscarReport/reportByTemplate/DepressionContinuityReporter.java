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

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.oscarehr.common.dao.BillingONCHeader1Dao;
import org.oscarehr.common.dao.OscarAppointmentDao;
import org.oscarehr.common.model.BillingONCHeader1;
import org.oscarehr.common.model.BillingONItem;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.util.ConversionUtils;

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
        Date diag_date_from = ConversionUtils.fromDateString(request.getParameter("diag_date_from"));
        Date diag_date_to = ConversionUtils.fromDateString(request.getParameter("diag_date_to"));
        Date visit_date_from = ConversionUtils.fromDateString(request.getParameter("visit_date_from"));
        Date visit_date_to = ConversionUtils.fromDateString(request.getParameter("visit_date_to"));
        String strDxCodes = request.getParameter("dxCodes:list");
        List<String> dxCodes = null;
        if( strDxCodes != null ) {
            dxCodes = Arrays.asList(strDxCodes.split(","));
        }
        
        
        if( diag_date_from == null ||  diag_date_to == null || visit_date_from == null || visit_date_to == null || dxCodes == null ) {
            rsHtml.append("All dates must be set and at least one Dx Code must be set");
            request.setAttribute("errormsg", rsHtml.toString());
            request.setAttribute("templateid", templateId);
            return false;
        }
        
        BillingONCHeader1Dao bDao = SpringUtils.getBean(BillingONCHeader1Dao.class);
        
        String cohortSQL = " -- N/A -- Migrated to JPA ";
        String apptSQL = " -- N/A -- Migrated to JPA ";
                
        Boolean odd = new Boolean(true);
        try {
            rsHtml = this.makeHTMLHeader();
            csv = this.makeCSVHeader();
            StringBuilder html = new StringBuilder();
            StringBuilder csvTmp = new StringBuilder();
            String curDemo = null;
            for(Object[] o : bDao.findDemographicsAndBillingsByDxAndServiceDates(dxCodes, diag_date_from, diag_date_to)) {
            	 // d.demographic_no, bi.service_date, bi.dx 
            	Demographic d = (Demographic) o[0];
            	BillingONCHeader1 bc = (BillingONCHeader1) o[1];
            	BillingONItem bi = (BillingONItem) o[2];
            	
            	 String demographic_no = d.getDemographicNo().toString(); 
            	 String service_date = ConversionUtils.toDateString(bi.getServiceDate());
            	 String dx = bi.getDx();
            	
                if( curDemo == null ) {
                    curDemo = demographic_no;
                }

                if( curDemo.equalsIgnoreCase(demographic_no)) {
                    html.append(this.addCodeEntry(demographic_no, service_date, dx, odd));
                    csvTmp.append(this.csvCodeEntry(demographic_no, service_date, dx));
                }

                if( !curDemo.equalsIgnoreCase(demographic_no)) {
                    demographics.put(curDemo, html);
                    csvMap.put(curDemo, csvTmp);
                    html = this.addCodeEntry(demographic_no, service_date, dx, odd);
                    csvTmp = new StringBuilder(this.csvCodeEntry(demographic_no, service_date, dx));
                }
                curDemo = demographic_no;
            }
            
            if( curDemo != null ) {
                demographics.put(curDemo, html);
                csvMap.put(curDemo, csvTmp);
            }

            this.addAppt(apptSQL, curDemo, diag_date_from, diag_date_to, odd);

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

    private StringBuilder addCodeEntry(String demographic_no, String service_date, String dx, Boolean odd)  {
         StringBuilder html = new StringBuilder("<tr class=\"");
        if( odd ) {
            html.append("reportRow1\">");
        }
        else {
            html.append("reportRow2\">");
        }
        odd = !odd;
        html.append("<td>" + demographic_no + "</td><td>" + service_date + "</td><td>" + dx + "</td>");
        html.append("<td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td>");
        html.append("<td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td>");
        html.append("</tr>");

        return html;
    }

    private String csvCodeEntry(String demographic_no, String service_date, String dx) {
        String csvCode =  demographic_no + "," + service_date + "," + dx + ", , , , , , \n";
        return csvCode;
    }

    private void addAppt(String apptSQL, String curDemo, Date from, Date to, Boolean odd) {
    	OscarAppointmentDao dao = SpringUtils.getBean(OscarAppointmentDao.class);

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
           
        String rxName, rxPrescriber, tmpDemo = "";
        for(Object[] o : dao.findAppointmentsByDemographicIds(setDemo, from, to)) {
			
			Date p1 = (Date) o[0]; 	// "a.appointment_date, " +
			String p2 = (String) o[1]; //"concat(pAppt.first_name, ' ', pAppt.last_name), " +
			String p3 = (String) o[2]; //"concat(pFam.first_name, ' ', pFam.last_name), " +
			String p4 = (String) o[3]; // "bi.service_code, " +
			String p5 = (String) o[4]; //"drugs.BN, " +
			String p6 = (String) o[5]; //"concat(pDrug.first_name,' ',pDrug.last_name), " +
			Integer p7 = (Integer) o[6]; //"a.demographic_no, " +
			String p8 = (String) o[7]; //"drugs.GN, " +
			String p9 = (String) o[8]; //"drugs.customName " +
			
            if( !tmpDemo.equals(p7)) {
                rsHtml.append(demographics.get("" + p7));
                csv.append(csvMap.get("" + p7));
                tmpDemo = "" + p7;
            }
            rsHtml.append("<tr class=\"");
            if( odd ) {
                rsHtml.append("reportRow1\">");
            }
            else {
                rsHtml.append("reportRow2\">");
            }
            odd = !odd;
            rsHtml.append("<td>" + p7 + "</td><td>&nbsp;</td><td>&nbsp;</td>");
            rsHtml.append("<td>" + p1 + "</td><td>" + p2 + "</td><td>" + p3 + "</td>");

            rxName = p5;
            if( rxName == null || rxName.equalsIgnoreCase("null") ) {
                rxName = p8;
                if( rxName == null || rxName.equalsIgnoreCase("null") ) {
                    rxName = p9;
                }
            }
            if( rxName == null ) {
                rxName = "";
            }

            rxPrescriber = p6 == null ? " " : p6;

            rsHtml.append("<td>" + p4 + "</td><td>" + rxName  + "</td><td>" + rxPrescriber + "</td>");
            rsHtml.append("</tr>");

            csv.append(curDemo + ", , ");
            csv.append("," + p1 + "," + p2 + "," + p3);                        
            csv.append("," + p4 + "," + rxName + "," + rxPrescriber + "\n");

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

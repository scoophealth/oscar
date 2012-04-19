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

import org.oscarehr.util.MiscUtils;

import oscar.oscarDB.DBHandler;

/**
 *
 * @author rjonasz
 */
public class ThirdApptTimeReporter implements Reporter{
    
    /**
     * Creates a new instance of UnusedMinutesReporter
     */
    public ThirdApptTimeReporter() {
    }
    
    public boolean generateReport( HttpServletRequest request ) {
        String templateId = request.getParameter("templateId");
        ReportObject curReport = (new ReportManager()).getReportTemplateNoParam(templateId);
        String date_from = request.getParameter("date_from");
        
        String tmp = request.getParameter("scheduleSymbols:list");
        String[] schedSymbols = null;
        if( tmp != null ) {
            schedSymbols = tmp.split(",");
        }

        tmp = request.getParameter("provider_no:list");
        String[] providers = null;
        if( tmp != null ) {
            providers = tmp.split(",");
        }

        int apptLength = Integer.parseInt(request.getParameter("apptLength"));
        int numDays = -1;
        String rsHtml = "";
        String csv = "";
        if( date_from == null ||  providers == null || schedSymbols == null ) {
            rsHtml = "date_from and provider must be set and at least one schedule symbol must be set";
            request.setAttribute("errormsg", rsHtml);
            request.setAttribute("templateid", templateId);
            return false;
        }

        StringBuilder providerList = new StringBuilder();
        for( int idx = 0; idx < providers.length; ++idx ) {
            providerList.append("'" + providers[idx] + "'");
            if( idx < providers.length - 1) {
                providerList.append(",");
            }
        }

        
        String scheduleSQL = "select scheduledate.provider_no, scheduletemplate.timecode, scheduledate.sdate from scheduletemplate, scheduledate where scheduletemplate.name=scheduledate.hour and scheduledate.sdate >= '" + date_from + "' and  scheduledate.provider_no in (" + providerList.toString() + ") and scheduledate.status = 'A' and (scheduletemplate.provider_no=scheduledate.provider_no or scheduletemplate.provider_no='Public') order by scheduledate.sdate";
        String apptSQL = "";//select start_time, end_time from appointment where provider_no = '" + provider_no + "' and status not like '%C%' and appointment_date = '";
        String schedDate = "";
        ResultSet rs = null;
        ResultSet rs2 = null;
        int unbooked = 0;

        try {
            
            rs = DBHandler.GetSQL(scheduleSQL);
            int duration;
            String timecodes, code;
            String tmpApptSQL;           
            String apptTime;

            int dayMins = 24*60;
            int iHours,iMins,apptHour_s,apptMin_s,apptHour_e,apptMin_e;
            int codePos;            
            int latestApptHour, latestApptMin;            
            int third = 3;
            int numAppts = 0;
            boolean codeMatch;
            while(rs.next() && numAppts < third) {
                timecodes = rs.getString("timecode"); 

                duration = dayMins/timecodes.length();

                schedDate = rs.getString("sdate");
                tmpApptSQL = "select start_time, end_time from appointment where provider_no = '" + rs.getString("provider_no") + "' and status not like '%C%' and appointment_date = '" + schedDate + "' order by start_time asc";

                rs2 = DBHandler.GetSQL(tmpApptSQL);
                codePos = 0;
                latestApptHour = latestApptMin = 0;
                unbooked = 0;
                for(int iTotalMin = 0; iTotalMin < dayMins; iTotalMin+=duration) {
                    code = timecodes.substring(codePos,codePos+1);
                    ++codePos;
                    iHours = iTotalMin/60;
                    iMins = iTotalMin % 60;
                    while( rs2.next() ) {
                        apptTime = rs2.getString("start_time");
                        apptHour_s = Integer.parseInt(apptTime.substring(0,2));
                        apptMin_s = Integer.parseInt(apptTime.substring(3,5));

                        if( iHours == apptHour_s && iMins == apptMin_s ) {
                            apptTime = rs2.getString("end_time");
                            apptHour_e = Integer.parseInt(apptTime.substring(0,2));
                            apptMin_e = Integer.parseInt(apptTime.substring(3,5));
                            
                            if( apptHour_e > latestApptHour || (apptHour_e == latestApptHour && apptMin_e > latestApptMin) ) {
                                latestApptHour = apptHour_e;
                                latestApptMin = apptMin_e;
                            }                    
                        }
                        else {
                            rs2.previous();                            
                            break;
                        }
                        
                    }

                    codeMatch = false;
                    for( int schedIdx = 0; schedIdx < schedSymbols.length; ++schedIdx ) {
                        
                        if( code.equals(schedSymbols[schedIdx]) ) {                        
                            codeMatch = true;
                            MiscUtils.getLogger().debug("codeMatched " + codeMatch);
                            if( iHours > latestApptHour || (iHours == latestApptHour && iMins > latestApptMin)) {
                                unbooked += duration;

                                if( unbooked >= apptLength ) {
                                    unbooked = 0;
                                    ++numAppts;
                                    if( numAppts == third ) {
                                        break;
                                    }
                                }
                            }
                            
                        }
                    } //end for schedule symbols

                    if( numAppts == third ) {
                        break;
                    }

                    if( !codeMatch ) {
                        unbooked = 0;
                    }
                    
                } //end for
                
            } //end while
            
            
            String calcDaysSQL = "select datediff('" + schedDate + "','" + date_from + "')";
            if( numAppts == third ) {
                rs = DBHandler.GetSQL(calcDaysSQL);
                if( rs.next() ) {
                    numDays = rs.getInt(1);
                }
            }
            
        }catch(Exception e) {
            MiscUtils.getLogger().error("Error", e);
            
        }
        rsHtml = makeHTML(numDays,date_from, schedDate);
        csv = makeCSV(numDays,date_from, schedDate);
        String sql = scheduleSQL + ";\n " + apptSQL;
        request.setAttribute("reportobject", curReport);
        request.setAttribute("resultsethtml", rsHtml);
        request.setAttribute("csv", csv);
        request.setAttribute("sql", sql);
        return true;
    }


    private String makeHTML(int numDays, String requestDate, String thirdDate) {
        String html = "<table class=\"reportTable\">\n";                        
        if( numDays == -1 ) {
            html +=  "<tr class=\"reportRow1\"><td>There are no 3rd appointments available" + "</td>\n</tr>\n</table>\n";
        }
        else {
            html += "<th class=\"reportHeader\">Request Date</th><th class=\"reportHeader\">3rd Appointment Date</th><th class=\"reportHeader\">Days Waiting</th>" +
                "<tr class=\"reportRow1\"><td>" + requestDate + "</td>\n<td>" + thirdDate +  "</td>\n<td>" + String.valueOf(numDays) +
                "</td>\n</tr>\n</table>\n";
        }
        
        return html;
    }
    
    private String makeCSV(int numDays, String requestDate, String thirdDate) {
        String csv;
        if( numDays == -1 ) {
            csv = "";
        }
        else {
            csv = "Request Date," + requestDate + ",3rd Appointment Date," + thirdDate + ",Days Waiting," + String.valueOf(numDays);
        }
        return csv;
    }

}

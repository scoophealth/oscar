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

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.oscarehr.common.dao.OscarAppointmentDao;
import org.oscarehr.common.dao.ScheduleTemplateDao;
import org.oscarehr.common.model.Appointment;
import org.oscarehr.common.model.ScheduleDate;
import org.oscarehr.common.model.ScheduleTemplate;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.util.ConversionUtils;

public class UnusedMinutesReporter implements Reporter{
    
    /**
     * Creates a new instance of UnusedMinutesReporter
     */
    public UnusedMinutesReporter() {
    }
    
    public boolean generateReport( HttpServletRequest request ) {
        String templateId = request.getParameter("templateId");
        ReportObject curReport = (new ReportManager()).getReportTemplateNoParam(templateId);
        String date_from = request.getParameter("date_from");
        String date_to = request.getParameter("date_to");
        String provider_no = request.getParameter("provider_no");
        String rsHtml = "";
        String csv = "";
        if( date_from == null || date_to == null || provider_no == null ) {
            rsHtml = "date_from, date_to and provider_no must be set";
            request.setAttribute("errormsg", rsHtml);
            request.setAttribute("templateid", templateId);
            return false;
        }
        
        // String scheduleSQL = "select scheduletemplate.timecode, scheduledate.sdate from scheduletemplate, scheduledate where scheduletemplate.name=scheduledate.hour and scheduledate.sdate >= '" + date_from + "' and scheduledate.sdate <= '" + date_to + "' and  scheduledate.provider_no='" + provider_no + "' and scheduledate.status = 'A' and (scheduletemplate.provider_no=scheduledate.provider_no or scheduletemplate.provider_no='Public') order by scheduledate.sdate";
        // String apptSQL = "select start_time, end_time from appointment where provider_no = '" + provider_no + "' and appointment_date = '";
        int booked, unbooked;
        booked = unbooked = 0;
        
        try {
            int duration;
            String timecodes, code;
                       
            String apptTime;
            int dayMins = 24*60;
            int iHours,iMins,apptHour_s,apptMin_s,apptHour_e,apptMin_e;
            int codePos;            
            int latestApptHour, latestApptMin;
            
            ScheduleTemplateDao dao = SpringUtils.getBean(ScheduleTemplateDao.class);
            OscarAppointmentDao apptDao = SpringUtils.getBean(OscarAppointmentDao.class);
            for(Object[] o : dao.findSchedules(ConversionUtils.fromDateString(date_from), ConversionUtils.fromDateString(date_to), provider_no)) {
            	ScheduleTemplate st = (ScheduleTemplate) o[0];
            	ScheduleDate sd = (ScheduleDate) o[1];

                timecodes = st.getTimecode(); 
                duration = dayMins/timecodes.length();

                List<Appointment> appts = apptDao.findByDateAndProvider(sd.getDate(), provider_no);
                
                codePos = 0;
                latestApptHour = latestApptMin = 0;
                int i = 0;
                for(int iTotalMin = 0; iTotalMin < dayMins; iTotalMin+=duration) {
                    code = timecodes.substring(codePos,codePos+1);
                    ++codePos;
                    iHours = iTotalMin/60;
                    iMins = iTotalMin % 60;
                    
                    while( i < appts.size()) {
                    	Appointment appt = appts.get(i);
                        apptTime = ConversionUtils.toTimeString(appt.getStartTime());
                        apptHour_s = Integer.parseInt(apptTime.substring(0,2));
                        apptMin_s = Integer.parseInt(apptTime.substring(3,5));

                        if( iHours == apptHour_s && iMins == apptMin_s ) {
                            apptTime = ConversionUtils.toTimeString(appt.getEndTime());
                            apptHour_e = Integer.parseInt(apptTime.substring(0,2));
                            apptMin_e = Integer.parseInt(apptTime.substring(3,5));
                            
                            if( apptHour_e > latestApptHour || (apptHour_e == latestApptHour && apptMin_e > latestApptMin) ) {
                                latestApptHour = apptHour_e;
                                latestApptMin = apptMin_e;
                            }
                            i++;
                        }
                        else {
                           	i--;                            
                            break;
                        }
                        
                    }
                    
                    if( code.equalsIgnoreCase("1") || code.equalsIgnoreCase("2") || code.equalsIgnoreCase("3") || code.equalsIgnoreCase("4") || code.equalsIgnoreCase("6") ) {
                        if( iHours > latestApptHour || (iHours == latestApptHour && iMins > latestApptMin)) {
                            unbooked += duration;
                        }
                        else {
                            booked += duration;
                        }
                    }
                    
                }                
                
            }
        }catch(Exception e) {
            MiscUtils.getLogger().error("Error", e);
            
        }
        rsHtml = makeHTML(booked,unbooked);
        csv = makeCSV(booked,unbooked);
        // String sql = scheduleSQL + ";\n " + apptSQL;
        request.setAttribute("reportobject", curReport);
        request.setAttribute("resultsethtml", rsHtml);
        request.setAttribute("csv", csv);
        request.setAttribute("sql", " -- MIGRATED TO JPA -- "); // it's ok here - as it's only used for display in resultReport.jsp
        return true;
    }


    private String makeHTML(int booked, int unbooked) {
        String html = "<table class=\"reportTable\">\n" + 
                "<th class=\"reportHeader\">Booked Minutes</th><th class=\"reportHeader\">Unbooked Minutes</th>" +
                "<tr class=\"reportRow1\"><td>" + String.valueOf(booked) + "</td>\n<td>" + String.valueOf(unbooked) +
                "</td>\n</tr>\n</table>\n";
        
        return html;
    }
    
    private String makeCSV(int booked, int unbooked) {
        String csv = "Booked Minutes," + String.valueOf(booked) + ",Unbooked Minutes," + String.valueOf(unbooked);
        return csv;
    }

}

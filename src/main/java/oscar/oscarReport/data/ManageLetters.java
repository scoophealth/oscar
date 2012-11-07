/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
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
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */


package oscar.oscarReport.data;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;

import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperReport;

import org.oscarehr.common.dao.LogLettersDao;
import org.oscarehr.common.dao.ReportLettersDao;
import org.oscarehr.common.model.LogLetters;
import org.oscarehr.common.model.ReportLetters;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.OscarDocumentCreator;

/**
 *
 * @author jay
 */


public class ManageLetters {

	private ReportLettersDao reportLettersDao = SpringUtils.getBean(ReportLettersDao.class);
	private LogLettersDao logLettersDao = SpringUtils.getBean(LogLettersDao.class);

    public ManageLetters() {
    }

    //method to save a new report
    public void saveReport(String providerNo,String reportName,String fileName, byte[] in){
    	ReportLetters r = new ReportLetters();
    	r.setProviderNo(providerNo);
    	r.setReportName(reportName);
    	r.setFileName(fileName);
    	r.setReportFile(in);
    	r.setDateTime(new Date());
    	r.setArchive("0");
    	reportLettersDao.persist(r);
    }

    //method to archive an existing report
    public void archiveReport(String id){
    	ReportLetters r = reportLettersDao.find(Integer.parseInt(id));
    	if(r != null) {
    		r.setArchive("1");
    		reportLettersDao.merge(r);
    	}
    }

    //method getReport for id
    public JasperReport getReport(String id){
        JasperReport  jasperReport = null;
        
        ReportLetters r = reportLettersDao.find(Integer.parseInt(id));
    	if(r != null) {
    		OscarDocumentCreator osc = new OscarDocumentCreator();
            jasperReport = osc.getJasperReport(r.getReportFile());
    	}
        return jasperReport;
    }

    public static String[] getReportParams(JasperReport  jasperReport){
        JRParameter[] jrp =  jasperReport.getParameters();

        ArrayList<String> list = new ArrayList<String>();
        if(jrp != null){
            for (int i = 0 ; i < jrp.length; i++){
                if(!jrp[i].isSystemDefined()){
                    list.add(jrp[i].getName());
                    MiscUtils.getLogger().debug("JRP "+i+" :"+jrp[i].getName());
                }
            }

        }
        String[] s = new String[list.size()];
        return list.toArray(s);
    }

    //method to write file to stream
    public void writeLetterToStream(String id,OutputStream out){
    	ReportLetters r = reportLettersDao.find(Integer.parseInt(id));
    	if(r != null) {
    		try {
    			out.write(r.getReportFile(), 0, r.getReportFile().length);
    		}catch(IOException e) {
    			 MiscUtils.getLogger().error("Error", e);
    		}
    	}
    }



    //method to validate xml

    //method to list active reports
    public ArrayList<Hashtable<String,Object>> getActiveReportList(){

        ArrayList<Hashtable<String,Object>> list = new ArrayList<Hashtable<String,Object>>();
        
        for(ReportLetters l:reportLettersDao.findCurrent()) {
        	 Hashtable<String,Object> h = new Hashtable<String,Object>();
             h.put("ID",l.getId().toString());
             h.put("provider_no",l.getProviderNo());
             h.put("report_name",l.getReportName());
             h.put("file_name",l.getFileName());
             h.put("date_time",l.getDateTime());
             list.add(h);
        }
        return list;
    }

    public Hashtable<String,Object> getReportData(String id){
        Hashtable<String,Object> h = null;
        ReportLetters l = reportLettersDao.find(Integer.parseInt(id));
        if(l != null) {
        	h = new Hashtable<String,Object>();
            h.put("ID",l.getId().toString());
            h.put("provider_no",l.getProviderNo());
            h.put("report_name",l.getReportName());
            h.put("file_name",l.getFileName());
            h.put("date_time",l.getDateTime());
        }
        return h;
    }

    public void logLetterCreated(String providerNo,String reportId,String[] demos){
    	LogLetters l = new LogLetters();
    	l.setProviderNo(providerNo);
    	l.setReportId(Integer.parseInt(reportId));
    	l.setLog(serializeDemographic(demos));
    	l.setDateTime(new Date());
    	logLettersDao.persist(l);
    }

    private String serializeDemographic(String[] demos){
        StringBuilder serialString = new StringBuilder();
        if(demos != null){
            for ( int i = 0; i < demos.length; i++){
                serialString.append(demos[i]);
                if (i < (demos.length - 1) ){
                    serialString.append(",");
                }
            }
        }
        return serialString.toString();
    }

}

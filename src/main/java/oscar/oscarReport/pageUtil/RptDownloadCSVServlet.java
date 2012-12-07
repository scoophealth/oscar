/**
 * Copyright (c) 2006-. OSCARservice, OpenSoft System. All Rights Reserved.
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
 */

package oscar.oscarReport.pageUtil;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.oscarehr.util.MiscUtils;

import oscar.OscarProperties;
import oscar.login.DBHelp;
import oscar.oscarReport.data.RptReportConfigData;
import oscar.oscarReport.data.RptReportCreator;
import oscar.oscarReport.data.RptReportItem;

import com.Ostermiller.util.CSVPrinter;

public class RptDownloadCSVServlet extends HttpServlet {

    private static final Logger _logger = Logger.getLogger(RptDownloadCSVServlet.class);
    String reportName = "";
    String DELIMETER = "\t";

    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null)
            return;
        String in="";
        try {
            in = request.getParameter("demoReport") != null ? demoReport(request) : formReport(request);
        } catch (ServletException e1) {
            _logger.error("RptDownloadCSVServlet service() - form report");
        } catch (Exception e1) {
            _logger.error("RptDownloadCSVServlet service() - form report");
        }


        String filename = reportName + ".csv"; // request.getParameter("filename");
        OutputStream out = null;
        try {
            if (in != null) {
                out = new BufferedOutputStream(response.getOutputStream());
                byte[] b = in.getBytes();
                int len = b.length;
                int n = 0;
                int FIXED_LEN = 2048;
                String contentType = "application/unknow";
                MiscUtils.getLogger().debug("contentType: " + contentType);
                response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
                while (n <= len - FIXED_LEN) {
                    out.write(b, n, FIXED_LEN); // out.write(b);
                    n += FIXED_LEN;
                }
                if (n > len - FIXED_LEN) {
                    out.write(b, n, len - n);
                }
            }
        } finally {
            if (out != null)
                try {
                    out.close();
                } catch (Exception e) {
                }
        }

    }

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    private String formReport(HttpServletRequest request) {


        String SAVE_AS = "default";
        String reportId = request.getParameter("id") != null ? request.getParameter("id") : "0";
        // get form name
        //String reportName = "";
        String in = "";
        try {
            reportName = (new RptReportItem()).getReportName(reportId);
            RptFormQuery formQuery = new RptFormQuery();
            String reportSql = formQuery.getQueryStr(reportId, request);

            RptReportConfigData formConfig = new RptReportConfigData();
            Vector[] vecField = formConfig.getAllFieldNameValue(SAVE_AS, reportId);
            Vector vecFieldCaption = vecField[1];


            Vector vecFieldValue = (new RptReportCreator()).query(reportSql, vecFieldCaption);

            StringWriter swr = new StringWriter();
            CSVPrinter csvp = new CSVPrinter(swr);
            csvp.changeDelimiter('\t');

            for (int i = 0; i < vecFieldCaption.size(); i++) {
                csvp.write((String) vecFieldCaption.get(i));
            }

            for (int i = 0; i < vecFieldValue.size(); i++) {
                Properties prop = (Properties) vecFieldValue.get(i);
                csvp.writeln();
                for (int j = 0; j < vecFieldCaption.size(); j++) {
                    csvp.write(prop.getProperty((String) vecFieldCaption.get(j), ""));
                }
            }
            in = swr.toString();



        } catch (Exception e1) {
            _logger.error("service() - form report");
        }
        return in;
    }

    private String demoReport(HttpServletRequest request) throws Exception {
        reportName = "clientDatabaseReport";
        String in = "";
        

        String ARTYPE = "formBCAR";
        if (request.getParameter("bcartype") != null && request.getParameter("bcartype").equals("BCAR2007")){
            ARTYPE = "formBCAR2007";
        }

        MiscUtils.getLogger().debug("AR TYPE "+ARTYPE);


        Properties propDemoSelect = new Properties();
        Properties propSpecSelect = new Properties();
        Properties propARSelect = new Properties();
        propDemoSelect.setProperty("last_name", "Last Name");
        propDemoSelect.setProperty("first_name", "First Name");
        propDemoSelect.setProperty("date_joined", "Date Joined");
        propDemoSelect.setProperty("hin", "Health Ins.");
        propDemoSelect.setProperty("hc_type", "HC Type");
        propDemoSelect.setProperty("address", "Address");
        propDemoSelect.setProperty("city", "City");
        propDemoSelect.setProperty("postal", "Postal Code");
        propDemoSelect.setProperty("phone", "Phone (H)");
        propDemoSelect.setProperty("phone2", "Phone (W)");
        propDemoSelect.setProperty("email", "Email");
        Vector vecSeqDemoSelect = new Vector();
        vecSeqDemoSelect.add("last_name");
        vecSeqDemoSelect.add("first_name");
        vecSeqDemoSelect.add("date_joined");
        vecSeqDemoSelect.add("hin");
        vecSeqDemoSelect.add("hc_type");
        vecSeqDemoSelect.add("address");
        vecSeqDemoSelect.add("city");
        vecSeqDemoSelect.add("postal");
        vecSeqDemoSelect.add("phone");
        vecSeqDemoSelect.add("phone2");
        vecSeqDemoSelect.add("email");

        Vector vecSeqSpecSelect = new Vector();
        propSpecSelect.setProperty("prefer_language", "Preferred Language");
        OscarProperties oscarProps = OscarProperties.getInstance();
        if(oscarProps.getProperty("demographicExt") != null) {
            String [] propDemoExt = oscarProps.getProperty("demographicExt","").split("\\|");
            for(int i=0; i<propDemoExt.length; i++) {
                propSpecSelect.setProperty(propDemoExt[i].replace(' ', '_'), propDemoExt[i]);
                vecSeqSpecSelect.add(propDemoExt[i].replace(' ', '_'));
            }
        }

        propARSelect.setProperty("c_EDD", "EDD");
        propARSelect.setProperty("pg1_famPhy", "Family Physician");
        propARSelect.setProperty("pg1_partnerName", "Partner Name");
        Vector vecSeqARSelect = new Vector();
        vecSeqARSelect.add("c_EDD");
        vecSeqARSelect.add("ga");
        vecSeqARSelect.add("pg1_famPhy");
        vecSeqARSelect.add("pg1_partnerName");

        propARSelect.setProperty("ga", "GA Today");
        propARSelect.setProperty("b_primiparous", "Primiparous");

//        get selection
        boolean bDemoSelect = false;
        boolean bARSelect = false;
        boolean bSpecSelect = false;
        String sDemoSelect = "";
        String sSpecSelect = "";
        String sARSelect = "";


        String CHECK_BOX = "filter_";
        String VALUE = "value_";
        String DATE_FORMAT = "dateFormat_";
        String VARNAME_FORMAT = "startDate\\d|endDate\\d";
        Vector vecValue = new Vector();
        Vector vecDateFormat = new Vector();
        Properties propTempDemoSelect = new Properties();
        Properties propTempARSelect = new Properties();
        Properties propTempSpecSelect = new Properties();

        Enumeration varEnum = request.getParameterNames();
        while (varEnum.hasMoreElements()) {
            String name = (String) varEnum.nextElement();
            if(propDemoSelect.containsKey(name)) {
                bDemoSelect = true;
                propTempDemoSelect.setProperty(name, "");
            }
            if(propARSelect.containsKey(name)) {
                bARSelect = true;



                if(!name.equals("ga") && !name.equals("b_primiparous"))
                    sARSelect += (sARSelect.length()<1?"":",") + ARTYPE+"."+name;
            }
            if(propSpecSelect.containsKey(name)) {
                bSpecSelect = true;
                sSpecSelect += (sSpecSelect.length()<1?"":",") + "demographicExt."+name;
            }

            if (name.startsWith(VALUE)) {
                String serialNo = name.substring(VALUE.length());
                if (request.getParameter(CHECK_BOX + serialNo) == null)
                    continue;

                vecValue.add(request.getParameter(name));
                vecDateFormat.add(request.getParameter(DATE_FORMAT + serialNo));

            }
        }
//         get seq. select string
        for(int i=0; i<vecSeqDemoSelect.size(); i++) {
            if(propTempDemoSelect.getProperty((String)vecSeqDemoSelect.get(i)) != null) {
                sDemoSelect += (sDemoSelect.length()<1?"":",") + "demographic." + vecSeqDemoSelect.get(i);
            }
        }
        for(int i=0; i<vecSeqARSelect.size(); i++) {
            if(propTempARSelect.getProperty((String)vecSeqARSelect.get(i)) != null) {
                sARSelect += (sARSelect.length()<1?"":",") + ARTYPE+"." + vecSeqARSelect.get(i);
            }
        }
        for(int i=0; i<vecSeqSpecSelect.size(); i++) {
            if(propTempSpecSelect.getProperty((String)vecSeqSpecSelect.get(i)) != null) {
                sSpecSelect += (sSpecSelect.length()<1?"":",") + "demographicExt." + vecSeqSpecSelect.get(i);
            }
        }

        MiscUtils.getLogger().debug(":" + bDemoSelect + bSpecSelect + bARSelect);
        MiscUtils.getLogger().debug(":" + sDemoSelect + sSpecSelect + sARSelect);

//        get replaced filter
//         filling the var with the real date value
        Vector vecFilter = new Vector();
        boolean bDemoFilter = false;
        boolean bARFilter = false;
        boolean bSpecFilter = false;
        String sDemoFilter = "";
        String sSpecFilter = "";
        String sARFilter = "";
        for (int i = 0; i < vecValue.size(); i++) {
            String tempVal = (String) vecValue.get(i);
            Vector vecVar = RptReportCreator.getVarVec(tempVal);
            Vector vecVarValue = new Vector();
            for (int j = 0; j < vecVar.size(); j++) {
                // conver date format if needed
                if (((String) vecVar.get(j)).matches(VARNAME_FORMAT) && ((String) vecDateFormat.get(i)).length() > 1) {
                    vecVarValue.add(RptReportCreator.getDiffDateFormat(request.getParameter((String) vecVar.get(j)),
                        (String) vecDateFormat.get(i), "yyyy-MM-dd"));
                } else {
                    vecVarValue.add(request.getParameter((String) vecVar.get(j)));
                }
            }
            String strFilter = RptReportCreator.getWhereValueClause(tempVal, vecVarValue);
            if(strFilter.indexOf("demographic.")>=0) {
                bDemoFilter = true;
                sDemoFilter += (sDemoFilter.length()<1?"":" and ") + strFilter;
            }
            if(strFilter.indexOf("demographicExt.")>=0) {
                bSpecFilter = true;
                sSpecFilter += (sSpecFilter.length()<1?"":" and ") + strFilter;
            }
            if(strFilter.indexOf(ARTYPE+".")>=0) {
                bARFilter = true;
                //"formBCAR.demographic_no in (select distinct demographic_no from formBCBirthSumMo)"
                if(strFilter.indexOf("formBCBirthSumMo") > 0) {
                    ResultSet rs = DBHelp.searchDBRecord("select distinct demographic_no from formBCBirthSumMo");
                    String sBirthSumNo = "";
                    while (rs.next()) {
                        sBirthSumNo += (sBirthSumNo.length()>0? ",":"") + rs.getInt("demographic_no") ;
                    }
                    sBirthSumNo = sBirthSumNo.length()>0 ? sBirthSumNo : "0";
                    strFilter = " "+ARTYPE+".demographic_no in (" + sBirthSumNo + ")";
                }

                sARFilter += (sARFilter.length()<1?"":" and ") + strFilter;
            }
            MiscUtils.getLogger().debug(i + tempVal + " tempVal: " + vecVarValue);
            MiscUtils.getLogger().debug(i + strFilter);
            vecFilter.add(strFilter);
        }

//        query sub
//        todo: filt out Delivered Clients
//         one table: demographic
        Vector vecFieldCaption = new Vector();
        Vector vecFieldName = new Vector();
        Vector vecFieldValue = new Vector();
        String ORDER_BY = " order by demographic.last_name, demographic.first_name";
        if(bDemoSelect && !bARSelect && !bSpecSelect && bDemoFilter && !bARFilter && !bSpecFilter) {
            String sql = "select " + sDemoSelect + " from demographic where " + sDemoFilter + ORDER_BY;
            MiscUtils.getLogger().debug(" one table: demographic: " + sql);
            String [] temp = sDemoSelect.replaceAll("demographic.","").split(",");
            for(int i=0; i<temp.length; i++) {
                vecFieldCaption.add(propDemoSelect.getProperty(temp[i].trim()));
                vecFieldName.add(temp[i].trim());
                MiscUtils.getLogger().debug(" vecFieldCaption: " + propDemoSelect.getProperty(temp[i].trim()));
            }
            vecFieldValue = (new RptReportCreator()).query(sql, vecFieldName);
        }

//         table: demographic and demographicExt
        Vector vecSpecCaption = new Vector();
        Properties propSpecValue = new Properties();
        if( (bDemoSelect && !bARSelect && bSpecSelect && !bARFilter) || (!bARFilter && bSpecFilter) ) {
            if(bDemoSelect && !bARSelect && bSpecSelect && !bSpecFilter) {
                vecFieldName.add("demographic_no");
                String sql = "select demographic_no," + sDemoSelect + " from demographic where " + sDemoFilter + ORDER_BY;
                MiscUtils.getLogger().debug(" demographic and demographicExt: " + sql);
                String [] temp = sDemoSelect.replaceAll("demographic.","").split(",");
                for(int i=0; i<temp.length; i++) {
                    vecFieldCaption.add(propDemoSelect.getProperty(temp[i].trim()));
                    vecFieldName.add(temp[i].trim());
                    MiscUtils.getLogger().debug(" vecFieldCaption: " + propDemoSelect.getProperty(temp[i].trim()));
                }
                vecFieldValue = (new RptReportCreator()).query(sql, vecFieldName);
                vecFieldName.remove(0); // remove "demographic_no"

                //get demographic_no
                String strDemoNo = "";
                for(int j=0; j<vecFieldValue.size(); j++) {
                    Properties prop = (Properties) vecFieldValue.get(j);
                    strDemoNo += (strDemoNo.length()<1? "" : ",") + prop.getProperty("demographic_no");
                }
                temp = sSpecSelect.replaceAll("demographicExt.","").split(",");
                for(int i=0; i<temp.length; i++) {
                    vecSpecCaption.add(propSpecSelect.getProperty(temp[i].trim()));
                    sql = "select demographic_no,value from demographicExt where key_val='" + temp[i] + "' and demographic_no in (";
                    sql += strDemoNo + ") order by date_time desc limit 1";
                    ResultSet rs = DBHelp.searchDBRecord(sql);
                    while (rs.next()) {
                        propSpecValue.setProperty(rs.getString("demographic_no")+temp[i], rs.getString("value"));
                    }
                }
                MiscUtils.getLogger().debug(" demographic and demographicExt: " + sql);
            }
            if(bSpecFilter) {
                vecFieldName.add("demographic_no");
                // get demoNo
                String sql = null;
                ResultSet rs = null;
                String sTempEle = sSpecFilter.length()>0? (" and "+sSpecFilter) : "";
                String subQuery = "select distinct(demographic.demographic_no) from demographicExt, demographic where demographic.demographic_no=demographicExt.demographic_no ";
                subQuery += " and " + sDemoFilter + sTempEle + "  ";
                MiscUtils.getLogger().debug(" demographic and demographicExt subQuery: " + subQuery);
                String subFormDemoNo = "";
                rs = DBHelp.searchDBRecord(subQuery);
                while (rs.next()) {
                    subFormDemoNo += (subFormDemoNo.length()>0? ",":"") +rs.getInt("demographic.demographic_no");
                }
                subFormDemoNo = subFormDemoNo.length()>0? subFormDemoNo : "0";
                // get value for spec
                String [] temp = sSpecSelect.replaceAll("demographicExt.","").split(",");
                for(int i=0; i<temp.length; i++) {
                    vecSpecCaption.add(propSpecSelect.getProperty(temp[i].trim()));
                    sql = "select demographic_no,value from demographicExt where key_val='" + temp[i] + "' and demographic_no in (";
                    sql += subFormDemoNo + ") order by date_time desc limit 1";
                    MiscUtils.getLogger().debug(" demographic and demographicExt: " + sql);
                    rs = DBHelp.searchDBRecord(sql);
                    while (rs.next()) {
                        propSpecValue.setProperty(rs.getString("demographic_no")+temp[i], rs.getString("value"));
                    }
                }

                //sTempEle = sSpecSelect.length()>0? (","+sSpecSelect) : "";
                sql = "select demographic.demographic_no," + sDemoSelect + " from demographic where ";
                sql += " demographic.demographic_no in (" + subFormDemoNo + ") " + ORDER_BY;
                MiscUtils.getLogger().debug(" demographic and demographicExt: " + sql);

                temp = sDemoSelect.replaceAll("demographic.","").split(",");
                for(int i=0; i<temp.length; i++) {
                    vecFieldCaption.add(propDemoSelect.getProperty(temp[i].trim()));
                    vecFieldName.add(temp[i].trim());
                    MiscUtils.getLogger().debug(" vecFieldCaption: " + propDemoSelect.getProperty(temp[i].trim()));
                }
                /*
                if(bSpecSelect) {
                    temp = sSpecSelect.replaceAll("demographicExt.","").split(",");
                    for(int i=0; i<temp.length; i++) {
                        vecFieldCaption.add(propSpecSelect.getProperty(temp[i].trim()));
                        vecFieldName.add(temp[i].trim());
                        MiscUtils.getLogger().debug(" vecFieldCaption: " + propSpecSelect.getProperty(temp[i].trim()));
                    }
                }
                */
                vecFieldValue = (new RptReportCreator()).query(sql, vecFieldName);
                vecFieldName.remove(0); // remove "demographic_no"
            }
        }

//         table: demographic and formBCAR


        if( (bDemoSelect && bARSelect && !bSpecSelect && !bSpecFilter) || (!bSpecSelect && bARFilter && !bSpecFilter) ) {
            String sTempEle = sARFilter.length()>0? (" and "+sARFilter) : "";
            String subQuery = "select max(ID) from "+ARTYPE+", demographic where demographic.demographic_no="+ARTYPE+".demographic_no ";
            subQuery += " and " + sDemoFilter + sTempEle + " group by "+ARTYPE+".demographic_no,"+ARTYPE+".formCreated ";
            MiscUtils.getLogger().debug(" demographic and "+ARTYPE+" subQuery: " + subQuery);
            String subFormId = "";
            ResultSet rs = DBHelp.searchDBRecord(subQuery);
            while (rs.next()) {
                subFormId += (subFormId.length()>0? ",":"") +rs.getInt("max(ID)");
            }

            sTempEle = sARSelect.length()>0? (","+sARSelect) : "";
            subFormId = subFormId.length()>0? subFormId : "0";
            String sql = "select demographic.demographic_no," + sDemoSelect + sTempEle + " from demographic,"+ ARTYPE+" where ";
            sql += " "+ARTYPE+".ID in (" + subFormId + ") and demographic.demographic_no="+ARTYPE+".demographic_no " + ORDER_BY;
            MiscUtils.getLogger().debug(" demographic and "+ARTYPE+": " + sql);

            String [] temp = sDemoSelect.replaceAll("demographic.","").split(",");
            for(int i=0; i<temp.length; i++) {
                vecFieldCaption.add(propDemoSelect.getProperty(temp[i].trim()));
                vecFieldName.add(temp[i].trim());
                MiscUtils.getLogger().debug(" vecFieldCaption: " + propDemoSelect.getProperty(temp[i].trim()));
            }
            if(bARSelect) {
                temp = sARSelect.replaceAll(ARTYPE+".","").split(",");
                for(int i=0; i<temp.length; i++) {
                    vecFieldCaption.add(propARSelect.getProperty(temp[i].trim()));
                    vecFieldName.add(temp[i].trim());
                    MiscUtils.getLogger().debug(" vecFieldCaption: " + propARSelect.getProperty(temp[i].trim()));
                }
            }
            vecFieldValue = (new RptReportCreator()).query(sql, vecFieldName);

            //vecFieldName.remove(0); // remove "demographic_no"
        }

//         table: all
        if( (bDemoSelect && bARSelect && bSpecSelect) || (bARFilter && bSpecFilter) ) {
            if(bDemoSelect && bARSelect && bSpecSelect && !bSpecFilter) {
                vecFieldName.add("demographic_no");
                String sTempEle = sARFilter.length()>0? (" and "+sARFilter) : "";
                String subQuery = "select max(ID) from "+ ARTYPE+", demographic where demographic.demographic_no="+ARTYPE+".demographic_no ";
                subQuery += " and " + sDemoFilter + sTempEle + " group by "+ARTYPE+".demographic_no,"+ARTYPE+".formCreated ";
                MiscUtils.getLogger().debug(" demographic and "+ ARTYPE+" subQuery: " + subQuery);
                String subFormId = "";
                ResultSet rs = DBHelp.searchDBRecord(subQuery);
                while (rs.next()) {
                    subFormId += (subFormId.length()>0? ",":"") +rs.getInt("max(ID)");
                }

                sTempEle = sARSelect.length()>0? (","+sARSelect) : "";
                subFormId = subFormId.length()>0? subFormId : "0";
                String sql = "select demographic.demographic_no," + sDemoSelect + sTempEle + " from demographic,"+ARTYPE+" where ";
                sql += " "+ARTYPE+".ID in (" + subFormId + ") and demographic.demographic_no="+ARTYPE+".demographic_no " + ORDER_BY;
                MiscUtils.getLogger().debug(" demographic and "+ARTYPE+": " + sql);

                String [] temp = sDemoSelect.replaceAll("demographic.","").split(",");
                for(int i=0; i<temp.length; i++) {
                    vecFieldCaption.add(propDemoSelect.getProperty(temp[i].trim()));
                    vecFieldName.add(temp[i].trim());
                    MiscUtils.getLogger().debug(" vecFieldCaption: " + propDemoSelect.getProperty(temp[i].trim()));
                }
                if(bARSelect) {
                    temp = sARSelect.replaceAll(ARTYPE+".","").split(",");
                    for(int i=0; i<temp.length; i++) {
                        vecFieldCaption.add(propARSelect.getProperty(temp[i].trim()));
                        vecFieldName.add(temp[i].trim());
                        MiscUtils.getLogger().debug(" vecFieldCaption: " + propARSelect.getProperty(temp[i].trim()));
                    }
                }
                vecFieldValue = (new RptReportCreator()).query(sql, vecFieldName);
                vecFieldName.remove(0); // remove "demographic_no"

                //get demographic_no
                String strDemoNo = "";
                for(int j=0; j<vecFieldValue.size(); j++) {
                    Properties prop = (Properties) vecFieldValue.get(j);
                    strDemoNo += (strDemoNo.length()<1? "" : ",") + prop.getProperty("demographic_no");
                }
                temp = sSpecSelect.replaceAll("demographicExt.","").split(",");
                for(int i=0; i<temp.length; i++) {
                    vecSpecCaption.add(propSpecSelect.getProperty(temp[i].trim()));
                    sql = "select demographic_no,value from demographicExt where key_val='" + temp[i] + "' and demographic_no in (";
               		sql += strDemoNo + ") order by date_time ";
               		rs = DBHelp.searchDBRecord(sql);
                    while (rs.next()) {
                        propSpecValue.setProperty(rs.getString("demographic_no")+temp[i], rs.getString("value"));
                    }
                }
            }
            MiscUtils.getLogger().debug(" table: all: " );

            if(bARFilter && bSpecFilter) {
                // spec first
                vecFieldName.add("demographic_no");
                // get demoNo
                String sql = null;
                ResultSet rs = null;
                String sTempEle = sSpecFilter.length()>0? (" and "+sSpecFilter) : "";
                String subQuery = "select distinct(demographic.demographic_no) from demographicExt, demographic where demographic.demographic_no=demographicExt.demographic_no ";
                subQuery += " and " + sDemoFilter + sTempEle + "  ";
                MiscUtils.getLogger().debug(" demographic and demographicExt subQuery: " + subQuery);
                String subFormDemoNo = "";
                rs = DBHelp.searchDBRecord(subQuery);
                while (rs.next()) {
                    subFormDemoNo += (subFormDemoNo.length()>0? ",":"") +rs.getInt("demographic.demographic_no");
                }
                // get value for spec
                String [] temp = sSpecSelect.replaceAll("demographicExt.","").split(",");
                for(int i=0; i<temp.length; i++) {
                    vecSpecCaption.add(propSpecSelect.getProperty(temp[i].trim()));
                    sql = "select demographic_no,value from demographicExt where key_val='" + temp[i] + "' and demographic_no in (";
                    sql += subFormDemoNo + ") order by date_time desc limit 1";
                    rs = DBHelp.searchDBRecord(sql);
                    while (rs.next()) {
                        propSpecValue.setProperty(rs.getString("demographic_no")+temp[i], rs.getString("value"));
                    }
                }

                // formAR second
                sTempEle = sARFilter.length()>0? (" and "+sARFilter) : "";
                subQuery = "select max(ID) from "+ ARTYPE+ ", demographic where demographic.demographic_no="+ARTYPE+".demographic_no ";
                subQuery += " and " + sDemoFilter + sTempEle + " group by "+ARTYPE+".demographic_no,"+ARTYPE+".formCreated ";
                MiscUtils.getLogger().debug(" demographic and "+ARTYPE+" subQuery: " + subQuery);
                String subFormId = "";
                rs = DBHelp.searchDBRecord(subQuery);
                while (rs.next()) {
                    subFormId += (subFormId.length()>0? ",":"") +rs.getInt("max(ID)");
                }

                // total
                sTempEle = sARSelect.length()>0? (","+sARSelect) : "";
                subFormId = subFormId.length()>0? subFormId : "0";
                sql = "select demographic.demographic_no," + sDemoSelect + sTempEle + " from demographic,"+ARTYPE+" where ";
                sql += " demographic.demographic_no in (" +  subFormDemoNo + ") and ";
                sql += " "+ARTYPE+".ID in (" + subFormId + ") and demographic.demographic_no="+ARTYPE+".demographic_no " + ORDER_BY;
                MiscUtils.getLogger().debug(" total: " + sql);

                temp = sDemoSelect.replaceAll("demographic.","").split(",");
                for(int i=0; i<temp.length; i++) {
                    vecFieldCaption.add(propDemoSelect.getProperty(temp[i].trim()));
                    vecFieldName.add(temp[i].trim());
                    MiscUtils.getLogger().debug(" vecFieldCaption: " + propDemoSelect.getProperty(temp[i].trim()));
                }
                if(bARSelect) {
                    temp = sARSelect.replaceAll(ARTYPE+".","").split(",");
                    for(int i=0; i<temp.length; i++) {
                        vecFieldCaption.add(propARSelect.getProperty(temp[i].trim()));
                        vecFieldName.add(temp[i].trim());
                        MiscUtils.getLogger().debug(" vecFieldCaption: " + propARSelect.getProperty(temp[i].trim()));
                    }
                }
                vecFieldValue = (new RptReportCreator()).query(sql, vecFieldName);
                vecFieldName.remove(0); // remove "demographic_no"

            }
        }

        StringWriter swr = new StringWriter();
        CSVPrinter csvp = new CSVPrinter(swr);
        csvp.changeDelimiter('\t');

        csvp.write("id");
        for(int i=0; i<vecFieldCaption.size(); i++) {
            csvp.write( (String) vecFieldCaption.get(i));
        }
        if(bSpecSelect) {
            for(int i=0; i<vecSpecCaption.size(); i++) {
                csvp.write((String) vecSpecCaption.get(i));
            }
        }

        for(int i=0; i<vecFieldValue.size(); i++) {
            Properties prop = (Properties) vecFieldValue.get(i);
            csvp.writeln();
            csvp.write(""+(i+1));

            for(int j=0; j<vecFieldName.size(); j++) {
                csvp.write(prop.getProperty((String) vecFieldName.get(j), ""));
            }
            if(bSpecSelect) {
                String demoNo = prop.getProperty("demographic_no");
                for(int j=0; j<vecSpecCaption.size(); j++) {
                    csvp.write(propSpecValue.getProperty(demoNo+((String) vecSpecCaption.get(j)).replaceAll(" ","_"), ""));
                }
            }
        }

        in = swr.toString();
        return in;
    }
}

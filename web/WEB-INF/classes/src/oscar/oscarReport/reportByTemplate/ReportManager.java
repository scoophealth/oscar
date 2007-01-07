//This is the main utility object that:
//   -Makes all database transactions (except one in GenerateReportAction)
//   -Loads/saves all the reports
//   -Saves/loads parameters
//   -Parses/saves all XML

/*
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
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
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */

package oscar.oscarReport.reportByTemplate;
import java.util.*;
import java.sql.*;
import oscar.oscarDB.DBHandler;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;
import java.io.*;
import oscar.oscarReport.reportByTemplate.*;
import oscar.util.UtilXML;
import org.apache.commons.lang.StringEscapeUtils;
/**
 * Created on December 27, 2006, 10:54 AM
 * @apavel (Paul)
 */
public class ReportManager {
    
    /** Creates a new instance of reportManager */
    public ReportManager() {
    }

    public ArrayList getReportTemplatesNoParam() {
        String sql = "SELECT templateid, templatetitle, templatedescription FROM reportTemplates WHERE active=1";
        ArrayList reports = new ArrayList();
        try {
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            ResultSet rs = db.GetSQL(sql);
            db.CloseConn();
            while (rs.next()) {
                ReportObjectGeneric curReport = new ReportObjectGeneric(rs.getString("templateid"), rs.getString("templatetitle"), rs.getString("templatedescription"));
                reports.add(curReport);
            }
        } catch (SQLException sqe) {
            sqe.printStackTrace();
        }
        return reports;
    }
        
    
    
    //gets the ReportObject without the parameters (don't always need parameters, no need to parse XML)
    public ReportObject getReportTemplateNoParam(String templateid) {
        String sql = "SELECT * FROM reportTemplates WHERE templateId='" + templateid + "'";
        ReportObjectGeneric curReport = new ReportObjectGeneric();
        try {
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            ResultSet rs = db.GetSQL(sql);
            db.CloseConn();
            if (rs.next()) {
                String templatetitle = rs.getString("templatetitle");
                String templatedescription = rs.getString("templatedescription");
                curReport.setTemplateId(templateid);
                curReport.setTitle(templatetitle);
                curReport.setDescription(templatedescription);
            }
        } catch (SQLException sqe) {
            sqe.printStackTrace();
        }
        return curReport;
    }
    public ReportObject getReportTemplate(String templateid) {
        String sql = "SELECT * FROM reportTemplates WHERE templateId='" + templateid + "'";
        try {
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            ResultSet rs = db.GetSQL(sql);
            db.CloseConn();
            if (rs.next()) {
                String templatetitle = rs.getString("templatetitle");
                String templatedescription = rs.getString("templatedescription");
                String paramXML = rs.getString("templateparamxml");
                ArrayList params = new ArrayList();
                if (!paramXML.equals("")) {
                    paramXML = UtilXML.escapeXML(paramXML);  //escapes anomalies such as "date >= {mydate}" the '>' character
                    SAXBuilder parser = new SAXBuilder();
                    Document doc = parser.build(new java.io.ByteArrayInputStream(paramXML.getBytes()));        
                    Element root = doc.getRootElement();
                    List paramsXml = root.getChildren("param");
                    for (int i=0; i<paramsXml.size(); i++) {
                        Element param = (Element) paramsXml.get(i);
                        String paramid = param.getAttributeValue("id");
                        if (paramid == null) return new ReportObjectGeneric(templateid, "Error: Param id not found");
                        String paramtype = param.getAttributeValue("type");
                        if (paramtype == null) return new ReportObjectGeneric(templateid, "Error: Param type not found on param '" + paramid + "'");
                        String paramdescription = param.getAttributeValue("description");
                        if (paramdescription == null) return new ReportObjectGeneric(templateid, "Error: Param description not found on param '" + paramid + "'");
                        List choicesXml = param.getChildren("choice");
                        ArrayList choices = new ArrayList();
                        String paramquery = param.getChildText("param-query"); //if retrieving choices from the DB
                        if (paramquery != null) {
                            DBHandler db2 = new DBHandler(DBHandler.OSCAR_DATA);
                            ResultSet rschoices = db2.GetSQL(paramquery);
                            db2.CloseConn();
                            while (rschoices.next()) {
                                String choiceid = rschoices.getString(1);
                                String choicetext = rschoices.getString(2);
                                if (choicetext == null) choicetext = choiceid;
                                Choice curchoice = new Choice(choiceid, choicetext);
                                choices.add(curchoice);
                            }
                        }
                        for (int i2=0; i2<choicesXml.size(); i2++) {
                            Element choice = (Element) choicesXml.get(i2);
                            String choiceid = choice.getAttributeValue("id");
                            String choicetext = choice.getTextTrim();
                            if (choiceid == null) choiceid = choicetext;
                            Choice curchoice = new Choice(choiceid, choicetext);
                            choices.add(curchoice);
                        }
                        Parameter curparam = new Parameter(paramid, paramtype, paramdescription, choices);
                        params.add(curparam);
                    }
                }
                ReportObjectGeneric curreport = new ReportObjectGeneric(templateid, templatetitle, templatedescription, params);
                return curreport;
            } else {
                return new ReportObjectGeneric(templateid, "Template Not Found");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ReportObjectGeneric(templateid, "Parameter Parsing Exception: check the configuration file");
        }
        /*
         *   
<param id="preventionType" type="list" description="Prevention Type">
      <element id="dTap">dTap</element>
      <element id="DTaP-IPV">DTaP-IPV</element>
      <element id="Flu">Influenza</element>
      <element id="Hib">Hib</element>
      <element id="MAM">Mammogram</element>
      <element id="PAP">Pap smear</element>
      <element id="Pneu-C">Pneu-C</element>
      <element id="Pneumovax">Pneumovax</element>
      <element id="Td">Td</element>
      <element id="TdP">TdP</element>
      <element id="VZ">VZ</element>
</param>*/
    }
    
    public String getSQL(String templateId) {
        String sql = "SELECT templatesql FROM reportTemplates WHERE templateid='" + templateId + "'";
        try {
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            ResultSet rs = db.GetSQL(sql);
            db.CloseConn();
            if (rs.next()) {
                return rs.getString("templatesql");
            } else return "";
        } catch (SQLException sqe) {
            sqe.printStackTrace();
            return "";
        }
    }
    
    public String getTemplateXml(String templateid) {
        String sql = "SELECT templateparamxml FROM reportTemplates WHERE templateid='" + templateid + "'";
        String xml = "";
        try {
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            ResultSet rs = db.GetSQL(sql);
            db.CloseConn();
            if (rs.next()) xml = rs.getString("templateparamxml");
            if (xml == null) xml = "";
        } catch (SQLException sqe) {
            sqe.printStackTrace();
        }
        return xml;
    }
    
    public String updateTemplateXml(String xmltext) {
        String sqldelete = "DELETE FROM reportTemplates";
        String sqlinsert = "INSERT INTO reportTemplates VALUES ('globalxml', 'Global XML file', '', '', '" +
                StringEscapeUtils.escapeSql(UtilXML.unescapeXML(xmltext)) + "', 0)";
        try {
            DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
            db.RunSQL(sqldelete);
            db.RunSQL(sqlinsert);
            db.CloseConn();
        } catch (SQLException sqe) {
            sqe.printStackTrace();
        }
        return loadInReports();
    }
    /*
CREATE TABLE reportTemplates (
  templateid varchar(40) NOT NULL,
  templatetitle varchar(80) NOT NULL DEFAULT '',
  templatedescription text NOT NULL DEFAULT '',
  templatesql text NOT NULL DEFAULT '',
  templateparamxml text NOT NULL DEFAULT '',
  active tinyint NOT NULL DEFAULT 1,
  PRIMARY KEY (templateid)
);*/
    //templateid must not repeat
    public String loadInReports() {
        String xml = getTemplateXml("globalxml");
        if (xml == "") return "Error: Could not save the template file in the database.";
        try {
            SAXBuilder parser = new SAXBuilder();
            xml = UtilXML.escapeXML(xml);  //escapes anomalies such as "date >= {mydate}" the '>' character
            //xml = UtilXML.escapeAllXML(xml, "<param-list>");  //escapes all markup in <report> tag, otherwise can't retrieve element.getText()
            Document doc = parser.build(new java.io.ByteArrayInputStream(xml.getBytes()));        
            Element root = doc.getRootElement();
            List reports = root.getChildren("report");
            
            for (int i=0; i<reports.size(); i++) {
                Element report = (Element) reports.get(i);
                
                String templateid = StringEscapeUtils.escapeSql(report.getAttributeValue("id"));
                if (templateid == null) return "Error: Attribute 'id' missing in <report> tag";
                
                String templateTitle = StringEscapeUtils.escapeSql(report.getAttributeValue("title"));
                if (templateTitle == null) return "Error: Attribute 'title' missing in <report> tag";
                
                String templateDescription = StringEscapeUtils.escapeSql(report.getAttributeValue("description"));
                if (templateDescription == null) return "Error: Attribute 'description' missing in <report> tag";
                
                String querysql = StringEscapeUtils.escapeSql(report.getChildText("query"));
                if (querysql == null || querysql.length() == 0) return "Error: The sql query is missing in <report> tag";
                XMLOutputter reportout = new XMLOutputter();
                String reportXML = reportout.outputString(report).trim();
                reportXML = UtilXML.unescapeXML(reportXML);
                reportXML = StringEscapeUtils.escapeSql(reportXML);
                String active = report.getAttributeValue("active");
                int activeint;
                try {
                    activeint = Integer.parseInt(active);
                } catch (Exception e) {
                    activeint = 1;
                }
                String sql = "INSERT INTO reportTemplates (templateid, templatetitle, templatedescription, templatesql, templateparamxml, active) " +
                        "VALUES ('" + templateid + "', '" + templateTitle + "', '" + templateDescription + "', '" + querysql + "', '" + reportXML + "', " + activeint + ")";
                //System.out.println("sql: " + sql);
                try {
                    DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
                    db.RunSQL(sql);
                    db.CloseConn();
                } catch (SQLException sqe) {
                    sqe.printStackTrace();
                    System.out.println("Report Error Caught: assumed duplicate report id");
                    return "Database Error: check for duplicate report id on the '" + templateTitle + "' report";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error parsing template file.";
        }
        
        return "Saved Successfully";
    }
    
}

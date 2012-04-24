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
//This is the main utility object that:
//   -Makes all database transactions (except one in GenerateReportAction)
//   -Loads/saves all the reports
//   -Saves/loads parameters
//   -Parses/saves all XML



package oscar.oscarReport.reportByTemplate;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;
import org.oscarehr.util.MiscUtils;

import oscar.oscarDB.DBHandler;
import oscar.util.UtilXML;

/**
 * Created on December 27, 2006, 10:54 AM
 * @apavel (Paul)
 */
public class ReportManager {

    /** Creates a new instance of reportManager */
    public ReportManager() {
    }

    public ArrayList<ReportObjectGeneric> getReportTemplatesNoParam() {
        String sql = "SELECT templateid, templatetitle, templatedescription FROM reportTemplates WHERE active=1";
        ArrayList<ReportObjectGeneric> reports = new ArrayList<ReportObjectGeneric>();
        try {

            ResultSet rs = DBHandler.GetSQL(sql);
            while (rs.next()) {
                ReportObjectGeneric curReport = new ReportObjectGeneric(rs.getString("templateid"), rs.getString("templatetitle"), rs.getString("templatedescription"));
                reports.add(curReport);
            }
        } catch (SQLException sqe) {
            MiscUtils.getLogger().error("Error", sqe);
        }
        return reports;
    }



    //gets the ReportObject without the parameters (don't always need parameters, no need to parse XML)
    public ReportObject getReportTemplateNoParam(String templateid) {
        String sql = "SELECT * FROM reportTemplates WHERE templateId='" + templateid + "'";
        ReportObjectGeneric curReport = new ReportObjectGeneric();
        try {

            ResultSet rs = DBHandler.GetSQL(sql);
            if (rs.next()) {
                String templatetitle = rs.getString("templatetitle");
                String templatedescription = rs.getString("templatedescription");
                curReport.setTemplateId(templateid);
                curReport.setTitle(templatetitle);
                curReport.setDescription(templatedescription);
            }
        } catch (SQLException sqe) {
            MiscUtils.getLogger().error("Error", sqe);
        }
        return curReport;
    }
    public ReportObject getReportTemplate(String templateid) {
        String sql = "SELECT * FROM reportTemplates WHERE templateId='" + templateid + "'";
        try {

            ResultSet rs = DBHandler.GetSQL(sql);
            if (rs.next()) {
                String templatetitle = rs.getString("templatetitle");
                String templatedescription = rs.getString("templatedescription");
                String type = rs.getString("type") == null?"":rs.getString("type");
                String paramXML = rs.getString("templatexml");
                ArrayList<Parameter> params = new ArrayList<Parameter>();
                if (!paramXML.equals("")) {
                    paramXML = UtilXML.escapeXML(paramXML);  //escapes anomalies such as "date >= {mydate}" the '>' character
                    SAXBuilder parser = new SAXBuilder();
                    Document doc = parser.build(new java.io.ByteArrayInputStream(paramXML.getBytes()));
                    Element root = doc.getRootElement();
                    List<Element> paramsXml = root.getChildren("param");
                    for (int i=0; i<paramsXml.size(); i++) {
                        Element param = paramsXml.get(i);
                        String paramid = param.getAttributeValue("id");
                        if (paramid == null) return new ReportObjectGeneric(templateid, "Error: Param id not found");
                        String paramtype = param.getAttributeValue("type");
                        if (paramtype == null) return new ReportObjectGeneric(templateid, "Error: Param type not found on param '" + paramid + "'");
                        String paramdescription = param.getAttributeValue("description");
                        if (paramdescription == null) return new ReportObjectGeneric(templateid, "Error: Param description not found on param '" + paramid + "'");
                        List<Element> choicesXml = param.getChildren("choice");
                        ArrayList<Choice> choices = new ArrayList<Choice>();
                        String paramquery = param.getChildText("param-query"); //if retrieving choices from the DB
                        if (paramquery != null) {
                            ResultSet rschoices = DBHandler.GetSQL(paramquery);
                            while (rschoices.next()) {
                                String choiceid = rschoices.getString(1);
                                String choicetext = rschoices.getString(2);
                                if (choicetext == null) choicetext = choiceid;
                                Choice curchoice = new Choice(choiceid, choicetext);
                                choices.add(curchoice);
                            }
                        }
                        for (int i2=0; i2<choicesXml.size(); i2++) {
                            Element choice = choicesXml.get(i2);
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
                ReportObjectGeneric curreport = new ReportObjectGeneric(templateid, templatetitle, templatedescription, type, params);
                return curreport;
            } else {
                return new ReportObjectGeneric(templateid, "Template Not Found");
            }
        } catch (Exception e) {
            MiscUtils.getLogger().error("Error", e);
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

            ResultSet rs = DBHandler.GetSQL(sql);
            if (rs.next()) {
                return rs.getString("templatesql");
            } else return "";
        } catch (SQLException sqe) {
            MiscUtils.getLogger().error("Error", sqe);
            return "";
        }
    }

    public String getTemplateXml(String templateid) {
        String sql = "SELECT templatexml FROM reportTemplates WHERE templateid='" + templateid + "'";
        String xml = "";
        try {

            ResultSet rs = DBHandler.GetSQL(sql);
            if (rs.next()) xml = rs.getString("templatexml");
            if (xml == null) xml = "";
        } catch (SQLException sqe) {
            MiscUtils.getLogger().error("Error", sqe);
        }
        return xml;
    }

    public String updateTemplateXml(String xmltext) {
        String sqldelete = "DELETE FROM reportTemplates";
        String sqlinsert = "INSERT INTO reportTemplates VALUES ('globalxml', 'Global XML file', '', '', '" +
                StringEscapeUtils.escapeSql(UtilXML.unescapeXML(xmltext)) + "', 0)";
        try {

            DBHandler.RunSQL(sqldelete);
            DBHandler.RunSQL(sqlinsert);
        } catch (SQLException sqe) {
            MiscUtils.getLogger().error("Error", sqe);
        }
        return loadInReports();
    }
    /*
CREATE TABLE reportTemplates (
  templateid varchar(40) NOT NULL,
  templatetitle varchar(80) NOT NULL DEFAULT '',
  templatedescription text NOT NULL DEFAULT '',
  templatesql text NOT NULL DEFAULT '',
  templatexml text NOT NULL DEFAULT '',
  active tinyint NOT NULL DEFAULT 1,
  PRIMARY KEY (templateid)
);*/
    //templateid must not repeat
    public String loadInReports() {
        String xml = getTemplateXml("1");
        if (xml == "") return "Error: Could not save the template file in the database.";
        try {
            SAXBuilder parser = new SAXBuilder();
            xml = UtilXML.escapeXML(xml);  //escapes anomalies such as "date >= {mydate}" the '>' character
            //xml = UtilXML.escapeAllXML(xml, "<param-list>");  //escapes all markup in <report> tag, otherwise can't retrieve element.getText()
            Document doc = parser.build(new java.io.ByteArrayInputStream(xml.getBytes()));
            Element root = doc.getRootElement();
            List<Element> reports = root.getChildren("report");

            for (int i=0; i<reports.size(); i++) {
                Element report = reports.get(i);

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
                String sql = "INSERT INTO reportTemplates (templatetitle, templatedescription, templatesql, templatexml, active) " +
                        "VALUES ('" + templateTitle + "', '" + templateDescription + "', '" + querysql + "', '" + reportXML + "', " + activeint + ")";

                try {

                    DBHandler.RunSQL(sql);
                } catch (SQLException sqe) {
                    MiscUtils.getLogger().error("Error", sqe);
                    MiscUtils.getLogger().debug("Report Error Caught: assumed duplicate report id");
                    return "Database Error: check for duplicate report id on the '" + templateTitle + "' report";
                }
            }
        } catch (Exception e) {
            MiscUtils.getLogger().error("Error", e);
            return "Error parsing template file.";
        }

        return "Saved Successfully";
    }

    public Document readXml(String xml) throws Exception {
        SAXBuilder parser = new SAXBuilder();
        xml = UtilXML.escapeXML(xml);  //escapes anomalies such as "date >= {mydate}" the '>' character
        //xml  UtilXML.escapeAllXML(xml, "<param-list>");  //escapes all markup in <report> tag, otherwise can't retrieve element.getText()
        Document doc = parser.build(new java.io.ByteArrayInputStream(xml.getBytes()));
        if (doc.getRootElement().getName().equals("report")) {
            Element newRoot = new Element("report-list");
            Element oldRoot = doc.detachRootElement();
            newRoot.setContent(oldRoot);
            doc.removeContent();
            doc.setRootElement(newRoot);
        }
        return doc;
    }

    //returns any error messages
    //templateId = null if adding a new template
    public String addUpdateTemplate(String templateId, Document templateXML) {
        try {
            Element rootElement = templateXML.getRootElement();
            List<Element> reports = rootElement.getChildren();
            for (int i=0; i<reports.size(); i++) {
                Element report = reports.get(i);
//reading title
                String templateTitle = StringEscapeUtils.escapeSql(report.getAttributeValue("title"));
                if (templateTitle == null) return "Error: Attribute 'title' missing in <report> tag";
//reading description
                String templateDescription = StringEscapeUtils.escapeSql(report.getAttributeValue("description"));
                if (templateDescription == null) return "Error: Attribute 'description' missing in <report> tag";
//reading type
                String type = report.getChildTextTrim("type");
                if( type == null ) {
                    type = "";
                }
//reading sql
                String querysql = StringEscapeUtils.escapeSql(report.getChildText("query"));
                if (type.equalsIgnoreCase(ReportFactory.SQL_TYPE) && (querysql == null || querysql.length() == 0)) return "Error: The sql query is missing in <report> tag";
//reading active switch
                String active = report.getAttributeValue("active");
                int activeint;
                try {
                    activeint = Integer.parseInt(active);
                } catch (Exception e) {
                    activeint = 1;
                }

//processing XML for sql storage
                XMLOutputter templateout = new XMLOutputter();
                String templateXMLstr = templateout.outputString(report).trim();
                templateXMLstr = UtilXML.unescapeXML(templateXMLstr);
                templateXMLstr = StringEscapeUtils.escapeSql(templateXMLstr);
                String sql = "";

                if (templateId == null)
                    sql = "INSERT INTO reportTemplates (templatetitle, templatedescription, templatesql, templatexml, active, type) " +
                        "VALUES ('" + templateTitle + "', '" + templateDescription + "', '" + querysql + "', '" + templateXMLstr + "', " + activeint + ", '" + type + "')";
                else
                    sql = "UPDATE reportTemplates SET templatetitle='" + templateTitle + "', templatedescription='" + templateDescription + "', " +
                        "templatesql='" + querysql + "', templatexml='" + templateXMLstr + "', active=" + activeint + ", type= '" + type + "' WHERE templateid='" + templateId + "'";

                try {

                    DBHandler.RunSQL(sql);
                } catch (SQLException sqe) {
                    MiscUtils.getLogger().error("Error", sqe);
                    MiscUtils.getLogger().debug("Report Template Writing Error Caught");
                    return "Database Error: Could not write to database";
                }
            }
        } catch (Exception e) {
            MiscUtils.getLogger().error("Error", e);
            return "Error parsing template file, make sure the root element is set.";
        }
        return "Saved Successfully";
    }

    public String deleteTemplate(String templateid) {
        String sql = "DELETE FROM reportTemplates WHERE templateid='" + templateid + "'";
        try {

            DBHandler.RunSQL(sql);
        } catch (SQLException sqe) {
            MiscUtils.getLogger().error("Error", sqe);
            return "Database Error: Could not delete template";
        }
        return "";
    }

    public String addTemplate(String templateXML) {
        try {
            Document templateXMLdoc = readXml(templateXML);
            return addUpdateTemplate(null, templateXMLdoc);
        } catch (Exception e) {
            MiscUtils.getLogger().error("Error", e);
            return "Error: Error parsing file, make sure the root element is set.";
        }
    }

    public String updateTemplate(String templateId, String templateXML) {
        try {
            Document templateXMLdoc = readXml(templateXML);
            return addUpdateTemplate(templateId, templateXMLdoc);
        } catch (Exception e) {
            MiscUtils.getLogger().error("Error", e);
            return "Error: Error parsing file";
        }
    }

}

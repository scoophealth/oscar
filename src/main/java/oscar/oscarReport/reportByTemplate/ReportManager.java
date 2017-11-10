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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.StringEscapeUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;
import org.oscarehr.common.dao.ReportTemplatesDao;
import org.oscarehr.common.model.ReportTemplates;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.log.LogAction;
import oscar.oscarReport.reportByTemplate.actions.GenerateReportAction;
import oscar.util.ConversionUtils;
import oscar.util.UtilXML;

/**
 * Created on December 27, 2006, 10:54 AM
 * 
 * @author apavel (Paul)
 */
public class ReportManager {

	private ReportTemplatesDao dao = SpringUtils.getBean(ReportTemplatesDao.class);

	/** Creates a new instance of reportManager */
	public ReportManager() {
	}

	public ArrayList<ReportObjectGeneric> getReportTemplatesNoParam(String roles) {
		ReportTemplatesDao dao = SpringUtils.getBean(ReportTemplatesDao.class);
		ArrayList<ReportObjectGeneric> reports = new ArrayList<ReportObjectGeneric>();
		for (ReportTemplates r : dao.findActive()) {
			try {
				GenerateReportAction.checkSecurity(r.getId().toString(), roles);
				ReportObjectGeneric curReport = new ReportObjectGeneric(r.getId().toString(), r.getTemplateTitle(), r.getTemplateDescription(),r.getCategory());
				reports.add(curReport);
			}catch(SecurityException e) {
				LogAction.addLogSynchronous(LoggedInInfo.getLoggedInInfoAsCurrentClassAndMethod(), "ReportManager.getReportTemplatesNoParam", "access denied for =" + r.getId());
			}
		}
		return reports;
	}

	//gets the ReportObject without the parameters (don't always need parameters, no need to parse XML)
	public ReportObject getReportTemplateNoParam(String templateid) {
		ReportTemplatesDao dao = SpringUtils.getBean(ReportTemplatesDao.class);
		ReportTemplates rt = dao.find(ConversionUtils.fromIntString(templateid));

		ReportObjectGeneric curReport = new ReportObjectGeneric();
		if (rt == null) {
			return curReport;
		}

		String templatetitle = rt.getTemplateTitle();
		String templatedescription = rt.getTemplateDescription();
		curReport.setTemplateId(templateid);
		curReport.setTitle(templatetitle);
		curReport.setDescription(templatedescription);
		curReport.setSequence(rt.isSequence());
		curReport.setCategory(rt.getCategory());

		return curReport;
	}

	@SuppressWarnings("unchecked")
    public ReportObject getReportTemplate(String templateid) {
		ReportTemplatesDao dao = SpringUtils.getBean(ReportTemplatesDao.class);
		ReportTemplates rt = dao.find(ConversionUtils.fromIntString(templateid));
		if (rt == null) {
			return new ReportObjectGeneric(templateid, "Template Not Found");
		}
		
		try {
			String templatetitle = rt.getTemplateTitle();
			String templatedescription = rt.getTemplateDescription();
			String type = rt.getType() == null ? "" : rt.getType();
			String paramXML = rt.getTemplateXml();
			ArrayList<Parameter> params = new ArrayList<Parameter>();
			boolean sequence = rt.isSequence();
			String category = rt.getCategory();
			
			if (!paramXML.equals("")) {
				paramXML = UtilXML.escapeXML(paramXML); //escapes anomalies such as "date >= {mydate}" the '>' character
				SAXBuilder parser = new SAXBuilder();
				Document doc = parser.build(new java.io.ByteArrayInputStream(paramXML.getBytes()));
				Element root = doc.getRootElement();
				
				List<Element> paramsXml = root.getChildren("param");
				for (int i = 0; i < paramsXml.size(); i++) {
					Element param = paramsXml.get(i);
					String paramid = param.getAttributeValue("id");
					if (paramid == null) return new ReportObjectGeneric(templateid, "Error: Param id not found");
					String paramtype = param.getAttributeValue("type");
					if (paramtype == null) return new ReportObjectGeneric(templateid, "Error: Param type not found on param '" + paramid + "'");
					String paramdescription = param.getAttributeValue("description");
					if (paramdescription == null) return new ReportObjectGeneric(templateid, "Error: Param description not found on param '" + paramid + "'");
					List<Element> choicesXml = param.getChildren("choice");
					ArrayList<Choice> choices = new ArrayList<Choice>();
					String priority = param.getAttributeValue("priority") != null? param.getAttributeValue("priority"): "query";
					
					String paramquery = param.getChildText("param-query"); //if retrieving choices from the DB
					if (paramquery != null) {
						for(Object[] o : dao.runNativeQuery(paramquery)) {							
							String choiceid = "", choicetext = "";
							if (o.length >= 1) {
								choiceid = String.valueOf(o[0]);
							}
							
							if (o.length >= 2) {
								choicetext = String.valueOf(o[1]);
							}
							
							if (choicetext == null) {
								choicetext = choiceid;
							}
							Choice curchoice = new Choice(choiceid, choicetext);
							choices.add(curchoice);
						}
					}
					List<Choice> tmp = new ArrayList<Choice>();
					for (int i2 = 0; i2 < choicesXml.size(); i2++) {
						Element choice = choicesXml.get(i2);
						String choiceid = choice.getAttributeValue("id");
						String choicetext = choice.getTextTrim();
						if (choiceid == null) choiceid = choicetext;
						Choice curchoice = new Choice(choiceid, choicetext);
						tmp.add(curchoice);
					}
					
					if("choice".equals(priority)) {
						choices.addAll(0, tmp);
					} else {
						choices.addAll(tmp);
					}
					
					Parameter curparam = new Parameter(paramid, paramtype, paramdescription, choices);
					params.add(curparam);
				}
			}
			ReportObjectGeneric curreport = new ReportObjectGeneric(templateid, templatetitle, templatedescription, type, params);
			return curreport;
			
		} catch (Exception e) {
			MiscUtils.getLogger().error("Error", e);
			return new ReportObjectGeneric(templateid, "Parameter Parsing Exception: check the configuration file");
		}
	}

	public String getSQL(String templateId) {
		ReportTemplatesDao dao = SpringUtils.getBean(ReportTemplatesDao.class);
		ReportTemplates rt = dao.find(ConversionUtils.fromIntString(templateId));
		if (rt == null) {
			return "";
		}
		return rt.getTemplateSql();
	}

	public String getTemplateXml(String templateid) {
		ReportTemplatesDao dao = SpringUtils.getBean(ReportTemplatesDao.class);
		ReportTemplates rt = dao.find(ConversionUtils.fromIntString(templateid));
		if (rt == null) {
			return "";
		}
		return rt.getTemplateXml();
	}

	public String updateTemplateXml(String xmltext) {
		for (ReportTemplates r : dao.findAll()) {
			dao.remove(r.getId());
		}
		
		ReportTemplates r = new ReportTemplates();
		r.setTemplateTitle("globalxml");
		r.setTemplateDescription("Global XML File");
		r.setTemplateSql("");
		r.setTemplateXml(UtilXML.unescapeXML(xmltext));
		r.setActive(0);
		r.setType("");

		return loadInReports();
	}

	//templateid must not repeat
	@SuppressWarnings("unchecked")
    public String loadInReports() {
		String xml = getTemplateXml("1");
		if (xml == "") return "Error: Could not save the template file in the database.";
		try {
			SAXBuilder parser = new SAXBuilder();
			xml = UtilXML.escapeXML(xml); //escapes anomalies such as "date >= {mydate}" the '>' character
			//xml = UtilXML.escapeAllXML(xml, "<param-list>");  //escapes all markup in <report> tag, otherwise can't retrieve element.getText()
			Document doc = parser.build(new java.io.ByteArrayInputStream(xml.getBytes()));
			Element root = doc.getRootElement();
			List<Element> reports = root.getChildren("report");

			for (int i = 0; i < reports.size(); i++) {
				Element report = reports.get(i);

				String templateid = StringEscapeUtils.escapeSql(report.getAttributeValue("id"));
				if (templateid == null) return "Error: Attribute 'id' missing in <report> tag";

				String templateTitle = StringEscapeUtils.escapeSql(report.getAttributeValue("title"));
				if (templateTitle == null) return "Error: Attribute 'title' missing in <report> tag";

				String templateDescription = StringEscapeUtils.escapeSql(report.getAttributeValue("description"));
				if (templateDescription == null) return "Error: Attribute 'description' missing in <report> tag";

				String category  = StringEscapeUtils.escapeSql(report.getAttributeValue("category"));
				
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
				
				ReportTemplates r = new ReportTemplates();
				r.setTemplateTitle(templateTitle);
				r.setTemplateDescription(templateDescription);
				r.setTemplateSql(querysql);
				r.setTemplateXml(reportXML);
				r.setActive(activeint);
				r.setCategory(category);
				r.setType("");
				dao.persist(r);

			}
		} catch (Exception e) {
			MiscUtils.getLogger().error("Error", e);
			return "Error parsing template file.";
		}

		return "Saved Successfully";
	}

	public Document readXml(String xml) throws Exception {
		SAXBuilder parser = new SAXBuilder();
		xml = UtilXML.escapeXML(xml); //escapes anomalies such as "date >= {mydate}" the '>' character
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
	@SuppressWarnings("unchecked")
    public String addUpdateTemplate(String uuid, String templateId, Document templateXML, LoggedInInfo loggedInInfo) {
		try {
			Element rootElement = templateXML.getRootElement();
			List<Element> reports = rootElement.getChildren();
			for (int i = 0; i < reports.size(); i++) {
				Element report = reports.get(i);
				//reading title
				String templateTitle = report.getAttributeValue("title");
				if (templateTitle == null) return "Error: Attribute 'title' missing in <report> tag";
				//reading description
				String templateDescription = report.getAttributeValue("description");
				if (templateDescription == null) return "Error: Attribute 'description' missing in <report> tag";
				//reading type
				String type = report.getChildTextTrim("type");
				if (type == null) {
					type = "";
				}
				//reading sql
				String querysql = report.getChildText("query");
				if (type.equalsIgnoreCase(ReportFactory.SQL_TYPE) && (querysql == null || querysql.length() == 0)) return "Error: The sql query is missing in <report> tag";
				//reading active switch
				String active = report.getAttributeValue("active");
				
				boolean sequence;
				try {
					sequence = Boolean.valueOf(report.getAttributeValue("sequence"));
				} catch (Exception e) {
					sequence=false;
				}
				
				String templateCategory = report.getAttributeValue("category");
				
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

				ReportTemplates r = null;
				if(uuid != null) {
					r = dao.findByUuid(uuid);
				} else if(templateId != null) {
					r = dao.find(Integer.parseInt(templateId));
				}
				
				if (r == null) {
					r = new ReportTemplates();
					r.setTemplateTitle(templateTitle);
					r.setTemplateDescription(templateDescription);
					r.setTemplateSql(querysql);
					r.setTemplateXml(templateXMLstr);
					r.setActive(activeint);
					r.setType(type);
					r.setSequence(sequence);
					r.setCategory(templateCategory);
					if(uuid != null) {
						r.setUuid(uuid);
					} else {
						r.setUuid(UUID.randomUUID().toString());
					}
					dao.persist(r);
					LogAction.addLogSynchronous(loggedInInfo, "ReportManager.addUpdateTemplate", "id=" + r.getId());
				} else {
					r.setTemplateTitle(templateTitle);
					r.setTemplateDescription(templateDescription);
					r.setTemplateSql(querysql);
					r.setTemplateXml(templateXMLstr);
					r.setActive(activeint);
					r.setType(type);
					r.setSequence(sequence);
					r.setCategory(templateCategory);
					dao.merge(r);
					LogAction.addLogSynchronous(loggedInInfo, "ReportManager.addUpdateTemplate", "id=" + r.getId());
				}

			}
		} catch (Exception e) {
			MiscUtils.getLogger().error("Error", e);
			return "Error parsing template file, make sure the root element is set.";
		}
		return "Saved Successfully";
	}

	public String deleteTemplate(String templateid) {
		dao.remove(dao.find(Integer.parseInt(templateid)).getId());
		return "";
	}

	public String addTemplate(String uuid, String templateXML, LoggedInInfo loggedInInfo) {
		try {
			Document templateXMLdoc = readXml(templateXML);
			return addUpdateTemplate(uuid, null, templateXMLdoc, loggedInInfo);
		} catch (Exception e) {
			MiscUtils.getLogger().error("Error", e);
			return "Error: Error parsing file, make sure the root element is set.";
		}
	}

	public String updateTemplate(String uuid, String templateId, String templateXML, LoggedInInfo loggedInInfo) {
		try {
			Document templateXMLdoc = readXml(templateXML);
			return addUpdateTemplate(uuid, templateId, templateXMLdoc, loggedInInfo);
		} catch (Exception e) {
			MiscUtils.getLogger().error("Error", e);
			return "Error: Error parsing file";
		}
	}

}

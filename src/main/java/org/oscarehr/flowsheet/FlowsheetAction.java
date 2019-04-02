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
package org.oscarehr.flowsheet;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.xmlbeans.XmlOptions;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.dao.FlowSheetUserCreatedDao;
import org.oscarehr.common.dao.Icd9Dao;
import org.oscarehr.common.dao.MeasurementTypeDao;
import org.oscarehr.common.dao.ValidationsDao;
import org.oscarehr.common.model.FlowSheetUserCreated;
import org.oscarehr.common.model.Icd9;
import org.oscarehr.common.model.MeasurementType;
import org.oscarehr.common.model.Validations;
import org.oscarehr.flowsheets.FlowsheetDocument;
import org.oscarehr.flowsheets.FlowsheetDocument.Flowsheet;
import org.oscarehr.flowsheets.FlowsheetDocument.Flowsheet.Header;
import org.oscarehr.flowsheets.FlowsheetDocument.Flowsheet.Header.Item;
import org.oscarehr.flowsheets.FlowsheetDocument.Flowsheet.Header.Item.Rules;
import org.oscarehr.flowsheets.FlowsheetDocument.Flowsheet.Header.Item.Rules.Recommendation;
import org.oscarehr.flowsheets.FlowsheetDocument.Flowsheet.Header.Item.Rules.Recommendation.Condition;
import org.oscarehr.flowsheets.FlowsheetDocument.Flowsheet.Header.Item.Ruleset;
import org.oscarehr.flowsheets.FlowsheetDocument.Flowsheet.Header.Item.Ruleset.Rule;
import org.oscarehr.flowsheets.FlowsheetDocument.Flowsheet.Indicator;
import org.oscarehr.flowsheets.FlowsheetDocument.Flowsheet.Measurement;
import org.oscarehr.flowsheets.FlowsheetDocument.Flowsheet.Measurement.ValidationRule;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.oscarEncounter.oscarMeasurements.MeasurementFlowSheet;
import oscar.oscarEncounter.oscarMeasurements.MeasurementTemplateFlowSheetConfig;
import oscar.oscarPrevention.PreventionDisplayConfig;

public class FlowsheetAction extends DispatchAction {

	private FlowSheetUserCreatedDao flowsheetUserCreatedDao = SpringUtils.getBean(FlowSheetUserCreatedDao.class);
	private ProviderDao providerDao = SpringUtils.getBean(ProviderDao.class);
	private Icd9Dao icd9Dao = SpringUtils.getBean(Icd9Dao.class);
	private DemographicDao demographicDao = SpringUtils.getBean(DemographicDao.class);
	private MeasurementTypeDao measurementTypeDao = SpringUtils.getBean(MeasurementTypeDao.class);
	private ValidationsDao validationsDao = SpringUtils.getBean(ValidationsDao.class);
	
	
	private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");

	
	private FlowSheetUserCreated create(FlowSheetUserCreated fsuc) {
		
		FlowsheetDocument fd = FlowsheetDocument.Factory.newInstance();
		Flowsheet fs = fd.addNewFlowsheet();
		fs.setName(fsuc.getName());
		fs.setDsRules("");
		fs.setDxcodeTriggers(fsuc.getDxcodeTriggers());
		fs.setDisplayName(fsuc.getDisplayName());
		fs.setWarningColour("#E00000");
		fs.setRecommendationColour("yellow");
		fs.setTopHTML("");
		
		Indicator i1 = fs.addNewIndicator();
		i1.setKey("HIGH_1");
		i1.setColour("#E00000");
		
		Indicator i2 = fs.addNewIndicator();
		i2.setKey("HIGH");
		i2.setColour("orange");
		
		Indicator i3 = fs.addNewIndicator();
		i3.setKey("LOW");
		i3.setColour("#9999FF");
		
		Header h1 = fs.addNewHeader();
		h1.setDisplayName("General");
		
		XmlOptions options = new XmlOptions();
		options.setSavePrettyPrint();
		options.setCharacterEncoding("UTF-8");
		options.setUseDefaultNamespace();
		
		Map<String,String> dnsMap = new java.util.HashMap<String,String>();
		dnsMap.put("", "flowsheets.oscarehr.org");
		options.setSaveImplicitNamespaces(dnsMap);
	       
	    String data = null;
		try {
			StringWriter writer = new StringWriter();
			fd.save(writer, options);
			data = writer.toString();
		} catch(IOException e) {
			MiscUtils.getLogger().error("Error",e);
		}
		
		if(data != null) {
			fsuc.setXmlContent("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + data.replaceAll("<flowsheet ", "<flowsheet xmlns=\"flowsheets.oscarehr.org\" "));
			flowsheetUserCreatedDao.merge(fsuc);
		}
		
		return fsuc;
	}
	
	private FlowSheetUserCreated create2(FlowSheetUserCreated fsuc, Flowsheet flowsheet)  {
		
		FlowsheetDocument fd = FlowsheetDocument.Factory.newInstance();
		Flowsheet fs = fd.addNewFlowsheet();
		fs.setName(fsuc.getName());
		fs.setDsRules("");
		fs.setDxcodeTriggers(fsuc.getDxcodeTriggers());
		fs.setDisplayName(fsuc.getDisplayName());
		fs.setWarningColour(fsuc.getWarningColour());
		fs.setRecommendationColour(fsuc.getRecommendationColour());
		fs.setTopHTML("");
		
		
		if(flowsheet.getIndicatorArray() != null) {
			for(int x=0;x<flowsheet.getIndicatorArray().length;x++) {
				Indicator tmp = flowsheet.getIndicatorArray()[x];
				Indicator i = fs.addNewIndicator();
				i.setColour(tmp.getColour());
				i.setKey(tmp.getKey());
			}
		}
		
		if(flowsheet.getHeaderArray() != null) {
			Header newHeader = fs.addNewHeader();
			newHeader.setDisplayName("General");
			for(int x=0;x<flowsheet.getHeaderArray().length;x++) {
				Header h = flowsheet.getHeaderArray()[x];
				for(int y=0;y<h.getItemArray().length;y++) {
					Item tmp = h.getItemArray()[y];
					Item i = newHeader.addNewItem();
					i.setDisplayName(tmp.getDisplayName());
					if(!StringUtils.isEmpty(tmp.getDsRules())) {
						i.setDsRules(tmp.getDsRules());
					}
					i.setGraphable(tmp.getGraphable());
					i.setGuideline(tmp.getGuideline());
					i.setValueName(tmp.getValueName());
					if(!StringUtils.isEmpty(tmp.getMeasurementType())) {
						i.setMeasurementType(tmp.getMeasurementType());
					}
					if(!StringUtils.isEmpty(tmp.getPreventionType())) {
						i.setPreventionType(tmp.getPreventionType());
					}
					
					if(tmp.getRules() != null) {
						Rules r = i.addNewRules();
						for(int z=0;z<tmp.getRules().getRecommendationArray().length;z++) {
							Recommendation rec = tmp.getRules().getRecommendationArray()[z];
							Recommendation newRec = r.addNewRecommendation();
							if(rec.getCondition() != null) {
								Condition c = newRec.addNewCondition();
								c.setParam(rec.getCondition().getParam());
								c.setStringValue(rec.getCondition().getStringValue());
								c.setType(rec.getCondition().getType());
								c.setValue(rec.getCondition().getValue());
							}
							newRec.setStrength(rec.getStrength());
						}
					}
					
					if(tmp.getRuleset() != null) {
						Ruleset r = i.addNewRuleset();
						for(int z=0;z<tmp.getRuleset().getRuleArray().length;z++) {
							Rule rule = tmp.getRuleset().getRuleArray()[z];
							Rule newRule = r.addNewRule();
							newRule.setIndicationColor(rule.getIndicationColor());
							for(int m = 0; m < rule.getConditionArray().length;m++) {
								FlowsheetDocument.Flowsheet.Header.Item.Ruleset.Rule.Condition c = rule.getConditionArray()[m];
								FlowsheetDocument.Flowsheet.Header.Item.Ruleset.Rule.Condition newC = newRule.addNewCondition();
								newC.setType(c.getType());
								newC.setParam(c.getParam());
								newC.setStringValue(c.getStringValue());
								newC.setValue(c.getValue());
							}
						}
					}
				}
			}
		}
		
		if(flowsheet.getMeasurementArray() != null) {
			for(int x=0;x<flowsheet.getMeasurementArray().length;x++) {
				Measurement tmp = flowsheet.getMeasurementArray()[x];
				Measurement m = fs.addNewMeasurement();
				m.setMeasuringInstrc(tmp.getMeasuringInstrc());
				m.setType(tmp.getType());
				m.setTypeDesc(tmp.getTypeDesc());
				m.setTypeDisplayName(tmp.getTypeDisplayName());
				
				if(tmp.getValidationRule() != null) {
					ValidationRule vr = m.addNewValidationRule();
					vr.setIsDate(tmp.getValidationRule().getIsDate());
					vr.setIsNumeric(tmp.getValidationRule().getIsNumeric());
					vr.setMaxLength(tmp.getValidationRule().getMaxLength());
					vr.setMaxValue(tmp.getValidationRule().getMaxValue());
					vr.setMinLength(tmp.getValidationRule().getMinLength());
					vr.setMinValue(tmp.getValidationRule().getMinValue());
					vr.setName(tmp.getValidationRule().getName());
				}
			}
		}
		
		
		XmlOptions options = new XmlOptions();
		options.setSavePrettyPrint();
		options.setCharacterEncoding("UTF-8");
		options.setUseDefaultNamespace();
		
		Map<String,String> dnsMap = new java.util.HashMap<String,String>();
		dnsMap.put("", "flowsheets.oscarehr.org");
		options.setSaveImplicitNamespaces(dnsMap);
	       
	    String data = null;
		try {
			StringWriter writer = new StringWriter();
			fd.save(writer, options);
			data = writer.toString();
		} catch(IOException e) {
			MiscUtils.getLogger().error("Error",e);
		}
		
		if(data != null) {
			fsuc.setXmlContent("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + data.replaceAll("<flowsheet ", "<flowsheet xmlns=\"flowsheets.oscarehr.org\" "));
		}
		
		return fsuc;
	}
	
	private void addMeasurement(FlowSheetUserCreated fsuc, MeasurementType measurementType) {
		FlowsheetDocument fd = null;
		Flowsheet flowsheet = null;
		try {
			 fd = FlowsheetDocument.Factory.parse(new StringReader(fsuc.getXmlContent()));
			flowsheet = fd.getFlowsheet();
		}catch (Exception e) {
			MiscUtils.getLogger().error("Error",e);
		}
		
		if(flowsheet != null) {
			
			//check that we havn't already added this type
			for(int x=0;x<flowsheet.getHeaderArray(0).getItemArray().length;x++) {
				Item i = flowsheet.getHeaderArray(0).getItemArray(x);
				if(i.getMeasurementType() != null && i.getMeasurementType().equals(measurementType.getType())) {
					return;
				}
			}
			
			Item item = flowsheet.getHeaderArray(0).addNewItem();
			item.setMeasurementType(measurementType.getType());
			item.setDisplayName(measurementType.getTypeDescription());
			item.setGuideline("todo");
			item.setGraphable("yes");
			item.setValueName(measurementType.getTypeDescription());
			
			//should check measurement not already added
			Measurement m = flowsheet.addNewMeasurement();
			m.setType(measurementType.getType());
			m.setTypeDesc(measurementType.getTypeDescription());
			m.setTypeDisplayName(measurementType.getTypeDisplayName());
			m.setMeasuringInstrc(measurementType.getMeasuringInstruction());
			if(!StringUtils.isEmpty(measurementType.getValidation())) {
				Validations v = validationsDao.find(Integer.parseInt(measurementType.getValidation()));
				if(v != null) {
					ValidationRule vr = m.addNewValidationRule();
					vr.setName(v.getName());
					vr.setMaxValue(v.getMaxValue() != null ? v.getMaxValue().toString() : "");
					vr.setMinValue(v.getMaxValue() != null ? v.getMinValue().toString() : "");
					vr.setIsDate(v.isDate() != null  && v.isDate()? "1" : "");
					vr.setIsNumeric(v.isNumeric() != null  && v.isNumeric()? "1" : "");
					vr.setRegularExp(StringUtils.trimToEmpty(v.getRegularExp()));
					vr.setMaxLength(v.getMaxLength() != null ? v.getMaxLength().toString() : "");
					vr.setMinLength(v.getMinLength() != null ? v.getMinLength().toString() : "");
				}
			}
			
			
			XmlOptions options = new XmlOptions();
			options.setSavePrettyPrint();
			options.setCharacterEncoding("UTF-8");
			options.setUseDefaultNamespace();
			
			Map<String,String> dnsMap = new java.util.HashMap<String,String>();
			dnsMap.put("", "flowsheets.oscarehr.org");
			options.setSaveImplicitNamespaces(dnsMap);
		       
		    String data = null;
			try {
				StringWriter writer = new StringWriter();
				fd.save(writer, options);
				data = writer.toString();
			} catch(IOException e) {
				MiscUtils.getLogger().error("Error",e);
			}
			
			if(data != null) {
				fsuc.setXmlContent("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + data.replaceAll("<flowsheet ", "<flowsheet xmlns=\"flowsheets.oscarehr.org\" "));
				flowsheetUserCreatedDao.merge(fsuc);
			}
		}
		
	}
	
	private void addPrevention(FlowSheetUserCreated fsuc, String preventionType) {
		FlowsheetDocument fd = null;
		Flowsheet flowsheet = null;
		try {
			 fd = FlowsheetDocument.Factory.parse(new StringReader(fsuc.getXmlContent()));
			flowsheet = fd.getFlowsheet();
		}catch (Exception e) {
			MiscUtils.getLogger().error("Error",e);
		}
		
		if(flowsheet != null) {
			
			//check that we havn't already added this type
			for(int x=0;x<flowsheet.getHeaderArray(0).getItemArray().length;x++) {
				Item i = flowsheet.getHeaderArray(0).getItemArray(x);
				if(i.getPreventionType() != null && i.getPreventionType().equals(preventionType)) {
					return;
				}
			}
			
			Item item = flowsheet.getHeaderArray(0).addNewItem();
			item.setPreventionType(preventionType);
			item.setDisplayName(preventionType);
			item.setGuideline("");
			item.setGraphable("no");
			item.setValueName("");	
			
			XmlOptions options = new XmlOptions();
			options.setSavePrettyPrint();
			options.setCharacterEncoding("UTF-8");
			options.setUseDefaultNamespace();
			
			Map<String,String> dnsMap = new java.util.HashMap<String,String>();
			dnsMap.put("", "flowsheets.oscarehr.org");
			options.setSaveImplicitNamespaces(dnsMap);
		       
		    String data = null;
			try {
				StringWriter writer = new StringWriter();
				fd.save(writer, options);
				data = writer.toString();
			} catch(IOException e) {
				MiscUtils.getLogger().error("Error",e);
			}
			
			if(data != null) {
				fsuc.setXmlContent("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + data.replaceAll("<flowsheet ", "<flowsheet xmlns=\"flowsheets.oscarehr.org\" "));
				flowsheetUserCreatedDao.merge(fsuc);
			}
		}
		
	}
	
	public ActionForward getTemplateDetails(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws JSONException,IOException {
		String template = request.getParameter("template");
		
		Hashtable<String, String> systemFlowsheets = MeasurementTemplateFlowSheetConfig.getInstance().getFlowsheetDisplayNames();
		JSONObject resp = new JSONObject();
		
		for(String name : systemFlowsheets.keySet()) {
			if(name.equals(template)) {
				MeasurementFlowSheet flowSheet = MeasurementTemplateFlowSheetConfig.getInstance().getFlowSheet(name);
				resp.put("recommendationColour",flowSheet.getRecommendationColour());
				resp.put("warningColour",flowSheet.getWarningColour());
				resp.put("dxTriggers",flowSheet.getDxTriggersString());
			}
		}
		
		resp.write(response.getWriter());
		return null;
	}
	
	public ActionForward addMeasurement(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		String flowsheetId = request.getParameter("flowsheetId");
		String measurementTypeId = request.getParameter("measurementTypeId");

		FlowSheetUserCreated fsuc = flowsheetUserCreatedDao.find(Integer.parseInt(flowsheetId));
		MeasurementType measurementType = measurementTypeDao.find(Integer.parseInt(measurementTypeId));
		
		String xmlData = fsuc.getXmlContent();
		
		if(xmlData == null) {
			fsuc = create(fsuc);
		}
		
		addMeasurement(fsuc,measurementType);
		
		
		return null;
	}
	
	public ActionForward addPrevention(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		String flowsheetId = request.getParameter("flowsheetId");
		String preventionType = request.getParameter("preventionType");

		FlowSheetUserCreated fsuc = flowsheetUserCreatedDao.find(Integer.parseInt(flowsheetId));
		
		String xmlData = fsuc.getXmlContent();
		
		if(xmlData == null) {
			fsuc = create(fsuc);
		}
		
		addPrevention(fsuc,preventionType);
		
		
		return null;
	}
	
	public ActionForward getValidations(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, JSONException  {
		List<Validations> validationList = validationsDao.findAll();
		
		JSONObject resp = new JSONObject();
		JSONArray respArr = new JSONArray();
		
		for(Validations v : validationList) {
			JSONObject i = new JSONObject();
			i.put("id", v.getId());
			i.put("maxLength", v.getMaxLength());
			i.put("maxValue", v.getMaxValue());
			i.put("minLength", v.getMinLength());
			i.put("minValue", v.getMinValue());
			i.put("name", v.getName());
			i.put("regularExp", v.getRegularExp());
			respArr.put(i);
		}
		resp.put("results", respArr);
		
		resp.write(response.getWriter());
		
		return null;
		
	}
	
	public ActionForward getMeasurementTypes(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, JSONException  {
	
		List<MeasurementType> typeList =  measurementTypeDao.findAll();
		
		JSONObject resp = new JSONObject();
		JSONArray respArr = new JSONArray();
		
		for(MeasurementType mt : typeList) {
			JSONObject i = new JSONObject();
			i.put("id", mt.getId());
			i.put("type", mt.getType());
			i.put("description", mt.getType());
			i.put("displayName", mt.getTypeDisplayName());
			i.put("instruction", mt.getMeasuringInstruction());
			
			if(!StringUtils.isEmpty(mt.getValidation())) {
				Validations v = validationsDao.find(Integer.parseInt(mt.getValidation()));
				i.put("validation", v.getName());
			}
			respArr.put(i);
		}
		resp.put("results", respArr);
		
		resp.write(response.getWriter());
		
		return null;
	}
	
	public ActionForward getPreventionTypes(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, JSONException  {

		PreventionDisplayConfig pdc = PreventionDisplayConfig.getInstance();
		ArrayList<HashMap<String,String>> prevList = pdc.getPreventions();

		JSONObject resp = new JSONObject();
		JSONArray respArr = new JSONArray();
		
		for(HashMap<String,String> item : prevList) {
			JSONObject i = new JSONObject();
			i.put("id", item.get("name"));
			i.put("displayName", item.get("name"));
			respArr.put(i);
		}
		resp.put("results", respArr);
		
		resp.write(response.getWriter());
		
		return null;
	}
	
	public ActionForward addNewFlowsheet(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, JSONException  {
		
		String name = request.getParameter("name");
		String template = request.getParameter("template");
		String scope = request.getParameter("scope");
		String providerNo = request.getParameter("providerNo");
		String demographicNo = request.getParameter("demographicNo");
		String triggers = request.getParameter("triggers");
		String recommendationColour = request.getParameter("recommendationColour");
		String warningColour = request.getParameter("warningColour");
		
		FlowSheetUserCreated fsuc = new FlowSheetUserCreated();
		fsuc.setName(UUID.randomUUID().toString().substring(0, 4));
		fsuc.setDisplayName(name);
		fsuc.setTemplate(template);
		fsuc.setScope(scope);
		if("patient".equals(scope)) {
				fsuc.setScopeDemographicNo(Integer.parseInt(demographicNo));
		}
		if("provider".equals(scope)) {
			fsuc.setScopeProviderNo(providerNo);
		}
		fsuc.setDxcodeTriggers(triggers);
		fsuc.setRecommendationColour(recommendationColour);
		fsuc.setWarningColour(warningColour);
		fsuc.setArchived(false);
		fsuc.setTopHTML("");
		fsuc.setCreatedDate(new Date());
		fsuc.setCreatedBy(LoggedInInfo.getLoggedInInfoFromSession(request).getLoggedInProviderNo());
		
		//load the file
		File f = MeasurementTemplateFlowSheetConfig.getInstance().getFileMap().get(fsuc.getTemplate());
		if(f != null) {
			
			String fileData = FileUtils.readFileToString(f);
			fileData = fileData.replaceAll("<flowsheet ", "<flowsheet xmlns=\"flowsheets.oscarehr.org\" ");
			
			try {
				
				 FlowsheetDocument fd = FlowsheetDocument.Factory.parse(new StringReader(fileData));
				 Flowsheet flowsheet = fd.getFlowsheet();
				 
				 
				 String xmlData = fsuc.getXmlContent();
					
				 if(xmlData == null) {
					 fsuc = create2(fsuc,flowsheet);
				 }
		
				 
			}catch (Exception e) {
				MiscUtils.getLogger().error("Error",e);
			}
		}
		
	
		
		flowsheetUserCreatedDao.persist(fsuc);
		
		JSONObject obj = new JSONObject();
		obj.put("success", true);
		obj.put("id", fsuc.getId());
		obj.write(response.getWriter());
		
		MeasurementTemplateFlowSheetConfig.getInstance().reloadFlowsheets();            
		
		return null;
	}
	
	public ActionForward deleteFlowsheet(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, JSONException  {
		String id = request.getParameter("id");

		flowsheetUserCreatedDao.remove(Integer.parseInt(id));
		
		JSONObject obj = new JSONObject();
		obj.put("success", true);
		obj.put("id", id);
		obj.write(response.getWriter());
		
		MeasurementTemplateFlowSheetConfig.getInstance().reloadFlowsheets();            
		
		return null;
	}
	
	public ActionForward removeItem(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, JSONException  {
		String flowsheetId = request.getParameter("flowsheetId");
		String type = request.getParameter("id");

		FlowSheetUserCreated fsuc = flowsheetUserCreatedDao.find(Integer.parseInt(flowsheetId));
		
		FlowsheetDocument fd = null;
		Flowsheet flowsheet = null;
		
		
		if(fsuc != null) {
			try {
				 fd = FlowsheetDocument.Factory.parse(new StringReader(fsuc.getXmlContent()));
				 flowsheet = fd.getFlowsheet();
			}catch (Exception e) {
				MiscUtils.getLogger().error("Error",e);
			}
			
		//	Item[] items = new Item[flowsheet.getHeaderArray(0).getItemArray().length - 1];
			List<Item> items = new ArrayList<Item>();
			
			int y=0;
			for(int x=0;x<flowsheet.getHeaderArray(0).getItemArray().length;x++) {
				Item i = flowsheet.getHeaderArray(0).getItemArray(x);
				if(i.getMeasurementType() != null && i.getMeasurementType().equals(type)) {
					continue;
				}
				if(i.getPreventionType() != null && i.getPreventionType().equals(type)) {
					continue;
				}
				items.add(i);
			}
			
			flowsheet.getHeaderArray(0).setItemArray(items.toArray(new Item[flowsheet.getHeaderArray(0).getItemArray().length-1]));
			
			XmlOptions options = new XmlOptions();
			options.setSavePrettyPrint();
			options.setCharacterEncoding("UTF-8");
			options.setUseDefaultNamespace();
			
			Map<String,String> dnsMap = new java.util.HashMap<String,String>();
			dnsMap.put("", "flowsheets.oscarehr.org");
			options.setSaveImplicitNamespaces(dnsMap);
		       
		    String data = null;
			try {
				StringWriter writer = new StringWriter();
				fd.save(writer, options);
				data = writer.toString();
			} catch(IOException e) {
				MiscUtils.getLogger().error("Error",e);
			}
			
			if(data != null) {
				fsuc.setXmlContent("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + data.replaceAll("<flowsheet ", "<flowsheet xmlns=\"flowsheets.oscarehr.org\" "));
				flowsheetUserCreatedDao.merge(fsuc);
			}
		}
		
		JSONObject obj = new JSONObject();
		obj.put("success", true);
		obj.write(response.getWriter());
		return null;
	}

	
	public ActionForward getWarnings(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, JSONException  {

		String flowsheetId = request.getParameter("flowsheetId");
		String measurementType = request.getParameter("measurementType");

		FlowSheetUserCreated fsuc = flowsheetUserCreatedDao.find(Integer.parseInt(flowsheetId));
		
		FlowsheetDocument fd = null;
		Flowsheet flowsheet = null;
	
		JSONObject obj = new JSONObject();
		
		
		if(fsuc != null) {
			try {
				 fd = FlowsheetDocument.Factory.parse(new StringReader(fsuc.getXmlContent()));
				 flowsheet = fd.getFlowsheet();
			}catch (Exception e) {
				MiscUtils.getLogger().error("Error",e);
			}
			
			Item item = findItem(flowsheet,measurementType);
			
			Rules rules = item.getRules();
			
			if(rules != null) {
				JSONArray objRules = new JSONArray();
				
				for(int x=0;x<rules.getRecommendationArray().length;x++) {
					Recommendation recommendation = rules.getRecommendationArray(x);
					Condition condition = recommendation.getCondition();
					
					JSONObject r = new JSONObject();
					
					r.put("strength", recommendation.getStrength());
					r.put("type", condition.getType());
					r.put("param", condition.getParam());
					r.put("value", condition.getValue());
					
					String sha256hex = DigestUtils.sha256Hex(recommendation.getStrength() + ":" + condition.getType() + ":" + condition.getParam() + ":" + condition.getValue() );

					r.put("hash",sha256hex);
					
					objRules.put(r);
				}
				
				obj.put("rules", objRules);
			}
		}
		
		obj.write(response.getWriter());
		return null;
		
	}
	
	public ActionForward removeWarning(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, JSONException  {
		String flowsheetId = request.getParameter("flowsheetId");
		String measurementType = request.getParameter("type");
		String hash = request.getParameter("hash");

		FlowSheetUserCreated fsuc = flowsheetUserCreatedDao.find(Integer.parseInt(flowsheetId));
		
		FlowsheetDocument fd = null;
		Flowsheet flowsheet = null;
		
		
		if(fsuc != null) {
			try {
				 fd = FlowsheetDocument.Factory.parse(new StringReader(fsuc.getXmlContent()));
				 flowsheet = fd.getFlowsheet();
			}catch (Exception e) {
				MiscUtils.getLogger().error("Error",e);
			}
			
			Item item = findItem(flowsheet, measurementType);
		
			if(item != null) {
				Rules rules = item.getRules();
				if(rules != null) {
					List<Recommendation> newRecs = new ArrayList<Recommendation>();
					for(int x=0;x<rules.getRecommendationArray().length;x++) {
						Recommendation r = rules.getRecommendationArray(x);
						Condition condition = r.getCondition();
						String sha256hex = DigestUtils.sha256Hex(r.getStrength() + ":" + condition.getType() + ":" + condition.getParam() + ":" + condition.getValue() );

						if(!hash.equals(sha256hex)) {
							newRecs.add(r);
						}
					}
					rules.setRecommendationArray(newRecs.toArray(new Recommendation[newRecs.size()]));
				}
			}
			
			XmlOptions options = new XmlOptions();
			options.setSavePrettyPrint();
			options.setCharacterEncoding("UTF-8");
			options.setUseDefaultNamespace();
			
			Map<String,String> dnsMap = new java.util.HashMap<String,String>();
			dnsMap.put("", "flowsheets.oscarehr.org");
			options.setSaveImplicitNamespaces(dnsMap);
		       
		    String data = null;
			try {
				StringWriter writer = new StringWriter();
				fd.save(writer, options);
				data = writer.toString();
			} catch(IOException e) {
				MiscUtils.getLogger().error("Error",e);
			}
			
			if(data != null) {
				fsuc.setXmlContent("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + data.replaceAll("<flowsheet ", "<flowsheet xmlns=\"flowsheets.oscarehr.org\" "));
				flowsheetUserCreatedDao.merge(fsuc);
			}
		}
		
		JSONObject obj = new JSONObject();
		obj.put("success", true);
		obj.write(response.getWriter());
		return null;
	}
	
	public ActionForward removeTarget(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, JSONException  {
		String flowsheetId = request.getParameter("flowsheetId");
		String measurementType = request.getParameter("type");
		String hash = request.getParameter("hash");

		FlowSheetUserCreated fsuc = flowsheetUserCreatedDao.find(Integer.parseInt(flowsheetId));
		
		FlowsheetDocument fd = null;
		Flowsheet flowsheet = null;
		
		
		if(fsuc != null) {
			try {
				 fd = FlowsheetDocument.Factory.parse(new StringReader(fsuc.getXmlContent()));
				 flowsheet = fd.getFlowsheet();
			}catch (Exception e) {
				MiscUtils.getLogger().error("Error",e);
			}
			
			Item item = findItem(flowsheet, measurementType);
		
			if(item != null) {
				Ruleset ruleSet = item.getRuleset();
				if(ruleSet != null) {
					//List<Rule> newRules = new ArrayList<Rule>();
					for(int x=0;x<ruleSet.getRuleArray().length;x++) {
						Rule r = ruleSet.getRuleArray(x);
						
						for(int y=0;y<r.getConditionArray().length;y++) {
							
							String sha256hex = DigestUtils.sha256Hex(r.getIndicationColor() + ":" + r.getConditionArray()[y].getType() + ":" +  r.getConditionArray()[y].getParam() + ":" +  r.getConditionArray()[y].getValue() );
							
							if(hash.equals(sha256hex)) {
								r.removeCondition(y);
							//	newRules.add(r);
							}
						}
						if(r.getConditionArray().length == 0) {
							ruleSet.removeRule(x);
						}
					}
				//	ruleSet.setRuleArray(newRules.toArray(new Rule[newRules.size()]));
				}
			}
			
			XmlOptions options = new XmlOptions();
			options.setSavePrettyPrint();
			options.setCharacterEncoding("UTF-8");
			options.setUseDefaultNamespace();
			
			Map<String,String> dnsMap = new java.util.HashMap<String,String>();
			dnsMap.put("", "flowsheets.oscarehr.org");
			options.setSaveImplicitNamespaces(dnsMap);
		       
		    String data = null;
			try {
				StringWriter writer = new StringWriter();
				fd.save(writer, options);
				data = writer.toString();
			} catch(IOException e) {
				MiscUtils.getLogger().error("Error",e);
			}
			
			if(data != null) {
				fsuc.setXmlContent("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + data.replaceAll("<flowsheet ", "<flowsheet xmlns=\"flowsheets.oscarehr.org\" "));
				flowsheetUserCreatedDao.merge(fsuc);
			}
		}
		
		JSONObject obj = new JSONObject();
		obj.put("success", true);
		obj.write(response.getWriter());
		return null;
	}
	
	public ActionForward getIndicators(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, JSONException  {

		String flowsheetId = request.getParameter("flowsheetId");
		
		FlowSheetUserCreated fsuc = flowsheetUserCreatedDao.find(Integer.parseInt(flowsheetId));
		
		FlowsheetDocument fd = null;
		Flowsheet flowsheet = null;
	
		JSONObject obj = new JSONObject();
		
		
		JSONArray jIndicators = new JSONArray();
		
		
		if(fsuc != null) {
			try {
				 fd = FlowsheetDocument.Factory.parse(new StringReader(fsuc.getXmlContent()));
				 flowsheet = fd.getFlowsheet();
			}catch (Exception e) {
				MiscUtils.getLogger().error("Error",e);
			}
			
			Indicator[] indicators =flowsheet.getIndicatorArray();
			
			for(int x=0;x<indicators.length;x++) {
				JSONObject r = new JSONObject();
				r.put("key", indicators[x].getKey());
				r.put("colour", indicators[x].getColour());
				jIndicators.put(r);
			}
		}
		
		obj.put("indicators",jIndicators);
		
		obj.write(response.getWriter());
		return null;
		
	}
	
	public ActionForward getTargets(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, JSONException  {

		String flowsheetId = request.getParameter("flowsheetId");
		String measurementType = request.getParameter("measurementType");

		FlowSheetUserCreated fsuc = flowsheetUserCreatedDao.find(Integer.parseInt(flowsheetId));
		
		FlowsheetDocument fd = null;
		Flowsheet flowsheet = null;
	
		JSONObject obj = new JSONObject();
		
		
		if(fsuc != null) {
			try {
				 fd = FlowsheetDocument.Factory.parse(new StringReader(fsuc.getXmlContent()));
				 flowsheet = fd.getFlowsheet();
			}catch (Exception e) {
				MiscUtils.getLogger().error("Error",e);
			}
			
			Item item = findItem(flowsheet,measurementType);
			
			Ruleset rules = item.getRuleset();
			
			if(rules != null) {
				JSONArray objRules = new JSONArray();
				
				for(int x=0;x<rules.getRuleArray().length;x++) {
					Rule rule = rules.getRuleArray(x);
					
					
					for(int y=0;y<rule.getConditionArray().length;y++) {
						FlowsheetDocument.Flowsheet.Header.Item.Ruleset.Rule.Condition c = rule.getConditionArray(y);
						JSONObject r = new JSONObject();
						
						r.put("indicator", rule.getIndicationColor());
						r.put("type", c.getType());
						r.put("param", c.getParam());
						r.put("value", c.getValue());
						
						String sha256hex = DigestUtils.sha256Hex(rule.getIndicationColor() + ":" + c.getType() + ":" + c.getParam() + ":" + c.getValue() );

						r.put("hash",sha256hex);
						
						
						objRules.put(r);
					}
					
				}
				
				obj.put("rules", objRules);
			}
		}
		
		obj.write(response.getWriter());
		return null;
		
	}

	public ActionForward saveFlowsheetItemWarning(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, JSONException  {
		String flowsheetId = request.getParameter("flowsheetId");
		String measurementType = request.getParameter("measurementType");

		FlowSheetUserCreated fsuc = flowsheetUserCreatedDao.find(Integer.parseInt(flowsheetId));
		
		FlowsheetDocument fd = null;
		Flowsheet flowsheet = null;
		
		
		if(fsuc != null) {
			try {
				 fd = FlowsheetDocument.Factory.parse(new StringReader(fsuc.getXmlContent()));
				 flowsheet = fd.getFlowsheet();
			}catch (Exception e) {
				MiscUtils.getLogger().error("Error",e);
			}
			
		
			Item i = findItem(flowsheet,measurementType);
			
			Rules r = i.getRules();
			
			if(r == null) {
				r = i.addNewRules();
			}
			
			Recommendation rec = r.addNewRecommendation();
			
			String strength = request.getParameter("strength");
			rec.setStrength(strength);
			//rec.setmessage();
			Condition cond = rec.addNewCondition();
			
			cond.setParam(request.getParameter("param"));
			cond.setType(request.getParameter("condition"));
			cond.setValue(request.getParameter("value"));
			
			
			
			XmlOptions options = new XmlOptions();
			options.setSavePrettyPrint();
			options.setCharacterEncoding("UTF-8");
			options.setUseDefaultNamespace();
			
			Map<String,String> dnsMap = new java.util.HashMap<String,String>();
			dnsMap.put("", "flowsheets.oscarehr.org");
			options.setSaveImplicitNamespaces(dnsMap);
		       
		    String data = null;
			try {
				StringWriter writer = new StringWriter();
				fd.save(writer, options);
				data = writer.toString();
			} catch(IOException e) {
				MiscUtils.getLogger().error("Error",e);
			}
			
			if(data != null) {
				fsuc.setXmlContent("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + data.replaceAll("<flowsheet ", "<flowsheet xmlns=\"flowsheets.oscarehr.org\" "));
				flowsheetUserCreatedDao.merge(fsuc);
			}
		}
		
		JSONObject obj = new JSONObject();
		obj.put("success", true);
		obj.write(response.getWriter());
		return null;
	}
	
	public ActionForward saveFlowsheetItemTarget(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, JSONException  {
		String flowsheetId = request.getParameter("flowsheetId");
		String measurementType = request.getParameter("measurementType");
		String param = request.getParameter("param");
		String value = request.getParameter("value");
		String indicator = request.getParameter("indicator");
		String type = request.getParameter("type");
		
		FlowSheetUserCreated fsuc = flowsheetUserCreatedDao.find(Integer.parseInt(flowsheetId));
		
		FlowsheetDocument fd = null;
		Flowsheet flowsheet = null;
		
		
		if(fsuc != null) {
			try {
				 fd = FlowsheetDocument.Factory.parse(new StringReader(fsuc.getXmlContent()));
				 flowsheet = fd.getFlowsheet();
			}catch (Exception e) {
				MiscUtils.getLogger().error("Error",e);
			}
			
		
			Item i = findItem(flowsheet,measurementType);
			
			Ruleset r = i.getRuleset();
			
			if(r == null) {
				r = i.addNewRuleset();
			}
			
			Rule rule = findRuleInRuleset(r,indicator);
			
			if(rule == null) {
				rule = r.addNewRule();
				rule.setIndicationColor(indicator);
			}
			
			FlowsheetDocument.Flowsheet.Header.Item.Ruleset.Rule.Condition c = rule.addNewCondition();
			c.setType(type);
			if(!StringUtils.isEmpty(param)) {
				c.setParam(param);
			}
			c.setValue(value);
			
			XmlOptions options = new XmlOptions();
			options.setSavePrettyPrint();
			options.setCharacterEncoding("UTF-8");
			options.setUseDefaultNamespace();
			
			Map<String,String> dnsMap = new java.util.HashMap<String,String>();
			dnsMap.put("", "flowsheets.oscarehr.org");
			options.setSaveImplicitNamespaces(dnsMap);
		       
		    String data = null;
			try {
				StringWriter writer = new StringWriter();
				fd.save(writer, options);
				data = writer.toString();
			} catch(IOException e) {
				MiscUtils.getLogger().error("Error",e);
			}
			
			if(data != null) {
				fsuc.setXmlContent("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + data.replaceAll("<flowsheet ", "<flowsheet xmlns=\"flowsheets.oscarehr.org\" "));
				flowsheetUserCreatedDao.merge(fsuc);
			}
		}
		
		JSONObject obj = new JSONObject();
		obj.put("success", true);
		obj.write(response.getWriter());
		return null;
	}
	
	public Rule findRuleInRuleset(Ruleset ruleSet, String indicator) {
		for(int x=0;x<ruleSet.getRuleArray().length;x++) {
			Rule r = ruleSet.getRuleArray(x);
			if(indicator.equals(r.getIndicationColor())) {
				return r;
			}
		}
		return null;
	}
	
	public ActionForward saveFlowsheetItem(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, JSONException  {

		String flowsheetId = request.getParameter("flowsheetId");
		String measurementType = request.getParameter("measurementType");

		FlowSheetUserCreated fsuc = flowsheetUserCreatedDao.find(Integer.parseInt(flowsheetId));
		
		FlowsheetDocument fd = null;
		Flowsheet flowsheet = null;
		
		
		if(fsuc != null) {
			try {
				 fd = FlowsheetDocument.Factory.parse(new StringReader(fsuc.getXmlContent()));
				 flowsheet = fd.getFlowsheet();
			}catch (Exception e) {
				MiscUtils.getLogger().error("Error",e);
			}
			
		
			Item i = findItem(flowsheet,measurementType);
			
			i.setDisplayName(request.getParameter("displayName"));
			i.setGuideline(request.getParameter("guideline"));
			i.setGraphable(request.getParameter("graphable"));
			
			
			Measurement m = findMeasurement(flowsheet, measurementType);
			m.setMeasuringInstrc(request.getParameter("measuringInstruction"));
			
			Validations v = validationsDao.find(Integer.parseInt(request.getParameter("validations")));
			if(v != null) {
				m.getValidationRule().setName(v.getName());
			}
			
			XmlOptions options = new XmlOptions();
			options.setSavePrettyPrint();
			options.setCharacterEncoding("UTF-8");
			options.setUseDefaultNamespace();
			
			Map<String,String> dnsMap = new java.util.HashMap<String,String>();
			dnsMap.put("", "flowsheets.oscarehr.org");
			options.setSaveImplicitNamespaces(dnsMap);
		       
		    String data = null;
			try {
				StringWriter writer = new StringWriter();
				fd.save(writer, options);
				data = writer.toString();
			} catch(IOException e) {
				MiscUtils.getLogger().error("Error",e);
			}
			
			if(data != null) {
				fsuc.setXmlContent("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + data.replaceAll("<flowsheet ", "<flowsheet xmlns=\"flowsheets.oscarehr.org\" "));
				flowsheetUserCreatedDao.merge(fsuc);
			}
		}
		
		JSONObject obj = new JSONObject();
		obj.put("success", true);
		obj.write(response.getWriter());
		return null;
	}
	
	public ActionForward getFlowsheetItem(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, JSONException  {
		String flowsheetId = request.getParameter("flowsheetId");
		String measurementType = request.getParameter("measurementType");

		JSONObject obj = new JSONObject();
		
		FlowSheetUserCreated fsuc = flowsheetUserCreatedDao.find(Integer.parseInt(flowsheetId));
		
		FlowsheetDocument fd = null;
		Flowsheet flowsheet = null;
		try {
			 fd = FlowsheetDocument.Factory.parse(new StringReader(fsuc.getXmlContent()));
			flowsheet = fd.getFlowsheet();
		}catch (Exception e) {
			MiscUtils.getLogger().error("Error",e);
		}
		
		Item i = findItem(flowsheet, measurementType);
		
		obj.put("displayName", i.getDisplayName());
		obj.put("graphable", i.getGraphable());
		obj.put("guideline", i.getGuideline());
		
		Measurement m = findMeasurement(flowsheet,measurementType);
		
		obj.put("measuringInstruction", m.getMeasuringInstrc());
		obj.put("type", m.getType());
		obj.put("typeDesc", m.getTypeDesc());
		obj.put("typeDisplayName", m.getTypeDisplayName());
		
		if(m.getValidationRule() != null ) {
			obj.put("validation", m.getValidationRule().getName());
			
			
			List<Validations> vList = validationsDao.findByName(m.getValidationRule().getName());
			if(!vList.isEmpty()) {
				obj.put("validationId",vList.get(0).getId());
			}
		}
		
		obj.write(response.getWriter());
		return null;
	}
	
	public ActionForward getFlowsheet(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, JSONException  {
		String id = request.getParameter("id");

		FlowSheetUserCreated fsuc = flowsheetUserCreatedDao.find(Integer.parseInt(id));
		
		JSONObject obj = new JSONObject();
		
		obj.put("name", fsuc.getName());
		obj.put("createdBy", providerDao.getProvider(fsuc.getCreatedBy()).getFormattedName());
		obj.put("createdDate",formatter.format( fsuc.getCreatedDate()));
		obj.put("displayName", fsuc.getDisplayName());
		String triggers = "";
		for(String trigger:fsuc.getDxcodeTriggers().split(",")) {
			String x = "";
			String[] parts = trigger.split(":");
			if(parts.length == 2 && "icd9".equals(parts[0])) {
				Icd9 item = icd9Dao.findByCode(parts[1]);
				if(item != null) {
					x = item.getDescription() + "(" + trigger + ")";
				} else {
					x = trigger;
				}
				
			} else {
				x=trigger;
			}
			triggers += !triggers.isEmpty() ? "<br/>" + x : x;
		}
		
		obj.put("dxCodeTriggers", triggers);
		obj.put("id", fsuc.getId());
		obj.put("recommendationColour", fsuc.getRecommendationColour());
		obj.put("scope", fsuc.getScope());
		obj.put("topHTML" , fsuc.getTopHTML());
		obj.put("scopeDemographicNo" , fsuc.getScopeDemographicNo());
		obj.put("scopeProviderNo" , fsuc.getScopeProviderNo());
		obj.put("warningColour" , fsuc.getWarningColour());
		obj.put("template" , fsuc.getTemplate());
		
		
		FlowsheetDocument fd = null;
		Flowsheet flowsheet = null;
		try {
			 fd = FlowsheetDocument.Factory.parse(new StringReader(fsuc.getXmlContent()));
			flowsheet = fd.getFlowsheet();
		}catch (Exception e) {
			MiscUtils.getLogger().error("Error",e);
		}
		
		if(flowsheet != null) {
			JSONArray iArr = new JSONArray();
			for(int x=0;x<flowsheet.getHeaderArray(0).getItemArray().length;x++) {
				Item item = flowsheet.getHeaderArray(0).getItemArray(x);
				JSONObject i = new JSONObject();
				i.put("measurementType", item.getMeasurementType());
				i.put("preventionType", item.getPreventionType());
				i.put("displayName", item.getDisplayName());
				i.put("guideline", item.getGuideline());
				i.put("graphable", item.isSetGraphable());
				
				Measurement m = findMeasurement(flowsheet,item.getMeasurementType());
				if(m != null) {
					i.put("measuringInstruction", m.getMeasuringInstrc());
					if(m.getValidationRule() != null) {
						i.put("validation", m.getValidationRule().getName());
					}
				}
				iArr.put(i);
				
			}
			obj.put("items", iArr);
		}
		
		obj.write(response.getWriter());
		return null;
	}
	
	private Measurement findMeasurement(Flowsheet flowsheet, String measurementType) {
		if(measurementType == null) {
			return null;
		}
		for(int x=0;x<flowsheet.getMeasurementArray().length;x++) {
			Measurement m = flowsheet.getMeasurementArray(x);
			if(measurementType.equals(m.getType())) {
				return m;
			}
		}
		return null;
	}
	
	private Item findItem(Flowsheet flowsheet, String measurementType) {
		for(int x=0;x<flowsheet.getHeaderArray(0).getItemArray().length;x++) {
			Item i = flowsheet.getHeaderArray(0).getItemArray(x);
			if(measurementType.equals(i.getMeasurementType())) {
				return i;
			}
		}
		return null;
	}
	
	public ActionForward getTemplateNames(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, JSONException {
		Hashtable<String, String> systemFlowsheets = MeasurementTemplateFlowSheetConfig.getInstance().getFlowsheetDisplayNames();
		JSONObject resp = new JSONObject();
		JSONArray fsList = new JSONArray();
		
		
		for(String name:systemFlowsheets.keySet()) {
			MeasurementFlowSheet flowSheet = MeasurementTemplateFlowSheetConfig.getInstance().getFlowSheet(name);
			if(flowSheet.isCustom()) {
				continue;
			}
			String displayName = systemFlowsheets.get(name);
			JSONObject i = new JSONObject();
			i.put("name", name);
			fsList.put(i);
		}
		
		resp.put("results", fsList);
		resp.write(response.getWriter());
		
		return null;
	}
	
	public ActionForward listSystem(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, JSONException {
		Hashtable<String, String> systemFlowsheets = MeasurementTemplateFlowSheetConfig.getInstance().getFlowsheetDisplayNames();
		JSONObject resp = new JSONObject();
		JSONArray fsList = new JSONArray();
		
		
		for(String name:systemFlowsheets.keySet()) {
			MeasurementFlowSheet flowSheet = MeasurementTemplateFlowSheetConfig.getInstance().getFlowSheet(name);
			if(flowSheet.isCustom()) {
				continue;
			}
			String displayName = systemFlowsheets.get(name);
			JSONObject i = new JSONObject();
			i.put("name", name);
			i.put("displayName", displayName);
			String triggers = "";
			if(flowSheet.getDxTriggers() != null) {
				for(String trigger:flowSheet.getDxTriggers()) {
					String x = "";
					String[] parts = trigger.split(":");
					if(parts.length == 2 && "icd9".equals(parts[0])) {
						Icd9 item = icd9Dao.findByCode(parts[1]);
						if(item != null) {
							x = item.getDescription() + "(" + trigger + ")";
						} else {
							x = trigger;
						}
						
					} else {
						x=trigger;
					}
					triggers += !triggers.isEmpty() ? "<br/>" + x : x;
				}
			}
			
			i.put("triggers", triggers);
			fsList.put(i);
			
		}
		
		resp.put("results", fsList);
		resp.write(response.getWriter());
		
		return null;
	}
	
	public ActionForward list(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, JSONException {
		
		String scope = request.getParameter("scope");
		List<FlowSheetUserCreated> flowsheets = null;
		if(!StringUtils.isEmpty(scope)) {
			 flowsheets =  flowsheetUserCreatedDao.findActiveByScope(scope);
		} else {
			flowsheets = flowsheetUserCreatedDao.getAllUserCreatedFlowSheets();
		}
		
		
		JSONObject resp = new JSONObject();
		JSONArray fsList = new JSONArray();
		
		for(FlowSheetUserCreated fs : flowsheets) {
			JSONObject i = new JSONObject();
			i.put("id", fs.getId());
			i.put("name", fs.getName());
			i.put("displayName", fs.getDisplayName());
			i.put("template",fs.getTemplate());
			String triggers = "";
			for(String trigger:fs.getDxcodeTriggers().split(",")) {
				String x = "";
				String[] parts = trigger.split(":");
				if(parts.length == 2 && "icd9".equals(parts[0])) {
					Icd9 item = icd9Dao.findByCode(parts[1]);
					if(item != null) {
						x = item.getDescription() + "(" + trigger + ")";
					} else {
						x = trigger;
					}
					
				} else {
					x=trigger;
				}
				triggers += !triggers.isEmpty() ? "<br/>" + x : x;
			}
			
			i.put("triggers", triggers);
			i.put("dateCreated", formatter.format(fs.getCreatedDate()));
			i.put("createdBy", !StringUtils.isEmpty(fs.getCreatedBy()) ? providerDao.getProvider(fs.getCreatedBy()).getFormattedName() : "");
			i.put("scope", fs.getScope());
			if(FlowSheetUserCreated.SCOPE_PATIENT.equals(fs.getScope())) {
				i.put("demographicNo", fs.getScopeDemographicNo());
				i.put("details", demographicDao.getDemographicById(fs.getScopeDemographicNo()).getFormattedName());
			}
			else if(FlowSheetUserCreated.SCOPE_PROVIDER.equals(fs.getScope())) {
				i.put("providerNo", fs.getScopeProviderNo());
				i.put("details", providerDao.getProvider(fs.getScopeProviderNo()).getFormattedName());
			} else {
				i.put("details", "");
			}
			fsList.put(i);
			
		}
		resp.put("results", fsList);
		
		resp.write(response.getWriter());
		return null;
	}
	
	public ActionForward reload(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		MeasurementTemplateFlowSheetConfig.getInstance().reloadFlowsheets();            
		
		return null;
	}
	
}

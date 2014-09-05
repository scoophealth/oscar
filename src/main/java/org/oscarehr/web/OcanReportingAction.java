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


package org.oscarehr.web;

import java.awt.Color;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.apache.commons.lang.WordUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.util.LabelValueBean;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.dao.OcanStaffFormDao;
import org.oscarehr.common.dao.OcanStaffFormDataDao;
import org.oscarehr.common.model.OcanStaffForm;
import org.oscarehr.common.model.OcanStaffFormData;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;
import org.oscarehr.web.reports.ocan.IndividualNeedRatingOverTimeReportGenerator;
import org.oscarehr.web.reports.ocan.NeedRatingOverTimeReportGenerator;
import org.oscarehr.web.reports.ocan.SummaryOfActionsAndCommentsReportGenerator;
import org.oscarehr.web.reports.ocan.beans.CachedOcanFormAndData;
import org.oscarehr.web.reports.ocan.beans.OcanConsumerStaffNeedBean;
import org.oscarehr.web.reports.ocan.beans.OcanDomainConsumerStaffBean;
import org.oscarehr.web.reports.ocan.beans.OcanIndividualNeedsOverTimeBean;
import org.oscarehr.web.reports.ocan.beans.OcanNeedRatingOverTimeNeedBreakdownBean;
import org.oscarehr.web.reports.ocan.beans.OcanNeedRatingOverTimeSummaryOfNeedsBean;
import org.oscarehr.web.reports.ocan.beans.SummaryOfActionsAndCommentsDomainBean;
import org.oscarehr.web.reports.ocan.beans.SummaryOfActionsAndCommentsOCANBean;
import org.oscarehr.web.reports.ocan.beans.SummaryOfActionsAndCommentsReportBean;

public class OcanReportingAction extends DispatchAction {

	Logger logger = MiscUtils.getLogger();

	private OcanStaffFormDao ocanStaffFormDao = (OcanStaffFormDao) SpringUtils.getBean("ocanStaffFormDao");
	private OcanStaffFormDataDao ocanStaffFormDataDao = (OcanStaffFormDataDao) SpringUtils.getBean("ocanStaffFormDataDao");
	private DemographicDao demographicDao = (DemographicDao)SpringUtils.getBean("demographicDao");

	/*
	 * return first OCAN that was done at this facility and call it either "Initial OCAN" or "Reassessment"
	 * return the latest OCAN and name it "Current OCAN"
	 * return the last OCAN (prior to current) and name it "Previous OCAN"
	 * return anything between previous and first OCAN chronologically, and number them, and display the datel
	 *
	 */
	public ActionForward getAssessments(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException  {
		String strClientId = request.getParameter("clientId");
		SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");

		LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
		Integer facilityId = loggedInInfo.getCurrentFacility().getId();
		List<OcanStaffForm> staffForms = ocanStaffFormDao.findLatestSignedOcanForms(facilityId,Integer.parseInt(strClientId));
		Collections.reverse(staffForms);

		List<LabelValueBean> results = new ArrayList<LabelValueBean>();

		if(staffForms.size() == 0) {
			MiscUtils.getLogger().info("No OCAN Assessments found for this client: " + strClientId);
	        JSONArray jsonArray = JSONArray.fromObject( results );
	        response.getWriter().print(jsonArray);
			return null;
		}

		//we know there's atleast 1.
		OcanStaffForm firstOcan = staffForms.get(0);
		String label = convertOcanReasonUserString(firstOcan.getReasonForAssessment());
		results.add(new LabelValueBean(label + " - " + dateFormatter.format(firstOcan.getCreated()),String.valueOf(firstOcan.getId())));

		if(staffForms.size()>1) {
			OcanStaffForm latestOcan = staffForms.get(staffForms.size()-1);
			results.add(new LabelValueBean("Current OCAN - " + dateFormatter.format(latestOcan.getCreated()),String.valueOf(latestOcan.getId())));
		}

		if(staffForms.size()>2) {
			OcanStaffForm previousOcan = staffForms.get(staffForms.size()-2);
			results.add(1,new LabelValueBean("Previous OCAN - " +dateFormatter.format(previousOcan.getCreated()),String.valueOf(previousOcan.getId())));
		}
		int counter=1;
		if(staffForms.size()>3) {
			for(int x=1;x<=staffForms.size()-3;x++) {
				OcanStaffForm staffForm = staffForms.get(x);
				label = convertOcanReasonUserString(staffForm.getReasonForAssessment()) + " "+ counter + " - " + dateFormatter.format(staffForm.getCreated());
				results.add(counter,new LabelValueBean(label,String.valueOf(staffForm.getId())));
				counter++;
			}
		}


		JSONArray jsonArray = JSONArray.fromObject( results );
		response.getWriter().print(jsonArray);

		return null;
	}

	private String convertOcanReasonUserString(String type) {
		if(type == null) {
			return "OCAN";
		}
		if(type.equals("IA")) {
			return "Initial Assessment";
		}
		if(type.equals("RA")) {
			return "Reassessment";
		}
		if(type.equals("DIS")) {
			return "(Prior to) Discharge";
		}
		if(type.equals("OTHR")) {
			return "Other";
		}
		if(type.equals("SC")) {
			return "Significant Change";
		}
		if(type.equals("REV")) {
			return "Review";
		}
		if(type.equals("REK")) {
			return "Re-key";
		}
		return "OCAN";
	}

	public ActionForward generateIndividualNeedRatingOverTimeReport(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)   {
		LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);

		String clientId = request.getParameter("client");
		String[] assessments = request.getParameterValues("assessment");
		String needsMet = request.getParameter("needs_met");
		String needsUnmet = request.getParameter("needs_unmet");
		String needsNo = request.getParameter("needs_no");
		String needsUnknown = request.getParameter("needs_unknown");

		logger.debug("Generate Individual Needs Rating over Time Report");
		logger.debug("clientId="+clientId);
		for(int x=0;x<assessments.length;x++)
			logger.debug("assessment="+assessments[x]);
		logger.debug("needsMet="+needsMet);
		logger.debug("needsUnmet="+needsUnmet);
		logger.debug("needsNo="+needsNo);
		logger.debug("needsUnknown="+needsUnknown);

		//bean which will feed our generator
		OcanIndividualNeedsOverTimeBean bean = new OcanIndividualNeedsOverTimeBean();
		bean.setConsumerName(demographicDao.getClientByDemographicNo(Integer.parseInt(clientId)).getFormattedName());
		bean.setStaffName(loggedInInfo.getLoggedInProvider().getFormattedName());
		bean.setReportDate(new Date());
		bean.setShowUnmetNeeds(needsUnmet!=null);
		bean.setShowMetNeeds(needsMet!=null);
		bean.setShowNoNeeds(needsNo !=null);
		bean.setShowUnknownNeeds(needsUnknown!=null);

		//list of charts
		List<IndividualNeedsOverTimeChartBean> results = new ArrayList<IndividualNeedsOverTimeChartBean>();

		for(int x=0;x<assessments.length;x++) {
			Integer formId = Integer.valueOf(assessments[x].split(";")[0]);

			//get the latest form for the assessment
			OcanStaffForm ocanForm = ocanStaffFormDao.find(formId);
			//get data fields
			List<OcanStaffFormData> ocanFormData = ocanStaffFormDataDao.findByForm(ocanForm.getId());
			//convert to map
			Map<String,OcanStaffFormData> ocanFormDataMap = new HashMap<String,OcanStaffFormData>();
			for(OcanStaffFormData tmp:ocanFormData) {
				ocanFormDataMap.put(tmp.getQuestion(), tmp);
			}
			//create and initialize map of counts
			Map<String, Integer> needsCountMap = new HashMap<String,Integer>();
			needsCountMap.put("unmet",0);
			needsCountMap.put("met",0);
			needsCountMap.put("no",0);
			needsCountMap.put("unknown",0);
			needsCountMap.put("clientUnmet",0);
			needsCountMap.put("clientMet",0);
			needsCountMap.put("clientNo",0);
			needsCountMap.put("clientUnknown",0);

			for(int i=1;i<=24;i++) {
				addToNeedCount_Client(needsCountMap,"client_" + i + "_1",ocanFormDataMap);
			}

			for(int i=1;i<=24;i++) {
				addToNeedCount_Staff(needsCountMap,i + "_1",ocanFormDataMap);
			}

			IndividualNeedsOverTimeChartBean chartBean = new IndividualNeedsOverTimeChartBean(ocanForm,needsCountMap,assessments[x].split(";")[1]);
			results.add(chartBean);
		}

		if(bean.isShowUnmetNeeds()) {
			bean.setUnmetNeedsChart(generateChart("Number of Unmet Needs over Time (Consumer and Staff)","unmet","clientUnmet",results));
		}

		if(bean.isShowMetNeeds()) {
			bean.setMetNeedsChart(generateChart("Number of Met Needs over Time (Consumer and Staff)","met","clientMet",results));
		}

		if(bean.isShowNoNeeds()) {
			bean.setNoNeedsChart(generateChart("Number of No Needs over Time (Consumer and Staff)","no","clientNo",results));
		}
		if(bean.isShowUnknownNeeds()) {
			bean.setUnknownNeedsChart(generateChart("Number of Unknown Needs over Time (Consumer and Staff)","unknown","clientUnknown",results));
		}

		IndividualNeedRatingOverTimeReportGenerator reportGen = new IndividualNeedRatingOverTimeReportGenerator();
		reportGen.setReportBean(bean);

		try {
			response.setContentType("application/pdf");
			response.setHeader("content-disposition", "inline; filename=\"IndividualNeedRatingOverTime.pdf\"");
			reportGen.generateReport(response.getOutputStream());
		}catch(Exception e) {
			logger.error("error",e);
		}

		return null;
	}


	private JFreeChart generateChart(String title, String needType, String clientNeedType, List<IndividualNeedsOverTimeChartBean> chartBeanList ) {

		DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		for(IndividualNeedsOverTimeChartBean chartBean:chartBeanList) {
			dataset.addValue(chartBean.getNeedsCountMap().get(needType), "Staff", chartBean.getLabel());
		}

		for(IndividualNeedsOverTimeChartBean chartBean:chartBeanList) {
			dataset.addValue(chartBean.getNeedsCountMap().get(clientNeedType), "Consumer", chartBean.getLabel());

		}

		JFreeChart chart = ChartFactory.createLineChart(
	            title,      // chart title
	            "OCANs",                      // x axis label
	            "# of "+WordUtils.capitalize(needType)+" Needs",                      // y axis label
	            dataset,                  // data
	            PlotOrientation.VERTICAL,
	            true,                     // include legend
	            true,                     // tooltips
	            false                     // urls
	        );

		chart.setBackgroundPaint(Color.white);
		CategoryPlot plot = chart.getCategoryPlot();

		plot.setBackgroundPaint(Color.lightGray);
        plot.setRangeGridlinePaint(Color.white);

        LineAndShapeRenderer renderer = new LineAndShapeRenderer();
        plot.setRenderer(renderer);

        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        rangeAxis.setAutoRangeIncludesZero(true);

        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setTickMarksVisible(true);

        return chart;
	}

	private void addToNeedCount_Staff(Map<String,Integer> needsCountMap, String question, Map<String,OcanStaffFormData> questionMap) {
		OcanStaffFormData dataValue = questionMap.get(question);
		if(dataValue != null) {
			if(dataValue.getAnswer().equals("0")) {
				needsCountMap.put("no",needsCountMap.get("no")+1);
			}
			else if(dataValue.getAnswer().equals("1")) {
				needsCountMap.put("met",needsCountMap.get("met")+1);
			}
			else if(dataValue.getAnswer().equals("2")) {
				needsCountMap.put("unmet",needsCountMap.get("unmet")+1);
			}
			else if(dataValue.getAnswer().equals("9")) {
				needsCountMap.put("unknown",needsCountMap.get("unknown")+1);
			} else {
				logger.warn("Invalid answer to question - " + question);
			}
		}
	}

	private void addToNeedCount_Client(Map<String,Integer> needsCountMap, String question, Map<String,OcanStaffFormData> questionMap) {
		OcanStaffFormData dataValue = questionMap.get(question);
		if(dataValue != null) {
			if(dataValue.getAnswer().equals("0")) {
				needsCountMap.put("clientNo",needsCountMap.get("clientNo")+1);
			}
			else if(dataValue.getAnswer().equals("1")) {
				needsCountMap.put("clientMet",needsCountMap.get("clientMet")+1);
			}
			else if(dataValue.getAnswer().equals("2")) {
				needsCountMap.put("clientUnmet",needsCountMap.get("clientUnmet")+1);
			}
			else if(dataValue.getAnswer().equals("9")) {
				needsCountMap.put("clientUnknown",needsCountMap.get("clientUnknown")+1);
			} else {
				logger.warn("Invalid answer to question - " + question);
			}
		}
	}

	public ActionForward generateNeedRatingOverTimeReport(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)  {
		LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);

		String clientId = request.getParameter("client");
		String[] assessments = request.getParameterValues("assessment");
		String[] domains = request.getParameterValues("domains");

		logger.debug("Generate Individual Needs Rating over Time Report");
		logger.debug("clientId="+clientId);
		for(int x=0;x<assessments.length;x++)
			logger.debug("assessment="+assessments[x]);
		for(int x=0;x<domains.length;x++)
			logger.debug("domain="+domains[x]);


		NeedRatingOverTimeReportGenerator reportGen = new NeedRatingOverTimeReportGenerator();

		reportGen.setConsumerName(demographicDao.getClientByDemographicNo(Integer.parseInt(clientId)).getFormattedName());
		reportGen.setStaffName(loggedInInfo.getLoggedInProvider().getFormattedName());
		reportGen.setReportDate(new Date());

		//get need rating for each domain selected, and add them up.
		List<OcanNeedRatingOverTimeSummaryOfNeedsBean> summaryOfNeedsBeanList = new ArrayList<OcanNeedRatingOverTimeSummaryOfNeedsBean>();
		List<OcanNeedRatingOverTimeSummaryOfNeedsBean> summaryOfNeedsNoDomainBeanList = new ArrayList<OcanNeedRatingOverTimeSummaryOfNeedsBean>();

		for(int x=0;x<assessments.length;x++) {
			Integer formId = Integer.valueOf(assessments[x].split(";")[0]);

			//get the latest form for the assessment
			OcanStaffForm ocanForm = ocanStaffFormDao.find(formId);
			//get data fields
			List<OcanStaffFormData> ocanFormData = ocanStaffFormDataDao.findByForm(ocanForm.getId());
			//convert to map
			Map<String,OcanStaffFormData> ocanFormDataMap = new HashMap<String,OcanStaffFormData>();
			for(OcanStaffFormData tmp:ocanFormData) {
				ocanFormDataMap.put(tmp.getQuestion(), tmp);
			}

			OcanNeedRatingOverTimeSummaryOfNeedsBean summaryOfNeedsBean  = new
					OcanNeedRatingOverTimeSummaryOfNeedsBean();
			summaryOfNeedsBean.setOcanName(assessments[x].split(";")[1]);
			if(summaryOfNeedsBean.getOcanName().indexOf("-")!=-1) {
				summaryOfNeedsBean.setOcanName(summaryOfNeedsBean.getOcanName().split("-")[0].trim());
			}
			summaryOfNeedsBean.setOcanDate(ocanForm.getCreated());

			for(int y=0;y<domains.length;y++) {
				String domainId = domains[y].split(";")[0];
				//String domainName = domains[y].split(";")[1];

				OcanStaffFormData staffAnswer = ocanFormDataMap.get(domainId+"_1");
				if(staffAnswer != null) {
					if(staffAnswer.getAnswer().equals("0")) {
						summaryOfNeedsBean.getStaffNeedMap().put("no", summaryOfNeedsBean.getStaffNeedMap().get("no")+1);
					}
					else if(staffAnswer.getAnswer().equals("1")) {
						summaryOfNeedsBean.getStaffNeedMap().put("met", summaryOfNeedsBean.getStaffNeedMap().get("met")+1);
					}
					else if(staffAnswer.getAnswer().equals("2")) {
						summaryOfNeedsBean.getStaffNeedMap().put("unmet", summaryOfNeedsBean.getStaffNeedMap().get("unmet")+1);
					}
					else if(staffAnswer.getAnswer().equals("9")) {
						summaryOfNeedsBean.getStaffNeedMap().put("unknown", summaryOfNeedsBean.getStaffNeedMap().get("unknown")+1);
					}
				}
				OcanStaffFormData clientAnswer = ocanFormDataMap.get("client_"+domainId+"_1");
				if(clientAnswer != null) {
					if(clientAnswer.getAnswer().equals("0")) {
						summaryOfNeedsBean.getConsumerNeedMap().put("no", summaryOfNeedsBean.getConsumerNeedMap().get("no")+1);
					}
					else if(clientAnswer.getAnswer().equals("1")) {
						summaryOfNeedsBean.getConsumerNeedMap().put("met", summaryOfNeedsBean.getConsumerNeedMap().get("met")+1);
					}
					else if(clientAnswer.getAnswer().equals("2")) {
						summaryOfNeedsBean.getConsumerNeedMap().put("unmet", summaryOfNeedsBean.getConsumerNeedMap().get("unmet")+1);
					}
					else if(clientAnswer.getAnswer().equals("9")) {
						summaryOfNeedsBean.getConsumerNeedMap().put("unknown", summaryOfNeedsBean.getConsumerNeedMap().get("unknown")+1);
					}
				}
			}
			summaryOfNeedsBeanList.add(summaryOfNeedsBean);


			OcanNeedRatingOverTimeSummaryOfNeedsBean summaryOfNeedsNoDomainBean  = new
					OcanNeedRatingOverTimeSummaryOfNeedsBean();
			summaryOfNeedsNoDomainBean.setOcanName(summaryOfNeedsBean.getOcanName());
			summaryOfNeedsNoDomainBean.setOcanDate(ocanForm.getCreated());

			for(int y=1;y<=24;y++) {
				OcanStaffFormData staffAnswer = ocanFormDataMap.get(y + "_1");
				if(staffAnswer != null) {
					if(staffAnswer.getAnswer().equals("0")) {
						summaryOfNeedsNoDomainBean.getStaffNeedMap().put("no", summaryOfNeedsNoDomainBean.getStaffNeedMap().get("no")+1);
					}
					else if(staffAnswer.getAnswer().equals("1")) {
						summaryOfNeedsNoDomainBean.getStaffNeedMap().put("met", summaryOfNeedsNoDomainBean.getStaffNeedMap().get("met")+1);
					}
					else if(staffAnswer.getAnswer().equals("2")) {
						summaryOfNeedsNoDomainBean.getStaffNeedMap().put("unmet", summaryOfNeedsNoDomainBean.getStaffNeedMap().get("unmet")+1);
					}
					else if(staffAnswer.getAnswer().equals("9")) {
						summaryOfNeedsNoDomainBean.getStaffNeedMap().put("unknown", summaryOfNeedsNoDomainBean.getStaffNeedMap().get("unknown")+1);
					}
				}
				OcanStaffFormData clientAnswer = ocanFormDataMap.get("client_"+y+"_1");
				if(clientAnswer != null) {
					if(clientAnswer.getAnswer().equals("0")) {
						summaryOfNeedsNoDomainBean.getConsumerNeedMap().put("no", summaryOfNeedsNoDomainBean.getConsumerNeedMap().get("no")+1);
					}
					else if(clientAnswer.getAnswer().equals("1")) {
						summaryOfNeedsNoDomainBean.getConsumerNeedMap().put("met", summaryOfNeedsNoDomainBean.getConsumerNeedMap().get("met")+1);
					}
					else if(clientAnswer.getAnswer().equals("2")) {
						summaryOfNeedsNoDomainBean.getConsumerNeedMap().put("unmet", summaryOfNeedsNoDomainBean.getConsumerNeedMap().get("unmet")+1);
					}
					else if(clientAnswer.getAnswer().equals("9")) {
						summaryOfNeedsNoDomainBean.getConsumerNeedMap().put("unknown", summaryOfNeedsNoDomainBean.getConsumerNeedMap().get("unknown")+1);
					}
				}
			}
			summaryOfNeedsNoDomainBeanList.add(summaryOfNeedsNoDomainBean);

		}

		//get the current OCAN
		String currentOcanFormId = assessments[assessments.length-1].split(";")[0];
		OcanStaffForm ocanForm = ocanStaffFormDao.find(Integer.parseInt(currentOcanFormId));
		List<OcanStaffFormData> ocanFormData = ocanStaffFormDataDao.findByForm(ocanForm.getId());
		Map<String,OcanStaffFormData> ocanFormDataMap = new HashMap<String,OcanStaffFormData>();
		for(OcanStaffFormData tmp:ocanFormData) {
			ocanFormDataMap.put(tmp.getQuestion(), tmp);
		}
		//now we want to make the properly ordered list (0-no,1-met,2-unmet,9-unknown)
		//need a triplet  - domain, consumer need value, staff need value
		List<OcanDomainConsumerStaffBean> test = new ArrayList<OcanDomainConsumerStaffBean>();
		for(int x=1;x<=24;x++) {
			OcanStaffFormData staffNeedFormData = ocanFormDataMap.get(x+"_1");
			OcanStaffFormData consumerNeedFormData = ocanFormDataMap.get("client_"+x+"_1");
			String staffNeed=null;
			String consumerNeed=null;
			if(staffNeedFormData!=null) {
				staffNeed = staffNeedFormData.getAnswer();
			}
			if(consumerNeedFormData!=null) {
				consumerNeed = consumerNeedFormData.getAnswer();
			}
			OcanDomainConsumerStaffBean odcsb = new OcanDomainConsumerStaffBean(x,consumerNeed,staffNeed);
			test.add(odcsb);
		}

		List<OcanDomainConsumerStaffBean> orderedDomainList;
		orderedDomainList = test;
		Collections.sort(orderedDomainList,OcanDomainConsumerStaffBean.getNeedsComparator1());
		Collections.reverse(orderedDomainList);

		//filter out domains we are not interested in
		List<OcanDomainConsumerStaffBean> filteredDomainList = new ArrayList<OcanDomainConsumerStaffBean>();
		List<String> domainList = new ArrayList<String>();
		for(int x=0;x<domains.length;x++) {
			domainList.add(domains[x].split(";")[0]);
		}
		for(OcanDomainConsumerStaffBean b:orderedDomainList) {
			if(domainList.contains(String.valueOf(b.getDomainId()))) {
				filteredDomainList.add(b);
			}
		}

		List<String> domainNames= new ArrayList<String>();
		for(OcanDomainConsumerStaffBean t:filteredDomainList) {
			domainNames.add(getDomainNameFromId(domains,t.getDomainId()));
		}

		//build list of beans
		List<OcanNeedRatingOverTimeNeedBreakdownBean> breakdownBeanList =
				new ArrayList<OcanNeedRatingOverTimeNeedBreakdownBean>();

		for(int x=0;x<assessments.length;x++) {
			int formId = Integer.parseInt(assessments[x].split(";")[0]);
			//get the latest form for the assessment
			ocanForm = ocanStaffFormDao.find(formId);
			//get data fields
			ocanFormData = ocanStaffFormDataDao.findByForm(ocanForm.getId());
			//convert to map
			ocanFormDataMap = new HashMap<String,OcanStaffFormData>();
			for(OcanStaffFormData tmp:ocanFormData) {
				ocanFormDataMap.put(tmp.getQuestion(), tmp);
			}

			OcanNeedRatingOverTimeNeedBreakdownBean breakdownBean =
					new OcanNeedRatingOverTimeNeedBreakdownBean();
			String ocanName = assessments[x].split(";")[1];
			ocanName=(ocanName.indexOf("-")!=-1?ocanName.split("-")[0].trim():ocanName);
			breakdownBean.setOcanName(ocanName);
			breakdownBean.setOcanDate(ocanForm.getCreated());
			for(OcanDomainConsumerStaffBean d:filteredDomainList) {
				OcanStaffFormData staffValue = ocanFormDataMap.get(d.getDomainId()+"_1");
				OcanStaffFormData consumerValue = ocanFormDataMap.get("client_" + d.getDomainId()+"_1");
				String staffNeed="", consumerNeed="";
				if(staffValue!=null) {
					staffNeed = staffValue.getAnswer();
				}
				if(consumerValue != null) {
					consumerNeed = consumerValue.getAnswer();
				}
				OcanConsumerStaffNeedBean needBean = new OcanConsumerStaffNeedBean();
				needBean.setConsumerNeed(convertNeedToWord(consumerNeed));
				needBean.setStaffNeed(convertNeedToWord(staffNeed));
				breakdownBean.getNeeds().add(needBean);
			}
			breakdownBeanList.add(breakdownBean);
		}


		Collections.reverse(summaryOfNeedsBeanList);
		reportGen.setSummaryOfNeedsBeanList(summaryOfNeedsBeanList);
		Collections.reverse(summaryOfNeedsNoDomainBeanList);
		reportGen.setSummaryOfNeedsNoDomainBeanList(summaryOfNeedsNoDomainBeanList);
		reportGen.setDomains(domainNames);
		Collections.reverse(breakdownBeanList);
		reportGen.setNeedBreakdownListByOCAN(breakdownBeanList);


		try {
			response.setContentType("application/pdf");
			response.setHeader("content-disposition", "inline; filename=\"NeedRatingOverTime.pdf\"");
			reportGen.generateReport(response.getOutputStream());
		}catch(Exception e) {
			logger.error("error",e);
		}


		return null;
	}

	private String getDomainNameFromId(String[] domains, int id) {
		for(int x=0;x<domains.length;x++) {
			int tmp = Integer.parseInt(domains[x].split(";")[0]);
			if(tmp == id)
				return domains[x].split(";")[1];
		}
		return "";
	}

	private String convertNeedToWord(String key) {
		if(key == null)
			return "";
		if(key.equals("2")) {
			return "Unmet Need";
		}
		if(key.equals("1")) {
			return "Met Need";
		}
		if(key.equals("0")) {
			return "No Need";
		}
		if(key.equals("9")) {
			return "Unknown";
		}
		return "";
	}

	public ActionForward generateSummaryOfActionsAndCommentsReport(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)  {
		LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);

		String clientId = request.getParameter("client");
		String[] assessments = request.getParameterValues("assessment");
		String[] domains = request.getParameterValues("domains");
		String comments = request.getParameter("comments");

		logger.debug("Generate Summary Of Actions and Comments Report");
		logger.debug("clientId="+clientId);
		for(int x=0;x<assessments.length;x++)
			logger.debug("assessment="+assessments[x]);
		for(int x=0;x<domains.length;x++)
			logger.debug("domain="+domains[x]);
		logger.debug("comments="+comments);

		SummaryOfActionsAndCommentsReportBean reportBean = new SummaryOfActionsAndCommentsReportBean();

		//loop through the selected domains, figure out which category it goes into (using
		//current OCAN). Create a SummaryOfActionsAndCommentsDomainBean, and put it into the
		//right need category of the report bean

		String currentOcanFormId = assessments[assessments.length-1].split(";")[0];
		OcanStaffForm ocanForm = ocanStaffFormDao.find(Integer.parseInt(currentOcanFormId));
		List<OcanStaffFormData> ocanFormData = ocanStaffFormDataDao.findByForm(ocanForm.getId());
		Map<String,OcanStaffFormData> ocanFormDataMap = new HashMap<String,OcanStaffFormData>();
		for(OcanStaffFormData tmp:ocanFormData) {
			ocanFormDataMap.put(tmp.getQuestion(), tmp);
		}

		for(int x=0;x<domains.length;x++) {
			SummaryOfActionsAndCommentsDomainBean domainBean = new SummaryOfActionsAndCommentsDomainBean();
			String domainId = domains[x].split(";")[0];
			String domainName = domains[x].split(";")[1];

			OcanStaffFormData staffFormData = ocanFormDataMap.get(domainId+"_1");
			OcanStaffFormData consumerFormData = ocanFormDataMap.get("client_"+domainId+"_1");
			String staffValue = (staffFormData==null)?null:staffFormData.getAnswer();
			String consumerValue = (consumerFormData==null)?null:consumerFormData.getAnswer();
			String needCategory = this.determineBestNeed(staffValue, consumerValue);
			domainBean.setDomainId(domainId);
			domainBean.setDomainName(domainName);
			if(needCategory.equals("unmet")) {
				reportBean.getUnmetNeeds().add(domainBean);
			} else if(needCategory.equals("met")) {
				reportBean.getMetNeeds().add(domainBean);
			} else if(needCategory.equals("no")) {
				reportBean.getNoNeeds().add(domainBean);
			} else {
				reportBean.getUnknown().add(domainBean);
			}
		}

		//we need to pre-load all the OCANs..or else we will be constantly getting it from db.
		List<CachedOcanFormAndData> ocanFormAndDataList = new ArrayList<CachedOcanFormAndData>();
		for(int x=0;x<assessments.length;x++) {
			Integer formId = Integer.valueOf(assessments[x].split(";")[0]);

			//get the latest form for the assessment
			ocanForm = ocanStaffFormDao.find(formId);
			//get data fields
			ocanFormData = ocanStaffFormDataDao.findByForm(ocanForm.getId());
			//convert to map
			ocanFormDataMap = new HashMap<String,OcanStaffFormData>();
			for(OcanStaffFormData tmp:ocanFormData) {
				ocanFormDataMap.put(tmp.getQuestion(), tmp);
			}
			ocanFormAndDataList.add(0,new CachedOcanFormAndData(ocanForm, ocanFormDataMap));
		}
		//Collections.reverse(ocanFormAndDataList);

		//we now know which domain goes in which category. now we will go through each domain
		//bean, and populate with OCAN data.
		boolean includeComments = (comments != null && comments.equals("comments"))?true:false;
		for(SummaryOfActionsAndCommentsDomainBean domainBean: reportBean.getUnmetNeeds()) {
			populateOcansIntoDomain(ocanFormAndDataList,domainBean,domainBean.getOcanBeans(),includeComments,assessments);
		}
		for(SummaryOfActionsAndCommentsDomainBean domainBean: reportBean.getMetNeeds()) {
			populateOcansIntoDomain(ocanFormAndDataList,domainBean,domainBean.getOcanBeans(),includeComments,assessments);
		}
		for(SummaryOfActionsAndCommentsDomainBean domainBean: reportBean.getNoNeeds()) {
			populateOcansIntoDomain(ocanFormAndDataList,domainBean,domainBean.getOcanBeans(),includeComments,assessments);
		}
		for(SummaryOfActionsAndCommentsDomainBean domainBean: reportBean.getUnknown()) {
			populateOcansIntoDomain(ocanFormAndDataList,domainBean,domainBean.getOcanBeans(),includeComments,assessments);
		}

		SummaryOfActionsAndCommentsReportGenerator reportGen = new SummaryOfActionsAndCommentsReportGenerator();
		reportGen.setReportBean(reportBean);
		reportGen.setConsumerName(demographicDao.getClientByDemographicNo(Integer.parseInt(clientId)).getFormattedName());
		reportGen.setStaffName(loggedInInfo.getLoggedInProvider().getFormattedName());
		reportGen.setReportDate(new Date());
		reportGen.setIncludeComments(includeComments);

		try {
			response.setContentType("application/pdf");
			response.setHeader("content-disposition", "inline; filename=\"SummaryOfActionsAndComments.pdf\"");
			reportGen.generateReport(response.getOutputStream());
		}catch(Exception e) {
			logger.error("error",e);
		}

		return null;
	}

	/*
	 * Check to see if there's  actions/comments for this domain.
	 * client_1_comments
	 * 3_comments
	 * 3_actions
	 * 3_review_date
	 * 1_by_whom
	 */
	private void populateOcansIntoDomain(List<CachedOcanFormAndData> ocanFormAndDataList, SummaryOfActionsAndCommentsDomainBean domainBean, List<SummaryOfActionsAndCommentsOCANBean> ocanBeans,boolean includeComments, String[] assessments) {
		//SummaryOfActionsAndCommentsOCANBean
		for(CachedOcanFormAndData ocan:ocanFormAndDataList) {
			OcanStaffFormData actionsFormData = ocan.getOcanFormDataMap().get(domainBean.getDomainId() + "_actions");
			OcanStaffFormData staffCommentsFormData = ocan.getOcanFormDataMap().get(domainBean.getDomainId() + "_comments");
			OcanStaffFormData consumerCommentsFormData = ocan.getOcanFormDataMap().get("client_" + domainBean.getDomainId() + "_comments");
			OcanStaffFormData byWhomFormData = ocan.getOcanFormDataMap().get(domainBean.getDomainId() + "_by_whom");
			OcanStaffFormData consumerNeedRatingFormData = ocan.getOcanFormDataMap().get("client_" + domainBean.getDomainId() + "_1");
			OcanStaffFormData reviewDateFormData = ocan.getOcanFormDataMap().get(domainBean.getDomainId() + "_review_date");
			OcanStaffFormData staffNeedRatingFormData = ocan.getOcanFormDataMap().get(domainBean.getDomainId() + "_1");


			String actions = (actionsFormData!=null)?actionsFormData.getAnswer():null;
			String staffComments = (staffCommentsFormData!=null)?staffCommentsFormData.getAnswer():null;
			String consumerComments = (consumerCommentsFormData!=null)?consumerCommentsFormData.getAnswer():null;
			String byWhom = (byWhomFormData!=null)?byWhomFormData.getAnswer():null;
			String consumerNeedRating = (consumerNeedRatingFormData!=null)?consumerNeedRatingFormData.getAnswer():null;
			String reviewDate = (reviewDateFormData!=null)?reviewDateFormData.getAnswer():null;
			String staffNeedRating = (staffNeedRatingFormData!=null)?staffNeedRatingFormData.getAnswer():null;

			if(actions == null && staffComments == null && consumerComments == null) {
				continue;
			}
			if(!includeComments && actions == null) {
				continue;
			}
			SummaryOfActionsAndCommentsOCANBean oBean = new SummaryOfActionsAndCommentsOCANBean();
			oBean.setFormId(ocan.getOcanForm().getId());
			oBean.setAssessmentId(ocan.getOcanForm().getAssessmentId());
			oBean.setOcanDate(ocan.getOcanForm().getCreated());
			oBean.setOcanName(getOcanName(String.valueOf(ocan.getOcanForm().getId()),assessments));
			oBean.setActions(actions);
			oBean.setByWhom(byWhom);
			oBean.setConsumerComments(consumerComments);
			oBean.setConsumerNeedRating(consumerNeedRating);
			oBean.setReviewDate(reviewDate);
			oBean.setStaffComments(staffComments);
			oBean.setStaffNeedRating(staffNeedRating);

			domainBean.getOcanBeans().add(oBean);
		}
	}

	private String getOcanName(String formId, String[] assessments) {
		for(String assessment:assessments) {
			String tmp = assessment.split(";")[0];
			if(tmp.equals(formId)) {
				String t=   assessment.split(";")[1];
				if(t.indexOf("-")!=-1) {
					t = t.substring(0,t.indexOf("-")).trim();
				}
				return t;
			}
		}
		return "OCAN";
	}

	private String determineBestNeed(String need1, String need2) {
		if(need1 !=null && need1.equals("2")) {	return "unmet";}
		if(need2 !=null && need2.equals("2")) {	return "unmet";}
		if(need1 !=null && need1.equals("1")) {	return "met";}
		if(need2 !=null && need2.equals("1")) {	return "met";}
		if(need1 !=null && need1.equals("0")) {	return "no";}
		if(need2 !=null && need2.equals("0")) {	return "no";}
		if(need1 !=null && need1.equals("9")) {	return "unknown";}
		if(need2 !=null && need2.equals("9")) {	return "unknown";}
		return "unknown";
	}

	class IndividualNeedsOverTimeChartBean {

		private Map<String, Integer> needsCountMap;
		private OcanStaffForm form;
		private String label;

		public IndividualNeedsOverTimeChartBean() {

		}

		public IndividualNeedsOverTimeChartBean(OcanStaffForm form, Map<String, Integer> needsCountMap, String label) {
			this.needsCountMap = needsCountMap;
			this.form = form;
			this.label = label;
		}

		public Map<String, Integer> getNeedsCountMap() {
			return needsCountMap;
		}
		public void setNeedsCountMap(Map<String, Integer> needsCountMap) {
			this.needsCountMap = needsCountMap;
		}
		public OcanStaffForm getForm() {
			return form;
		}
		public void setForm(OcanStaffForm form) {
			this.form = form;
		}

		public String getLabel() {
			return label;
		}

		public void setLabel(String label) {
			this.label = label;
		}


	}
}

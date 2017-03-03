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
package org.oscarehr.integration.dashboard;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.oscarehr.common.dao.ClinicDAO;
import org.oscarehr.common.model.IndicatorTemplate;
import org.oscarehr.common.model.Provider;
import org.oscarehr.dashboard.display.beans.IndicatorBean;
import org.oscarehr.dashboard.handler.IndicatorTemplateHandler;
import org.oscarehr.integration.dashboard.model.Clinic;
import org.oscarehr.integration.dashboard.model.MetricData;
import org.oscarehr.integration.dashboard.model.MetricOwner;
import org.oscarehr.integration.dashboard.model.MetricSet;
import org.oscarehr.integration.dashboard.model.Name;
import org.oscarehr.managers.DashboardManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import oscar.OscarProperties;

public class OutcomesDashboardUtils {

	private static Logger logger = MiscUtils.getLogger();
	
	private static DashboardManager dashboardManager = SpringUtils.getBean(DashboardManager.class);
	private static ClinicDAO clinicDao = SpringUtils.getBean(ClinicDAO.class);

	
	public static void sendProviderIndicatorData(LoggedInInfo x, Provider provider, IndicatorTemplate indicatorTemplate) throws Exception {
		SimpleDateFormat formatter = new SimpleDateFormat("MMM d, yyyy hh:mm:ss aaa");

		//logger.info("indicatorTemplate="+indicatorTemplate.toString());
		IndicatorTemplateHandler ith = new IndicatorTemplateHandler(x, indicatorTemplate.getTemplate().getBytes());

		//is there a mapping? read the XML template file for the sharedMetricSetName and sharedMappings
		String metricSetName = indicatorTemplate.getName();
		NodeList nl = ith.getIndicatorTemplateDocument().getElementsByTagName("sharedMetricSetName");
		if (nl != null && nl.getLength() > 0) {
			metricSetName = nl.item(0).getTextContent();
		}

		String metricDataId = "Result";
		nl = ith.getIndicatorTemplateDocument().getElementsByTagName("sharedMetricDataId");
		if (nl != null && nl.getLength() > 0) {
			metricDataId = nl.item(0).getTextContent();
		}

		Map<String, String> mappings = new HashMap<String, String>();

		nl = ith.getIndicatorTemplateDocument().getElementsByTagName("sharedMapping");
		for (int i = 0; i < nl.getLength(); i++) {
			Node n = nl.item(i);
			String fromLabel = n.getAttributes().getNamedItem("fromLabel").getNodeValue();
			String toLabel = n.getAttributes().getNamedItem("toLabel").getNodeValue();
			mappings.put(fromLabel, toLabel);
		}

		
		//run indicator
		IndicatorBean indicatorBean = dashboardManager.getIndicatorPanelForProvider(x, provider.getProviderNo(), indicatorTemplate.getId());

		//map results into Metric Set
		org.oscarehr.common.model.Clinic oClinic = clinicDao.getClinic();
		Clinic clinic = new Clinic();
		clinic.setApplication("oscar");
		
		String clinicIdentifier = OscarProperties.getInstance().getProperty("shared_outcomes_dashboard_clinic_id");
		
		if(clinicIdentifier == null || clinicIdentifier.length() == 0 || clinicIdentifier.length() > 42 ) {
			clinicIdentifier = oClinic.getClinicName();
		}
		
		clinic.setIdentifier(clinicIdentifier);
		
		clinic.setName(oClinic.getClinicName());

		MetricOwner metricOwner = new MetricOwner();
		metricOwner.setCity("");
		metricOwner.setFirstName(provider.getFirstName());
		metricOwner.setLastName(provider.getLastName());
		metricOwner.setPostalCode("");
		metricOwner.setProvince("ON");
		metricOwner.setUniqueIdentifier(null);
		metricOwner.setUsername(provider.getProviderNo());

		MetricSet metricSet = new MetricSet();
		/*
		metricSet.setName("OSCAR Metric Test");
		metricSet.setDate(formatter.format(new Date()));
		metricSet.getMetricData().add(createMetricData("Status","Up to date",200));
		metricSet.getMetricData().add(createMetricData("Status","Overdue",200));
		metricSet.getMetricData().add(createMetricData("Status","Not documented",400));
		*/

		metricSet.setName(metricSetName);
		metricSet.setDate(formatter.format(new Date()));

		JSONObject plots = JSONObject.fromObject(indicatorBean.getOriginalJsonPlots());

		JSONArray arr = plots.getJSONArray("results");

		for (int i = 0; i < arr.size(); i++) {
			JSONObject obj = (JSONObject) arr.get(i);
			String name = (String) obj.names().get(0);
			int value = obj.getInt(name);

			//map name            		
			if (mappings.get(name) != null) {
				name = mappings.get(name);
			}

			metricSet.getMetricData().add(createMetricData(metricDataId, name, value));
		}

		JSONObject data = new JSONObject();
		data.put("clinic", clinic);
		data.put("metricOwner", metricOwner);
		data.put("metricSet", metricSet);

		List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
		urlParameters.add(new BasicNameValuePair("version", "1.1"));
		urlParameters.add(new BasicNameValuePair("data", data.toString()));

		//send to shared dashboard

		HttpClient httpClient = new DefaultHttpClient();
		HttpPost postMethod = new HttpPost(OscarProperties.getInstance().getProperty("shared_outcomes_dashboard_send_url"));

		postMethod.setEntity(new UrlEncodedFormEntity(urlParameters));

		logger.info(data);

		postMethod.setHeader("Content-Type", "application/x-www-form-urlencoded");
		
		HttpResponse response = httpClient.execute(postMethod);
		
		logger.info(response.getStatusLine().getStatusCode());
		if( response.getStatusLine().getStatusCode() == HttpStatus.SC_OK ) {
			HttpEntity httpEntity = response.getEntity();
			String content = EntityUtils.toString(httpEntity);
			logger.info(content);
			logger.info("sent");
		}
	}
	
	protected static MetricData createMetricData(String identifier, String label, int value) {
		MetricData md = new MetricData();
		Name n = new Name();
		n.setIdentifier(identifier);
		n.setLabel(label);
		md.getName().add(n);
		md.setValue(value);

		return md;
	}

	
}

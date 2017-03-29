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

package org.oscarehr.billing.Clinicaid.util;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;

import java.io.BufferedReader;
import java.io.OutputStreamWriter;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
//import java.util.List;


import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.oscarehr.util.LoggedInInfo;

import oscar.util.UtilMisc;
import oscar.oscarBilling.data.BillingFormData;
import oscar.OscarProperties;

//import org.oscarehr.casemgmt.dao.CaseManagementIssueDAO;
//import org.oscarehr.casemgmt.model.CaseManagementIssue;

public class ClinicaidCommunication {

	public ClinicaidCommunication()
	{
	}

	public String buildClinicaidURL(HttpServletRequest request, String action) 
	{
		OscarProperties oscarProps = OscarProperties.getInstance();
		String clinicaid_domain = oscarProps.getProperty("clinicaid_domain");
		String clinicaid_link = "";
		String nonce = "";

		HttpSession session = request.getSession();
		String user_no = (String)session.getAttribute("user");
		String user_first_name = (String)session.getAttribute("userfirstname");
		String user_last_name = (String)session.getAttribute("userlastname");

		try
		{
			nonce = this.getNonce(user_no, user_first_name, user_last_name);
		}
		catch (IOException E)
		{
			return null;
		}

		// If creating a new invoice in Clinicaid
		if(action.equals("create_invoice"))
		{
			BillingFormData billform = new BillingFormData();
			String service_recipient_oscar_number = request.getParameter("demographic_no");
			String appointment_provider_no = request.getParameter("apptProvider_no");

			oscar.oscarDemographic.data.DemographicData demoData =
				new oscar.oscarDemographic.data.DemographicData();
			org.oscarehr.common.model.Demographic demo =
				demoData.getDemographic(LoggedInInfo.getLoggedInInfoFromSession(request), service_recipient_oscar_number);

			String referral_doc = demo.getFamilyDoctor();
			if (referral_doc == null)
			{
				referral_doc = "";
			}

			// Get latest diagnostic code
			String dx_codes = "";
			/*
			CaseManagementIssueDAO caseManagementIssueDAO = new CaseManagementIssueDAO();
			List<CaseManagementIssue> issues = 
				caseManagementIssueDAO.getIssuesByDemographicOrderActive(
						2, false);

			for (CaseManagementIssue issue : issues)
			{
				dx_codes = dx_codes + "|" + issue.getIssue_id();
			}
			*/

			String referral_no = demo.getFamilyDoctorNumber();
			String referral_first_name = demo.getFamilyDoctorFirstName();
			String referral_last_name = demo.getFamilyDoctorLastName();

			String provider_no = "";
			String provider_uli = "";
			String provider_first_name = "";
			String provider_last_name = "";

			// If this invoice was created from an appointment, use the provider
			// who performed the appointment as the billing provider
			if(appointment_provider_no != null)
			{
				try
				{
					provider_no = appointment_provider_no;

					// Make sure the provider_no is a valid integer. 
					// ProviderData will throw an un-catchable exception if it 
					// is not
					Integer test = Integer.parseInt(provider_no);

					provider_uli = billform.getPracNo(provider_no);

					oscar.oscarProvider.data.ProviderData providerData =
						new oscar.oscarProvider.data.ProviderData(provider_no);

					provider_first_name = providerData.getFirst_name();
					provider_last_name = providerData.getLast_name();
					provider_no = providerData.getProviderNo();
				}
				catch (Exception E)
				{
					provider_no = null;
				}

			}

			// If no appointment provider exists, try to get the patients 
			// provider to use for billing
			if(provider_no == null || provider_no == "")
			{
				try
				{
					provider_no = demo.getProviderNo();

					// Make sure the provider_no is a valid integer. If a patient doesn't
					// have a provider assigned to them, the demo returns an invalid provider_no
					// which then causes ProviderData to throw an un-catchable exception 
					Integer test = Integer.parseInt(provider_no);

					provider_uli = billform.getPracNo(provider_no);
					oscar.oscarProvider.data.ProviderData providerData =
						new oscar.oscarProvider.data.ProviderData(provider_no);

					provider_first_name = providerData.getFirst_name();
					provider_last_name = providerData.getLast_name();
					provider_no = providerData.getProviderNo();
				}
				catch (Exception E)
				{
					provider_no = null;
				}


			}

			// If no patient or appointment provider was found, use the 
			// current user for billing
			if(provider_no == null || provider_no == "")
			{
				try
				{
					provider_no = user_no;
					provider_first_name = user_first_name;
					provider_last_name = user_last_name;
					provider_uli = billform.getPracNo(provider_no);
				}
				catch (Exception E)
				{
					provider_uli = "";
				}

			}
			if(provider_uli == null)
			{
				provider_uli = "";
			}

			String encoded_provider_no = this.urlEncode(provider_no);
			String encoded_provider_first_name =
				this.urlEncode(provider_first_name);
			String encoded_provider_last_name =
				this.urlEncode(provider_last_name);
			String encoded_provider_uli = this.urlEncode(provider_uli);
			String encoded_diagnostic_code = this.urlEncode(dx_codes);
			String encoded_appointment_number = request.getParameter("appointment_no");
			String encoded_patient_dob =
				this.urlEncode(demo.getYearOfBirth() + "-" +
						demo.getMonthOfBirth() + "-" + demo.getDateOfBirth());
			String encoded_patient_gender = this.urlEncode(demo.getSex());
			String encoded_first_name =
				this.urlEncode(UtilMisc.toUpperLowerCase(demo.getFirstName()));
			String encoded_last_name =
				this.urlEncode(UtilMisc.toUpperLowerCase(demo.getLastName()));
			String encoded_province =
				this.urlEncode(StringUtils.upperCase(demo.getProvince()));
			String encoded_hc_province =
				this.urlEncode(StringUtils.upperCase(demo.getHcType()));
			String encoded_city = this.urlEncode(demo.getCity());
			String encoded_address = this.urlEncode(demo.getAddress());
			String encoded_postal_code = this.urlEncode(demo.getPostal());
			String encoded_service_recipient_oscar_number =
				this.urlEncode(service_recipient_oscar_number);
			String encoded_status = this.urlEncode(demo.getPatientStatus());
			String encoded_hin = this.urlEncode(demo.getHin());
			String encoded_age = this.urlEncode(demo.getAge());
			String encoded_referral_number = this.urlEncode(referral_no);
			String encoded_referral_first_name =
				this.urlEncode(referral_first_name);
			String encoded_referral_last_name =
				this.urlEncode(referral_last_name);
			String encoded_appointment_start_time =
				this.urlEncode(request.getParameter("start_time"));
			String encoded_chart_no =
				  this.urlEncode(request.getParameter("chart_no"));
			String encoded_service_start_date =
				this.urlEncode(request.getParameter("appointment_date"));
			clinicaid_link =
				clinicaid_domain + "/?nonce=" + nonce +
				"#/invoice/add?service_recipient_first_name=" + encoded_first_name +
				"&service_recipient_uli=" + encoded_hin +
				"&service_recipient_last_name=" + encoded_last_name +
				"&service_recipient_oscar_number=" +
				encoded_service_recipient_oscar_number +
				"&service_recipient_status=" + encoded_status +
				"&service_recipient_age=" + encoded_age +
				"&service_recipient_gender=" + encoded_patient_gender +
				"&service_provider_oscar_number=" + encoded_provider_no +
				"&service_provider_first_name=" + encoded_provider_first_name +
				"&service_provider_last_name=" + encoded_provider_last_name +
				"&service_provider_uli=" + encoded_provider_uli +
				"&service_start_date=" + encoded_service_start_date +
				"&province=" + encoded_province +
				"&hc_province=" + encoded_hc_province +
				"&city=" + encoded_city +
				"&postal_code=" + encoded_postal_code +
				"&chart_number=" + encoded_chart_no +
				"&service_recipient_birth_date=" + encoded_patient_dob +
				"&appointment_number=" + encoded_appointment_number +
				"&appointment_start_time=" + encoded_appointment_start_time +
				"&referral_number=" + encoded_referral_number +
				"&referral_first_name=" + encoded_referral_first_name +
				"&referral_last_name=" + encoded_referral_last_name +
				"&diagnostic_code=" + encoded_diagnostic_code +
				"&address=" + encoded_address;
		}
		else if(action.equals("invoice_reports"))
		{
			clinicaid_link = clinicaid_domain + "/?nonce=" + nonce + "#/reports";
		}
		return clinicaid_link;
	}

	/**
	 * Authenticates with Clinicaid and returns a security nonce to be used
	 * for all communications in this session
	 *
	 * @param identifier id of the logged in user 
	 * @param first_name first name of the logged in user
	 * @param last_name last name of the logged in user
	 * @return Demographic
	 */
	private String getNonce(
			String identifier,
			String first_name,
			String last_name) throws IOException
	{
		OscarProperties oscarProps = OscarProperties.getInstance();

		// Create the URL
		final String clinicaid_domain = oscarProps.getProperty("clinicaid_domain");
		final String instance_name = oscarProps.getProperty("clinicaid_instance_name");
		final String api_key = oscarProps.getProperty("clinicaid_api_key");
		
		String url_string = clinicaid_domain + "/auth/pushed_login/";
		URL nonce_url;
		try
		{
			nonce_url = new URL(url_string);
		}
		catch(Exception e)
		{
			return e.getMessage();
		}
		
		String post_data = 
			"{\"identifier\":\"" + identifier + 
			"\",\"first_name\":\"" + first_name + 
			"\",\"last_name\":\"" + last_name + "\"}";

		String userpass = instance_name + ":" + api_key;
		String userpass_base64_string = 
			new String(new Base64().encode(userpass.getBytes()));
		userpass_base64_string = 
			userpass_base64_string.replaceAll("\n", "").replaceAll("\r", "");
		String basicauth = "Basic " + userpass_base64_string;

		String output = "";
		HttpURLConnection uc = null;
		OutputStreamWriter wr = null;
		BufferedReader in = null;
		try
		{
			uc = (HttpURLConnection) nonce_url.openConnection();
			uc.setRequestMethod("POST");

			// Auth
			uc.setRequestProperty("Authorization", basicauth);

			// POST data
			uc.setDoOutput(true);
			wr = new OutputStreamWriter(uc.getOutputStream());
			wr.write(post_data);
			wr.flush();

			// Read the result
			in = new BufferedReader(new InputStreamReader(uc.getInputStream()));

			String inputLine;
			while ((inputLine = in.readLine()) != null) 
			{
				output += inputLine;
			}
			in.close();

			Pattern p = Pattern.compile(".*\"nonce\":\"([a-zA-Z0-9-]*)\".*");
			Matcher m = p.matcher(output);

			if(!m.matches())
			{
				// TODO: handle error
				return "didnt match " + output;
			}

			return m.group(1);
		}
		catch(Exception e)
		{
			return output + e.toString();
		}
		finally
		{
			if(uc != null) 
			{
				uc.disconnect();
			}
			if(wr != null) 
			{
				wr.close();
			}
			if(in != null) 
			{
				in.close();
			}
		}
	}

	private String urlEncode(String inValue)
	{
		String outValue;

		if (inValue == null)
		{
			outValue = "";
		}
		else
		{
			try
			{
				outValue = URLEncoder.encode(inValue, "UTF-8");
			}
			catch (UnsupportedEncodingException E)
			{
				outValue = "";
			}
		}

		return outValue;
	}
}

/**
 * Copyright (c) 2008-2012 Indivica Inc.
 *
 * This software is made available under the terms of the
 * GNU General Public License, Version 2, 1991 (GPLv2).
 * License details are available via "indivica.ca/gplv2"
 * and "gnu.org/licenses/gpl-2.0.html".
 */
package oscar.eform.actions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.SpringUtils;

import oscar.eform.EFormLoader;
import oscar.eform.EFormUtil;
import oscar.eform.data.DatabaseAP;

public final class FetchUpdatedDataAction extends DispatchAction {
	
	private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);
	
	public ActionForward ajaxFetchData(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException {
		String demographic = request.getParameter("demographic");
		String provider = request.getParameter("provider");
		String uuid = request.getParameter("uuid");
		String fields = request.getParameter("fields");
		
		if(!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_eform", "r", null)) {
			throw new SecurityException("missing required security object (_eform)");
		}
		

		HashMap<String, String> outValues = new HashMap<String, String>();

		if (fields != null) {
			List<String> oscarUpdateFields = Arrays.asList(fields.split(","));

			for (String field : oscarUpdateFields) {
				DatabaseAP ap = EFormLoader.getAP(field);
				if (ap != null) {
					String sql = ap.getApSQL();
					String output = ap.getApOutput();
					//replace ${demographic} with demogrpahicNo
					if (sql != null) {
						sql = DatabaseAP.parserReplace("demographic", demographic, sql);
						sql = DatabaseAP.parserReplace("provider", provider, sql);
						sql = DatabaseAP.parserReplace("uuid", uuid, sql);
						//sql = replaceAllFields(sql);
						log.debug("SQL----" + sql);
						ArrayList<String> names = DatabaseAP.parserGetNames(output); //a list of ${apName} --> apName
						sql = DatabaseAP.parserClean(sql);  //replaces all other ${apName} expressions with 'apName'
						
						if (ap.isJsonOutput()) {
							JSONArray values = EFormUtil.getJsonValues(names, sql);
							output = values.toString(); //in case of JsonOutput, return the whole JSONArray and let the javascript deal with it
						}
						else {
							ArrayList<String> values = EFormUtil.getValues(names, sql);
							if (values.size() != names.size()) {
								output = "";
							} else {
								for (int i=0; i<names.size(); i++) {
									output = DatabaseAP.parserReplace(names.get(i), values.get(i), output);
								}
							}
						}
						outValues.put(field, output);
					}
				}
			}
		}

		JSONObject json = JSONObject.fromObject(outValues);

		response.getOutputStream().write(json.toString().getBytes());

		return null;
	}
}
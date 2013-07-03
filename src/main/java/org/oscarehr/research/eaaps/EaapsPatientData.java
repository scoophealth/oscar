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
package org.oscarehr.research.eaaps;

import java.io.Serializable;
import java.util.Date;

import oscar.util.ConversionUtils;
import net.sf.json.JSONObject;

/**
 * Encapsulates response of the EAAPS web service.
 */
public class EaapsPatientData implements Serializable {

    private static final long serialVersionUID = 1L;
    
	private JSONObject json;

	public EaapsPatientData() {
	}

	public EaapsPatientData(JSONObject json) {
		setJson(json);
	}

	public JSONObject getJson() {
		return json;
	}

	public void setJson(JSONObject json) {
		this.json = json;
	}

	public JSONObject getStatus() {
		return getJson().getJSONObject("status");
	}

	private boolean getBoolean(String statusFieldName) {
		String fieldValue = getStatus().getString(statusFieldName);
		if (fieldValue == null || "0".equals(fieldValue) || "-1".equals(fieldValue)) {
			return false;
		}
		return true;
	}

	public boolean isEligibleForStudy() {
		return getBoolean("eligibleForStudy");
	}

	public boolean isQuestionnaireStarted() {
		return getBoolean("questionnaireStarted");
	}

	public boolean isQuestionnaireCompleted() {
		return getBoolean("questionnaireCompleted");
	}

	public boolean isMedsConfirmed() {
		return getBoolean("medsConfirmed");
	}

	public boolean isRecommendationsAvailable() {
		return getBoolean("recommendationsAvailable");
	}

	public boolean isRecommendationsReviewStarted() {
		return getBoolean("recommendationsReviewStarted");
	}
	
	public boolean isRecommendationsReviewCompleted() {
		return getBoolean("recommendationsReviewCompleted");
	}
	
	public Date getUpdatedTimestamp() {
		String updatedTimestampString = getStatus().getString("updatedTimestamp");
		return ConversionUtils.fromDateString(updatedTimestampString);
	}

	public boolean isAapAvailable() {
		return getBoolean("aapAvailable");
	}

	public boolean isAapReviewCompleted() {
		return getBoolean("aapReviewCompleted");
	}
	
	public boolean isAapReviewStarted() {
		return getBoolean("aapReviewStarted");
	}

	public String getUrl() {
		String result = getStatus().getString("url");
		if ("null".equals(result)) {
			return null;
		}
		return result;
	}

	public String getMessage() {
		return getStatus().getString("shortMessage");
	}

	/**
	 * Replaces the response URL with the specified value. Previous URL value is
	 * kept as "previousUrl" JSON field. 
	 * 
	 * @param url
	 * 		URL to replace the existing URL with.
	 */
	public void replaceUrl(String url) {
		String previousUrl = getUrl();
		getStatus().put("url", url);
		getStatus().put("previousUrl", previousUrl);
	}

}

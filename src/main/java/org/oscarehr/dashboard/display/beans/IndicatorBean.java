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
package org.oscarehr.dashboard.display.beans;

import java.util.List;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

/**
 * Indicator display POJO
 */
public class IndicatorBean extends AbstractDataDisplayBean {

	private List<GraphPlot[]> graphPlots;
	private String jsonPlots;
	private String jsonTooltips;
	private String stringArrayPlots;
	private String stringArrayTooltips;
	private String originalJsonPlots;

	public List<GraphPlot[]> getGraphPlots() {
		return graphPlots;
	}

	public void setGraphPlots(List<GraphPlot[]> graphPlots) {
		this.graphPlots = graphPlots;
	}

	public String getJsonPlots() {
		return jsonPlots;
	}

	public void setJsonPlots(String jsonPlots) {
		this.jsonPlots = jsonPlots;
	}

	public String getJsonTooltips() {
		return jsonTooltips;
	}

	public void setJsonTooltips(String jsonTooltips) {
		this.jsonTooltips = jsonTooltips;
	}

	public String getStringArrayPlots() {
		return stringArrayPlots;
	}

	public void setStringArrayPlots(String stringArrayPlots) {
		this.stringArrayPlots = stringArrayPlots;
	}

	public String getStringArrayTooltips() {
		return stringArrayTooltips;
	}

	public void setStringArrayTooltips(String stringArrayTooltips) {
		this.stringArrayTooltips = stringArrayTooltips;
	}
	
	public String getOriginalJsonPlots() {
		return originalJsonPlots;
	}

	public void setOriginalJsonPlots(String originalJsonPlots) {
		this.originalJsonPlots = originalJsonPlots;
	}

	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

}

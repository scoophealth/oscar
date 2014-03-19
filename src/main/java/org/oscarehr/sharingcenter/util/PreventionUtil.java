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




package org.oscarehr.sharingcenter.util;

import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.common.model.Prevention;
import org.oscarehr.common.model.PreventionExt;
import org.oscarehr.common.dao.PreventionDao;
import org.oscarehr.common.dao.PreventionExtDao;
import org.oscarehr.util.SpringUtils;

import java.util.HashMap;
import java.util.List;


public class PreventionUtil {
	
	public static final String DATE = "Prevention Date";
	public static final String COMPLETED = "Completed";
	public static final String PROVIDER = "Provider";
	public static final String NAME = "Name";
	public static final String DOSE = "Dose";
	public static final String LOCATION = "Location";
	public static final String ROUTE = "Route";
	public static final String LOT = "Lot";
	public static final String MANUFACTURER = "Manufacturer";
	public static final String COMMENTS = "Comments";
	
	public static final String[] COLUMNS = { DATE, COMPLETED, PROVIDER, NAME, DOSE, LOCATION, ROUTE, LOT, MANUFACTURER, COMMENTS };
	
	private static final String LOCATION_KEY = "location";
	private static final String LOT_KEY = "lot";
	private static final String ROUTE_KEY = "route";
	private static final String DOSE_KEY = "dose";
	private static final String COMMENTS_KEY = "comments";
	private static final String MANUFACTURE_KEY = "manufacture";
	private static final String NAME_KEY = "name";
	
	private static final PreventionDao preventionDao = SpringUtils.getBean(PreventionDao.class);
	private static final PreventionExtDao preventionExtensionDao = SpringUtils.getBean(PreventionExtDao.class);
	private static final ProviderDao providerDao = SpringUtils.getBean(ProviderDao.class);
	
	private PreventionUtil() {
	}
	
	public static List<Prevention> getPreventions(int demographicId) {
		return preventionDao.findActiveByDemoId(demographicId);
	}
	
	public static List<PreventionExt> getPreventionExts(int preventionId) {
		return preventionExtensionDao.findByPreventionId(preventionId);
	}
	
	public static String getPreventionProvider(int providerNo) {
		return providerDao.getProvider(String.valueOf(providerNo)).getFormattedName();
	}
	
	public static String getPreventionName(int preventionId) {
		return getPreventionExtension(preventionId).get(NAME_KEY);
	}
	
	public static String getPreventionDose(int preventionId) {
		return getPreventionExtension(preventionId).get(DOSE_KEY);
	}
	
	public static String getPreventionLocation(int preventionId) {
		return getPreventionExtension(preventionId).get(LOCATION_KEY);
	}
	
	public static String getPreventionRoute(int preventionId) {
		return getPreventionExtension(preventionId).get(ROUTE_KEY);
	}
	
	public static String getPreventionLot(int preventionId) {
		return getPreventionExtension(preventionId).get(LOT_KEY);
	}
	
	public static String getPreventionManufacturer(int preventionId) {
		return getPreventionExtension(preventionId).get(MANUFACTURE_KEY);
	}
	
	public static String getPreventionComments(int preventionId) {
		return getPreventionExtension(preventionId).get(COMMENTS_KEY);
	}
	
	private static HashMap<String, String> getPreventionExtension(int preventionId) {
		return preventionExtensionDao.getPreventionExt(preventionId);
	}	
}

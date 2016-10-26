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

package oscar.oscarMDS.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.beanutils.BeanComparator;
import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.common.model.Provider;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public class ProviderData {

	public ProviderData(String refDoctor, String conDoctor, String admDoctor) {
		referringDoctor = beautifyProviderName(refDoctor);
		consultingDoctor = beautifyProviderName(conDoctor);
		admittingDoctor = beautifyProviderName(admDoctor);
	}

	public String referringDoctor;
	public String consultingDoctor;
	public String admittingDoctor;

	public static String beautifyProviderName(String name) {
		String[] subStrings;

		if (name.length() > 0) {
			try {
				subStrings = name.split("\\^");
				if (subStrings.length >= 18) {
					return subStrings[5] + " " + subStrings[1] + ", " + subStrings[17] + " " + subStrings[13];
				} else if (subStrings.length >= 14) {
					return subStrings[5] + " " + subStrings[1] + ", " + subStrings[13];
				} else if (subStrings.length >= 6) {
					return subStrings[5] + " " + subStrings[1];
				} else {
					return subStrings[1];
				}
			} catch (Exception e) {
				MiscUtils.getLogger().debug("Error in ProviderData: " + e.toString());
				return name.replace('^', ' ');
			}
		} else {
			return "";
		}

	}

	@SuppressWarnings("unchecked")
	public static ArrayList<ArrayList<String>> getProviderList() {
		try {
			ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();
			ProviderDao dao = SpringUtils.getBean(ProviderDao.class);
			List<Provider> providers = dao.getProvidersByType(ProviderDao.PR_TYPE_DOCTOR);
                        List<Provider> residents = dao.getProvidersByType(ProviderDao.PR_TYPE_RESIDENT);
                        
                        providers.addAll(residents);
			Collections.sort(providers, new BeanComparator("formattedName"));
			for (Provider p : providers) {
				ArrayList<String> provider = new ArrayList<String>();
				provider.add(p.getProviderNo());
				provider.add(p.getFirstName());
				provider.add(p.getLastName());
                                result.add(provider);
			}
                        
                        
                        
                        
			return result;
		} catch (Exception e) {
			MiscUtils.getLogger().debug("exception in ProviderData:" + e);
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public static ArrayList<ArrayList<String>> getProviderListWithLabNo() {
		try {
			ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();

			ProviderDao dao = SpringUtils.getBean(ProviderDao.class);
			List<Provider> providers = dao.getProvidersByTypeWithNonEmptyOhipNo(ProviderDao.PR_TYPE_DOCTOR);
			Collections.sort(providers, new BeanComparator("formattedName"));
			for (Provider p : providers) {
				ArrayList<String> provider = new ArrayList<String>();
				provider.add(p.getProviderNo());
				provider.add(p.getFirstName());
				provider.add(p.getLastName());
				result.add(provider);
			}
			return result;
		} catch (Exception e) {
			MiscUtils.getLogger().debug("exception in ProviderData:" + e);
			return null;
		}
	}

	public static String getProviderName(String providerNo) {
		ProviderDao dao = SpringUtils.getBean(ProviderDao.class);
		Provider p = dao.getProvider(providerNo);
		if (p == null) {
			return "";
		}

		return p.getFullName();
	}
}

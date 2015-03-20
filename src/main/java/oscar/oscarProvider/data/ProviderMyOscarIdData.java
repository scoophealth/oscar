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


package oscar.oscarProvider.data;

import java.util.ArrayList;
import java.util.List;

import org.oscarehr.common.dao.PropertyDao;
import org.oscarehr.common.model.Property;
import org.oscarehr.util.SpringUtils;

/**
 * Manages MyOscar Login Id for provider
 */
public final class ProviderMyOscarIdData {

	private static PropertyDao dao = SpringUtils.getBean(PropertyDao.class);

	private static final String PROPERTY_KEY = "MyOscarId";

	private ProviderMyOscarIdData() {
		// this is a utility class not an object, it shouldn't be instantiated.
	}

	/**
	 *Retrieve myOscar login id for current provider first by querying property table
	 */
	public static String getMyOscarId(String providerNo) {
		
		List<Property> props = dao.findByNameAndProvider(PROPERTY_KEY, providerNo);
		if(props.size()>0) {
			return props.get(0).getValue();
		}

		return new String();
	}

	/**
	 *set myOscar login id in property table
	 */
	public static boolean setId(String providerId, String id) {
		String sql;
		boolean ret = true;

		List<Property> props = dao.findByNameAndProvider(PROPERTY_KEY, providerId);
		Property p = new Property();
		if(props.size()>0) {
			p = props.get(0);
			p.setValue(id);
			dao.merge(p);
		} else {
			p.setName(PROPERTY_KEY);
			p.setValue(id);
			p.setProviderNo(providerId);
			dao.persist(p);
		}

		return ret;
	}

	public static boolean idIsSet(String providerId)  {
		return getMyOscarId(providerId).length()>0;

	}

	// get provider number knowing the indivo id
	public static String getProviderNo(String myOscarUserName) {
		String providerNo = "";
		
		List<Property> props = dao.findByNameAndValue(PROPERTY_KEY, myOscarUserName);
		for(Property p:props) {
			providerNo = p.getProviderNo();
		}

		
		return providerNo;
	}

	public static List<String> listMyOscarProviderNums() {
		ArrayList<String> providerIdList = new ArrayList<String>();
		
		List<Property> props = dao.findByName(PROPERTY_KEY);	
		for(Property p:props) {
			providerIdList.add(p.getProviderNo());
		}
		

		return providerIdList;
	}
}

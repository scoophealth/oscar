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

package oscar.oscarBilling.ca.bc.data;

import java.util.List;

import javax.persistence.Query;

import org.oscarehr.common.dao.AbstractDao;
import org.springframework.stereotype.Repository;

import oscar.util.ConversionUtils;

/**
 * 
 * Responsible for CRUD operation a user Billing Module Preferences
 * 
 * @author not attributable
 * @version 1.0
 */
@Repository
public class BillingPreferencesDAO extends AbstractDao<BillingPreference> {

	public BillingPreferencesDAO() {
		super(BillingPreference.class);
	}

	@SuppressWarnings("unchecked")
	public BillingPreference getUserBillingPreference(String providerNo) {
		Query query = createQuery("bp", "bp.providerNo = :providerNo");
		query.setParameter("providerNo", ConversionUtils.fromIntString(providerNo));

		List<BillingPreference> prefs = query.getResultList();
		if (prefs.isEmpty()) return null;
		return prefs.get(0);
	}

	/**
	 * Saves the preferences for a specific user, if a record exists for the specific user,
	 * the values in that record are updated otherwise a new record is created
	 * @param pref the preferences
	 * 
	 * @deprecated 
	 */
	public void saveUserPreferences(BillingPreference pref) {
		saveEntity(pref);
	}

}

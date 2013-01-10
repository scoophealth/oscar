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

package oscar.oscarMessenger.pageUtil;

import java.util.Collections;
import java.util.List;
import java.util.Vector;

import org.apache.commons.beanutils.BeanComparator;
import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.common.model.Provider;
import org.oscarehr.util.SpringUtils;

public class MsgDocListForm implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	private Vector<String> providerNoVector;
	private Vector<String> providerFirstName;
	private Vector<String> providerLastName;

	/**
	 * If the vectors used to set up the checkbox list are no itialize this will call
	 * setUpVector to init them
	 * @return Vector, With strings of provider No
	 */
	public Vector<String> getProviderNoVector() {
		if (providerNoVector == null) {
			setUpVector();
		}
		return this.providerNoVector;
	}

	/**
	 * If the vectors used to set up the checkbox list are no itialize this will call
	 * setUpVector to init them
	 * @return Vector, With Strings of providers first names
	 */
	public Vector<String> getProviderFirstName() {
		if (providerFirstName == null) {
			setUpVector();
		}
		return this.providerFirstName;
	}

	/**
	 * If the vectors used to set up the checkbox list are no itialize this will call
	 * setUpVector to init them
	 * @return Vector, With Strings of providers last names
	 */
	public Vector<String> getProviderLastName() {
		if (providerLastName == null) {
			setUpVector();
		}
		return this.providerLastName;
	}

	/**
	 * Used to set the Provider No Vector
	 * @param noVector
	 */
	public void setProviderNoVector(Vector<String> noVector) {
		this.providerNoVector = noVector;
	}

	/**
	 * Used to set the Providers last name
	 * @param lName Vector,
	 */
	public void setProviderLastName(Vector<String> lName) {
		this.providerLastName = lName;
	}

	/**
	 * Used to set the Providers first name
	 * @param fName
	 */
	public void setProviderFirstName(Vector<String> fName) {
		this.providerFirstName = fName;
	}

	/**
	 * Sets up three vectors used in for the CreateMessage.jsp page
	 * They are used to init the checkboxes that can be checked to send messages
	 */
	@SuppressWarnings("unchecked")
	private void setUpVector() {
		providerNoVector = new Vector<String>();
		providerLastName = new Vector<String>();
		providerFirstName = new Vector<String>();

		ProviderDao dao = SpringUtils.getBean(ProviderDao.class);
		List<Provider> ps = dao.getProviders();
		Collections.sort(ps, new BeanComparator("firstName"));
		for (Provider p : ps) {
			providerNoVector.add(p.getProviderNo());
			providerFirstName.add(p.getFirstName());
			providerLastName.add(p.getLastName());
		}
	}

}//DocListForm

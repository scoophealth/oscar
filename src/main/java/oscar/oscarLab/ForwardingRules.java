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

/*
 * ForwardingRules.java
 *
 * Created on July 16, 2007, 10:29 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package oscar.oscarLab;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.oscarehr.common.dao.IncomingLabRulesDao;
import org.oscarehr.common.model.IncomingLabRules;
import org.oscarehr.common.model.Provider;
import org.oscarehr.util.SpringUtils;

/**
 *
 * @author wrighd
 */
public class ForwardingRules {

	Logger logger = Logger.getLogger(ForwardingRules.class);

	/** Creates a new instance of ForwardingRules */
	public ForwardingRules() {
	}

	public ArrayList<ArrayList<String>> getProviders(String providerNo) {
		ArrayList<ArrayList<String>> ret = new ArrayList<ArrayList<String>>();
		IncomingLabRulesDao dao = SpringUtils.getBean(IncomingLabRulesDao.class);

		for (Object[] i : dao.findRules(providerNo)) {
			Provider p = (Provider) i[1];

			ArrayList<String> info = new ArrayList<String>();
			info.add(p.getProviderNo());
			info.add(p.getFirstName());
			info.add(p.getLastName());
			ret.add(info);
		}
		return ret;
	}

	public String getStatus(String providerNo) {
		String ret = "N";
		IncomingLabRulesDao dao = SpringUtils.getBean(IncomingLabRulesDao.class);
		List<IncomingLabRules> rules = dao.findCurrentByProviderNo(providerNo);
		if (!rules.isEmpty()) {
			IncomingLabRules rule = rules.get(0);
			ret = rule.getStatus();
		}
		return ret;
	}

	public boolean isSet(String providerNo) {
		IncomingLabRulesDao dao = SpringUtils.getBean(IncomingLabRulesDao.class);
		List<IncomingLabRules> rules = dao.findCurrentByProviderNo(providerNo);
		return !rules.isEmpty();
	}

}

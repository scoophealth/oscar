/**
 * Copyright (c) 2006-. OSCARservice, OpenSoft System. All Rights Reserved.
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
 */

package oscar.oscarBilling.ca.on.data;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;

import oscar.util.UtilDateUtilities;

public class JdbcBillingLog {
	private static final Logger _logger = Logger.getLogger(JdbcBillingLog.class);
	BillingONDataHelp dbObj = new BillingONDataHelp();

	public boolean addBillingLog(String providerNo, String action, String comment, String object) {
		boolean retval = false;
		String createdatetime = UtilDateUtilities.getToday("yyyy-MM-dd HH:mm:ss");
		String sql = "insert into billing_on_proc values(\\N, '" + providerNo + "','" + action + "','"
				+ StringEscapeUtils.escapeSql(comment) + "','" + object + "','" + createdatetime + "')";
		_logger.info("addBillingLog(sql = " + sql + ")");

		retval = dbObj.updateDBRecord(sql);

		if (retval) {
			_logger.error("addBillingLog(sql = " + sql + ")");
		}
		return retval;
	}

}

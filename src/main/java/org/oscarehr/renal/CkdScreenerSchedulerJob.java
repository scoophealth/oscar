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
package org.oscarehr.renal;

import java.util.TimerTask;

import org.apache.log4j.Logger;
import org.oscarehr.util.DbConnectionFilter;
import org.oscarehr.util.MiscUtils;

import oscar.OscarProperties;

public class CkdScreenerSchedulerJob extends TimerTask {

	private Logger logger = MiscUtils.getLogger();

	@Override
	public void run() {
		if(!OscarProperties.getInstance().getProperty("ORN_PILOT", "no").equalsIgnoreCase("yes")) {
			return;
		}
		try {
			logger.info("starting renal background ckd screening job");
			
			CkdScreener screener = new CkdScreener();
			screener.screenPopulation();
			
			logger.info("done renal background ckd screening job");
			
		}catch(Throwable e ) {
			logger.error("Error",e);
		} finally {
			DbConnectionFilter.releaseAllThreadDbResources();
		}
	}

}

/**
 *
 * Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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
 * This software was written for
 * Centre for Research on Inner City Health, St. Michael's Hospital,
 * Toronto, Ontario, Canada
 */

package org.caisi.tickler.prepared;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.oscarehr.util.MiscUtils;

public class PreparedTicklerManager {

	static Logger log=MiscUtils.getLogger();

	private List<PreparedTickler> ticklers;

	public PreparedTicklerManager() {
		ticklers = new ArrayList<PreparedTickler>();
	}

	/* loads up the runtime plugins */
	public void setPath(String path) {
		ticklers.clear();
		File f = new File(path + "/WEB-INF/classes/org/caisi/tickler/prepared/runtime");
		if(f.isDirectory()) {
			File[] files = f.listFiles();
			for(int x=0;x<files.length;x++) {
				String fileName = files[x].getName();
				fileName = fileName.substring(0,fileName.lastIndexOf('.'));
				PreparedTickler pt = null;
				if((pt = loadClass("org.caisi.tickler.prepared.runtime." + fileName)) != null) {
					ticklers.add(pt);
				}
			}
		}

	}

	public PreparedTickler loadClass(String className) {
		PreparedTickler pt = null;
		ClassLoader cl = Thread.currentThread().getContextClassLoader();

		try {
			 pt = (PreparedTickler)cl.loadClass(className).newInstance();
		}catch(Exception e) {
			log.warn("Warning", e);
		}

		return pt;
	}

	public List<PreparedTickler> getTicklers() {
		return ticklers;
	}

	public PreparedTickler getTickler(String name) {
		for(int x=0;x<ticklers.size();x++) {
			PreparedTickler tickler = ticklers.get(x);
			if(tickler.getName().equals(name)) {
				return tickler;
			}
		}
		return null;
	}
}

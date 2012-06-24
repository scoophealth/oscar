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

package org.oscarehr.PMmodule.exporter;

import java.util.HashMap;
import java.util.Map;

public class DATISExporterFactory {
	
	private static DATISExporterFactory factory = null; // singleton
	private Map<String, AbstractIntakeExporter> files;
	
	private DATISExporterFactory() {
		files = new HashMap<String, AbstractIntakeExporter>();
		files.put("agencyinformation", new DATISAgencyInformation());
		files.put("listofprograms", new DATISListOfPrograms());
		files.put("main", new DATISMain());
		files.put("programinformation", new DATISProgramInformation());
		files.put("gamblingform", new DATISGamingForm());
		files.put("nonclientservice", new DATISNonClientService());
	}
	
	public synchronized static DATISExporterFactory getInstance() {
		if(factory == null) {
			factory = new DATISExporterFactory();
		}
		
		return factory;
	}
	
	public AbstractIntakeExporter getExporter(String filename) {
		return files.get(filename);
	}
}

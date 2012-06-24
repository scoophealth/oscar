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

package org.oscarehr.PMmodule.web.reports.custom;

import java.io.File;

import org.apache.commons.digester.Digester;
import org.apache.log4j.Logger;
import org.oscarehr.util.MiscUtils;

public class UCRConfigurationManager {

	private static Logger logger = MiscUtils.getLogger();
	static Digester digester = new Digester();
	static UCRConfiguration config;
	
	private String filename;
	
	static {
		digester.setValidating(false);
		digester.addObjectCreate("report-config", UCRConfiguration.class);
		
		digester.addObjectCreate("report-config/data-sources/data-source", DataSource.class);
		digester.addBeanPropertySetter("report-config/data-sources/data-source/type", "type");
		digester.addBeanPropertySetter("report-config/data-sources/data-source/bean", "bean");		
		digester.addSetNext("report-config/data-sources/data-source","addDataSource");
		
		digester.addObjectCreate("report-config/data-sources/data-source/forms/form", Form.class);
		digester.addBeanPropertySetter("report-config/data-sources/data-source/forms/form/name", "name");				
		digester.addSetNext("report-config/data-sources/data-source/forms/form","addForm");
		
		digester.addObjectCreate("report-config/data-sources/data-source/forms/form/items/item", Item.class);
		digester.addBeanPropertySetter("report-config/data-sources/data-source/forms/form/items/item/name", "name");
		digester.addBeanPropertySetter("report-config/data-sources/data-source/forms/form/items/item/type", "valueType");
		//digester.addBeanPropertySetter("report-config/data-sources/data-source/forms/form/items/item/page", "pageId");				
		//digester.addBeanPropertySetter("report-config/data-sources/data-source/forms/form/items/item/section", "sectionId");				
		//digester.addBeanPropertySetter("report-config/data-sources/data-source/forms/form/items/item/question", "questionId");				
		digester.addSetNext("report-config/data-sources/data-source/forms/form/items/item","addItem");			
	}
	
	public UCRConfigurationManager() {
		
	}
	
	public void setFilename(String filename) {
		this.filename = filename;
	}
	
	public UCRConfiguration getConfig(String basePath) throws Exception {
		logger.debug("loading up custom reports config");
		if(config == null) {
			File f = new File(basePath,filename);
			if(f.exists()) {
				logger.debug("found config file");
			}
			config = (UCRConfiguration)digester.parse(f);
			logger.debug("parsed config file");
			return config;
		} else {
			return config;
		}
	}
	
	public UCRConfiguration getConfig() throws Exception {
		return getConfig("");
	}
	
}

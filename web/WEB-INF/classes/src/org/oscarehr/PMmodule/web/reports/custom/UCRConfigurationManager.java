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

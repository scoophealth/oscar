package org.oscarehr.PMmodule.web.reports.custom;

import java.io.File;

import org.apache.commons.digester.Digester;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class UCRConfigurationManager {

	static Log logger = LogFactory.getLog(UCRConfigurationManager.class);
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
	public static void main(String args[]) throws Exception {
		File f = new File(args[0]);
		UCRConfiguration config = (UCRConfiguration)digester.parse(f);
		for(DataSource ds:config.getDataSources()) {
			System.out.println("data-source type=" + ds.getType());
			for(Form form:ds.getForms()) {
				System.out.println("\tform name=" + form.getName());
				for(Item item:form.getItems()) {
					System.out.println("\t\titem name=" + item.getName());
					System.out.println("\t\titem type=" + item.getValueType());
				//	System.out.println("\t\titem page=" + item.getPageId());
				//	System.out.println("\t\titem section=" + item.getSectionId());
				//	System.out.println("\t\titem question=" + item.getQuestionId());
					System.out.println();
				}
				System.out.println();
			}
			System.out.println();
		}
	}
	
}

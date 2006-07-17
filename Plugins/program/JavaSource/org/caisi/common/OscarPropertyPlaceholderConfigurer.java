package org.caisi.common;

import java.util.Properties;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

public class OscarPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {


	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.config.PropertyPlaceholderConfigurer#resolvePlaceholder(java.lang.String, java.util.Properties, int)
	 */
	protected String resolvePlaceholder(String placeholder, Properties properties, int systemPropertiesMode) {
		//System.out.println("resolvePlaceholder 1 " + placeholder);
		Properties p2 = OscarProperties.getProperties();
		if(p2 != null && placeholder.startsWith("oscar.")) {
			String value = p2.getProperty(placeholder.substring(6));
			//System.out.println(placeholder.substring(6) +"="+value);
			if(value != null) {
				return value;
			}
		}

		return super.resolvePlaceholder(placeholder, properties, systemPropertiesMode);
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.config.PropertyPlaceholderConfigurer#resolvePlaceholder(java.lang.String, java.util.Properties)
	 */
	protected String resolvePlaceholder(String placeholder, Properties properties) {
		//System.out.println("resolvePlaceholder 2" + placeholder);
		Properties p2 = OscarProperties.getProperties();
		if(p2 != null && placeholder.startsWith("oscar.")) {
			String value = p2.getProperty(placeholder.substring(6));
			//System.out.println(placeholder.substring(6) +"="+value);
			if(value != null) {
				return value;
			}
		}
		return super.resolvePlaceholder(placeholder, properties);
	}

	
}

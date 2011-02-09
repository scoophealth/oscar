package oscar.util.plugin;

import java.util.Properties;

import org.oscarehr.util.MiscUtils;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

public class OscarPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {


	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.config.PropertyPlaceholderConfigurer#resolvePlaceholder(java.lang.String, java.util.Properties, int)
	 */
	protected String resolvePlaceholder(String placeholder, Properties properties, int systemPropertiesMode) {

		Properties p2 = oscar.OscarProperties.getInstance();
		MiscUtils.getLogger().debug("oscarproperties="+p2.toString());
		if(p2 != null && placeholder.startsWith("oscar.")) {
			String value = p2.getProperty(placeholder.substring(6));
			MiscUtils.getLogger().debug("resolveplaceholder1:"+placeholder.substring(6) +"="+value);
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

		Properties p2 = oscar.OscarProperties.getInstance();
		MiscUtils.getLogger().debug("oscarproperties="+p2.toString());
		if(p2 != null && placeholder.startsWith("oscar.")) {
			String value = p2.getProperty(placeholder.substring(6));
			MiscUtils.getLogger().debug("resolveplaceholder2:"+placeholder.substring(6) +"="+value);
			if(value != null) {
				return value;
			}
		}
		return super.resolvePlaceholder(placeholder, properties);
	}

	
}

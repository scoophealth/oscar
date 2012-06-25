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

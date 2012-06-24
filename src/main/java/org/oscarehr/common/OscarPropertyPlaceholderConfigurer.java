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

package org.oscarehr.common;

import java.util.Properties;

import org.apache.log4j.Logger;
import org.oscarehr.util.MiscUtils;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

public class OscarPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {
        private static Logger log = MiscUtils.getLogger();
	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.config.PropertyPlaceholderConfigurer#resolvePlaceholder(java.lang.String, java.util.Properties, int)
	 */
	protected String resolvePlaceholder(String placeholder, Properties properties, int systemPropertiesMode) {
		log.debug("resolvePlaceholder Start " + placeholder);
                Properties props = oscar.OscarProperties.getInstance();
                if(props.containsKey(placeholder)){
                    log.debug("setting property "+placeholder+" with "+props.getProperty(placeholder));
                    properties.setProperty(placeholder,props.getProperty(placeholder));
                }
		return super.resolvePlaceholder(placeholder, properties, systemPropertiesMode);
	}

	/* (non-Javadoc)
	 * @see org.springframework.beans.factory.config.PropertyPlaceholderConfigurer#resolvePlaceholder(java.lang.String, java.util.Properties)
	 */
	protected String resolvePlaceholder(String placeholder, Properties properties) {
		log.debug("resolvePlaceholder 2" + placeholder);
		return super.resolvePlaceholder(placeholder, properties);
	}	
}

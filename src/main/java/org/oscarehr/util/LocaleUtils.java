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

package org.oscarehr.util;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.servlet.ServletRequest;

import org.apache.log4j.Logger;

public final class LocaleUtils
{
	private static final Logger logger=MiscUtils.getLogger();
	private static final Locale DEFAULT_LOCALE=Locale.ENGLISH;
	private static final String BASE_NAME="oscarResources";

	public static String getMessage(ServletRequest request, String key)
	{
		return(getMessage(request.getLocale(), key));
	}
	
	public static String getMessage(String isoLanguageCode, String key)
	{
		return(getMessage(new Locale(isoLanguageCode), key));
	}
	
	public static String getMessage(Locale locale, String key)
	{
		// try the requested locale
        try {
	        return(ResourceBundle.getBundle(BASE_NAME, locale).getString(key));	        
        } catch (MissingResourceException e) {
    		// if not found, use the default locale, and log and error
        	String message="Resource not found. BASE_NAME="+BASE_NAME+", Locale="+locale+", key="+key;
        	logger.error(message);
    		logger.debug(message, e);
	        try {
	            return(ResourceBundle.getBundle(BASE_NAME, DEFAULT_LOCALE).getString(key));
            } catch (MissingResourceException e1) {
            	message="Resource not found. BASE_NAME="+BASE_NAME+", DEFAULT_LOCALE="+DEFAULT_LOCALE+", key="+key;
            	logger.error(message);
        		logger.debug(message, e);
        		return(key);
            }	        
        }			
	}
}

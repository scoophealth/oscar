/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
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
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */
package org.oscarehr.integration.excelleris.com.colcamex.www.util;
/**
 * @author Dennis Warren Colcamex Resources
 * 
 * Major Contributors: 
 *  OSCARprn
 *  NERD
 *   
 * This community edition of Expedius is for use at your own risk, without warranty, and 
 * support. 
 * 
 */
public class FilterUtility {  
    /**
     * Trim the crap
     * @param input
     * @return
     */
    public static String filter(String input) {
    	if(input == null) {
    		return null;
    	}
        StringBuilder filtered = new StringBuilder(input.length());
        char c;
            for(int i=0; i<input.length(); i++) {
              c = input.charAt(i);
              if (c == '<') {
                filtered.append("");
              } else if (c == '>') {
                filtered.append("");
              } else if (c == '"') {
                filtered.append("");
              } else if (c == '&') {
                filtered.append("&amp;");
              } else {
                filtered.append(c);
              }
            }
        return(filtered.toString().trim());
    }

}

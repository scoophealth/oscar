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

package oscar.caisi;

public class CaisiUtil
{
	public static String removeAttr(String str, String attr)
	{
	    if (str==null) return(null);
	    
		/*delete a parameter from query string*/
		int index,index1;
		String temps;
		index=str.indexOf(attr);
		if (index==-1) return str;
		temps=str.substring(index);
		index1=temps.indexOf("&");
		if (index1!=-1)  return str.substring(0,index)+temps.substring(index1+1);
		else {
			temps=str.substring(0,index);
			if (temps.endsWith("&")) return str.substring(0,index-1);
			else return temps;
		}
		
	}

}

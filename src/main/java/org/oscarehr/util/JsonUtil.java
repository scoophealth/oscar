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
package org.oscarehr.util;

import java.util.Iterator;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

/*
 * Author: Dennis Warren 
 * Company: Colcamex Resources
 * Date: November 2014
 * For: UBC Pharmacy Clinic and McMaster Department of Family Medicine
 */

public class JsonUtil {

	private static final JSONArray jsonArray = new JSONArray();
	// set default methods to be ignored here.
	private static final JsonConfig jsonConfig = new JsonConfig();

	public static final String pojoCollectionToJson(final List<?> pojoList ) {
		return pojoCollectionToJson(pojoList, null); 
	}
	
	public static final String pojoCollectionToJson(final List<?> pojoList, String[] ignoreMethods) {		
		jsonArray.clear();
		
		Iterator<?> it = null;
		if(pojoList.size() > 0) {
			
			it = pojoList.iterator();
			while( it.hasNext() ) {
				jsonArray.add( pojoToJson( it.next(), ignoreMethods ) );
			}
			
		}
		
		return jsonArray.toString();		
	}
	
	public static final JSONObject pojoToJson(final Object pojo) {
		return pojoToJson(pojo, null);
	}
	
	public static final JSONObject pojoToJson(final Object pojo, final String[] ignoreMethods) {		

		if( ignoreMethods != null ) {
			jsonConfig.setExcludes(ignoreMethods);
		}
		
		return JSONObject.fromObject( pojo, jsonConfig );
	}
	
	public static final List<?> jsonToPojoList(final String json, final Class<?> clazz) {
		return jsonToPojoList(JSONArray.fromObject(json), clazz);
	}

    public static final List<?> jsonToPojoList(final JSONArray jsonArray, final Class<?> clazz) {
    	return (List<?>) JSONArray.toCollection(jsonArray, clazz);
	}
	
	public static final Object jsonToPojo(final String json, final Class<?> clazz) {
		return jsonToPojo(JSONObject.fromObject(json), clazz);
	}
	
	public static final Object jsonToPojo(final JSONObject jsonObject, final Class<?> clazz) {
		return JSONObject.toBean(jsonObject, clazz);
	}

}

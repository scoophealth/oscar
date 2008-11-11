/*
* Copyright (c) 2007-2008. CAISI, Toronto. All Rights Reserved.
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
* CAISI, 
* Toronto, Ontario, Canada 
*/

package org.oscarehr.util;

public class FacilitySegmentedTimeClearedHashMap extends TimeClearedHashMap<String, Object>
{
    public FacilitySegmentedTimeClearedHashMap(long maxDataAge, long checkPeriod) {
    	super(maxDataAge, checkPeriod);
    }
 
    public static final String getCompositeFacilityKey(Integer facilityId, String key)
    {
    	return(facilityId.toString()+':'+key);
    }
    
	public Object put(Integer facilityId, String key, Object value)
	{
		return(put(getCompositeFacilityKey(facilityId, key), value));
	}

	public Object get(Integer facilityId, String key)
	{
		return(get(getCompositeFacilityKey(facilityId, key)));
	}
	
	public Object remove(Integer facilityId, String key)
	{
		return(remove(getCompositeFacilityKey(facilityId, key)));
	}
}
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

package org.caisi.tickler.prepared.runtime;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.caisi.tickler.prepared.PreparedTickler;
import org.oscarehr.util.MiscUtils;

public abstract class AbstractPreparedTickler implements PreparedTickler {

	public void setDependency(String name, Object o) {
		String methodName = "set" + name.substring(0,1).toUpperCase() + name.substring(1,name.length());
		try {
			Method methods[] = this.getClass().getMethods();
			for(int x=0;x<methods.length;x++) {
				if(methods[x].getName().equals(methodName)) {
					methods[x].invoke(this,new Object[] {o});
				}
			}	
		}catch(InvocationTargetException e) {
			MiscUtils.getLogger().error("Error", e);
		}catch(IllegalAccessException e) {
			MiscUtils.getLogger().error("Error", e);
		}
	}

}

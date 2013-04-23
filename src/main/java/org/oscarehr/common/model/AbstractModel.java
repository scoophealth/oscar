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

package org.oscarehr.common.model;

import java.util.List;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.oscarehr.util.MiscUtils;

public abstract class AbstractModel<T> implements java.io.Serializable
{
	protected static final String OBJECT_NOT_YET_PERISTED="The object is not persisted yet, this operation requires the object to already be persisted.";

	public abstract T getId();

	@Override
    public String toString()
	{
		return(ReflectionToStringBuilder.toString(this));
	}

	@Override
    public int hashCode()
	{
		if (getId() == null)
		{
			StackTraceElement[] stack = new Throwable().getStackTrace();
			for (int i = 0; i < stack.length; i++) {
		        if(stack[i].getClassName().equals("org.oscarehr.common.model.AbstractModel")
		        		&& stack[i].getMethodName().equals("toString")) {
		        	return super.hashCode();
		        }
	        }
			MiscUtils.getLogger().warn(OBJECT_NOT_YET_PERISTED, new Exception());
			return(super.hashCode());
		}

		return(getId().hashCode());
	}

	@Override
	public boolean equals(Object o)
	{
		if (getClass()!=o.getClass()) return(false);

		@SuppressWarnings("unchecked")
		AbstractModel<T> abstractModel=(AbstractModel<T>)o;
		if (getId() == null)
		{
			MiscUtils.getLogger().warn(OBJECT_NOT_YET_PERISTED, new Exception());
		}

		return(getId().equals(abstractModel.getId()));
	}

	/**
	 * This method checks to see if there is an entry in the list with the corresponding primary key, it does not check to see that the other values are the
	 * same or not.
	 */
	// TODO Move this to the base DAO instead ??? 
	public static <X extends AbstractModel<?>> boolean existsId(List<X> list, X searchModel)
	{
		Object searchPk = searchModel.getId();
		for (X tempModel : list)
		{
			Object tempPk = tempModel.getId();
			if (searchPk.equals(tempPk)) return(true);
		}

		return(false);
	}
	
	/**
	 * THis method returns a comma separated list of the ids as a string, useful for logging or debugging.
	 */
	public static <X extends AbstractModel<?>> String getIdsAsStringList(List<X> list)
	{
		StringBuilder sb=new StringBuilder();
		
		for (X model : list)
		{
			sb.append(model.getId().toString());
			sb.append(',');
		}
		
		return(sb.toString());
	}
	
	/**
	 * Checks if the persistent id has been assigned to this instance.
	 * 
	 * @return
	 * 		Returns true if the ID is not null and false otherwise.
	 */
	public boolean isPersistent() {
		return getId() != null;
	}
}

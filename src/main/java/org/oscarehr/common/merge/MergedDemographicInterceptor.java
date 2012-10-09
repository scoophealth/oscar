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
package org.oscarehr.common.merge;

import java.util.Collection;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.oscarehr.common.dao.DemographicDao;
import org.springframework.aop.framework.ReflectiveMethodInvocation;

public class MergedDemographicInterceptor implements MethodInterceptor {

	private DemographicDao dao;

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Object invoke(MethodInvocation invocation) throws Throwable {
		// invoke the parent method
		Object invocationResult = invocation.proceed();

		// do some sanity & bad configuration checks
		if (invocationResult == null) throw new IllegalStateException("Invalid result. The wrapped class must return a Collection instance.");
		if (!Collection.class.isAssignableFrom(invocationResult.getClass())) throw new IllegalStateException("Invalid return type. The result class must implement Collection interface.");

		Collection result = (Collection) invocationResult;

		Integer demographicId = getDemographicId(invocation.getArguments());
		Collection<Integer> mergedIds = getDao().getMergedDemographics(demographicId);
		if (mergedIds.isEmpty()) return result;

		for (Integer mergedId : mergedIds) {
			MethodInvocation invocationClone = ((ReflectiveMethodInvocation) invocation).invocableClone();
			setDemographicId(mergedId, invocationClone.getArguments());
			Collection r = (Collection) invocationClone.proceed();
			result.addAll(r);
		}
		return result;
	}

	@SuppressWarnings("rawtypes")
	private void setDemographicId(Integer mergedId, Object[] arguments) {
		// TODO add support for multiple params
		Class entryType = arguments.getClass().getComponentType();
		if (String.class.isAssignableFrom(entryType)) {
			arguments[0] = mergedId.toString();
		} else if (Integer.class.isAssignableFrom(entryType)) {
			arguments[0] = mergedId;
		} else {
			throw new IllegalArgumentException("Unsupported array element type " + entryType);
		}
	}

	private Integer getDemographicId(Object[] arguments) {
		// TODO add support for multiple params 
		if (arguments.length == 1) return Integer.parseInt("" + arguments[0]);
		throw new IllegalArgumentException("Parameter must be a single item!");
	}

	public DemographicDao getDao() {
		return dao;
	}

	public void setDao(DemographicDao dao) {
		this.dao = dao;
	}
}

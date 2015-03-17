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
package org.oscarehr.managers;

import org.apache.commons.lang.StringUtils;
import org.oscarehr.common.dao.AbstractCodeSystemDao;
import org.oscarehr.common.model.AbstractCodeSystemModel;
import org.oscarehr.util.SpringUtils;
import org.springframework.stereotype.Service;

@Service
public class CodingSystemManager {

	public String getCodeDescription(String codingSystem, String code) {
		String daoName = AbstractCodeSystemDao.getDaoName(AbstractCodeSystemDao.codingSystem.valueOf(codingSystem));
		if (daoName != null) {
			AbstractCodeSystemDao<AbstractCodeSystemModel<?>> csDao = (AbstractCodeSystemDao<AbstractCodeSystemModel<?>>) SpringUtils.getBean(daoName);
			if (csDao != null) {
				AbstractCodeSystemModel<?> codingSystemEntity = csDao.findByCode(code);
				if(codingSystemEntity != null) {
					return codingSystemEntity.getDescription();
				}
			}
		}
		return null;

	}
	
	public boolean isCodeAvailable(String codingSystem, String code) {
		String description = getCodeDescription(codingSystem, code);
		return StringUtils.isNotBlank(description);
	}
}

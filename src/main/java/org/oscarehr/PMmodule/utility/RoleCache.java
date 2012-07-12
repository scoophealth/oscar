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
package org.oscarehr.PMmodule.utility;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.map.MultiValueMap;
import org.oscarehr.PMmodule.dao.DefaultRoleAccessDAO;
import org.oscarehr.PMmodule.model.DefaultRoleAccess;
import org.oscarehr.common.dao.CaisiAccessTypeDao;
import org.oscarehr.common.model.CaisiAccessType;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import com.quatro.dao.security.SecroleDao;
import com.quatro.model.security.Secrole;

public class RoleCache {
	
	static SecroleDao secroleDao = (SecroleDao)SpringUtils.getBean("secroleDao");
	static DefaultRoleAccessDAO defaultRoleAccessDAO = (DefaultRoleAccessDAO)SpringUtils.getBean("defaultRoleAccessDAO");
	static CaisiAccessTypeDao accessTypeDao = (CaisiAccessTypeDao)SpringUtils.getBean(CaisiAccessTypeDao.class);
	
	static Map<Long,Secrole> roleMap = new HashMap<Long,Secrole>();
	static Map<String, Integer> accessTypeMap = new HashMap<String,Integer>();
	static MultiValueMap defaultRoleAccessMap = new MultiValueMap();
	
	public static void reload() {
		accessTypeMap.clear();
		for(CaisiAccessType at:accessTypeDao.findAll()) {
			accessTypeMap.put(at.getName(), at.getId());
		}
		roleMap.clear();
		for(Secrole role:secroleDao.getRoles()) {
			setRole(role.getId(),role);
		}
		defaultRoleAccessMap.clear();
		for(DefaultRoleAccess dra:defaultRoleAccessDAO.findAll()) {
			long roleId = dra.getRoleId();
			long accessTypeId = dra.getAccessTypeId();		
			defaultRoleAccessMap.put(accessTypeId, roleId);
		}
	}
	
	public static boolean hasAccess(String accessTypeName, long roleId) {
		Integer accessTypeId = accessTypeMap.get(accessTypeName);
		if(accessTypeId == null) {
			MiscUtils.getLogger().warn("Access Type not found:"+accessTypeName);
			return false;
		}
		Collection<Long> roles = defaultRoleAccessMap.getCollection(accessTypeId.longValue());
		if(roles != null && roles.contains(roleId))
			return true;
		return false;
	}
	
	public static Secrole getRole(Long id) {
		return roleMap.get(id);
	}
		
	public static void setRole(Long roleId,Secrole role) {
		roleMap.put(roleId, role);
	}
}

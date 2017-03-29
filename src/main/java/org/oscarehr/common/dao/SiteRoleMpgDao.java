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

package org.oscarehr.common.dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;

import org.oscarehr.common.model.SiteRoleMpg;
import org.springframework.stereotype.Repository;

@Repository
public class SiteRoleMpgDao extends AbstractDao<SiteRoleMpg>{
	public SiteRoleMpgDao() {
		super(SiteRoleMpg.class);
	}
	
	public void deleteAccessRoleFromSite(int siteId, int roleId) {
		Query q = entityManager.createQuery("delete from SiteRoleMpg where siteId = ?1"
				+ " and accessRoleId =?2");
		q.setParameter(1, siteId);
		q.setParameter(2, roleId);
		q.executeUpdate();
	}
	
	public void deleteAdmitDischargeRoleToSite(int siteId, int roleId) {
		String qr = "delete from SiteRoleMpg where siteId = ?1 and admitDischargeRoleId = ?2";
		Query q = entityManager.createQuery(qr);
		q.setParameter(1, siteId);
		q.setParameter(2, roleId);
		q.executeUpdate();
	}
	
	public void addAccessRoleToSite(int siteId, int roleId) {
		SiteRoleMpg siteRoleMpg = new SiteRoleMpg();
		siteRoleMpg.setAccessRoleId(roleId);
		siteRoleMpg.setSiteId(siteId);
		siteRoleMpg.setCrtDt(new Timestamp(new Date().getTime()));
		entityManager.persist(siteRoleMpg);
	}
	
	public void addAdmitDischargeRoleToSite(int siteId, int roleId) {
		SiteRoleMpg siteRoleMpg = new SiteRoleMpg();
		siteRoleMpg.setAdmitDischargeRoleId(roleId);
		siteRoleMpg.setSiteId(siteId);
		siteRoleMpg.setCrtDt(new Timestamp(new Date().getTime()));
		entityManager.persist(siteRoleMpg);
	}
	
	public List<Integer> getSiteIdByAccRoleId(List<Integer> roleIds) {
		if (roleIds == null || roleIds.size() == 0) {
			return new ArrayList<Integer>();
		}
		Query q = entityManager.createQuery("select siteId from SiteRoleMpg where accessRoleId in (:roleIds)");
		q.setParameter("roleIds", roleIds);
		
		return q.getResultList();
	}
	
	public List<Integer> getSiteIdByAdmitRoleId(List<Integer> roleIds) {
		if (roleIds == null || roleIds.size() == 0) {
			return new ArrayList<Integer>();
		}
		Query q = entityManager.createQuery("select siteId from SiteRoleMpg where admitDischargeRoleId in (:roleIds)");
		q.setParameter("roleIds", roleIds);
		
		return q.getResultList();
	}
	
	public List<Integer> getSiteIdAccRoleNotNull() {
		Query q = entityManager.createQuery("select distinct siteId from SiteRoleMpg where accessRoleId != NULL && accessRoleId != 0");
		return q.getResultList();
	}
	
	public List<Integer> getAccRoleIsBySiteId(int siteId) {
		Query q = entityManager.createQuery("select accessRoleId from SiteRoleMpg where siteId = :siteId and accessRoleId != null");
		q.setParameter("siteId", siteId);
		
		return q.getResultList();
	}

	public List<Integer> getAdmitRoleIsBySiteId(int siteId) {
		// TODO Auto-generated method stub
		Query q = entityManager.createQuery("select admitDischargeRoleId from SiteRoleMpg where siteId = :siteId and admitDischargeRoleId != null");
		q.setParameter("siteId", siteId);
		
		return q.getResultList();
	}
}

/*
* 
* Copyright (c) 2001-2002. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved. *
* This software is published under the GPL GNU General Public License. 
* This program is free software; you can redistribute it and/or 
* modify it under the terms of the GNU General Public License 
* as published by the Free Software Foundation; either version 2 
* of the License, or (at your option) any later version. * 
* This program is distributed in the hope that it will be useful, 
* but WITHOUT ANY WARRANTY; without even the implied warranty of 
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
* GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
* along with this program; if not, write to the Free Software 
* Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
* 
* <OSCAR TEAM>
* 
* This software was written for 
* Centre for Research on Inner City Health, St. Michael's Hospital, 
* Toronto, Ontario, Canada 
*/

package com.quatro.service.security;

import java.util.ArrayList;
import java.util.List;

import com.quatro.dao.security.SecProviderDao;
import com.quatro.dao.security.SecroleDao;
import com.quatro.dao.security.SecurityDao;
import com.quatro.dao.security.SecuserroleDao;
import com.quatro.model.security.SecProvider;
import com.quatro.model.security.Secrole;
import com.quatro.model.security.Security;
import com.quatro.model.security.Secuserrole;
import com.quatro.web.admin.UserSearchFormBean;




public class UsersManager {

	private SecroleDao secroleDao;
	private SecurityDao securityDao;	
	private SecProviderDao secProviderDao;
	private SecuserroleDao secuserroleDao;
	
	public void setSecProviderDao(SecProviderDao secProviderDao) {
		this.secProviderDao = secProviderDao;
	}
	public List getProfile(String providerNo) {
		return securityDao.getProfile(providerNo);
	}
	public List getAllUsers() {
		return securityDao.getAllUsers();
	}
	public List search(UserSearchFormBean formBean) {
		return securityDao.search(formBean);
	}
	public Security getUser(String userName) {
		Security user = null;
		List list = securityDao.findByUserName(userName);
		if (list.size() > 0) {
			user = (Security) list.get(0);
		}
		return user;
	}
	public List getUserByProviderNo(String providerNo) {
		return securityDao.findByProviderNo(providerNo);
	}
	public SecProvider getProviderByProviderNo(String providerNo) {
		return secProviderDao.findById(providerNo);
	}
	public SecProvider getProviderByProviderNo(String providerNo,String status) {
		return secProviderDao.findById(providerNo,status);
	}
	
	public List getRoles() {
		return secroleDao.getRoles();
	}
	public Secrole getRole(String id) {
		return secroleDao.getRole(Integer.valueOf(id));
	}
	public Secrole getRoleByRolename(String roleName) {
		return secroleDao.getRoleByName(roleName);
	}
	public void save(Secrole secrole) {
		secroleDao.save(secrole);
	}
	public void saveRolesToUser(List list, String providerNo) {
		List existLst = secuserroleDao.findByProviderNo(providerNo);
		saveAll(list, existLst);
	}
	
	public void saveStaffToProgram(List list, String orgcd) {
		List existLst = secuserroleDao.findByOrgcd(orgcd,false);
		saveAll(list, existLst);
		
	}
	public void saveAll(List newLst, List existLst) {
		
		ArrayList lstForDelete = new ArrayList();
		if(existLst.size()>0){
			
			for(int i = 0; i < existLst.size(); i++){
				boolean keepIt = false;
				Secuserrole sur1 = (Secuserrole)existLst.get(i);
				for(int j = 0; j < newLst.size(); j++){
					Secuserrole sur2 = (Secuserrole)newLst.get(j);
					if(compare(sur1, sur2)){
						keepIt = true;
						break;
					}
				}
				if(!keepIt){
					lstForDelete.add(sur1);
				}
				
			}
			for( int i = 0; i < lstForDelete.size(); i++){
				secuserroleDao.delete((Secuserrole)lstForDelete.get(i));				
			}
			
		}
		
		secuserroleDao.saveAll(newLst);
	}
	
	public boolean compare(Secuserrole sur1, Secuserrole sur2){
		boolean isSame = false;
		if(sur1.getOrgcd().equals(sur2.getOrgcd()) && 
				sur1.getProviderNo().equals(sur2.getProviderNo()) && 
				sur1.getRoleName().equals(sur2.getRoleName()))
			isSame = true;
		return isSame;
	}
	public List getSecUserRoles(String providerNo) {
		return secuserroleDao.findByProviderNo(providerNo);
	}
	
	public void save(SecProvider provider, Security user) {
		secProviderDao.saveOrUpdate(provider);
		user.setProviderNo(provider.getProviderNo());
		securityDao.saveOrUpdate(user);
		
	}

	public void updateUser(Security user) {
		securityDao.saveOrUpdate(user);
	}
	
	public void setSecurityDao(SecurityDao securityDao) {
		this.securityDao = securityDao;
	}
	public void setSecroleDao(SecroleDao dao) {
		this.secroleDao = dao;
	}
	public void setSecuserroleDao(SecuserroleDao secuserroleDao) {
		this.secuserroleDao = secuserroleDao;
	}
	
	
}

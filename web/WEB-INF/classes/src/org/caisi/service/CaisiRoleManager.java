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

package org.caisi.service;

import java.util.List;

import org.caisi.dao.AccessTypeDAO;
import org.caisi.dao.CaisiRoleDAO;
import org.caisi.model.CaisiRole;
import org.caisi.model.Role;
import org.oscarehr.PMmodule.dao.ProviderDao;

public class CaisiRoleManager {

    private CaisiRoleDAO caisiRoleDAO;
    private AccessTypeDAO accessTypeDAO;
    private ProviderDao providerDAO;

    public void setAccessTypeDAO(AccessTypeDAO dao) {
        this.accessTypeDAO = dao;
    }

    public void setCaisiRoleDAO(CaisiRoleDAO roleDAO) {
        this.caisiRoleDAO = roleDAO;
    }

    public void setProviderDAO(ProviderDao providerDAO) {
        this.providerDAO = providerDAO;
    }

    public List getProviders() {
        return this.providerDAO.getProviders();
    }

    public void saveRoleAssignment(CaisiRole role) {
        this.caisiRoleDAO.saveRoleAssignment(role);
    }

    public String saveRole(Role role) {
        String rolename = role.getName();
        if ("".equals(rolename.trim())) return "role.empty";
        if (caisiRoleDAO.hasExist(rolename)) return "role.hasexist";
        this.caisiRoleDAO.saveRole(role);

        accessTypeDAO.addAccessType("write " + role.getName() + " issues", "access");
        accessTypeDAO.addAccessType("read " + role.getName() + " issues", "access");
        accessTypeDAO.addAccessType("read " + role.getName() + " notes", "access");
        accessTypeDAO.addAccessType("read " + role.getName() + " ticklers", "access");
        
        return null;
    }

    public List getRoles() {
        return this.caisiRoleDAO.getRoles();
    }

    public void assignRole(String provider_no, String role_no) {
        //if choose 0 as role id, do nothing.
        if (Integer.valueOf(role_no).intValue() == 0) return;
        CaisiRole cr = new CaisiRole();
        cr.setProvider_no(provider_no);
        cr.setRole_id(Integer.valueOf(role_no).intValue());
        saveRoleAssignment(cr);
    }
}

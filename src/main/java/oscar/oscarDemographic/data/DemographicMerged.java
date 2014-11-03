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


/*
 * DemographicMergedDAO.java
 *
 * Created on September 14, 2007, 1:13 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package oscar.oscarDemographic.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.oscarehr.common.dao.DemographicMergedDao;
import org.oscarehr.common.dao.RecycleBinDao;
import org.oscarehr.common.dao.SecObjPrivilegeDao;
import org.oscarehr.common.model.RecycleBin;
import org.oscarehr.common.model.SecObjPrivilege;
import org.oscarehr.common.model.SecObjPrivilegePrimaryKey;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

/**
 *
 * @author wrighd
 */
public class DemographicMerged {

    Logger logger = MiscUtils.getLogger();
    private DemographicMergedDao dao = SpringUtils.getBean(DemographicMergedDao.class);
    private SecObjPrivilegeDao secObjPrivilegeDao = SpringUtils.getBean(SecObjPrivilegeDao.class);
    private RecycleBinDao recycleBinDao = SpringUtils.getBean(RecycleBinDao.class);
    
    public DemographicMerged() {
    }

    public void Merge(String demographic_no, String head) {

    	org.oscarehr.common.model.DemographicMerged dm = new org.oscarehr.common.model.DemographicMerged();
    	
    	 // always merge the head of records that have already been merged to the new head
        String record_head = getHead(demographic_no);
        if (record_head == null)
            dm.setDemographicNo(Integer.parseInt( demographic_no ));
        else
            dm.setDemographicNo(Integer.parseInt(record_head));

        dm.setMergedTo(Integer.parseInt( head ));
       
        dao.persist(dm);
        
        SecObjPrivilege sop = new SecObjPrivilege();
        SecObjPrivilegePrimaryKey pk = new SecObjPrivilegePrimaryKey();
        pk.setRoleUserGroup("_all");
        pk.setObjectName("_eChart$"+demographic_no);
        sop.setId(pk);
        sop.setPrivilege("|or|");
        sop.setPriority(0);
        sop.setProviderNo("0");
        secObjPrivilegeDao.persist(sop);
  
    }

    public void UnMerge(String demographic_no, String curUser_no) {

    	List<org.oscarehr.common.model.DemographicMerged> dms = dao.findByDemographicNo(Integer.parseInt(demographic_no));
    	for(org.oscarehr.common.model.DemographicMerged dm:dms) {
    		dm.setDeleted(1);
    		dao.merge(dm);
    	}
    	
    	 String privilege = "";
         String priority = "";
         String provider_no = "";
    	
    	List<SecObjPrivilege> sops = this.secObjPrivilegeDao.findByRoleUserGroupAndObjectName("_all", "_eChart$"+demographic_no);
    	for(SecObjPrivilege sop:sops) {
    		privilege = sop.getPrivilege();
    		priority = String.valueOf(sop.getPriority());
    		provider_no = sop.getProviderNo();
    		secObjPrivilegeDao.remove(sop.getId());
    	}
    	
    	RecycleBin rb = new RecycleBin();
    	rb.setProviderNo(curUser_no);
    	rb.setUpdateDateTime(new Date());
    	rb.setTableName("secObjPrivilege");
    	rb.setKeyword("_all|_eChart$"+ demographic_no);
    	rb.setTableContent("<roleUserGroup>_all</roleUserGroup>" + "<objectName>_eChart$" + demographic_no + "</objectName><privilege>" + privilege + "</privilege>" + "<priority>" + priority + "</priority><provider_no>" + provider_no + "</provider_no>");
    	recycleBinDao.persist(rb);

    }
    
    public String getHead(String demographic_no) {
    	Integer result = getHead(Integer.parseInt(demographic_no));
    	if(result != null) {
    		return result.toString();
    	}
    	return null;
    }


    public Integer getHead(Integer demographic_no)  {
    	Integer head = null;

    	List<org.oscarehr.common.model.DemographicMerged> dms = dao.findCurrentByDemographicNo(demographic_no);
    	for(org.oscarehr.common.model.DemographicMerged dm:dms) {
    		head = dm.getMergedTo();
    	}
        
        if (head != null)
            head = getHead(head);
        else
            head = demographic_no;

        return head;
    }

    public ArrayList<String> getTail(String demographic_no) {
    	ArrayList<String> tailArray = new ArrayList<String>();

    	List<org.oscarehr.common.model.DemographicMerged> dms = dao.findCurrentByMergedTo(Integer.parseInt(demographic_no));
    	for(org.oscarehr.common.model.DemographicMerged dm:dms) {
    		tailArray.add(String.valueOf(dm.getDemographicNo()));
    	}
    	
        int size = tailArray.size();
        for (int i=0; i < size; i++){
            tailArray.addAll(getTail(  tailArray.get(i) ));
        }

        return tailArray;

    }
}

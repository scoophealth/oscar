/**
 * Copyright (c) 2006-. OSCARservice, OpenSoft System. All Rights Reserved.
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
 */

package oscar.oscarProvider.data;

import java.util.Properties;

import org.apache.log4j.Logger;
import org.oscarehr.common.dao.BillCenterDao;
import org.oscarehr.common.dao.ProviderBillCenterDao;
import org.oscarehr.common.model.BillCenter;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

/**
 *
 * @author Toby
 */
public class ProviderBillCenter {
	
	Logger log = MiscUtils.getLogger();
	ProviderBillCenterDao dao = SpringUtils.getBean(ProviderBillCenterDao.class);
	BillCenterDao bcDao = SpringUtils.getBean(BillCenterDao.class);
	
    
    public ProviderBillCenter() {
    }
    
    public boolean hasBillCenter(String provider_no){
        boolean retval = false;
        org.oscarehr.common.model.ProviderBillCenter pbc = dao.find(provider_no);
        if(pbc != null && pbc.getBillCenterCode() != null && pbc.getBillCenterCode().length()>0) {
        	retval=true;
        }
        return retval;
    }

    public boolean hasProvider(String provider_no){
    	boolean retval = false;
        if(dao.find(provider_no) != null) {
        	retval=true;
        }
        return retval;
    }
    
    public void addBillCenter(String provider_no, String billCenterCode){
    	org.oscarehr.common.model.ProviderBillCenter pbc = new org.oscarehr.common.model.ProviderBillCenter();
    	pbc.setProviderNo(provider_no);
    	pbc.setBillCenterCode(billCenterCode);
    	dao.persist(pbc);
    }
    
    public String getBillCenter(String provider_no){
        String billCenterCode = "";
        
        org.oscarehr.common.model.ProviderBillCenter pbc = dao.find(provider_no);
        if(pbc != null) {
        	billCenterCode = pbc.getBillCenterCode();
        }
        return billCenterCode;
    }
    
    public void updateBillCenter(String provider_no, String billCenterCode){
        if (!hasProvider(provider_no)) {
            addBillCenter(provider_no, billCenterCode);
        } else {
        	org.oscarehr.common.model.ProviderBillCenter pbc = dao.find(provider_no);
            if(pbc != null) {
            	pbc.setBillCenterCode(billCenterCode);
            	dao.merge(pbc);
            }
        }    
    }
    
    public Properties getAllBillCenter(){
        Properties allBillCenter = new Properties();
        
        for(BillCenter bc:bcDao.findAll()) {
        	allBillCenter.setProperty(bc.getBillCenterCode(), bc.getBillCenterDesc());
        }
   
        return allBillCenter;
    }
    
}

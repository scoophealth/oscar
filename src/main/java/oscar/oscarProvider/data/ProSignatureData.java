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


package oscar.oscarProvider.data;


import org.oscarehr.casemgmt.model.ProviderExt;
import org.oscarehr.common.dao.ProviderExtDao;

import org.oscarehr.util.SpringUtils;


public class ProSignatureData {
	
	private ProviderExtDao providerExtDao = SpringUtils.getBean(ProviderExtDao.class);

    public boolean hasSignature(String proNo){
       boolean retval = false;
       
      ProviderExt pe =  providerExtDao.find(proNo);
      if(pe!=null && pe.getSignature()!=null) {
    	  retval=true;
      }
       
       return retval;
    }
    
    public String getSignature(String providerNo){
       String retval = "";
       ProviderExt pe =  providerExtDao.find(providerNo);
       if(pe != null) {
    	   retval = pe.getSignature();
       }
       return retval;
    }
   
    public void enterSignature(String providerNo,String signature){
       
	if (hasSignature(providerNo)){
           updateSignature(providerNo,signature);
        }else{
           addSignature(providerNo,signature);
        }

    }


    private void addSignature(String providerNo,String signature){
    	ProviderExt pe = new ProviderExt();
    	pe.setProviderNo(providerNo);
    	pe.setSignature(signature);
    	providerExtDao.persist(pe);
    }
 
    private void updateSignature(String providerNo,String signature){
    	ProviderExt pe =  providerExtDao.find(providerNo);
    	if(pe != null) {
    		pe.setSignature(signature);
    		providerExtDao.merge(pe);
    	}
    }
}

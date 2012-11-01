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


package oscar.oscarEncounter.immunization.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.common.dao.ImmunizationsDao;
import org.oscarehr.common.model.Immunizations;
import org.oscarehr.common.model.Provider;
import org.oscarehr.util.SpringUtils;

public class EctImmImmunizationData
{
	private static ImmunizationsDao dao = SpringUtils.getBean(ImmunizationsDao.class);
	private static ProviderDao providerDao = SpringUtils.getBean(ProviderDao.class);

    public String getImmunizations(String demographicNo) {
        String sRet = null;
        List<Immunizations> is = dao.findCurrentByDemographicNo(Integer.parseInt(demographicNo));
        for(Immunizations i:is) {
        	sRet = i.getImmunizations();
        }
   
        return sRet;
    }
    
    public static boolean hasImmunizations(String demographicNo) {        
        boolean retval = false;
        List<Immunizations> is = dao.findCurrentByDemographicNo(Integer.parseInt(demographicNo));
        if(!is.isEmpty())
        	retval=true;
        
        return retval;
    }

    public void saveImmunizations(String demographicNo, String providerNo, String immunizations) {
        Immunizations i = new Immunizations();
        i.setDemographicNo(Integer.parseInt(demographicNo));
        i.setProviderNo(providerNo);
        i.setImmunizations(immunizations);
        i.setSaveDate(new Date());
        i.setArchived(0);
        dao.persist(i);
        
        List<Immunizations> is = dao.findCurrentByDemographicNo(Integer.parseInt(demographicNo));
        for(Immunizations t:is) {
        	if(!t.getId().equals(i.getId())) {
        		t.setArchived(1);
        		dao.merge(t);
        	}
        }
    }

    public String[] getProviders() {
        List<String> vRet = new ArrayList<String>();
        List<Provider> providers = providerDao.getActiveProviders();
        for(Provider p:providers) {
        	String data = p.getProviderNo() + "/" + p.getLastName() + ", " + p.getFirstName();
        	vRet.add(data);
        }
        
       
        String ret[] = new String[vRet.size()];
        ret = vRet.toArray(ret);
        return ret;
    }
}

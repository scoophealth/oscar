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


package oscar.oscarRx.data;

import java.util.ArrayList;
import java.util.List;

import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.common.dao.ClinicDAO;
import org.oscarehr.common.dao.UserPropertyDAO;
import org.oscarehr.common.model.Clinic;
import org.oscarehr.common.model.UserProperty;
import org.oscarehr.util.SpringUtils;

import oscar.SxmlMisc;

public class RxProviderData {

	private ProviderDao providerDao = (ProviderDao)SpringUtils.getBean("providerDao");
	private UserPropertyDAO userPropertyDao = (UserPropertyDAO)SpringUtils.getBean("UserPropertyDAO");
	private ClinicDAO clinicDao = (ClinicDAO)SpringUtils.getBean("clinicDAO");
	
	public List<Provider> getAllProviders() {
		List<org.oscarehr.common.model.Provider> providers = providerDao.getActiveProviders();
		ArrayList<Provider> results = new ArrayList<Provider>();
		for (org.oscarehr.common.model.Provider p : providers) {
			results.add(convertProvider(p));
		}
		return results;
	}
	
    public Provider getProvider(String providerNo) {
        return convertProvider(providerDao.getProvider(providerNo));
    }
    
    public Provider convertProvider(org.oscarehr.common.model.Provider p) {
    	String surname=null, firstName=null,  clinicName=null, clinicAddress=null, clinicCity=null, clinicPostal=null, clinicPhone=null, clinicFax=null, clinicProvince=null, practitionerNo=null;
    	boolean useFullAddress=true;
        //Get Provider from database

        Clinic clinic = clinicDao.getClinic();
        if(clinic != null) {
        	clinicName = clinic.getClinicName();
        	clinicAddress = clinic.getClinicAddress();
        	clinicCity = clinic.getClinicCity();
        	clinicPostal = clinic.getClinicPostal();
        	clinicPhone = clinic.getClinicPhone();
        	clinicProvince = clinic.getClinicProvince();
        	clinicFax = clinic.getClinicFax();
        }
        
        Provider prov = null;
        String providerNo = null;
        
        if(p != null) {
        	surname = p.getLastName();
        	firstName = p.getFirstName();
        	practitionerNo = p.getPractitionerNo();
        	/*
        	if(firstName.indexOf("Dr.")<0) {
                firstName = "Dr. " + firstName;
            }*/
        	
        	if(p.getWorkPhone() != null && p.getWorkPhone().length()>0) {
        		clinicPhone = p.getWorkPhone();
        	}
        	
        	if(p.getComments() != null && p.getComments().length()>0) {
        		String pFax = SxmlMisc.getXmlContent(p.getComments(), "xml_p_fax");
        		if(pFax != null && pFax.length()>0) {
        			clinicFax = pFax;
        		}
        	}
        	
        	if(p.getAddress() != null && p.getAddress().length()>0) {
        		clinicAddress = p.getAddress();
        		useFullAddress=false;
        	}
        
        	providerNo = p.getProviderNo();
        	UserProperty prop = null;
        
        	prop = userPropertyDao.getProp(providerNo, "faxnumber");
        	if(prop != null && prop.getValue().length()>0) {
        		clinicFax = prop.getValue();
        	}
        
        	prop = userPropertyDao.getProp(providerNo, "rxPhone");
        	if(prop != null && prop.getValue().length()>0) {
        		clinicPhone = prop.getValue();
        	}
        
        	prop = userPropertyDao.getProp(providerNo, "rxAddress");
        	if(prop != null && prop.getValue().length()>0) {
        		//we're going to override with the preference address
        		clinicAddress = prop.getValue();
        		clinicCity = readProperty(providerNo,"rxCity");
        		clinicProvince = readProperty(providerNo,"rxProvince");
        		clinicPostal = readProperty(providerNo,"rxPostal");
        		useFullAddress=true;
        	}

        }
        
        prov =  new Provider(providerNo, surname, firstName, clinicName, clinicAddress,
                clinicCity, clinicPostal, clinicPhone, clinicFax, clinicProvince, practitionerNo);
        
        if(!useFullAddress) {
        	prov.fullAddress=false;
        }
        
        return prov;
    }
    
    private String readProperty(String providerNo, String key) {
    	UserProperty prop = userPropertyDao.getProp(providerNo, key);
        if(prop != null) {
        	return prop.getValue();
        }
        return "";
    }

    public class Provider{
    	boolean fullAddress=true;
    	
        String providerNo;
        String surname;
        String firstName;
        String clinicName;
        String clinicAddress;
        String clinicCity;
        String clinicPostal;
        String clinicPhone;
        String clinicFax;
        String clinicProvince;
        String practitionerNo;

        public Provider(String providerNo, String surname, String firstName,
        String clinicName, String clinicAddress, String clinicCity,
        String clinicPostal, String clinicPhone, String clinicFax, String practitionerNo){
            this.providerNo = providerNo;
            this.surname = surname;
            this.firstName = firstName;
            this.clinicName = clinicName;
            this.clinicAddress = clinicAddress;
            this.clinicCity = clinicCity;
            this.clinicPostal = clinicPostal;
            this.clinicPhone = clinicPhone;
            this.clinicFax = clinicFax;
	    this.practitionerNo = practitionerNo;
        }

        public Provider(String providerNo, String surname, String firstName,
        String clinicName, String clinicAddress, String clinicCity,
        String clinicPostal, String clinicPhone, String clinicFax,String clinicProvince, String practitionerNo){
        	this(providerNo,surname,firstName,clinicName,clinicAddress,clinicCity,clinicPostal,clinicPhone,clinicFax,practitionerNo);
            this.clinicProvince = clinicProvince;
        }


        public String getProviderNo(){
            return this.providerNo;
        }

        public String getSurname(){
            return this.surname;
        }

        public String getFirstName(){
            return this.firstName;
        }

        public String getClinicName(){
            return this.clinicName;
        }

        public String getClinicAddress(){
            return this.clinicAddress;
        }

        public String getClinicCity(){
            return this.clinicCity;
        }

        public String getClinicPostal(){
            return this.clinicPostal;
        }

        public String getClinicPhone(){
            return this.clinicPhone;
        }

        public String getClinicFax(){
            return this.clinicFax;
        }

        public String getClinicProvince(){
            return this.clinicProvince;
        }

		public String getPractitionerNo() {
		   return this.practitionerNo;
		}
		
		public String getFullAddress() {
			if(fullAddress)
				return (getClinicAddress() + "  " + getClinicCity() + "   " + getClinicProvince() + "  " + getClinicPostal()).trim();
			else
				return getClinicAddress().trim();
		}

    }
}

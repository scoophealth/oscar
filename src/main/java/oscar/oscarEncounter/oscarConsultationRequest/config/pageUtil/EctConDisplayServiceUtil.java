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


package oscar.oscarEncounter.oscarConsultationRequest.config.pageUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.oscarehr.common.dao.ConsultationServiceDao;
import org.oscarehr.common.dao.ProfessionalSpecialistDao;
import org.oscarehr.common.dao.ServiceSpecialistsDao;
import org.oscarehr.common.model.ConsultationServices;
import org.oscarehr.common.model.ProfessionalSpecialist;
import org.oscarehr.common.model.ServiceSpecialists;
import org.oscarehr.util.SpringUtils;

import oscar.util.ConversionUtils;

public class EctConDisplayServiceUtil
{
	private ConsultationServiceDao consultationServiceDao = (ConsultationServiceDao)SpringUtils.getBean("consultationServiceDao");
	
    public Vector<String> fNameVec;
    public Vector<String> lNameVec;
    public Vector<String> proLettersVec;
    public Vector<String> addressVec;
    public Vector<String> phoneVec;
    public Vector<String> faxVec;
    public Vector<String> websiteVec;
    public Vector<String> emailVec;
    public Vector<String> specTypeVec;
    public Vector<String> specIdVec;
    public Vector<String> serviceName;
    public Vector<String> serviceId;
    public ArrayList<String> referralNoVec;
	
    public String getServiceDesc(String serId)
    {
        String retval = new String();

        ConsultationServices cs = consultationServiceDao.find(Integer.parseInt(serId));
        if(cs != null) {
        	retval = cs.getServiceDesc();
        }

        return retval;
    }
    
    public void estSpecialist() {
    	estSpecialistVector();
    }

    public void estSpecialistVector()
    {
        fNameVec = new Vector<String>();
        lNameVec = new Vector<String>();
        proLettersVec = new Vector<String>();
        addressVec = new Vector<String>();
        phoneVec = new Vector<String>();
        faxVec = new Vector<String>();
        websiteVec = new Vector<String>();
        emailVec = new Vector<String>();
        specTypeVec = new Vector<String>();
        specIdVec = new Vector<String>();
        referralNoVec = new ArrayList<String>();
        
        ProfessionalSpecialistDao dao = SpringUtils.getBean(ProfessionalSpecialistDao.class);        
        for(ProfessionalSpecialist ps : dao.findAll()) {
            fNameVec.add(ps.getFirstName());
            lNameVec.add(ps.getLastName());
            proLettersVec.add(ps.getProfessionalLetters());
            addressVec.add(ps.getStreetAddress());
            phoneVec.add(ps.getPhoneNumber());
            faxVec.add(ps.getFaxNumber());
            websiteVec.add(ps.getWebSite());
            emailVec.add(ps.getEmailAddress());
            specTypeVec.add(ps.getSpecialtyType());
            specIdVec.add(ps.getId().toString());
        }
    }

    public Vector<String> getSpecialistInField(String serviceId) {
        Vector<String> vector = new Vector<String>();
        ServiceSpecialistsDao dao = SpringUtils.getBean(ServiceSpecialistsDao.class);
        
        for(ServiceSpecialists ss : dao.findByServiceId(ConversionUtils.fromIntString(serviceId))) {
        	vector.add("" + ss.getId().getSpecId());
        }
        return vector;
    }

    public void estServicesVectors()
    {
        serviceId = new Vector<String>();
        serviceName = new Vector<String>();

        List<ConsultationServices> services = consultationServiceDao.findActive();
        for(ConsultationServices service:services) {
        	serviceId.add(String.valueOf(service.getServiceId()));
        	serviceName.add(service.getServiceDesc());
        }

    }
}

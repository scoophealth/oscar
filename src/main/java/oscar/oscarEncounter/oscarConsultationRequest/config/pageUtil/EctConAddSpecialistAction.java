/*
 * 
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
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
 * This software was written for the 
 * Department of Family Medicine 
 * McMaster University 
 * Hamilton 
 * Ontario, Canada 
 */
package oscar.oscarEncounter.oscarConsultationRequest.config.pageUtil;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.oscarehr.common.dao.ProfessionalSpecialistDao;
import org.oscarehr.common.model.ProfessionalSpecialist;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public class EctConAddSpecialistAction extends Action {
    
	private static final Logger logger=MiscUtils.getLogger();
	
	private ProfessionalSpecialistDao professionalSpecialistDao=(ProfessionalSpecialistDao) SpringUtils.getBean("professionalSpecialistDao");
	
	@Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {        
    	
    	ProfessionalSpecialist professionalSpecialist=null;    	
        EctConAddSpecialistForm addSpecailistForm = (EctConAddSpecialistForm)form;
        
        int whichType = addSpecailistForm.getwhichType();
        if(whichType == 1) //create
        {
        	professionalSpecialist=new ProfessionalSpecialist();
            populateFields(professionalSpecialist, addSpecailistForm);
            professionalSpecialistDao.persist(professionalSpecialist);
        }
        else if (whichType == 2) // update
        {
            Integer specId = Integer.parseInt(addSpecailistForm.getSpecId());
        	professionalSpecialist=professionalSpecialistDao.find(specId);
            populateFields(professionalSpecialist, addSpecailistForm);
            professionalSpecialistDao.merge(professionalSpecialist);
        }
        else
        {
        	logger.error("missed a case, whichType="+whichType);
        }
        
        addSpecailistForm.resetForm();
        
        String added=""+professionalSpecialist.getFirstName()+" "+professionalSpecialist.getLastName();
        request.setAttribute("Added", added);
        return mapping.findForward("success");
    }

	private void populateFields(ProfessionalSpecialist professionalSpecialist, EctConAddSpecialistForm addSpecailistForm) {
	    professionalSpecialist.setFirstName(addSpecailistForm.getFirstName());
        professionalSpecialist.setLastName(addSpecailistForm.getLastName());
        professionalSpecialist.setProfessionalLetters(addSpecailistForm.getProLetters());

        String address = addSpecailistForm.getAddress();        
        StringBuilder sb = new StringBuilder();              
        for (int i =0 ; i < address.length(); i++){
            int a = address.charAt(i);           
            if ( a == 13 || a == 10 ){
                sb.append(" ");                                
            }else{
                sb.append((char)a);                
            }            
        }
        address = sb.toString();        
        professionalSpecialist.setStreetAddress(addSpecailistForm.getAddress());
        
        professionalSpecialist.setPhoneNumber(addSpecailistForm.getPhone());
        professionalSpecialist.setFaxNumber(addSpecailistForm.getFax());
        professionalSpecialist.setWebSite(addSpecailistForm.getWebsite());
        professionalSpecialist.setEmailAddress(addSpecailistForm.getEmail());
        professionalSpecialist.setSpecialtyType(addSpecailistForm.getSpecType());
        professionalSpecialist.seteDataUrl(addSpecailistForm.geteDataUrl());
        professionalSpecialist.seteDataOscarKey(addSpecailistForm.geteDataOscarKey());
        professionalSpecialist.seteDataServiceKey(addSpecailistForm.geteDataServiceKey());
        professionalSpecialist.seteDataServiceName(addSpecailistForm.geteDataServiceName());
        professionalSpecialist.setAnnotation(addSpecailistForm.getAnnotation());
    }
}



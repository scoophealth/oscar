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
package org.oscarehr.admin.web;

import java.util.Date;

import org.apache.log4j.Logger;
import org.oscarehr.common.dao.DemographicContactDao;
import org.oscarehr.common.dao.ProfessionalContactDao;
import org.oscarehr.common.dao.ProfessionalSpecialistDao;
import org.oscarehr.common.model.DemographicContact;
import org.oscarehr.common.model.ProfessionalContact;
import org.oscarehr.common.model.ProfessionalSpecialist;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

public class MigrateProfessionalContactsHelper {

	static Logger logger = MiscUtils.getLogger();
	static ProfessionalContactDao professionalContactDao = SpringUtils.getBean(ProfessionalContactDao.class);
	static ProfessionalSpecialistDao professionalSpecialistDao = (ProfessionalSpecialistDao)SpringUtils.getBean(ProfessionalSpecialistDao.class);
	static DemographicContactDao demographicContactDao = SpringUtils.getBean(DemographicContactDao.class);
	
	public static boolean doMigration() {
		logger.info("----------doing migration for professional contacts----------------");
		
		for(ProfessionalContact pc: professionalContactDao.findAll()) {
			ProfessionalSpecialist ps = new ProfessionalSpecialist();
			ps.setFirstName(pc.getFirstName());
			ps.setLastName(pc.getLastName());
			ps.setEmailAddress(pc.getEmail());
			ps.setFaxNumber(pc.getFax());
			ps.setLastUpdated(new Date());
			ps.setPhoneNumber(pc.getWorkPhone() + ((pc.getWorkPhoneExtension().length()>0)?" " + pc.getWorkPhoneExtension():""));
			ps.setReferralNo(pc.getCpso());
			ps.setSpecialtyType(pc.getSpecialty());
			
			StringBuilder annot = new StringBuilder();
			if(pc.getResidencePhone() != null && pc.getResidencePhone().length()>0) {
				annot.append("Res Phone: " + pc.getResidencePhone() + "\n");
			}
			if(pc.getCellPhone() != null && pc.getCellPhone().length()>0) {
				annot.append("Cell Phone:" + pc.getCellPhone() + "\n");
			}
			if(pc.getSystemId() != null && pc.getSystemId().length()>0) {
				annot.append("System Id:" + pc.getSystemId() + "\n");
			}
			if(pc.getNote() != null && pc.getNote().length()>0) {
				annot.append("Note:" + pc.getNote() + "\n");
			}
			ps.setAnnotation(annot.toString());
			
			StringBuilder address = new StringBuilder();
			address.append(pc.getAddress()+"\n");
			if(pc.getAddress2() != null && pc.getAddress2().length()>0) {
				address.append(pc.getAddress2() + "\n");
			}
			if(pc.getCity() != null && pc.getCity().length()>0) {
				address.append(pc.getCity());
			}
			if(pc.getProvince() != null && pc.getProvince().length()>0) {
				address.append("," + pc.getProvince() + "\n");
			} else {
				address.append("\n");
			}
			if(pc.getCountry() != null && pc.getCountry().length()>0) {
				address.append(pc.getCountry() + "\n");
			}
			if(pc.getPostal() != null && pc.getPostal().length()>0) {
				address.append(pc.getPostal() + "\n");
			}
			ps.setStreetAddress(address.toString());
			ps.setProfessionalLetters("");
			professionalSpecialistDao.persist(ps);
			logger.info("created professional specialist " + ps.getFirstName() + " " + ps.getLastName() + " with id " + ps.getId());
			
			
			//any demographic contact references?
			for(DemographicContact dc: demographicContactDao.findAllByContactIdAndCategoryAndType(pc.getId(),DemographicContact.CATEGORY_PROFESSIONAL,DemographicContact.TYPE_CONTACT)) {
				String lastContactId = dc.getContactId();
				dc.setContactId(ps.getId().toString());
				dc.setType(DemographicContact.TYPE_PROFESSIONALSPECIALIST);
				demographicContactDao.merge(dc);
				logger.info("updated demographic contact with id " + dc.getId() + " from contact id " + lastContactId + " to id " + dc.getContactId());
			}
			
			pc.setDeleted(true);
			professionalContactDao.merge(pc);
			logger.info("updated professional contact with id " + pc.getId() +" to deleted");
		}
		
		logger.info("----------completed migration for professional contacts----------------");
		
		
		return true;
	}
}

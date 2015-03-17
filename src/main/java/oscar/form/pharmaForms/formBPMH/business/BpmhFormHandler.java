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
package oscar.form.pharmaForms.formBPMH.business;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.common.dao.AllergyDao;
import org.oscarehr.common.dao.ContactSpecialtyDao;
import org.oscarehr.common.dao.DemographicContactDao;
import org.oscarehr.common.dao.DemographicCustDao;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.dao.DrugDao;
import org.oscarehr.common.dao.DrugReasonDao;
import org.oscarehr.common.dao.Icd9SynonymDao;
import org.oscarehr.common.dao.FormBPMHDao;
import org.oscarehr.common.dao.Icd9Dao;
import org.oscarehr.common.dao.OscarAppointmentDao;
import org.oscarehr.common.dao.ProfessionalContactDao;
import org.oscarehr.common.dao.ProfessionalSpecialistDao;
import org.oscarehr.common.model.Allergy;
import org.oscarehr.common.model.Appointment;
import org.oscarehr.common.model.Contact;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.DemographicContact;
import org.oscarehr.common.model.DemographicCust;
import org.oscarehr.common.model.Drug;
import org.oscarehr.common.model.DrugReason;
import org.oscarehr.common.model.Icd9Synonym;
import org.oscarehr.common.model.FormBPMH;
import org.oscarehr.common.model.Icd9;
import org.oscarehr.common.model.ProfessionalSpecialist;
import org.oscarehr.common.model.Provider;
import org.oscarehr.util.SpringUtils;

import oscar.form.pharmaForms.formBPMH.bean.BpmhDrug;
import oscar.form.pharmaForms.formBPMH.bean.BpmhFormBean;
import oscar.form.pharmaForms.formBPMH.util.CaseNoteParser;
import oscar.form.pharmaForms.formBPMH.util.JsonUtil;
import oscar.form.pharmaForms.formBPMH.util.SortDrugList;
import oscar.oscarRx.data.RxDrugData;

/*
 * Author: Dennis Warren 
 * Company: Colcamex Resources
 * Date: November 2014
 * For: UBC Pharmacy Clinic and McMaster Department of Family Medicine
 * 
 * 
 * This handler pulls specific data from Oscar's database related to a Demographic's Medication history.
 * Only certian data can be modified from a POST:
 * 
 * - Family Physician Fax and Phone numbers
 * - BpmhDrug Bean data: Why, When, How, Instruction 
 *   (Why, When and How have not been tested)
 * - Notes - summary notes at the bottom of a form
 *  
 * The following Order of use demonstrates a typical use for building a form into a
 * request scope.
 * RETRIEVE
 * 1. setFormHistory(formBPMH.id) to retrieve form history or setDemographic(Demographic.id) to create a new form
 * 2. populateFormBean() - requests required data from database.
 * 3. redirect bean to form
 * 
 * SAVE
 * 1. setDemographicNo(Demographic.id) - from request
 * 2. populateFormBean() - merges database data with edited request data
 * 3. saveFormHistory() - saves contents of form bean to the formBPMH data table.
 * 
 * Additional Notes:
 *  - data persists to the formBPMH table only.
 *  - Lazy collections of BpmhDrug and Allergy beans are persisted as JSON.
 *  - form history data is retrieved from the formBPMH table as JSON and then converted to beans.
 *  - if a NULL is sent to any of the set form bean signatures - parameters will be populated with
 *  	new data automatically.
 * 
 */
public class BpmhFormHandler {

	private static final Logger logger = Logger.getLogger("BpmhFormHandler");
	private static final String[] IGNORE_METHODS = new String [] {
		"handler", 
		"hibernateLazyInitializer",
		"hours", 
		"minutes", 
		"seconds"};
	private static final String PRIMARY_DR_TITLE = "family physician";

	private BpmhFormBean bpmhFormBean;
	private FormBPMH formBPMH;
	private int demographicNo;
	private AllergyDao allergyDao;
	private DemographicCustDao demographicCustDao;
	private DemographicDao demographicDao;
	private DrugDao drugDao;
	private DrugReasonDao drugReasonDao;
	private ProviderDao providerDao;
	private OscarAppointmentDao appointmentDao;
	private Icd9Dao icd9Dao;
	private Icd9SynonymDao dxCodeTranslationsDao;
	private FormBPMHDao formBPMHDao;
	private ProfessionalSpecialistDao professionalSpecialistDao;
	private ContactSpecialtyDao specialtyDao;
	private ProfessionalContactDao proContactDao;
	private DemographicContactDao demographicContactDao;
	
	/**
	 * Depends on: 
	 * (Set these after instantiation or auto-init with BpmhFormHandler(BpmhFormBean bpmhFormBean)): 
	 * 	 AllergyDao allergyDao;
	 *   DemographicCustDao demographicCustDao;
	 *   DemographicDao demographicDao;
	 *   DrugDao drugDao;
	 *   DrugReasonDao drugReasonDao;
	 *   ProviderDao providerDao;
	 *   OscarAppointmentDao appointmentDao;
	 *   Icd9Dao icd9Dao;
	 *   DxCodeTranslationsDao dxCodeTranslationsDao;
	 *   FormBPMHDao formBPMHDao;
	 *   ProfessionalSpecialistDao professionalSpecialistDao;
	 *   SpecialtyDao specialtyDao;
	 *   ProfessionalContactDao proContactDao;
	 *   DemographicContactDao demographicContactDao;
	 */
	public BpmhFormHandler() {
		// default constructor
	}
	
	public BpmhFormHandler(BpmhFormBean bpmhFormBean) {
		setBpmhFormBean(bpmhFormBean);
		_init();
	}

	private void _init() {		
		setDemographicDao( (DemographicDao) SpringUtils.getBean(DemographicDao.class) );
    	setAllergyDao( (AllergyDao) SpringUtils.getBean(AllergyDao.class) );
    	setDemographicCustDao( (DemographicCustDao) SpringUtils.getBean(DemographicCustDao.class) );
    	setDrugDao( (DrugDao) SpringUtils.getBean(DrugDao.class) );
    	setDrugReasonDao( (DrugReasonDao) SpringUtils.getBean(DrugReasonDao.class) );
    	setProviderDao( (ProviderDao) SpringUtils.getBean("providerDao") );
    	setAppointmentDao( (OscarAppointmentDao) SpringUtils.getBean(OscarAppointmentDao.class) );
    	setIcd9Dao( (Icd9Dao) SpringUtils.getBean(Icd9Dao.class) );
    	setDxCodeTranslationsDao((Icd9SynonymDao) SpringUtils.getBean(Icd9SynonymDao.class) );
    	setFormBPMHDao( (FormBPMHDao) SpringUtils.getBean(FormBPMHDao.class) ); 
    	setProfessionalSpecialistDao( (ProfessionalSpecialistDao) SpringUtils.getBean(ProfessionalSpecialistDao.class) );
    	setSpecialtyDao( (ContactSpecialtyDao) SpringUtils.getBean(ContactSpecialtyDao.class) );
    	setProContactDao( (ProfessionalContactDao) SpringUtils.getBean(ProfessionalContactDao.class) );
    	setDemographicContactDao( (DemographicContactDao) SpringUtils.getBean(DemographicContactDao.class) );
    }
	
    public void populateFormBean() { 
    	populateFormBean(null);
	}
	
	/**
	 * Populates and merges all parameters of a form bean based on directive(s):
	 *  - save form
	 *  - retrieve new form
	 *  - retrieve form history
	 */
	@SuppressWarnings("unchecked") // json conversion is unchecked.
    public void populateFormBean(BpmhFormBean bpmhFormBean) {
		if(bpmhFormBean != null) {
			setBpmhFormBean(bpmhFormBean);
		}
		
		Demographic formBeanDemographic = null;
		Provider formBeanProvider = null;
		String familyDrName = "";
		String familyDrPhone = "";
		String familyDrFax = "";
		List<BpmhDrug> formBeanDrugList = null;
		List<Allergy> formBeanAllergyList = null;

		// if form history is set, it has priority.
		if( getFormHistory() != null ) {
			
			logger.debug( "Retrieving form history number " + getFormHistory().getId() );
			
			setDemographicNo( getFormHistory().getDemographicNo() );
			getBpmhFormBean().setFormDate( getFormHistory().getFormCreated() );
			getBpmhFormBean().setFormId( getFormHistory().getId() + "" );
			getBpmhFormBean().setEditDate( getFormHistory().getFormEdited() );
			// provider is implicitly set from form table.
			formBeanProvider = getProviderDao().getProvider( getFormHistory().getProviderNo() + "" );
			familyDrName = getFormHistory().getFamilyDrName();
			familyDrPhone = getFormHistory().getFamilyDrPhone();
			familyDrFax = getFormHistory().getFamilyDrFax();
			// note is only set and saved from the form table.
			getBpmhFormBean().setNote( getFormHistory().getNote() );
			
			// allergies and drugs are stored as serialized JSON
			formBeanAllergyList = (List<Allergy>) JsonUtil.jsonToPojoList( getFormHistory().getAllergies(), Allergy.class );
			formBeanDrugList = (List<BpmhDrug>) JsonUtil.jsonToPojoList( getFormHistory().getDrugs(), BpmhDrug.class );
		}
		
		logger.debug( "Initializing BPMH form bean for demographic no " + getDemographicNo() );
		getBpmhFormBean().setDemographicNo( getDemographicNo() + "" );

		logger.debug("Populating form Bean... ");
		
		setFormBeanDemographic(formBeanDemographic);
		logger.debug("Setting Demographic");
		
		setFormBeanProvider(formBeanProvider);
		logger.debug("Setting Provider");
		
		setFormBeanFamilyDoctor(familyDrName, familyDrPhone, familyDrFax);
		logger.debug("Setting Family Doctor");
		
		setFormBeanDrugList( formBeanDrugList );
		logger.debug("Setting Drug List");
		
		setFormBeanAllergyList( formBeanAllergyList );
		logger.debug("Setting Allergy List");

	}
	
	public void setFormHistory(Integer formId) {
		this.formBPMH = getFormBPMHDao().find( formId );
	}
	
	public FormBPMH getFormHistory() {
		return this.formBPMH;
	}
	
	public Integer saveFormHistory() {
		return saveFormHistory( getBpmhFormBean() );
	}

	public Integer saveFormHistory(BpmhFormBean bpmhFormBean) {
		
		FormBPMH formBpmh = new FormBPMH(); // BPMH form JPA model bean
		
		// Init JSON strings
		String jsonDrugs = ""; 
		String jsonAllergies = "";
		
		// Posted drug and allergy lists
		List<BpmhDrug> drugList = bpmhFormBean.getDrugs();
		List<Allergy> allergyList = bpmhFormBean.getAllergies();

		formBpmh.setFormCreated( bpmhFormBean.getFormDate() );
		formBpmh.setFormEdited( bpmhFormBean.getEditDate() );
		
		formBpmh.setDemographicNo( Integer.parseInt( bpmhFormBean.getDemographicNo() ) );
		formBpmh.setProviderNo( Integer.parseInt( bpmhFormBean.getProvider().getProviderNo() ) );
		formBpmh.setNote( bpmhFormBean.getNote() );
		formBpmh.setFamilyDrName( bpmhFormBean.getFamilyDrName() );
		formBpmh.setFamilyDrPhone( bpmhFormBean.getFamilyDrPhone() );
		formBpmh.setFamilyDrFax( bpmhFormBean.getFamilyDrFax() );

		if( drugList != null ) {
			jsonDrugs = JsonUtil.pojoCollectionToJson( drugList, IGNORE_METHODS );
			logger.debug( "JSON serialization of drugs " + jsonDrugs );
		}
		
		if( allergyList != null ) {
			jsonAllergies = JsonUtil.pojoCollectionToJson( allergyList, IGNORE_METHODS );
			logger.debug( "JSON serialization of allergies " + jsonAllergies );
		}
		
		formBpmh.setAllergies( jsonAllergies );
		formBpmh.setDrugs( jsonDrugs );
		
		getFormBPMHDao().persist( formBpmh );
		
		return formBpmh.getId();

	}

	public void setFormBeanDemographic(Demographic demographic) {

		if( demographic == null ) {
			demographic = getDemographicDao().getDemographicById(demographicNo);	
		} 
			
		getBpmhFormBean().setDemographic(demographic);		
	}
	
	public void setFormBeanProvider(Provider provider) {
		
		List<Appointment> appointmentList = null;
		List<Provider> providerList = null;
		Appointment lastAppointment = null;
		
		if(provider == null) {
			
			appointmentList = getAppointmentDao().getAppointmentHistory( demographicNo, 0, 3 );
			providerList = getProviderDao().getProviderByPatientId(demographicNo);			
			
			logger.debug("Found " + appointmentList.size() + " appointments for this patient.");
			
			if( ( appointmentList != null ) && (  appointmentList.size() > 0 ) ) {
				lastAppointment = appointmentList.get(0);
			}
	
			if( providerList != null && providerList.size() > 0 ) {
				provider = providerList.get(0);
			}
			
			// check other sources.
			if( ( provider == null ) && ( lastAppointment != null ) ) {				
				provider = getProviderDao().getProvider( lastAppointment.getProviderNo() );	
			}
			
			// the prepared-on date is the same as the last appointment
			if( lastAppointment != null ) {
				
				logger.debug("Setting Form Prepared Date");				
				getBpmhFormBean().setFormDate( lastAppointment.getAppointmentDate() );
			}
			
		}
		
		getBpmhFormBean().setProvider(provider);

	}
	
	/**
	 * Sets the family Dr. Field of the BPMH form.
	 * First checks the Demographic if FP is set, then checks the 
	 * note fields.
	 */
	public void setFormBeanFamilyDoctor(String familyDrName, String familyDrPhone, String familyDrFax) {

		List<DemographicCust> demographicCustList = null;
		String parseMe = "";
		List<DemographicContact> demographicContacts = null;
		Integer demographicContactId = null;
		String familyDrRole = "";
		String familyDrContactId = "";
		Integer familyDrId = null;
		Integer contactType = null;
		ProfessionalSpecialist professionalSpecialist = null;
		Contact professionalContact = null;
		
		if(familyDrName.isEmpty() && familyDrPhone.isEmpty() && 
				familyDrFax.isEmpty() && getFormHistory() == null ) {

			// look for the Dr. in the demographic health care team.
			demographicContacts = getDemographicContactDao().findByDemographicNoAndCategory(demographicNo, DemographicContact.CATEGORY_PROFESSIONAL);

			for( DemographicContact demographicContact : demographicContacts ) {
				
				if( StringUtils.isNumeric( demographicContact.getRole() ) ) {
					familyDrRole = getSpecialtyDao().find( Integer.parseInt( demographicContact.getRole() ) ).getSpecialty();
				} else {
					familyDrRole = demographicContact.getRole();
				}
				
				if( PRIMARY_DR_TITLE.equalsIgnoreCase( familyDrRole ) ) {					
					demographicContactId = demographicContact.getId();
					familyDrContactId = demographicContact.getContactId().trim();
					
					if( StringUtils.isNumeric( familyDrContactId ) ) {
						familyDrId = Integer.parseInt( familyDrContactId );
					}
					
					contactType = demographicContact.getType();
				}
			}

			logger.debug("Found Family Dr. in Health Care Contacts. Provider Type: " + contactType + " Provider ID: " + familyDrId);
			
			// populate the Dr. found in the Health Care Team
			if( familyDrId != null && contactType != null ) {
				
				if( contactType == DemographicContact.TYPE_PROFESSIONALSPECIALIST ) {
					
					professionalSpecialist = getProfessionalSpecialistDao().find( familyDrId );
					familyDrName = professionalSpecialist.getFormattedName().trim();
					familyDrPhone = professionalSpecialist.getPhoneNumber().trim();
					familyDrFax = professionalSpecialist.getFaxNumber().trim();
					
				} else {
					
					professionalContact = getProContactDao().find(familyDrId);
					familyDrName = professionalContact.getFormattedName().trim();
					familyDrPhone = professionalContact.getWorkPhone().trim();
					familyDrFax = professionalContact.getFax().trim();
					
				}

			// otherwise check the Demogrpahic notes - just in case (soon to be deprecated.)
			} else {
			
				demographicCustList = getDemographicCustDao().findAllByDemographicNumber(demographicNo);
				
				if( (demographicCustList != null) && (demographicCustList.size() > 0) ) {
					parseMe = ( demographicCustList.get(0) ).getNotes();
				}
				
				logger.debug("Family Dr. may be in the demographic note field: " + parseMe);
		
				if(! parseMe.isEmpty()) {					
					familyDrName = CaseNoteParser.getFamilyDr(parseMe);
					familyDrPhone = CaseNoteParser.getPhoneNumber(parseMe);
					familyDrFax = CaseNoteParser.getFaxNumber(parseMe);							
				}
				
			}
		}
		
		getBpmhFormBean().setFamilyDrContactId( demographicContactId+"" );
		getBpmhFormBean().setFamilyDrName( familyDrName );
		getBpmhFormBean().setFamilyDrPhone( familyDrPhone );
		getBpmhFormBean().setFamilyDrFax( familyDrFax );

	}
	
	/**
	 * Set the BpmhFormBean with a list of BpmhDrug beans.
	 * 
	 * If sent a null parameter a new list will be built.
	 *  
	 * BpmhDrug beans contain data from the database Drugs JPA along
	 * with additional transient data.
	 * 
	 * Depends on DrugDao, BpmhFormBean, BpmhDrug
	 * 
	 * @param bpmhDrugBeans
	 */
	public void setFormBeanDrugList( List<BpmhDrug> bpmhDrugBeans ) {

		Iterator<Drug> drugListIterator = null;
		List<Drug> drugList = null;
		Drug drug;
		String drugId = null;
		String drugDIN = null;
		RxDrugData drugFetch = null;
		
		if( bpmhDrugBeans != null ) {

			getBpmhFormBean().setDrugs( bpmhDrugBeans );
			
		} else {
			
			drugList = getDrugDao().findByDemographicId( this.demographicNo, false );			

			for(Drug drugItem : drugList) {
				logger.debug("DRUG: " + drugItem.getGenericName());
			}
			
			if( (drugList != null) && (drugList.size() > 0) ) {
				
				bpmhDrugBeans = getBpmhFormBean().getDrugs();
				drugFetch = new RxDrugData();
				
				for(BpmhDrug bpmhDrugItem: bpmhDrugBeans) {
					logger.debug("BPMH DRUG: " + bpmhDrugItem.toString());
				}
												
				drugListIterator = drugList.iterator();
				while( drugListIterator.hasNext() ) {
					
					drug = drugListIterator.next();
					drugId = drug.getId()+"";
					drugDIN = drug.getRegionalIdentifier();
					BpmhDrug bpmhDrugBean;
					BpmhDrug bpmhDrug = null;
					RxDrugData.DrugMonograph drugData = null;
					StringBuilder stringBuilder = null;
					ArrayList<RxDrugData.DrugMonograph.DrugComponent> drugComponents = null;
					String drugName = "";
					String drugProduct = "";
					
					// check for updates to what is already contained in the form bean
					for(int i = 0; i < bpmhDrugBeans.size(); i++ ) {
						bpmhDrugBean = bpmhDrugBeans.get(i);
		
						if( drugId.equals( bpmhDrugBean.getId() ) ) {
							bpmhDrug = bpmhDrugBean;
						}
					}

					// start a new bpmhDrug if nothing turns up.
					if(bpmhDrug == null) {
						bpmhDrug = new BpmhDrug();
						bpmhDrugBeans.add(bpmhDrug);
					}
		
					try {
	                    BeanUtils.copyProperties(bpmhDrug, drug);
	                } catch (IllegalAccessException e) {               	
	                   logger.fatal("Failed to copy bean properties", e);                  
	                } catch (InvocationTargetException e) {                	
	                	logger.fatal("Failed to copy bean properties", e);                	
	                }
					
					// drug reason needs to be hacked together from different 
					// sources. 
					bpmhDrug.setWhy( getDrugReason( drug.getId() ) );					
					
					// Oscar's Drug table does not (or did not) hold accurate 
					// drug ingredient information. 
					// this block is added to ensure proper ingredient info is 
					// retrieved from DrugRef.
					// using this uses wayyyy too many resources, but it is the only way to 
					// ensure this form is extensible with other Oscar changes.
					//TODO: fix once othe parts merged in
					/*
					if( ! StringUtils.isBlank(drugDIN) ) {
						
						stringBuilder = new StringBuilder();
						try {
							drugData = drugFetch.getDrugByDIN(drugDIN);
                        } catch (Exception e) {
                        	logger.fatal("Failed to fetch Drug from DrugRef with DIN " + drugDIN, e); 
                        } 

						if( drugData != null ) {	
							
							drugName = drugData.getName();
							drugProduct = drugData.getProduct();
							drugComponents = drugData.getDrugComponentList();							
						}
						
						if( drugComponents != null && drugComponents.size() > 0 ) {
							
							int count = 0;
							for( RxDrugData.DrugMonograph.DrugComponent drugComponent : drugComponents ) {
								
								stringBuilder.append( drugComponent.getName() );
								stringBuilder.append(" ");
								stringBuilder.append( drugComponent.getStrength() );
								stringBuilder.append( drugComponent.getUnit() );
								
								count++;
								if( count > 0 && count != drugComponents.size() ) {
									stringBuilder.append( " / " );
								}
							}
							
							stringBuilder.append(" " + drugData.getDrugForm());
							
							bpmhDrug.setWhat( stringBuilder.toString() );
						
						} else if( ! StringUtils.isBlank( drugName ) ) {
							
							bpmhDrug.setGenericName( drugName );
							
						} else if( ! StringUtils.isBlank( drugProduct ) ) {
							
							bpmhDrug.setGenericName( drugProduct );
							
						} else {
							// do nothing
						}												
					}*/
				}
				
				SortDrugList.byPositionOrder( bpmhDrugBeans );
			}	
		}

	}
	
	public String getDrugReason(Integer drugId) {

		Icd9 icd9 = null;
		String patientFriendlyDx = "";
		String drugReasonCode = null;
		Icd9Synonym dxCodeTranslations = null;
		StringBuilder stringBuilder = new StringBuilder("");		
		List<DrugReason> drugReasonList = getDrugReasonDao().getReasonsForDrugID( drugId, true);
		
		if( ( drugReasonList != null ) && ( drugReasonList.size() > 0 ) ) {
					
			for( DrugReason drugReason : drugReasonList ) {
	
				if( drugReason.getCodingSystem().equalsIgnoreCase("icd9") ) {
					
					drugReasonCode = drugReason.getCode();
				}
				
				if(drugReasonCode != null) {
					dxCodeTranslations = getDxCodeTranslationsDao().findPatientFriendlyTranslationFor(drugReasonCode);
				}
				
				if(dxCodeTranslations != null) {
					
					patientFriendlyDx = dxCodeTranslations.getPatientFriendly();
					
				} else {
					
					icd9 = getIcd9Dao().findByCode(drugReasonCode);
					patientFriendlyDx = icd9.getDescription();
					
				}

				stringBuilder.append( patientFriendlyDx + " " );						
				stringBuilder.append( drugReason.getComments() );
			}

		}
		
		return stringBuilder.toString();
	}
	
	public void setFormBeanAllergyList(List<Allergy> allergyList) {

		if(allergyList == null && getFormHistory() == null ) {
			allergyList = getAllergyDao().findActiveAllergies(demographicNo);
		}
		
		if( ( allergyList != null ) && ( allergyList.size() > 0 ) ) {
			getBpmhFormBean().setAllergies( allergyList );
		}

	}

	public BpmhFormBean getBpmhFormBean() {
		if(bpmhFormBean == null) {
			throw new RuntimeException("BPMH Bean Not Set");
		}
		return bpmhFormBean;
	}

	public void setBpmhFormBean(BpmhFormBean bpmhFormBean) {
		this.bpmhFormBean = bpmhFormBean;	
	}

	public int getDemographicNo() {
		return demographicNo;
	}

	public void setDemographicNo(int demographicNo) {
		this.demographicNo = demographicNo;
	}

	public AllergyDao getAllergyDao() {
		return allergyDao;
	}

	public void setAllergyDao(AllergyDao allergyDao) {
		this.allergyDao = allergyDao;
	}

	public DemographicCustDao getDemographicCustDao() {
		return demographicCustDao;
	}

	public void setDemographicCustDao(DemographicCustDao demographicCustDao) {
		this.demographicCustDao = demographicCustDao;
	}

	public DemographicDao getDemographicDao() {
		return demographicDao;
	}

	public void setDemographicDao(DemographicDao demographicDao) {
		this.demographicDao = demographicDao;
	}

	public DrugDao getDrugDao() {
		return drugDao;
	}

	public void setDrugDao(DrugDao drugDao) {
		this.drugDao = drugDao;
	}

	public DrugReasonDao getDrugReasonDao() {
		return drugReasonDao;
	}

	public void setDrugReasonDao(DrugReasonDao drugReasonDao) {
		this.drugReasonDao = drugReasonDao;
	}

	public ProviderDao getProviderDao() {
		return providerDao;
	}

	public void setProviderDao(ProviderDao providerDao) {
		this.providerDao = providerDao;
	}

	public OscarAppointmentDao getAppointmentDao() {
		return appointmentDao;
	}

	public void setAppointmentDao(OscarAppointmentDao appointmentDao) {
		this.appointmentDao = appointmentDao;
	}

	public Icd9Dao getIcd9Dao() {
		return icd9Dao;
	}

	public void setIcd9Dao(Icd9Dao icd9Dao) {
		this.icd9Dao = icd9Dao;
	}

	public Icd9SynonymDao getDxCodeTranslationsDao() {
		return dxCodeTranslationsDao;
	}

	public void setDxCodeTranslationsDao(Icd9SynonymDao dxCodeTranslationsDao) {
		this.dxCodeTranslationsDao = dxCodeTranslationsDao;
	}

	public FormBPMHDao getFormBPMHDao() {
		return formBPMHDao;
	}

	public void setFormBPMHDao(FormBPMHDao formBPMHDao) {
		this.formBPMHDao = formBPMHDao;
	}

	public ProfessionalSpecialistDao getProfessionalSpecialistDao() {
		return professionalSpecialistDao;
	}

	public void setProfessionalSpecialistDao(ProfessionalSpecialistDao professionalSpecialistDao) {
		this.professionalSpecialistDao = professionalSpecialistDao;
	}

	public ContactSpecialtyDao getSpecialtyDao() {
		return specialtyDao;
	}

	public void setSpecialtyDao(ContactSpecialtyDao specialtyDao) {
		this.specialtyDao = specialtyDao;
	}

	public ProfessionalContactDao getProContactDao() {
		return proContactDao;
	}

	public void setProContactDao(ProfessionalContactDao proContactDao) {
		this.proContactDao = proContactDao;
	}

	public DemographicContactDao getDemographicContactDao() {
		return demographicContactDao;
	}

	public void setDemographicContactDao(DemographicContactDao demographicContactDao) {
		this.demographicContactDao = demographicContactDao;
	}	

}

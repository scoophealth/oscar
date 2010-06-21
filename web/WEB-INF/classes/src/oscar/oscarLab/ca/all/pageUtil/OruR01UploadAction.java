package oscar.oscarLab.ca.all.pageUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.oscarehr.common.dao.ProfessionalSpecialistDao;
import org.oscarehr.common.hl7.v2.oscar_to_oscar.OruR01;
import org.oscarehr.common.hl7.v2.oscar_to_oscar.OruR01.ObservationData;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.ProfessionalSpecialist;
import org.oscarehr.common.model.Provider;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.util.DateUtils;
import ca.uhn.hl7v2.model.v26.message.ORU_R01;

public class OruR01UploadAction extends Action {
    private static Logger logger = MiscUtils.getLogger();

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException  {
    	try {
	        OruR01UploadForm oruR01UploadForm = (OruR01UploadForm) form;
	        FormFile formFile=oruR01UploadForm.getUploadFile();
	        LoggedInInfo loggedInInfo=LoggedInInfo.loggedInInfo.get();
	        
	        Demographic demographic=getDemographicObject(oruR01UploadForm); 
	        
	        ArrayList<ObservationData> observationDataList=new ArrayList<ObservationData>();
	        observationDataList.add(new ObservationData(oruR01UploadForm.getDataName(), "txt", oruR01UploadForm.getTextData()));
	        String extension=FilenameUtils.getExtension(formFile.getFileName());
	        observationDataList.add(new ObservationData(oruR01UploadForm.getDataName(), extension, formFile.getFileData()));
	        
	        Provider sendingProvider=loggedInInfo.loggedInProvider;
	        Provider receivingProvider=getReceivingProvider(oruR01UploadForm);
	        
	        ORU_R01 hl7Message=OruR01.makeOruR01(loggedInInfo.currentFacility.getName(), demographic, observationDataList, sendingProvider, receivingProvider);
	        
System.err.println("FOOOOOOO : "+oruR01UploadForm);

System.err.println("---"+formFile.getContentType());
System.err.println("---"+formFile.getFileName());
System.err.println("---"+formFile.getFileSize());
System.err.println("---"+new String(formFile.getFileData()));
        } catch (Exception e) {
	        logger.error("Unexpected error.", e);
        }

    	return(null);
    }
    
    private Provider getReceivingProvider(OruR01UploadForm oruR01UploadForm) {
    	
    	ProfessionalSpecialistDao professionalSpecialistDao=(ProfessionalSpecialistDao)SpringUtils.getBean("professionalSpecialistDao");
    	ProfessionalSpecialist professionalSpecialist=professionalSpecialistDao.find(oruR01UploadForm.getProfessionalSpecialistId());
    	
    	Provider provider=new Provider();
    	
    	provider.setFirstName(professionalSpecialist.getFirstName());
    	provider.setLastName(professionalSpecialist.getLastName());
    	provider.setEmail(professionalSpecialist.getEmailAddress());
    	provider.setPhone(professionalSpecialist.getPhoneNumber());
    	provider.setTitle(professionalSpecialist.getProfessionalLetters());
    	provider.setAddress(professionalSpecialist.getStreetAddress());
    	
    	return(provider);
    }

	private static Demographic getDemographicObject(OruR01UploadForm oruR01UploadForm)
    {
    	Demographic demographic=new Demographic();
    	
    	demographic.setLastName(oruR01UploadForm.getClientLastName());
    	demographic.setFirstName(oruR01UploadForm.getClientFirstName());
    	demographic.setSex(oruR01UploadForm.getClientGender());
    	demographic.setHin(oruR01UploadForm.getClientHealthNumber());
    	
    	try
    	{
    		GregorianCalendar gregorianCalendar=DateUtils.toGregorianCalendarDate(oruR01UploadForm.getClientBirthDay());
    		demographic.setBirthDay(gregorianCalendar);
    	}
    	catch (Exception e)
    	{
    		logger.warn("Error parsing date:"+oruR01UploadForm.getClientBirthDay(), e);
    	}
    	
    	return(demographic);
    }
}

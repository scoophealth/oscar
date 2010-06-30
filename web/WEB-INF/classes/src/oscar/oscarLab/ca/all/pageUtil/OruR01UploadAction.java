package oscar.oscarLab.ca.all.pageUtil;

import java.io.IOException;
import java.util.GregorianCalendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.oscarehr.common.dao.ProfessionalSpecialistDao;
import org.oscarehr.common.hl7.v2.oscar_to_oscar.DataTypeUtils;
import org.oscarehr.common.hl7.v2.oscar_to_oscar.OruR01;
import org.oscarehr.common.hl7.v2.oscar_to_oscar.SendingUtils;
import org.oscarehr.common.hl7.v2.oscar_to_oscar.OruR01.ObservationData;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.ProfessionalSpecialist;
import org.oscarehr.common.model.Provider;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;
import org.oscarehr.util.WebUtils;

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
	        
	        ObservationData observationData=new ObservationData();
	        observationData.dataName=oruR01UploadForm.getDataName();
	        observationData.textData=oruR01UploadForm.getTextData();
	        observationData.binaryDataFileName=formFile.getFileName();
	        observationData.binaryData=formFile.getFileData();
	        
	    	Provider sendingProvider=loggedInInfo.loggedInProvider;

	    	ProfessionalSpecialistDao professionalSpecialistDao=(ProfessionalSpecialistDao)SpringUtils.getBean("professionalSpecialistDao");
	    	ProfessionalSpecialist professionalSpecialist=professionalSpecialistDao.find(oruR01UploadForm.getProfessionalSpecialistId());
	        Provider receivingProvider=DataTypeUtils.getReceivingProvider(professionalSpecialist);
	        
	        ORU_R01 hl7Message=OruR01.makeOruR01(loggedInInfo.currentFacility.getName(), demographic, observationData, sendingProvider, receivingProvider);
	        
	        SendingUtils.send(hl7Message, professionalSpecialist);
	        
	        WebUtils.addInfoMessage(request.getSession(), "Data successfully send.");
        } catch (Exception e) {
	        logger.error("Unexpected error.", e);
	        WebUtils.addErrorMessage(request.getSession(), "An error occurred while sending this data, please try again or send this manually.");
        }

    	return(mapping.findForward("result"));
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
    		String temp=StringUtils.trimToNull(oruR01UploadForm.getClientBirthDay());
    		if (temp!=null)
    		{
	    		GregorianCalendar gregorianCalendar=DateUtils.toGregorianCalendarDate(temp);
	    		demographic.setBirthDay(gregorianCalendar);
    		}
    	}
    	catch (Exception e)
    	{
    		logger.warn("Error parsing date:"+oruR01UploadForm.getClientBirthDay(), e);
    	}
    	
    	return(demographic);
    }
}

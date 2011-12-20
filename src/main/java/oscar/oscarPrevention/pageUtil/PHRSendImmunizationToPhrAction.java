/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package oscar.oscarPrevention.pageUtil;

import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.dao.PreventionDao;
import org.oscarehr.common.model.Prevention;
import org.oscarehr.common.service.MyOscarMedicalDataManager;
import org.oscarehr.myoscar_server.ws.MedicalDataType;
import org.oscarehr.util.XmlUtils;
import org.w3c.dom.Document;

import oscar.util.StringUtils;

/**
 *
 * @author Ronnie Cheng
 */
public class PHRSendImmunizationToPhrAction extends DispatchAction {
	private static final String TYPE_IMMUNIZATIONS = MedicalDataType.IMMUNISATION.name();
	
	private HashMap<String,String> preventionKeys = null;
	private PreventionDao preventionDao;
	private ProviderDao providerDao;
	private DemographicDao demographicDao;
	
	@Override
	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return send(mapping,form,request,response);
	}

	public ActionForward send(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Integer demographicNo = null;
		try {
			demographicNo = Integer.valueOf(request.getParameter("demographic_no"));
		} catch (NumberFormatException ex) {
			request.setAttribute("error_msg", "Error: Invalid demographic no");
			return mapping.findForward("loginPage");
		}

		MyOscarMedicalDataManager myOscarMedicalDataManager = new MyOscarMedicalDataManager(request.getSession());
		Integer lastId = myOscarMedicalDataManager.getLastTrackingId(demographicNo, TYPE_IMMUNIZATIONS);

		List<Prevention> preventionList = getPreventionDao().findByDemographicIdAfterId(demographicNo, lastId);
		String docAsString=null, msgReturn=null;
		Integer lastSentId = null;
		for (Prevention prevention : preventionList) {
			
			//create immunization xml
			Document doc = newXmlDoc(prevention);
			HashMap<String,String> preventionExt = getPreventionDao().getPreventionExt(prevention.getId());
			if (preventionExt.containsKey("result")) continue;
			
			writeXmlExtra(doc, preventionExt);
			docAsString = XmlUtils.toString(doc, false);
			
			msgReturn = myOscarMedicalDataManager.sendMedicalData(demographicNo, TYPE_IMMUNIZATIONS, docAsString, prevention.getId(), prevention.getProviderNo(), prevention.getPreventionDate());
			if (MyOscarMedicalDataManager.SUCCESS.equals(msgReturn)) {
				lastSentId = prevention.getId();
			} else {
				request.setAttribute("error_msg", "Error: " + msgReturn);
				
				//track the last object id sent
				if (lastSentId!=null) myOscarMedicalDataManager.addTracking(demographicNo, TYPE_IMMUNIZATIONS, lastSentId);
				return mapping.findForward("loginPage");
			}
		}
		
		if (preventionList.size()==0) {
			request.setAttribute("error_msg", "No new immunization to be sent");
		} else {
			myOscarMedicalDataManager.addTracking(demographicNo, TYPE_IMMUNIZATIONS, lastSentId);
		}
		return mapping.findForward("loginPage");
	}
	
	private Document newXmlDoc(Prevention prevention) throws ParserConfigurationException {
		Document doc = XmlUtils.newDocument("Immunization");
		XmlUtils.appendChildToRoot(doc, "Type", prevention.getPreventionType());

		if (prevention.getNextDate()!=null) {
			GregorianCalendar dateOfData = new GregorianCalendar();
			dateOfData.setTime(prevention.getNextDate());
			XmlUtils.appendChildToRoot(doc, "NextDate", DateFormatUtils.ISO_DATETIME_FORMAT.format(dateOfData));
		}
		
		if (prevention.isRefused()) XmlUtils.appendChildToRoot(doc, "Status", "Refused");
		else if (prevention.isIneligible()) XmlUtils.appendChildToRoot(doc, "Status", "Ineligible");
		else XmlUtils.appendChildToRoot(doc, "Status", "Completed");
		
		return doc;
	}
	
	private void writeXmlExtra(Document doc, HashMap<String,String> preventionExt) {
		if (preventionKeys==null) {
			fillPreventionKeys();
		}
		addToComments(preventionExt);
		for (String key : preventionKeys.keySet()) {
			if (preventionExt.get(key)!=null) {
				XmlUtils.appendChildToRoot(doc,  preventionKeys.get(key), preventionExt.get(key));
			}
		}
	}
	
	private void fillPreventionKeys() {
		if (preventionKeys!=null) return;
		
		preventionKeys = new HashMap<String,String>();
		preventionKeys.put("name", "VaccineName");
		preventionKeys.put("location", "AppliedBodyLocation");
		preventionKeys.put("route", "Route");
		preventionKeys.put("dose", "Dose");
		preventionKeys.put("lot", "Lot");
		preventionKeys.put("manufacture", "Manufacture");
		preventionKeys.put("comments", "Comments");
	}
	
	private void addToComments(HashMap<String,String> preventionExt) {
		String comments = preventionExt.get("comments");
		comments = appendLine(comments, "Dose1: ", preventionExt.get("dose1"));
		comments = appendLine(comments, "Dose2: ", preventionExt.get("dose2"));
		
		if (preventionExt.containsKey("comments")) preventionExt.remove("comments");
		if (StringUtils.filled(comments)) preventionExt.put("comments", comments);
	}
	
	private String appendLine(String s1, String label, String s2) {
		if (StringUtils.empty(s2)) return StringUtils.noNull(s1);
		
		if (StringUtils.filled(s1) && StringUtils.filled(s2)) s1 += "\n";
		return StringUtils.noNull(s1) + StringUtils.noNull(label) + StringUtils.noNull(s2);
	}
	
	
	
	public PreventionDao getPreventionDao() {
		return preventionDao;
	}
	public void setPreventionDao(PreventionDao preventionDao) {
		this.preventionDao = preventionDao;
	}

	public ProviderDao getProviderDao() {
		return providerDao;
	}
	public void setProviderDao(ProviderDao providerDao) {
		this.providerDao = providerDao;
	}

	public DemographicDao getDemographicDao() {
		return demographicDao;
	}
	public void setDemographicDao(DemographicDao demographicDao) {
		this.demographicDao = demographicDao;
	}
}

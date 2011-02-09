package oscar.form.study.HSFO.pageUtil;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import noNamespace.HsfoHbpsDataDocument;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.xmlbeans.XmlException;

import oscar.oscarDemographic.data.DemographicData;

public class TransferHSFOXmlAction extends Action
{
	protected static Logger logger = Logger
			.getLogger(TransferHSFOXmlAction.class);

	/**
	 * @param args
	 * @throws IOException
	 * @throws XmlException
	 */
    
	
	XMLTransferUtil tfutil=new XMLTransferUtil();

	public TransferHSFOXmlAction()
	{

	}

	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception
	{
		// parse("/Hsfo_Hbps_Data Sample ver 2007-02-12.xml");

		String providerNo = request.getParameter("xmlHsfoProviderNo");
		String demographicNo = request.getParameter("xmlHsfoDemographicNo");

		Integer demoNo = new Integer(0);
		if (demographicNo != null)
			demoNo = new Integer(demographicNo.trim());
		ArrayList message = new ArrayList();
		HsfoHbpsDataDocument doc = tfutil.generateXML(providerNo, demoNo);
		if (demoNo!=0){
			//if no internal doctor assigned for designated patient,report error.
			DemographicData demoData = new DemographicData();
			String internalId=demoData.getDemographic(demoNo.toString()).getProviderNo();
			
			if(internalId==null || internalId.length()==0){
				message.add("");
				message.add("Unable to upload. Please go to the master page, and assign a internal doctor to this patient.");
				request.setAttribute("HSFOmessage", (String) message.get(1));
				return mapping.findForward("HSFORE");
			}
		}
		if (doc == null)
		{
			message.add("");
			message.add("Patient(s) data not found in the database.");
		} else
		{
			message = tfutil.validateDoc(doc);
			if (message.size() != 0)
			{
				// set error message;
				request.setAttribute("HSFOmessage", message.get(0));
				return mapping.findForward("HSFORE");
			}
			String rs = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
					+ doc.xmlText();
			// send to hsfo web

			try
			{
				message = tfutil.soapHttpCall(tfutil.getSiteID().intValue(), tfutil.getUserId(),
						tfutil.getLoginPasswd(), rs);
			} catch (Exception e)
			{
				logger.error(e);
				throw e;
			}
		}
		
		String msg = "Code: " + (String) message.get(0) + " "
				+ (String) message.get(1);
		request.setAttribute("HSFOmessage", msg);
		return mapping.findForward("HSFORE");
	}
}
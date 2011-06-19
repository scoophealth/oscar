package oscar.oscarEncounter.pageUtil;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.struts.util.MessageResources;
import org.oscarehr.hospitalReportManager.dao.HRMDocumentDao;
import org.oscarehr.hospitalReportManager.dao.HRMDocumentToDemographicDao;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import java.util.List;

import oscar.util.DateUtils;
import oscar.util.OscarRoleObjectPrivilege;
import oscar.util.StringUtils;

import org.oscarehr.hospitalReportManager.model.HRMDocument;
import org.oscarehr.hospitalReportManager.model.HRMDocumentToDemographic;

public class EctDisplayHRMAction extends EctDisplayAction {

	 private static final String cmd = "HRM";
	 private HRMDocumentToDemographicDao hrmDocumentToDemographicDao = (HRMDocumentToDemographicDao) SpringUtils.getBean("HRMDocumentToDemographicDao");
	 private HRMDocumentDao hrmDocumentDao = (HRMDocumentDao) SpringUtils.getBean("HRMDocumentDao");
		
	 public boolean getInfo(EctSessionBean bean, HttpServletRequest request, NavBarDisplayDAO Dao, MessageResources messages) {

		 
			boolean a = true;
		 	List v = OscarRoleObjectPrivilege.getPrivilegeProp("_newCasemgmt.documents");
		    String roleName = (String)request.getSession().getAttribute("userrole") + "," + (String) request.getSession().getAttribute("user");
		    a = OscarRoleObjectPrivilege.checkPrivilege(roleName, (Properties) v.get(0), (List) v.get(1));
		 	if(!a) {
		 		return true; //Prevention link won't show up on new CME screen.
		 	} else {
		 	  
		 		String winName = "docs" + bean.demographicNo;
		 		String url = "popupPage(500,1115,'" + winName + "', '" + request.getContextPath() + "/hospitalReportManager/displayHRMDocList.jsp?" + "')";
		 		Dao.setLeftURL(url);
			    Dao.setLeftHeading(messages.getMessage(request.getLocale(), "oscarEncounter.Index.msgHRMDocuments"));
			  
			    Dao.setRightHeadingID(cmd); //no menu so set div id to unique id for this action
	
			    StringBuilder javascript = new StringBuilder("<script type=\"text/javascript\">");
			    String js = "";
			    List<HRMDocumentToDemographic> hrmDocListDemographic = hrmDocumentToDemographicDao.findByDemographicNo(bean.demographicNo);
			    String dbFormat = "yyyy-MM-dd";
			    String serviceDateStr = "";
			    String key;
			    String title;
			    int hash;
			    String BGCOLOUR = request.getParameter("hC");
			    Date date;
			    for (HRMDocumentToDemographic hrmDemoDocResult : hrmDocListDemographic) {
			    	List<HRMDocument> hrmDocumentList = hrmDocumentDao.findById(hrmDemoDocResult.getId());
			    	HRMDocument hrmDocument = hrmDocumentList.get(0);
			    	String dispFilename = hrmDocument.getReportType();
			    	String dispDocNo    = hrmDocument.getId().toString();
			     
			        boolean skip=false;
			       
			        if(skip)
			        	continue;
			        
			        title = StringUtils.maxLenString(dispFilename, MAX_LEN_TITLE, CROP_LEN_TITLE, ELLIPSES);
			        
			        DateFormat formatter = new SimpleDateFormat(dbFormat);
			        String dateStr = hrmDocument.getTimeReceived().toString();
			        NavBarDisplayDAO.Item item = Dao.Item();
			        try {
			            date = formatter.parse(dateStr);
			            serviceDateStr =  DateUtils.formatDate(date,request.getLocale()); 
			        }
			        catch(ParseException ex ) {
			            MiscUtils.getLogger().debug("EctDisplayHRMAction: Error creating date " + ex.getMessage());
			            serviceDateStr = "Error";
			            date = null;
			        }
	
			        String user = (String) request.getSession().getAttribute("user");
			        item.setDate(date);
			        hash = Math.abs(winName.hashCode());
			        url = "popupPage(700,800,'" + hash + "', '" + request.getContextPath() + "/hospitalReportManager/Display.do?id="+dispDocNo+"');";
			       
			        item.setLinkTitle(title + serviceDateStr);
			        item.setTitle(title);
			        key = StringUtils.maxLenString(dispFilename, MAX_LEN_KEY, CROP_LEN_KEY, ELLIPSES) + "(" + serviceDateStr + ")";
			        key = StringEscapeUtils.escapeJavaScript(key);
	
			        
			        js = "itemColours['" + key + "'] = '" + BGCOLOUR + "'; autoCompleted['" + key + "'] = \"" + url + "\"; autoCompList.push('" + key + "');";
			        javascript.append(js);
			        url += "return false;";
			        item.setURL(url);
			        Dao.addItem(item);

			    }
			    javascript.append("</script>");
	
			    Dao.setJavaScript(javascript.toString());
			    return true;
		 	}
		  }

	 @Override
    public String getCmd() {
	     return cmd;
	 }
	 
	 
}

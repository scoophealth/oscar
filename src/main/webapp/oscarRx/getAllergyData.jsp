<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="org.oscarehr.common.model.Allergy"%>
<%@page import="org.oscarehr.PMmodule.caisi_integrator.RemoteDrugAllergyHelper"%>
<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%@page import="org.oscarehr.util.MiscUtils"%>
<%@page import="java.util.*,net.sf.json.*,java.lang.reflect.*,java.io.*,org.apache.xmlrpc.*,oscar.oscarRx.util.*,oscar.oscarRx.data.*"  %>
<%
String atcCode =  request.getParameter("atcCode");
String id = request.getParameter("id");

String disabled = oscar.OscarProperties.getInstance().getProperty("rx3.disable_allergy_warnings","false");
if(disabled.equals("false")) {

oscar.oscarRx.pageUtil.RxSessionBean rxSessionBean = (oscar.oscarRx.pageUtil.RxSessionBean) session.getAttribute("RxSessionBean");
Allergy[] allergies = RxPatientData.getPatient(rxSessionBean.getDemographicNo()).getAllergies();

LoggedInInfo loggedInInfo = LoggedInInfo.loggedInInfo.get();
if (loggedInInfo.currentFacility.isIntegratorEnabled()) {
	try {
		ArrayList<Allergy> remoteAllergies=RemoteDrugAllergyHelper.getRemoteAllergiesAsAllergyItems(rxSessionBean.getDemographicNo());

		// now merge the 2 lists
		for (Allergy alleryTemp : allergies) remoteAllergies.add(alleryTemp);
		allergies=remoteAllergies.toArray(new Allergy[0]);
	} catch (Exception e) {
		MiscUtils.getLogger().error("error getting remote allergies", e);
	}
}

Allergy[] allergyWarnings = null;
   RxDrugData drugData = new RxDrugData();
   allergyWarnings = drugData.getAllergyWarnings(atcCode, allergies);


   // Hashtable d = new Hashtable();
    Hashtable d2=new Hashtable();

  //  d.put("id",id);
    d2.put("id",id);
    for(Allergy allg:allergyWarnings){
   	 	String temp=StringUtils.trimToEmpty(allg.getDescription());
        d2.put("DESCRIPTION", temp);

        temp=StringUtils.trimToEmpty(allg.getReaction());
        d2.put("reaction", temp);
    }

   try{
    response.setContentType("text/x-json");
    JSONObject jsonArray = (JSONObject) JSONSerializer.toJSON( d2 );
    jsonArray.write(out);
    }
   catch(Exception e){
	   MiscUtils.getLogger().error("Error", e);
    }
}
%>

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


package oscar.oscarEncounter.pageUtil;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.net.ssl.SSLContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.apache.struts.util.MessageResources;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.oscarehr.common.Gender;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.OscarProperties;


/**
 * Retrieves the eConsults for the demographic and displays them in the eChart
 */
public class EctDisplayEconsultAction extends EctDisplayAction {
    private String cmd = "eConsult";

    /**
     * Generates the eConsult module in the eChart, making it so that when the user clicks on the title it takes them to the demographic's eConsults, when they click the
     * + in the header then it opens a new eConsult, and listing out the eConsults in the eChart
     *
     * @param bean
     * 		Current session information
     * @param request
     * 		Current request
     * @param Dao
     * 		View DAO responsible for rendering encounter
     * @param messages
     * 		i18n message bundle
     * @return Always returns a true boolean
     */
    public boolean getInfo(EctSessionBean bean, HttpServletRequest request, NavBarDisplayDAO Dao, MessageResources messages) {
        Logger logger = MiscUtils.getLogger();
    	String oneIdEmail = request.getSession().getAttribute("oneIdEmail") != null ? request.getSession().getAttribute("oneIdEmail").toString() : "";
        if(oneIdEmail == null || oneIdEmail.equals("")) {
            return true;
        } else {
    	    try {
                //Gets the session and the oneId token from the session
                HttpSession session = request.getSession();
                String delegateOneIdEmail = request.getSession().getAttribute("delegateOneIdEmail") != null ? request.getSession().getAttribute("delegateOneIdEmail").toString() : "";
                String delegateEmailQueryString = "";
                String providerEmail = oneIdEmail; 
                String delegateEmail = "";
                
                //If there is a delegateOneIdEmail then it is used as the normal oneId email and the current user is the delegate as they are delegating for that person
                if (!delegateOneIdEmail.equals("")) {
                    delegateEmailQueryString = "&delegate_oneid_email=" + oneIdEmail;
                    
                    providerEmail = delegateOneIdEmail;
                }
                //Gets the oneIdEmail and encodes it for use in URLs
                String encodedOneIdEmail = providerEmail;
                //Gets the starting URL for the backend and frontend eConsult URLs
    		    OscarProperties oscarProperties = OscarProperties.getInstance();
    		    String frontendEconsultUrl = oscarProperties.getProperty("frontendEconsultUrl");
                String backendEconsultUrl = oscarProperties.getProperty("backendEconsultUrl");
                //Gets the demographic whose eChart is loading
	    	    DemographicDao demographicDao = SpringUtils.getBean(DemographicDao.class);
	    	    Demographic demographic = demographicDao.getDemographic(bean.demographicNo);

                //set lefthand module heading and link
	            Dao.setLeftHeading("eConsult");
                String eConsultDisplayUrl = frontendEconsultUrl +  "/?patient_id=" + URLEncoder.encode(demographic.getDemographicNo().toString(), "UTF-8") + "&oneid_email=" + providerEmail + delegateEmailQueryString + "#!/sent";
	            String url = "popupPage(700,960,'eConsult','" + eConsultDisplayUrl + "'); return false;";
	            Dao.setLeftURL(url);


	            //set the right hand heading link
	            String winName = "new eConsult" + bean.demographicNo;
	            String gender = Gender.valueOf(demographic.getSex()).getText().toLowerCase();
	            String createNewEconsultUrl = frontendEconsultUrl + "?oneid_email=" + providerEmail + delegateEmailQueryString + "#!/econsult?patient_id=" + URLEncoder.encode(demographic.getDemographicNo().toString(), "UTF-8") + "&salutation=" + URLEncoder.encode(demographic.getTitle(), "UTF-8") + "&first_name=" + URLEncoder.encode(demographic.getFirstName(), "UTF-8") + "&last_name=" + URLEncoder.encode(demographic.getLastName(), "UTF-8") + "&date_of_birth=" + URLEncoder.encode(demographic.getBirthDayAsString(), "UTF-8") + "&gender=" + URLEncoder.encode(gender, "UTF-8") + "&ohip_number=" + URLEncoder.encode(demographic.getHin(), "UTF-8") + "&ohip_code=" + URLEncoder.encode(demographic.getVer(), "UTF-8");
	            url = "popupPage(700,960,'" + winName + "','" + createNewEconsultUrl + "'); return false;";

	            Dao.setRightURL(url);
                Dao.setRightHeadingID(cmd);  //no menu so set div id to unique id for this action

                try {
                    //Sets the URL to get the list of eConsults
                    url = backendEconsultUrl + "/consult?count=" + 25 + "&offset=" + 0 + "&patient_id=" + demographic.getDemographicNo() + "&requester=" + providerEmail;
                    //Creates an HttpGet with the url to get eConsults and sets a header for the oneIdEmail
                    HttpGet httpGet = new HttpGet(url);

                    String oneIdToken = (String)session.getAttribute("oneid_token");
                    
                    httpGet.addHeader("x-oneid-email", encodedOneIdEmail);
                    httpGet.addHeader("x-access-token", oneIdToken);
                    
                    //Gets an HttpClient that will ignore SSL validation
                    HttpClient httpClient = getHttpClient2();
                    //Executes the GET request and stores the response
                    HttpResponse httpResponse = httpClient.execute(httpGet);
                    
                    if(httpResponse.getStatusLine().getStatusCode() >= 200 && httpResponse.getStatusLine().getStatusCode() < 300 ) {
	                    //Gets the entity from the response and stores it as a JSONObject
	                    String entity = EntityUtils.toString(httpResponse.getEntity());
	                    JSONObject object = new JSONObject(entity);
	
	                    //Creates the onClick string for each eConsult that will be listed
	                    url = "popupPage(700,960, 'eConsult', '" + frontendEconsultUrl + "?oneid_email=%s%s#!/econsult/%s?actor=requester'); return false;";
	                    //Gets the data and then entry sections of the response entity
	                    JSONObject data = object.getJSONObject("data");
	                    JSONArray entryArray = data.getJSONArray("entry");
	                    //Creates a SimpleDateFormat for parsing the dat
	                    SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
	                    //Gets the length of the entryArray
	                    Integer entryLength = entryArray.length();
	
	                    //Loops for the length of the array to output each eConsult
	                    for (Integer i = 0; i < entryLength; i++) {
	                        //Gets the JSONObject from the entryArray and then the resource object from the entry
	                        JSONObject entry = (JSONObject)entryArray.get(i);
	                        JSONObject resource = entry.getJSONObject("resource");
	                        //Gets the eConsultId
	                        String eConsultId = resource.getString("id");
	                        //Gets the last update date
	                        String date = resource.getString("date");
	                        //Gets the eConsult title
	                        String title = resource.getString("description");
	                        String status = resource.getString("status");
	
	                        //Creates a new item to populate and display the eConsult
	                        NavBarDisplayDAO.Item item = new NavBarDisplayDAO.Item();
	                        //Sets the item's title
	                        item.setTitle(title + "(" + status + ")");
	                        //Formats the url string with the oneIdEmail and eConsultId and sets the eConsult's URL
	                        String eConsultLink = String.format(url, providerEmail, delegateEmailQueryString, eConsultId);
	                        item.setURL(eConsultLink);
	                        try {
	                            //Parses and sets the eConsult's date
	                            item.setDate(dateFormatter.parse(date));
	                        }
	                        catch (ParseException e) {
	                            logger.error("Could not parse the date for eConsult " + eConsultId, e);
	                        }
	
	                        //Adds the eConsult to the Dao
	                        Dao.addItem(item);
	                    }
                    } else {
                    	 NavBarDisplayDAO.Item item = new NavBarDisplayDAO.Item();
                    	 item.setURL("javascript:void(0);return false;");
                    	 item.setTitle("Error retrieving data");
                    	 Dao.addItem(item);
                    }
                }
                catch (IOException e) {
                    logger.error("Failed to retrieve eConsults for the OneID account " + providerEmail, e);
                }
                catch (NoSuchAlgorithmException e) {
                    logger.error("Failed to create an HttpClient that allows all SSL", e);
                }
                catch (KeyManagementException e) {
                	 logger.error("Failed to create an HttpClient that allows all SSL", e);
                }
                catch (JSONException e) {
                    logger.error("Failed to convert the response entity to a JSON Object", e);
                } 
            }
            catch (UnsupportedEncodingException uee) {
            	logger.error("The eConsult URL could not be encoded", uee);
            }

            return true;
        }
    }

    public String getCmd() {
         return cmd;
    }

    protected HttpClient getHttpClient2() throws KeyManagementException, NoSuchAlgorithmException {

        //setup SSL
        SSLContext sslcontext = SSLContexts.custom().useTLS().build();
        sslcontext.getDefaultSSLParameters().setNeedClientAuth(true);
        sslcontext.getDefaultSSLParameters().setWantClientAuth(true);
        SSLConnectionSocketFactory sf = new SSLConnectionSocketFactory(sslcontext);

        //setup timeouts
        int timeout = Integer.parseInt(OscarProperties.getInstance().getProperty("dhir.timeout", "60"));
        RequestConfig config = RequestConfig.custom().setSocketTimeout(timeout * 1000).setConnectTimeout(timeout * 1000).build();

        CloseableHttpClient httpclient3 = HttpClients.custom().setDefaultRequestConfig(config).setSSLSocketFactory(sf).build();

        return httpclient3;

    }
}

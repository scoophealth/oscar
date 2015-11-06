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


package oscar.oscarBilling.ca.bc.Teleplan;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.log4j.Logger;
import org.oscarehr.util.MiscUtils;

import oscar.OscarProperties;

/**
 *
 * @author jay
 */
public class TeleplanAPI {
    static Logger log=MiscUtils.getLogger();
    	
    public static final String ExternalActionLogon      = "AsignOn";
    public static final String ExternalActionLogoff     = "AsignOff";
    public static final String ExternalActionChangePW   = "AchangePW";
    public static final String ExternalActionGetLog     = "AgetLog";
    public static final String ExternalActionGetLogList = "AgetLogList";
    public static final String ExternalActionGetRemit   = "AgetRemit";
    public static final String ExternalActionGetAscii   = "AgetAscii";
    public static final String ExternalActionGetAsciiMF = "AgetAsciiMF";
    public static final String ExternalActionPutAscii   = "AputAscii";
    public static final String ExternalActionPutRemit   = "AputRemit";
    public static final String ExternalActionCheckE45   = "AcheckE45";

    
    //public String CONTACT_URL = "https://tlpt2.moh.hnet.bc.ca/TeleplanBroker";
    public String CONTACT_URL = "https://teleplan.hnet.bc.ca/TeleplanBroker";
    
    HttpClient httpclient = null;
	
    /** Creates a new instance of TeleplanAPI */
    public TeleplanAPI() {
        getClient();
    }
    
    public TeleplanAPI(String username,String password){
        getClient();
        
    }
    
    
	

    private void getClient(){
       CONTACT_URL = OscarProperties.getInstance().getProperty("TELEPLAN_URL",CONTACT_URL);
       HttpState initialState = new HttpState();
        // Initial set of cookies can be retrieved from persistent storage and 
        // re-created, using a persistence mechanism of choice,
        Cookie mycookie = new Cookie("moh.hnet.bc.ca","mycookie", "stuff", "/", null, false);        // and then added to your HTTP state instance
        initialState.addCookie(mycookie);

        // Get HTTP client instance
        //HttpClientParams hcParams = new HttpClientParams();
        //hcParams.setParameter("User-Agent","TeleplanPerl 1.0");
        
        httpclient = new HttpClient(); //hcParams);
        httpclient.getHttpConnectionManager().getParams().setConnectionTimeout(30000);
        httpclient.setState(initialState);
        
        httpclient.getParams().setCookiePolicy(CookiePolicy.RFC_2109);
        httpclient.getParams().setParameter("User-Agent","TeleplanPerl 1.0");  
	
    }	
    
    private TeleplanResponse processRequest(String url,NameValuePair[] data){
        TeleplanResponse tr = null;
        try{
            PostMethod post = new PostMethod(url);
            post.setRequestBody(data);
            httpclient.executeMethod(post);

            InputStream in = post.getResponseBodyAsStream();
            log.debug("INPUT STREAM "+in+"\n");

            tr = new TeleplanResponse();
            tr.processResponseStream(in);
            TeleplanResponseDAO trDAO = new TeleplanResponseDAO();
            trDAO.save(tr);
        }catch(Exception e){
            MiscUtils.getLogger().error("Error", e);
        }
        return tr; 
            //display(in);
    }
    
    private TeleplanResponse processRequest(String url,Part[] parts){
        TeleplanResponse tr = null;
        try{ 
            PostMethod filePost = new PostMethod(url); 
            filePost.setRequestEntity( new MultipartRequestEntity(parts, filePost.getParams()) );  
            httpclient.executeMethod(filePost);
            
            InputStream in = filePost.getResponseBodyAsStream();
            tr = new TeleplanResponse();
            tr.processResponseStream(in); 
            TeleplanResponseDAO trDAO = new TeleplanResponseDAO();
            trDAO.save(tr);

            }catch(Exception e){
                MiscUtils.getLogger().error("Error", e);
            }
            return tr; 
    }
   
    
    
    //////////
    //-------------------------------------------------------------------------
    /**
    *Procedure parameters: userid, password, new password, new password
    *Parameters to TeleplanBroker are
    *ExternalAction = "AchangePW"
    *username         = $uid
    *password         = $pw
    *new.password     = $chgpw1
    *confirm.password = $chgpw2
    *Results from TeleplanBroker are:
    *                                "SUCCESS" Password was changed successfully
    *                                "FAILURE" Password was not changed
    */
	public TeleplanResponse changePassword(String username,String password,String newPassword,String confirmPassword){

            NameValuePair[] data = {
              new NameValuePair("username", username),
              new NameValuePair("password", password),
              new NameValuePair("new.password", newPassword),
              new NameValuePair("confirm.password", confirmPassword),

              new NameValuePair("ExternalAction", ExternalActionChangePW)
            };
            return processRequest(CONTACT_URL,data);
	}
	//-------------------------------------------------------------------------
	/**
	*Procedure parameters: userid, password
	*Parameters to TeleplanBroker are
	*ExternalAction = "AsignOn"
	*Username = $uid
	*password = $pw
	*Results from TeleplanBroker are:
	*                                "SUCCESS" for valid logon
	*                                "FAILURE" for invalid logon
	*                                "EXPIRED.PASSWORD" for valid username/password, user must change PW
	*                                                   before the application will return a SUCCESS
	*/
	public TeleplanResponse login(String username,String password){
            NameValuePair[] data = {
              new NameValuePair("username", username),
              new NameValuePair("password", password),
              new NameValuePair("ExternalAction", ExternalActionLogon)
            };
            return processRequest(CONTACT_URL,data);
	}
	//-------------------------------------------------------------------------
	/**
	*Procedure parameters:none
	*Parameters to TeleplanBroker are
	*ExternalAction = "AsignOff"
	*Results from TeleplanBroker are: "SUCCESS" for valid logoff
	*/
	public TeleplanResponse logoff(){
            NameValuePair[] data = {
                new NameValuePair("ExternalAction", ExternalActionLogoff)
            };
            return processRequest(CONTACT_URL,data);
	}
	//-------------------------------------------------------------------------
    /**
    *Procedure parameters: LogType which can be "T" or "L" or "S"
    */
	public TeleplanResponse getCurrentLog(String logtype){
            return getLog(logtype,"CURRENT");	
	}
	//-------------------------------------------------------------------------
	/**
	*Procedure parameters: LogType, LogName
	*Parameters to TeleplanBroker are
	*LOGNAME = Name representing an actual log file (from getLogList) or "CURRENT"
	*ExternalAction = "AgetLog"
	*LOGTYPE = "T" or "L" or "S"
	*MODE = "DOWNLOAD"
	*Results from TeleplanBroker are:
	*                                "SUCCESS" for valid download
	*                                "FAILURE" for problem
	*/
	public TeleplanResponse getLog(String logname,String logtype){
		NameValuePair[] data = {
                  new NameValuePair("LOGNAME", logname),
                  new NameValuePair("LOGTYPE", logtype),
                  new NameValuePair("MODE", "DOWNLOAD"),
                  new NameValuePair("ExternalAction", ExternalActionGetLog)
                };
                return processRequest(CONTACT_URL,data);
    
	}
	//-------------------------------------------------------------------------
	/**
	*Procedure parameters: None
	*Parameters to TeleplanBroker are
	*ExternalAction = "AgetLogList"
	*Results from TeleplanBroker are:
	*                                "SUCCESS" 
	*                                "FAILURE"
	*This will return a file contain a list (1 logname/line) of logs
	*the user can download
	*The format of the logname is: 001E2805.LOG
	*To convert this to the same style as the web download name you must
	*extract the century from the first character (0..Z 0=20, Z=56)
	*extract the Month which is the 4th character (A (Jan)..L(Dec))
	*Then you would create the filename CCYY_MM_DD_Gdd.LOG
	*The filename 001E2805.LOG would convert to 2001_5_28_G05.LOG
	*/
	public TeleplanResponse getLogList(){
            NameValuePair[] data = {
                new NameValuePair("ExternalAction", ExternalActionGetLogList)
            };
            return processRequest(CONTACT_URL,data);
	}
	//-------------------------------------------------------------------------
	/**
	*Procedure parameters: include remittance which can be either "true" or "false"
	*Parameters to TeleplanBroker are
	*remittance = $includeRemit which can be "true" or "false"
	*ExternalAction = "AgetRemit"
	*Results from TeleplanBroker are:
	*                                "SUCCESS" 
	*                                "FAILURE"
	*/
	public TeleplanResponse getRemittance(boolean includeRemittance){
            NameValuePair[] data = {
                new NameValuePair("remittance", Boolean.toString(includeRemittance)),
                new NameValuePair("ExternalAction", ExternalActionGetRemit)
            };
            return processRequest(CONTACT_URL,data);
        }
	//-------------------------------------------------------------------------
	/**
	*Procedure parameters: File Type, which is outlined below
	*Vendors use this section and command to obtain Other Processing files
	*Parameters to TeleplanBroker are
	*filechar = $filetype which can be:
	*      V = VENDORS TEST SELECTION ONLY
	*      I = MSP ICD9 Codes (4&3 some 5)
	*      D = Diagnostic Facility's Codes
	*      G = Geographic Differential Payment Codes
	*      R = Rural Retention Premium Codes
	*      ? = List of Services Available
	*      9 = MSP Technical Use ONLY
	*      3 = MSP Fee Schedule Costs
	*      2 = MSP ICD9 Codes (3 char)
	*      1 = MSP Explanatory Codes List
	*      0 = Get list of valid codes
	*ExternalAction = "AgetAscii"
	*Results from TeleplanBroker are:
	*                                "SUCCESS"
	*                                "FAILURE"
	*/
	public TeleplanResponse getAsciiFile(String filetype){
            NameValuePair[] data = {
                new NameValuePair("filechar", filetype),
                new NameValuePair("ExternalAction", ExternalActionGetAscii)
            };
            return processRequest(CONTACT_URL,data);
	}
	//-------------------------------------------------------------------------
	/**
	*Procedure parameters: File Type which is outlined below
	*#Parameters to TeleplanBroker are
	*This section and command restricted to MoH Internal staff access
	*filechar = $filetype which can be:
	*      V = VENDORS TEST SELECTION ONLY
	*      I = MSP ICD9 Codes (4&3 some 5)
	*      D = Diagnostic Facility's Codes
	*      G = Geographic Differential Payment Codes
	*      R = Rural Retention Premium Codes
	*      ? = List of Services Available
	*      9 = MSP Technical Use ONLY
	*      3 = MSP Fee Schedule Costs
	*      2 = MSP ICD9 Codes (3 char)
	*      1 = MSP Explanatory Codes List
	*ExternalAction = "AgetAsciiMF"
	*Results from TeleplanBroker are:
	*                                "SUCCESS"
	*                                "FAILURE"	
	*/
	public TeleplanResponse getAsciiFileMF(String filetype){
            NameValuePair[] data = {
                new NameValuePair("filechar", filetype),
                new NameValuePair("ExternalAction", ExternalActionGetAsciiMF)
            };
            return processRequest(CONTACT_URL,data);
	}
	//-------------------------------------------------------------------------
	/**
	*Procedure parameters: Filename which is a string representing a file on the local system
	*Parameters to TeleplanBroker are
	*ExternalAction = "AputAscii"
	*This is a RFC1867 Multi-part post
	*Results from TeleplanBroker are:
	*                                "SUCCESS" 
	*                                "FAILURE"
	*/
	public TeleplanResponse putAsciiFile(File f) throws FileNotFoundException{
            
            Part[] parts = { new StringPart("ExternalAction", "AputAscii"), new FilePart("submitASCII", f) }; 
            return processRequest(CONTACT_URL,parts);

//    my ($filename) = @_;
//    my $request = POST $WEBBASE, Content_type => 'form-data',
//                                 Content      => ['submitASCII'    => [ $filename ], 
//                                                  'ExternalAction' => 'AputAscii'
//                                                 ];
//    my $retVal = processRequest($request);	
//    if ($retVal == $SUCCESS)
//    {#valid response
//       if ($Result ne "SUCCESS")
//       {
//           $retVal = $VALID;
//       }
//    }
//    else
//    {
//       $retVal = $ERROR;
//    }
//    return $retVal;
            
    }
 //-------------------------------------------------------------------------
    /**
    *Procedure parameters: Filename which is a string representing a file on the local system
    *Parameters to TeleplanBroker are
    *ExternalAction = "AputRemit"
    *This is a RFC1867 Multi-part post
    *Results from TeleplanBroker are:
    *                                "SUCCESS"
    *                                "FAILURE"
    */
    public TeleplanResponse putMSPFile(File f) throws FileNotFoundException {
        Part[] parts = { new StringPart("ExternalAction", "AputRemit"), new FilePart("submitFile", f) }; 
            return processRequest(CONTACT_URL,parts);
//
//    my ($filename) = @_;
//    my $request = POST $WEBBASE, Content_Type => 'form-data',
//                                 Content      => ['submitFile'    => [ $filename ], 
//                                                  'ExternalAction' => 'AputRemit'
//                                                 ];
//    my $retVal = processRequest($request);	
//    if ($retVal == $SUCCESS)
//    {#valid response
//       if ($Result ne "SUCCESS")
//       {
//           $retVal = $VALID;
//       }
//    }
//    else
//    {
//       $retVal = $ERROR;
//    }
//    return $retVal;
//            return null;
    }
	//-------------------------------------------------------------------------
	/** 
	* Note: Internal field names(Patient Visit Charge) reflect Subsidy Insured Service
	* We cannot change Internal modules but you may reflect externally, see Browser screens
	*                                            
	*Procedure parameters: & Parameters to TeleplanBroker:
	*PHN	  	       	= $phn			(string representing valid PHN Number)
	*dateOfBirthyyyy	= $dateofbirthyyyy	(string denoting numeric year)
	*dateOfBirthmm		= $dateofbirthmm	(string denoting numeric month)
	*dateOfBirthdd		= $dateofbirthdd	(string denoting numeric day)
	*dateOfServiceyyyy	= $dateofserviceyyyy	(string denoting year)
	*dateOfServicemm	= $dateofservicemm	(string denoting numeric month)
	*dateOfServicedd	= $dateofservicedd	(string denoting numeric day)
	*PatientVisitCharge	= $patientvisitcharge	(string representing a boolean true/false)
	*LastEyeExam		= $lasteyeexam		(string representing a boolean true/false)
	*PatientRestriction 	= $patientrestriction	(string representing a boolean true/false)
	*ExternalAction 	= "AcheckE45"
	*Results from TeleplanBroker are:
	*           Still to be determined but "SUCCESS" & "FAILURE" will be returned
	*
	*/
	public TeleplanResponse checkElig(String phn, String dateofbirthyyyy, String dateofbirthmm, String dateofbirthdd, 
              String dateofserviceyyyy, String dateofservicemm, String dateofservicedd, 
              boolean patientvisitcharge, boolean lasteyeexam, boolean patientrestriction){
    
    
            NameValuePair[] data = {
                new NameValuePair("PHN",phn),
                new NameValuePair("dateOfBirthyyyy",dateofbirthyyyy),
                new NameValuePair("dateOfBirthmm",dateofbirthmm),
                new NameValuePair("dateOfBirthdd",dateofbirthdd),
                new NameValuePair("dateOfServiceyyyy",dateofserviceyyyy),
                new NameValuePair("dateOfServicemm",dateofservicemm),
                new NameValuePair("dateOfServicedd", dateofservicedd),
                new NameValuePair("PatientVisitCharge",Boolean.toString(patientvisitcharge)),
                new NameValuePair("LastEyeExam", Boolean.toString(lasteyeexam)),
                new NameValuePair("PatientRestriction", Boolean.toString(patientrestriction)),
                new NameValuePair("ExternalAction", ExternalActionCheckE45)
            };
            return processRequest(CONTACT_URL,data);                      
                                  

        }
//-------------------------------------------------------------------------
    
    ///////////
}

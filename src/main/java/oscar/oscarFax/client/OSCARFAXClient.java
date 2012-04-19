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


/*
 * OSCARFAXClient.java
 *
 * Created on February 17, 2003, 12:08 PM
 */
package oscar.oscarFax.client;
import java.util.Iterator;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

import org.oscarehr.util.MiscUtils;
/**
 *
 * @author  Jay
 */
public class OSCARFAXClient {
    SOAPConnectionFactory scf;
    SOAPConnection connection;
    MessageFactory mf;
    java.net.URL endpoint;
    javax.xml.messaging.URLEndpoint endpoint2;
    String oscarSoapMessageError  = null;
    String requestId = null;
    String jobId = null;
    /** Creates a new instance of OSCARFAXClient */
    public OSCARFAXClient() {
        try{
            scf = SOAPConnectionFactory.newInstance();
            connection = scf.createConnection();
            
            mf = MessageFactory.newInstance();
        }catch(Exception e){
            MiscUtils.getLogger().error("Error", e);
        }
    }
    
    public OSCARFAXSOAPMessage createOSCARFAXSOAPMessage() throws SOAPException{
        OSCARFAXSOAPMessage msg = new OSCARFAXSOAPMessage(mf);
        
        
        return msg;
    }
    
    //public boolean sendMessage(OSCARFAXSOAPMessage msg,java.net.URL  endpoint) throws SOAPException{
    public boolean sendMessage(OSCARFAXSOAPMessage msg,javax.xml.messaging.URLEndpoint  endpoint) throws SOAPException{
   
        this.endpoint2 = endpoint;
        SOAPMessage reply = connection.call(msg.getSOAPMessage(), endpoint2);
        return handleResponse(reply);        
    }
    
    public void OSCARFAXClose() throws SOAPException{
        connection.close();            
    }
    
    public boolean handleResponse(SOAPMessage msg){
        boolean retval = false;
        try{
            SOAPEnvelope enve = msg.getSOAPPart().getEnvelope();
            SOAPBody bod1 = enve.getBody();
                                             
            Iterator mainIt = bod1.getChildElements(enve.createName("JobAccepted"));
            if ( mainIt.hasNext() ){
                SOAPElement soapElement = (SOAPElement) mainIt.next();
                
                jobId = getTheValue(soapElement,  enve, "JobId");
                requestId = getTheValue(soapElement,enve, "RequestId");                  
                retval = true;
            }else{ //ERROR
                mainIt = bod1.getChildElements(enve.createName("Error"));
                if ( mainIt.hasNext() ){
                   SOAPElement soapElement = (SOAPElement) mainIt.next();
                   oscarSoapMessageError = getTheValue(soapElement,  enve, "ErrorMessage");                    
                }
            }
        }catch(Exception e){MiscUtils.getLogger().error("Error", e);}
            
        return retval;
    }
    
    public String getTheValue(SOAPElement sbe,SOAPEnvelope enve,String name){        
        String retval = null;
        String currNode= null;

        try{
            Iterator subMain = sbe.getChildElements(enve.createName(name));
            if (subMain.hasNext()){            
                SOAPElement SBE2 = ( SOAPElement )  subMain.next();                
                retval = SBE2.getValue();                    
            }                              
        }catch(Exception e){
        	MiscUtils.getLogger().error("I had probs with "+currNode, e);}
        return retval;
    }  
    
    public String getJobId(){ return jobId; }
    public String getRequestId(){ return requestId;}
    public String getErrorMessage(){return oscarSoapMessageError;}
    
    
}

/*
 * OSCARFAXClient.java
 *
 * Created on February 17, 2003, 12:08 PM
 */
package oscar.oscarFax.client;
import java.io.*;

import javax.xml.soap.*;
import java.net.URL;

import javax.mail.internet.*;

import javax.xml.transform.*;
import javax.xml.transform.stream.*;

import org.dom4j.*;
import javax.xml.parsers.*; 
import java.util.*;
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
            e.printStackTrace();
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
            msg.writeTo(System.out);
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
        }catch(Exception e){e.printStackTrace();}
            
        return retval;
    }
    
    public String getTheValue(SOAPElement sbe,SOAPEnvelope enve,String name){        
        String retval = null;
        String currNode= null;
        //System.out.println("sbe = "+sbe.getElementName().getLocalName());
        try{
            Iterator subMain = sbe.getChildElements(enve.createName(name));
            if (subMain.hasNext()){            
                SOAPElement SBE2 = ( SOAPElement )  subMain.next();                
                retval = SBE2.getValue();                    
            }                              
        }catch(Exception e){System.out.print("I had probs with "+currNode);}
        return retval;
    }  
    
    public String getJobId(){ return jobId; }
    public String getRequestId(){ return requestId;}
    public String getErrorMessage(){return oscarSoapMessageError;}
    
    
}



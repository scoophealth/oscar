/*
 * DefaultHandler.java
 *
 * Created on May 23, 2007, 4:30 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package oscar.oscarLab.ca.all.upload.handlers;



import org.apache.log4j.Logger;


/**
 *
 * @author wrighd
 */
public class HHSEmrDownloadHandler extends DefaultHandler implements MessageHandler {
    Logger logger = Logger.getLogger(HHSEmrDownloadHandler.class);
    
    @Override
    String getHl7Type(){
        System.out.println("HSSEmrDownloadHandler getting called");
        return "HHSEMR";
    }
    
    
    
    //TODO: Process accession Number
    //TODO: 
}
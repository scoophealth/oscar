/*
 * MessageHandler.java
 *
 * Created on June 4, 2007, 10:50 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package oscar.oscarLab.ca.all.parsers;

import ca.uhn.hl7v2.HL7Exception;
import java.util.ArrayList;

/**
 *
 * @author wrighd
 */
public interface MessageHandler {
    
    
    /**
     *  Initialize the message handler with the message and set up any global 
     *  variables that need to be specified.
     */
    public void init(String hl7Body) throws HL7Exception;    
    
    /**
     *  Return the message type
     */
    public String getMsgType();
    
    /**
     *  Return the date and time of the message
     */
    public String getMsgDate();
    
    /**
     *  Returns the priority of the message stored in the obr segments
     */
    public String getMsgPriority();
        
    /**
     *  Return the number of OBR Segments in the message
     */
    public int getOBRCount();    
    
    /**
     *  Return the number of OBX Segments within the OBR group specified by i.
     */
    public int getOBXCount( int i);
    
    /**
     *  Return the name of the ith OBR Segment, usually stored in the 
     *  UniversalServiceIdentifier
     */
    public String getOBRName( int i);
    
    /**
     *  Return the timestamp of the observastion, usually in the OBX segment
     */
    public String getTimeStamp( int i, int j);
    
    /**
     *  Return true if an abnormal flag other than 'N' is set for the OBX segment
     *  specified by j, in the ith OBR group. Return false otherwise
     */
    public boolean isOBXAbnormal( int i, int j);
    
    /**
     *  Retrieve the abnormal flag if any from the OBX segment specified by j in
     *  the ith OBR group.
     */
    public String getOBXAbnormalFlag( int i, int j);
    
    /**
     *  Return the observation header. May be stored in either the OBR or OBX
     *  segments. It is used to separate the observations into groups.
     *  ie/ 'CHEMISTRY' 'HEMATOLOGY' '
     */
    public String getObservationHeader( int i, int j);
    
    /**
     *  Return the identifier from the OBX Segment
     */
    public String getOBXIdentifier( int i, int j);
    
    /**
     *  Return the name of the OBX Segment specified by j in the ith OBR group
     */
    public String getOBXName( int i, int j);
    
    /**
     *  Return the result sorted in the jth OBX segment of the ith OBR group
     */
    public String getOBXResult( int i, int j);
    
    /**
     *  Return the reference range sorted in the jth OBX segment of the ith OBR group
     */
    public String getOBXReferenceRange( int i, int j);
    
    /**
     *  Return the units sorted in the jth OBX segment of the ith OBR group
     */
    public String getOBXUnits( int i, int j);
    
    /**
     *  Return the result status sorted in the jth OBX segment of the ith OBR group
     */
    public String getOBXResultStatus( int i, int j);
    
    /**
     *  Return a list of all possible headers retrieved from getObservationHeader
     *  each header will only occur once in the list
     */
    public ArrayList getHeaders();
    
    /**
     *  Return the number of comments that belong to the ith OBR segment.
     *  Used for comments which will belong to the group specified in the 
     *  ArrayList returned by getHeaders. They will appear at the end of the group
     *  and not be attached to any specific OBR or OBX segment
     */
    public int getOBRCommentCount( int i);
    
    /**
     *  Return the jth comment of the ith OBR segment. The comments will be placed
     *  as specified above.
     */
    public String getOBRComment( int i, int j);
    
    /**
     *  Return the number of comments belonging to the jth OBX segment of the
     *  ith OBR group.
     */
    public int getOBXCommentCount( int i, int j);
    
    /**
     *  Return the kth comment of the jth OBX segment of the ith OBR group
     */
    public String getOBXComment( int i, int j, int k);
    
    
    /**
     *  Return the name of the patient
     */
    public String getPatientName();
    
    /**
     *  Return the first name of the patient
     */ 
    public String getFirstName();
    
    /**
     *  Return the last name of the patient
     */
    public String getLastName();
    
    /**
     *  Return the patients date of birth
     */
    public String getDOB();
    
    
    /**
     *  Return the age of the patient (this is not specified in the message but
     *  can be calculated using the patients date of birth)
     */
    public String getAge();
    
    /**
     *  Return the gender of the patient: 'M' or 'F'
     */
    public String getSex();
    
    /**
     *  Return the patients 10-digit health number
     */
    public String getHealthNum();
    
    /**
     *  Return the home phone number of the patient
     */
    public String getHomePhone();
    
    /**
     *  Return the work phone number of the patient
     */
    public String getWorkPhone();
    
    
    /**
     *  Return the patients location, usually the facility from which the 
     *  report has been sent
     */
    public String getPatientLocation();
    
    
    /**
     *  Return the date at which the observations took place
     */
    public String getServiceDate();
    
    /**
     *  Return the status of the report, 'F' is returned for a final report, 
     *  otherwise the report is partial
     */
    public String getOrderStatus();
    
    /**
     *  Returns the number of obx segments that are final.
     */
    public int getOBXFinalResultCount();
    
    /**
     *  Return the clients reference number, usually corresponds to the doctor
     *  who requested the report or the requesting facility.
     */
    public String getClientRef();
    
    /**
     *  Return the accession number
     */
    public String getAccessionNum();
    
    /**
     *  Return the name of the doctor who requested the report
     */
    public String getDocName();

    /**
     *  Return the names of the doctors which the report should be copied to.
     */
    public String getCCDocs();
    
    /**
     *  Return an ArrayList of the requesting doctors billing number and the
     *  billing numbers of the cc'd docs
     */
    public ArrayList getDocNums();
}

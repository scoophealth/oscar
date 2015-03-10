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


package oscar.oscarLab.ca.all.parsers;

import java.util.ArrayList;

import ca.uhn.hl7v2.HL7Exception;

/**
 *  When implementing this class a global variable 'msg' should be created as
 *  follows:
 *      ORU_R01 msg = null;
 *
 *  ORU_R01 is located at 'ca.uhn.hl7v2.model.vXX.message.ORU_R01' where 'vXX'
 *  is the version specified by the hl7 messages that you inted to parse, it is
 *  stored in the 12th field of the MSH segment.
 *
 *  'msg' should be initialized in the init(String hl7Body) method
 *
 *  The results for the majority of the methods should be retrieved from the
 *  'msg' object
 */
public interface MessageHandler {


    /**
     *  Initialize the 'msg' object and any other global variables that may be
     *  needed.
     *
     *  The 'msg' object should be initialized with the following code:
     *       Parser p = new PipeParser();
     *       p.setValidationContext(new NoValidation());
     *       msg = (ORU_R01) p.parse(hl7Body.replaceAll( "\n", "\r\n" ));

     *	- If you wish to validate the message please see the hapi documentation at
     *		'http://hl7api.sourceforge.net/'
     *  - The replaceAll statement is necessary to ensure that the parser
     *  correctly reads the end of each line.
     */
    public void init(String hl7Body) throws HL7Exception;

    /**
     *  Return the message type
     *  - The message type returned should be the same as the prefix of your
     *  handlers name
     *      ie/ message type = XXXX
     *          handler name = XXXXHandler
     */
    public String getMsgType();

    /**
     *  Return the date and time of the message, usually located in the 7th
     *  field of the MSH segment
     */
    public String getMsgDate();

    /**
     *  A String containing a single letter represinting the priority
     *		"C" - Critical 		"S" - Stat/Urgent
     *		"U" - Unclaimed 	"A" - ASAP
     *		"L" - Alert 		""  - Routine
     *  If there is no priority specified in the documentation for your message
     *  type then just return the empty string ""
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
     *  Return the date and time of the observation refered to by the jth obx
     *  segment of the ith obr group. If the date and time is not specified
     *  within the obx segment it should be specified within the obr segment.
     */
    public String getTimeStamp( int i, int j);

    /**
     *  Return true if an abnormal flag other than 'N' is returned by
     *  getOBXAbnormalFlag( i, j ) for the OBX segment specified by j, in the
     *  ith OBR group. Return false otherwise.
     */
    public boolean isOBXAbnormal( int i, int j);

    /**
     *  Retrieve the abnormal flag if any from the OBX segment specified by j in
     *  the ith OBR group.
     */
    public String getOBXAbnormalFlag( int i, int j);

    /**
     *  Return the observation header which represents the observation stored in
     *  the jth OBX segment of the ith OBR group. May be stored in either the
     *  OBR or OBX segment. It is used to separate the observations into groups.
     *  ie/ 'CHEMISTRY' 'HEMATOLOGY' '
     */
    public String getObservationHeader( int i, int j);

    /**
     *  Return the identifier from jth OBX segment of the ith OBR group. It is
     *  usually stored in the first component of the third field of the OBX
     *  segment.
     */
    public String getOBXIdentifier( int i, int j);

    /**
     * Return the obx value type
     * @param i
     * @param j
     * @return String the obx value
     */
    public String getOBXValueType(int i, int j);


    /**
     *  Return the name of the jth OBX segment of the ith OBR group. It is
     *  usually stored in the second component of the third field of the OBX
     *  segment.
     */
    public String getOBXName( int i, int j);

    /**
     *  Return the result from the jth OBX segment of the ith OBR group
     */
    public String getOBXResult( int i, int j);

    /**
     *  Return the reference range from the jth OBX segment of the ith OBR group
     */
    public String getOBXReferenceRange( int i, int j);

    /**
     *  Return the units from the jth OBX segment of the ith OBR group
     */
    public String getOBXUnits( int i, int j);

    /**
     *  Return the result status from the jth OBX segment of the ith OBR group
     */
    public String getOBXResultStatus( int i, int j);

    /**
     *  Return a list of all possible headers retrieved from getObservationHeader
     *  each header will only occur once in the list
     */
    public ArrayList<String> getHeaders();

    /**
     *  Return the number of comments (usually NTE segments) that follow ith
     *  OBR segment, this should usually be either 0 or 1.
     */
    public int getOBRCommentCount( int i);

    /**
     *  Return the jth comment of the ith OBR segment.
     */
    public String getOBRComment( int i, int j);

    /**
     *  Return the number of comments (usually NTE segments) following the jth
     *  OBX segment of the ith OBR group.
     */
    public int getOBXCommentCount( int i, int j);

    /**
     *  Return the kth comment of the jth OBX segment of the ith OBR group
     */
    public String getOBXComment( int i, int j, int k);


    /**
     *  Return the name of the patient. The format should be the first name
     *  followed by the last name while being separated by a space.
     *  String firstName = getFirstName();
     *  String lastName = getLastName();
     */
    public String getPatientName();

    /**
     *  Return the given name of the patient
     */
    public String getFirstName();

    /**
     *  Return the family name of the patient
     */
    public String getLastName();

    /**
     *  Return the patients date of birth
     */
    public String getDOB();


    /**
     *  Return the age of the patient (this is not specified in the message but
     *  can be calculated using the patients date of birth)
     *
     *  Please see the other implementations of MessageHandler for an example of
     *  how this is done.
     */
    public String getAge();

    /**
     *  Return the gender of the patient: 'M' or 'F'
     */
    public String getSex();

    /**
     *  Return the patients health number
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
     *  report has been sent ( the 4th field of the MSH segment )
     */
    public String getPatientLocation();


    /**
     *  Return the service date of the message
     */
    public String getServiceDate();

    /**
     *  Return the request date of the message
     */
    public String getRequestDate(int i);

    /**
     *  Return the status of the report, 'F' is returned for a final report,
     *  otherwise the report is partial
     */
    public String getOrderStatus();

    /**
     *  Returns the number used to order labs with matching accession numbers.
     *
     *  - Multiple labs with the same accession number must display in a certain
     *  order. They are ordered by their date but if two labs with the same
     *  accession number have the same date they are ordered by the number
     *  retrievied by this method
     *
     *  - The newest lab will have the greatest number returned from this method.
     *
     *  - If the hl7 messages do not contain a version number or other such
     *  number, the total number of obx segments with final results should be
     *  returned
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
     *  Return the name of the doctor who requested the report, the name should
     *  be formatted as follows:
     *      'PREFIX' 'GIVEN NAME' 'MIDDLE INITIALS' 'FAMILY NAME' 'SUFFIX' 'DEGREE'
     */
    public String getDocName();

    /**
     *  Return the names of the doctors which the report should be copied to. The
     *  formatting of the names should be the same as in the method above. The
     *  names should be separated by a comma and a space.
     */
    public String getCCDocs();

    /**
     *  Return an ArrayList of the requesting doctors billing number and the
     *  billing numbers of the cc'd docs
     */
    public ArrayList getDocNums();

    /**
     * Returns a string audit of the messages.  If not required handler should just return an empty string;
     */
    public String audit();

    public String getFillerOrderNumber();

    public String getEncounterId();

    public String getRadiologistInfo();

    public String getNteForOBX(int i,int j);
    
    public String getNteForPID();
}

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


package oscar.oscarLab.ca.all.upload.handlers;

import org.oscarehr.util.LoggedInInfo;

/**
 *
 * @author wrighd
 */
public interface MessageHandler {
    
    
    /**
     *  The flat file specified by 'fileName' will be parsed and each hl7 message
     *  contained within the file will be uploaded to the database seperately.
     *      - The messages are usually separated by either MSH segments or PID
     *      segments. If they are seperated by PID segments the MSH segment from
     *      the beginning of the file must be included at the beginning of each
     *      hl7_message.
     *
     *  If the hl7 message is a lab and 'oscar.oscarLab.ca.all.parsers.XXXXHandler'
     *  has been created 'oscar.oscarLab.ca.all.upload.MessageUploader  routeReport("XXXX", hl7_message)'
     *  should be used to upload the message to the database.
     *
     *  If any exceptions are encountered the method should return 'null'
     *  otherwise it should return the String "success"
     */
    public String parse(LoggedInInfo loggedInInfo, String serviceName, String fileName,int fileId, String ipAddr);
    
}

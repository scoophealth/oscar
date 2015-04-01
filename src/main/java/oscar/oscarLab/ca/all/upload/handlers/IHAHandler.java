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

//home/marc/t/oscar/src/main/java/oscar/oscarLab/ca/all/upload/handlers/IHAHandler.java
//Created on December 8, 2009. Modified from DefaultHandler.java

package oscar.oscarLab.ca.all.upload.handlers;

import java.io.FileInputStream;
//*import java.sql.Connection;
//*import java.sql.PreparedStatement;
//*import java.sql.ResultSet;
//*import java.sql.SQLException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilderFactory;

//*import ca.uhn.hl7v2.HL7Exception;
//*import ca.uhn.hl7v2.model.Segment;


//*import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.oscarehr.common.hl7.v2.oscar_to_oscar.DynamicHapiLoaderUtils;
import org.oscarehr.util.LoggedInInfo;
//*import org.oscarehr.util.DbConnectionFilter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import oscar.oscarLab.ca.all.parsers.DefaultGenericHandler;
import oscar.oscarLab.ca.all.upload.MessageUploader;

public class IHAHandler extends DefaultGenericHandler implements MessageHandler {
    Logger logger = Logger.getLogger(IHAHandler.class);
    String hl7Type = null;
    String proNo,UserID, Password,Alias;
    ArrayList<String> headerList = null;
    Object terser;
    Object msg = null;


    String getHl7Type(){
        return "IHA";
    }

    @Override
    public String getMsgType(){
        return("IHA");
    }

    @Override
    public ArrayList<String> getHeaders(){
       headerList = new ArrayList<String>();

       for (int i = 0; i < getOBRCount();i++){
            headerList.add(getOBRName(i));
            logger.debug("ADDING to header "+getOBRName(i));
       }
       return headerList;
    }
    
    @Override
    public String getObservationHeader(int i, int j){
        return headerList.get(i);
    }

    @Override
    public String getOBXReferenceRange(int i, int j){
        return(getOBXField(i, j, 7, 0, 3));
    }

    @Override
    public String getAccessionNum(){
        try{
            String accessionNum = getString(DynamicHapiLoaderUtils.terserGet(terser,"/.MSH-10-1"));
            int firstDash = accessionNum.indexOf("-");
            int secondDash = accessionNum.indexOf("-", firstDash+1);
            return(accessionNum.substring(firstDash+1, secondDash));
        }catch(Exception e){
            return("");
        }
    }

    public void setParameters(String proNo,String UserID,String Password,String Alias) {
        this.proNo=proNo;
        this.UserID=UserID;
        this.Password=Password;
        this.Alias=Alias;
    }

    @Override
	public String parse(LoggedInInfo loggedInInfo, String serviceName, String fileName, int fileId, String ipAddr) {
        Document xmlDoc = getXML(fileName);
        Node          node;
        Element       element;
        NamedNodeMap  nnm = null;
        String msgId = null;
        String result = null;

        if(xmlDoc != null){
            String hl7Body = null;
            String attrName = null;
            try{
                msgId=null;
                NodeList allNodes = xmlDoc.getElementsByTagNameNS("*","*");
                for (int i=1; i<allNodes.getLength(); i++){
                	try {
	                    element = (Element)allNodes.item(i);
	                    nnm = element.getAttributes();
	                    if (nnm != null) {
	                        for (int j=0; j<nnm.getLength(); j++) {
	                            node = nnm.item(j);
	                            attrName = node.getNodeName();
	                            if(attrName.equals("msgId")) {
	                                msgId=node.getNodeValue();
	                            }
	                        }
	                    }
	                    hl7Body = allNodes.item(i).getFirstChild().getTextContent();
	                    if (hl7Body != null && hl7Body.indexOf("\nPID|") > 0){
	                        logger.info("using xml HL7 Type "+getHl7Type());
	                        MessageUploader.routeReport(loggedInInfo, serviceName, "IHA", hl7Body, fileId);
	                        result += "success:" + msgId + ",";
	                    }
                	}
                	catch(Exception e) {
                		result += "fail:" + msgId + ",";
                	}
                }
            }catch(Exception e){
                MessageUploader.clean(fileId);
                logger.error("ERROR:", e);
                return null;
            }
        }
        return(result);
    }

    /*
     *  Return the message as an xml document if it is in the xml format
     */
    private Document getXML(String fileName){
        try{
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(false);
            Document doc = factory.newDocumentBuilder().parse(new FileInputStream(fileName));
            return(doc);

            // Ignore exceptions and return false
        }catch(Exception e){
            return(null);
        }
    }
}

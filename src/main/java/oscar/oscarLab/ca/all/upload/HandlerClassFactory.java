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
 * HandlerClassFactory.java
 *
 * Created on May 23, 2007, 11:52 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package oscar.oscarLab.ca.all.upload;

import java.io.InputStream;
import java.util.List;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.oscarehr.util.MiscUtils;

import oscar.OscarProperties;
import oscar.oscarLab.ca.all.upload.handlers.DefaultHandler;
import oscar.oscarLab.ca.all.upload.handlers.MessageHandler;

public final class HandlerClassFactory {
    
    private static final Logger logger = MiscUtils.getLogger();
    
    private HandlerClassFactory(){
    	// don't instantiate
    }
    
    /*
     *  Create and return the message handler corresponding to the message type
     */
    public static MessageHandler getHandler(String type) {
        Document doc = null;
        String msgType;
        String msgHandler = "";
        
        String enabled = OscarProperties.getInstance().getProperty("lab.handler."+type+".enabled", "false");
        if(!"true".equals(enabled)) {
			logger.info("Handler " + type + " is not enabled. add lab.handler."+type+".enabled=true in your properties file");
			return null;
		}
        
        if (type.equals("")){
            logger.debug("Type not specified using Default Handler");
            return( new DefaultHandler());
        }
        
       
        
        try{
            InputStream is = HandlerClassFactory.class.getClassLoader().getResourceAsStream("oscar/oscarLab/ca/all/upload/message_config.xml");
            SAXBuilder parser = new SAXBuilder();
            doc = parser.build(is);
            
            Element root = doc.getRootElement();

            @SuppressWarnings("unchecked")
            List items = root.getChildren();
            for (int i = 0; i < items.size(); i++){
                Element e = (Element) items.get(i);
                msgType = e.getAttributeValue("name");
                
                String className = e.getAttributeValue("className");
                if (msgType.equals(type) && (className.indexOf(".")==-1) )
                    msgHandler = "oscar.oscarLab.ca.all.upload.handlers."+e.getAttributeValue("className");
                if (msgType.equals(type) && (className.indexOf(".")!=-1) )
                	msgHandler = className;
            }
            
            
            
        }catch(Exception e){
            logger.error("Could not parse config file", e);
        }
        // create and return the message handler
        if (msgHandler.equals("")){
            return( new DefaultHandler() );
        }else{
            try {
                @SuppressWarnings("unchecked")
                Class classRef = Class.forName(msgHandler);
                MessageHandler mh = (MessageHandler) classRef.newInstance();
                logger.debug("Message handler '"+msgHandler+"' created successfully");
                return(mh);
            } catch (Exception e){
                logger.error("Could not create message handler: "+msgHandler+", Using default message handler instead", e);
                return(new DefaultHandler());
            }
        }
    }
    
    // this method is added to get the DefaultHandler during Private/Decryption Key upload in HRM
    public static DefaultHandler getDefaultHandler(){
        logger.debug("Type not specified using Default Handler");
        return( new DefaultHandler());
    }

}

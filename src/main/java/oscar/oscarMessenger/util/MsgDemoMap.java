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
 * MsgDemoMap.java
 *
 * Created on April 24, 2005, 3:47 PM
 */

package oscar.oscarMessenger.util;

import java.util.Hashtable;
import java.util.Vector;

import org.oscarehr.common.dao.MsgDemoMapDao;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.MsgDemoMapPK;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.util.ConversionUtils;
/**
 *
 * @author root
 */
public class MsgDemoMap {
    
	private MsgDemoMapDao dao = SpringUtils.getBean(MsgDemoMapDao.class);
	
    public MsgDemoMap() {
    }
    
    public void linkMsg2Demo(String msgId, String demographic_no){

    	org.oscarehr.common.model.MsgDemoMap mdm = new org.oscarehr.common.model.MsgDemoMap();
    	mdm.setId(new MsgDemoMapPK(Integer.parseInt(msgId),Integer.parseInt(demographic_no)));
    	
    	if(dao.find(new MsgDemoMapPK(Integer.parseInt(msgId),Integer.parseInt(demographic_no))) == null)
    		dao.persist(mdm);
    	
    }
    
    public Hashtable getDemoMap (String msgId){
        Hashtable demoMap = new Hashtable();
        
        MsgDemoMapDao dao = SpringUtils.getBean(MsgDemoMapDao.class);
        dao.getMessagesAndDemographicsByMessageId(ConversionUtils.fromIntString(msgId));
        for(Object[] o : dao.getMessagesAndDemographicsByMessageId(ConversionUtils.fromIntString(msgId))) {
        	org.oscarehr.common.model.MsgDemoMap m = (org.oscarehr.common.model.MsgDemoMap) o[0];
        	Demographic d = (Demographic) o[1];
        	demoMap.put("" + d.getDemographicNo(), d.getFullName() );
        }

        return demoMap;
    }
    
    public Vector getMsgVector(String demographic_no){
        Vector msgVector= new Vector();
        MsgDemoMapDao dao = SpringUtils.getBean(MsgDemoMapDao.class);
        for(Object[] o : dao.getMapAndMessagesByDemographicNo(ConversionUtils.fromIntString(demographic_no))) {
        	org.oscarehr.common.model.MsgDemoMap map = (org.oscarehr.common.model.MsgDemoMap) o[0];
        	msgVector.add("" + map.getId().getMessageId());
        }
        return msgVector;
    }
    
    public void unlinkMsg (String demographic_no, String messageID){        
        MiscUtils.getLogger().debug("input msgId: " + messageID + "  input demographic_no: " + demographic_no);
                
    	dao.remove(new MsgDemoMapPK(Integer.parseInt(messageID),Integer.parseInt(demographic_no)));
       
    }
}

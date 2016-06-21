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
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.oscarehr.jobs;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import org.apache.log4j.Logger;
import org.oscarehr.common.dao.ProviderDataDao;
import org.oscarehr.common.dao.ResidentOscarMsgDao;
import org.oscarehr.common.dao.UserPropertyDAO;
import org.oscarehr.common.jobs.OscarRunnable;
import org.oscarehr.common.model.OscarMsgType;
import org.oscarehr.common.model.Provider;
import org.oscarehr.common.model.ProviderData;
import org.oscarehr.common.model.ResidentOscarMsg;
import org.oscarehr.common.model.Security;
import org.oscarehr.common.model.UserProperty;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;
import oscar.oscarMessenger.data.MsgMessageData;
import oscar.oscarMessenger.data.MsgProviderData;
import oscar.oscarMessenger.util.MsgDemoMap;

/**
 *
 * @author rjonasz
 */
public class OscarMsgReviewSender implements OscarRunnable {
    
    private Provider provider = null;
    private Security security = null;
    private static final String MESSAGE = "Hello, the following charts require your attention\n Sapere aude!";
    private static final String SUBJECT = "Chart Review";
    private static final Calendar DEFAULT_TIME = new GregorianCalendar(0, 0, 0, 9, 0);
    private final Logger logger = MiscUtils.getLogger();
    
    @Override
    public void run() {
        
        String userNo = provider.getProviderNo();
        String userName = "System";
        
        logger.info("Starting to send OSCAR Review Messages");
        
        Integer defaultHour = DEFAULT_TIME.get(Calendar.HOUR_OF_DAY);
        Integer defaultMin = DEFAULT_TIME.get(Calendar.MINUTE);
        
        Calendar now = GregorianCalendar.getInstance();
        Integer currentHour = now.get(Calendar.HOUR_OF_DAY);
        Integer currentMinute = now.get(Calendar.MINUTE);
        
        currentHour = (currentMinute > 45 ? currentHour + 1 : currentHour);
        currentMinute = (currentMinute > 45 || currentMinute < 15 ? 0 : 30);
        
        
        String time = String.valueOf(currentHour) + ":" + String.valueOf(currentMinute);
        
        ProviderDataDao providerDataDao = SpringUtils.getBean(ProviderDataDao.class);
        
        List<ProviderData> providerList = providerDataDao.findAllBilling("1");
        
        List<String> providerNosList = new ArrayList<String>();
        for( ProviderData p : providerList ) {
            providerNosList.add(p.getId());
        }
        
        UserPropertyDAO propertyDao = SpringUtils.getBean(UserPropertyDAO.class);        
        List<UserProperty> properties = propertyDao.getPropValues(UserProperty.OSCAR_MSG_RECVD, time);
        
        ResidentOscarMsgDao residentOscarMsgDao = SpringUtils.getBean(ResidentOscarMsgDao.class);
        List<ResidentOscarMsg> residentOscarMsgList;
        String[] providers = new String[1];
        MsgMessageData msgData = new MsgMessageData();
        ArrayList<MsgProviderData> providerListing, localProviderListing;
        String curLoco = null;
        String sentToWho = null;
        String messageId = null;
        
        for( UserProperty p : properties ) {
            
            if( providerNosList.indexOf(p.getProviderNo()) > -1 ) {
            
                residentOscarMsgList = residentOscarMsgDao.findBySupervisor(p.getProviderNo());
                StringBuilder msgInfo = new StringBuilder();
                int idx = 1;
                for( ResidentOscarMsg r : residentOscarMsgList ) {
                    msgInfo = msgInfo.append(r.getDemographic_no()).append(":").append((r.getAppointment_no()==null ? "null" : r.getAppointment_no())).append(":").append(r.getId()).append(":").append(r.getNote_id());
                    if( idx < residentOscarMsgList.size() ) {
                        msgInfo = msgInfo.append(",");
                    }
                    ++idx;
                }
                providerNosList.remove(p.getProviderNo());
                if( residentOscarMsgList.size() > 0 ) {
                    providers[0] = p.getProviderNo();
                    providers = msgData.getDups4(providers);
                    providerListing = msgData.getProviderStructure(providers);
                    localProviderListing = msgData.getLocalProvidersStructure();
                    curLoco = msgData.getCurrentLocationId();
                    sentToWho = msgData.createSentToString(localProviderListing);

                    messageId = msgData.sendMessageReview(MESSAGE, SUBJECT, userName, sentToWho, userNo, providerListing, null, null, OscarMsgType.OSCAR_REVIEW_TYPE,msgInfo.toString());
                    logger.info("SENT Review OSCAR MESSAGE");
                    if( messageId != null ) {
                        for( ResidentOscarMsg res : residentOscarMsgList ) {
                            MsgDemoMap msgDemoMap = new MsgDemoMap();
                            msgDemoMap.linkMsg2Demo(messageId, String.valueOf(res.getDemographic_no()));                        
                        }


                    }
                }
            }
        }
        
        if( currentHour.equals(defaultHour) && currentMinute.equals(defaultMin) && providerNosList.size() > 0  ) {
            for( String p : providerNosList ) {
                String userProp = propertyDao.getStringValue(p, UserProperty.OSCAR_MSG_RECVD);
                
                if( userProp == null ) {
                    residentOscarMsgList = residentOscarMsgDao.findBySupervisor(p);
                    StringBuilder msgInfo = new StringBuilder();
                    int idx = 1;
                    for( ResidentOscarMsg r : residentOscarMsgList ) {
                        msgInfo = msgInfo.append(r.getDemographic_no()).append(":").append(r.getAppointment_no()==null ? "null" : r.getAppointment_no()).append(":").append(r.getId()).append(":").append(r.getNote_id());
                        if( idx < residentOscarMsgList.size() ) {
                            msgInfo = msgInfo.append(",");
                        }
                        ++idx;
                    }
                    if( residentOscarMsgList.size() > 0 ) {                
                        providers[0] = p;
                        providers = msgData.getDups4(providers);
                        providerListing = msgData.getProviderStructure(providers);
                        localProviderListing = msgData.getLocalProvidersStructure();
                        curLoco = msgData.getCurrentLocationId();
                        sentToWho = msgData.createSentToString(localProviderListing);
                        messageId = msgData.sendMessageReview(MESSAGE, SUBJECT, userName, sentToWho, userNo, providerListing, null, null, OscarMsgType.OSCAR_REVIEW_TYPE,msgInfo.toString());
                        logger.info("SENT DEFAULT TIME OSCAR Review MESSAGE");
                        if( messageId != null ) {
                            for( ResidentOscarMsg res : residentOscarMsgList ) {
                                MsgDemoMap msgDemoMap = new MsgDemoMap();
                                msgDemoMap.linkMsg2Demo(messageId, String.valueOf(res.getDemographic_no()));
                            }                                
                        }   
                    }
                }
            }
        }
        
        logger.info("Completed Sending OSCAR Review Messages");
    }
    
    
    
    
    @Override
    public void setLoggedInProvider(Provider provider) {
	this.provider = provider;
    }

    @Override
    public void setLoggedInSecurity(Security security) {
        this.security = security;
    }
}

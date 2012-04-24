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


package oscar.oscarMessenger.data;

import org.apache.log4j.Logger;
import org.oscarehr.util.DbConnectionFilter;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;

import oscar.comm.client.SendMessageClient;
import oscar.oscarDB.DBHandler;

      /**
       *
       * <p>Title: OscarComm, for sending Remote messages</p>
       * <p>Description: This is a class that extends Thread so that Tomcat doesn't stall when sending remote messages</p>
       * <p>Copyright: Copyright (c) 2002</p>
       * <p>Company: jay@jayweb.ca</p>
       * @author jay gallagher
       * @version 1.0
       */
public  class MsgRemoteMessageData extends Thread{
    private static Logger logger=MiscUtils.getLogger();

         String messageId;
         String XMLMessage;
         String currentLocation;

         String message;
         String subject;
         String sentby;
         String sentbyNo;
         String sentto;
         String thetime;
         String thedate;
         String theaction;
         String theAttach;

         public MsgRemoteMessageData (String messageId,String CurrLoco){
            super("SendRemoteMessage");
            this.messageId = new String(messageId);
            XMLMessage = new String();
            currentLocation = new String(CurrLoco);
         }


         String replaceIllegalCharacters(String str){
            return str.replaceAll("&", "&amp;").replaceAll(">","&gt;").replaceAll("<","&lt;");
         }



         /*************************************************************************
         * getXMLMessage
         * @param messageID
         * @return
         */
         String getXMLMessage(String messageID){
            StringBuilder XMLstring;
            message    = new String();
            subject    = new String();
            sentby     = new String();
            sentbyNo   = new String();
            sentto     = new String();
            thetime    = new String();
            thedate    = new String();
            theaction  = new String();
            theAttach  = new String();
            java.util.Vector<String> providerNo = new java.util.Vector<String>();
            java.util.Vector<String> locationID = new java.util.Vector<String>();



            XMLstring = new StringBuilder("<?xml version=\"1.0\" ?>\n <message>\n ");

            try{

               java.sql.ResultSet rs_message, rs_whotoo;
               String sql_message = new String("Select * from messagetbl where messageid = '"+messageID+"'");
               String sql_whotoo = new String("Select * from messagelisttbl where message = '"+messageID+"'");
               rs_message = DBHandler.GetSQL(sql_message);

               if (rs_message.next()){
                  message   = replaceIllegalCharacters(rs_message.getString("themessage"));
                  subject   = replaceIllegalCharacters(rs_message.getString("thesubject"));
                  sentby    = (rs_message.getString("sentby"));
                  sentbyNo  = (rs_message.getString("sentbyNo"));
                  sentto    = (rs_message.getString("sentto"));
                  thetime   = (rs_message.getString("theime"));
                  thedate   = (rs_message.getString("thedate"));
                  theAttach = (rs_message.getString("attachment"));
                  //theaction = (rs_message.getString("actionstatus"));
               }

               if (theAttach != null && !theAttach.equals("null")){
               int getit = theAttach.indexOf('\n');
                if (getit > 6){
                    theAttach = theAttach.substring(getit+1);
                }
               }


               rs_whotoo = DBHandler.GetSQL(sql_whotoo);

               while (rs_whotoo.next()){
                  providerNo.add(rs_whotoo.getString("provider_no"));
                  locationID.add(rs_whotoo.getString("remoteLocation"));
               }

               for ( int i = 0; i < providerNo.size(); i++){

                     String locoId = locationID.elementAt(i);
                  if ( locoId == null ){
                     locoId = currentLocation;
                  }

                  XMLstring.append("<recipient providerNo=\""+providerNo.elementAt(i)+"\" locationId=\""+locoId+"\" />\n ");

               }
               XMLstring.append("<msgDate>"+thedate+"</msgDate>\n");
               XMLstring.append("<msgTime>"+thetime+"</msgTime>\n");
               XMLstring.append("<sentby>"+sentby+"</sentby>\n");
               XMLstring.append("<sentto>"+sentto+"</sentto>\n");
               XMLstring.append("<sentbyNo>"+sentbyNo+"</sentbyNo>\n");
               XMLstring.append("<sentbyLocation>"+currentLocation+"</sentbyLocation>\n");
               XMLstring.append("<subject>"+subject+"</subject>\n");
               XMLstring.append("<msgBody>"+message+"</msgBody>\n");
   //            XMLstring.append("<attachment>"+????????????+"</attachment>\n");   //---------------------Not Sure What to do about the attach yet
               XMLstring.append("<attachment>"+theAttach+"</attachment>\n");
               XMLstring.append("<actionstatus>"+theaction+"</actionstatus>\n");
               XMLstring.append("</message>\n");



               java.io.File file = new java.io.File("/home/torenvn/mes.xml");
               try {
               java.io.FileWriter fileWriter = new java.io.FileWriter(file);
               fileWriter.write(XMLstring.toString());
               fileWriter.close();
               }
               catch(java.io.IOException io){ MiscUtils.getLogger().error("Error", io); }

               rs_message.close();
               rs_whotoo.close();
            }catch (java.sql.SQLException e){MiscUtils.getLogger().error("Error", e); }

            return XMLstring.toString();
         }



         public void run(){

     		LoggedInInfo.setLoggedInInfoToCurrentClassAndMethod();

    		try {
                XMLMessage = getXMLMessage(messageId);
                SendMessageClient sendMessageClient = new SendMessageClient();
                boolean how = false;
                try{

                   how = sendMessageClient.sendMessage("localhost:3306/","oscar_spc",XMLMessage);
                }catch(Exception e){
                    defunctMessage();
                    logger.error("I be messin up", e);
                }

                if (!how){
                    defunctMessage();
                }
            }
            finally {
    			LoggedInInfo.loggedInInfo.remove();
                DbConnectionFilter.releaseAllThreadDbResources();
            }

         }


        private void defunctMessage(){
        //sendMessage2(String message, String subject,String userName,String sentToWho,String userNo,java.util.ArrayList providers )
        java.util.ArrayList<MsgProviderData> aList = new java.util.ArrayList<MsgProviderData>();
        StringBuilder stringBuffer = new StringBuilder("This message could not be delivered to remote Providers. \nPlease try again\n\n");
        MsgProviderData providerData = new MsgProviderData();
        providerData.locationId = currentLocation;
        providerData.providerNo = sentbyNo;
        aList.add(providerData);
        stringBuffer.append(message);

        String st = null;
        MsgMessageData messageData = new MsgMessageData();
        messageData.sendMessage2(stringBuffer.toString(),"Transmission Failed :"+subject,"Mailer Daemon", sentby,"",aList,st, "");
        }
}

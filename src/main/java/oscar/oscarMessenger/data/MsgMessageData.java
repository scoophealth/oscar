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
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;

import org.oscarehr.common.dao.MessageListDao;
import org.oscarehr.common.dao.MessageTblDao;
import org.oscarehr.common.model.MessageList;
import org.oscarehr.common.model.MessageTbl;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import oscar.oscarDB.DBPreparedHandler;
import oscar.oscarMessenger.util.Msgxml;

public class MsgMessageData {

    boolean areRemotes = false;
    boolean areLocals = false;
    java.util.ArrayList<MsgProviderData> providerArrayList = null;
    String currentLocationId = null;

    private String messageSubject;
    private String messageDate;
    private String messageTime;
    
    MessageTblDao messageTblDao = SpringUtils.getBean(MessageTblDao.class);
    MessageListDao messageListDao = SpringUtils.getBean(MessageListDao.class);
     

    public MsgMessageData(){
    }

    public MsgMessageData(String msgID){
        try{
            DBPreparedHandler db = new DBPreparedHandler();
            String sql = "";
            //sql = "select tbl.thedate, tbl.thesubject from msgDemoMap map, messagetbl tbl where demographic_no ='"+ demographic_no
            //        + "' and tbl.messageid = map.messageID order by tbl.thedate";
            sql = "select thesubject, thedate, theime from messagetbl where messageid='"+msgID+"'";

            ResultSet rs = db.queryResults(sql);
            if(rs.next()){
                this.messageSubject = oscar.Misc.getString(rs, "thesubject");
                this.messageDate = oscar.Misc.getString(rs, "thedate");
                this.messageTime = oscar.Misc.getString(rs, "theime");
            }
        }
        catch (java.sql.SQLException e){
            MiscUtils.getLogger().debug("Message data not found");
        }
    }
    public String getCurrentLocationId(){
        if (currentLocationId == null){
            try{
              DBPreparedHandler db = new DBPreparedHandler();
              java.sql.ResultSet rs;
              rs = db.queryResults("select locationId from oscarcommlocations where current1 = '1'");

              if (rs.next()) {
                currentLocationId = oscar.Misc.getString(rs, "locationId");
              }
              rs.close();
            }catch (java.sql.SQLException e){MiscUtils.getLogger().error("Error", e); }
        }
        return currentLocationId;
    }

     /************************************************************************
       * getDups4
       * @param strarray is a String Array,
       * @return turns a String Array
       */
   public String[] getDups4(String[] strarray){

		java.util.Vector<String> vector = new java.util.Vector<String>();
		String temp = new String();


		java.util.Arrays.sort(strarray);
		temp ="";

		for (int i =0 ; i < strarray.length ; i++){
		   if (!strarray[i].equals(temp) ){
                      vector.add(strarray[i]);
			  temp = strarray[i];
		   }
		}
	        String[] retval = new String[vector.size()];
                for (int i =0; i < vector.size() ; i++ ){
                   retval[i] = vector.elementAt(i);
                }
   	     return retval;
	}

    ////////////////////////////////////////////////////////////////////////////
    public String createSentToString(String[] providers){

            String sql = "select first_name, last_name from provider where ";
            StringBuilder temp = new StringBuilder(sql);
            StringBuilder sentToWho = new StringBuilder();


            //create SQL statement with the provider numbers
            for (int i =0 ; i < providers.length ; i++){
              if (i == (providers.length -1)){
                 temp.append(" provider_no = "+providers[i]);
              }
              else{
                temp.append(" provider_no = "+providers[i]+" or ");
              }
            }

            sql = temp.toString();

            //Create A "sent to who String" I thought it would be better to create this line
            //once than have to look this information up in the database everytime the
            //message was viewed. The names are delimited with a '.'
            try
            {
              DBPreparedHandler db = new DBPreparedHandler();
              java.sql.ResultSet rs;

              rs = db.queryResults(sql);

              boolean first = true;
              while (rs.next()) {
                  if (!first) {
                      sentToWho.append(", ");
                  } else {
                      first = false;
                  }
                  sentToWho.append(" "+oscar.Misc.getString(rs, "first_name") +" " +oscar.Misc.getString(rs, "last_name"));
              }
              sentToWho.append(".");

        rs.close();

      }catch (java.sql.SQLException e){MiscUtils.getLogger().error("Error", e); }



    return sentToWho.toString();
    }
    //=-------------------------------------------------------------------------

    ////////////////////////////////////////////////////////////////////////////
     public String createSentToString(java.util.ArrayList<MsgProviderData> providerList){

            String sql = "select first_name, last_name from provider where ";
            StringBuilder temp = new StringBuilder(sql);
            StringBuilder sentToWho = new StringBuilder();


            //create SQL statement with the provider numbers
            for (int i =0 ; i < providerList.size(); i++){
              MsgProviderData proData = providerList.get(i);
              String proNo = proData.providerNo;
              if (i == (providerList.size() -1)){
                 temp.append(" provider_no = '"+proNo + "'");
              }
              else{
                temp.append(" provider_no = '"+proNo+"' or ");
              }
            }

            sql = temp.toString();

            //Create A "sent to who String" I thought it would be better to create this line
            //once than have to look this information up in the database everytime the
            //message was viewed. The names are delimited with a '.'
            try
            {
              DBPreparedHandler db = new DBPreparedHandler();
              java.sql.ResultSet rs;

              rs = db.queryResults(sql);

              boolean first = true;
              while (rs.next()) {
                  if (!first) {
                      sentToWho.append(", ");
                  } else {
                      first = false;
                  }
                  sentToWho.append(" "+oscar.Misc.getString(rs, "first_name") +" " +oscar.Misc.getString(rs, "last_name"));
              }
              sentToWho.append(".");

        rs.close();

      }catch (java.sql.SQLException e){MiscUtils.getLogger().error("Error", e); }



    return sentToWho.toString();
    }
    //=-------------------------------------------------------------------------

    ////////////////////////////////////////////////////////////////////////////
   
    public String sendMessage(String message, String subject,String userName,String sentToWho,String userNo,String[] providers ){
       String messageid=null;
       oscar.oscarMessenger.util.MsgStringQuote str = new oscar.oscarMessenger.util.MsgStringQuote();
      
        
          MessageTbl mt = new MessageTbl();
          mt.setDate(new Date());
          mt.setTime(new Date());
          mt.setMessage(messageid);
          mt.setSubject(subject);
          mt.setSentBy(userName);
          mt.setSentTo(sentToWho);
          mt.setSentByNo(userNo);
          
          messageTblDao.persist(mt);
          int msgid = mt.getId();
         
          
          messageid = String.valueOf(msgid);
          for (int i =0 ; i < providers.length ; i++){
        	  MessageList ml = new MessageList();
        	  ml.setMessage(Integer.parseInt(messageid));
        	  ml.setProviderNo(providers[i]);
        	  ml.setStatus("new");
        	  messageListDao.persist(ml);
        
          }

      
      return messageid;
    }//=------------------------------------------------------------------------

    
    public String sendMessageReview(String message, String subject,String userName,String sentToWho,String userNo,ArrayList<MsgProviderData> providers,String attach, String pdfAttach, Integer type, String typeLink ){
        oscar.oscarMessenger.util.MsgStringQuote str = new oscar.oscarMessenger.util.MsgStringQuote();
      
     
        if (attach != null){
            attach = str.q(attach);
        }

        if (pdfAttach != null){
            pdfAttach = str.q(pdfAttach);
        }

     sentToWho = org.apache.commons.lang.StringEscapeUtils.escapeSql(sentToWho);
     userName = org.apache.commons.lang.StringEscapeUtils.escapeSql(userName);
     
     MessageTbl mt = new MessageTbl();
     mt.setDate(new Date());
     mt.setTime(new Date());
     mt.setMessage(message);
     mt.setSubject(subject);
     mt.setSentBy(userName);
     mt.setSentTo(sentToWho);
     mt.setSentByNo(userNo);
     mt.setSentByLocation(Integer.parseInt(getCurrentLocationId()));
     mt.setAttachment(attach);
     mt.setType(type);
     mt.setType_link(typeLink);
     if(pdfAttach !=null){
    	 mt.setPdfAttachment(pdfAttach.getBytes());
     }
     messageTblDao.persist(mt);
     
     
     String messageid = String.valueOf(mt.getId());

   

     for (MsgProviderData providerData : providers){
    	 MessageList ml = new MessageList();
    	 ml.setMessage(Integer.parseInt(messageid));
    	 ml.setProviderNo(providerData.providerNo);
    	 ml.setStatus("new");
    	 ml.setRemoteLocation(Integer.parseInt(providerData.locationId));
    	 messageListDao.persist(ml);
     }

      
      return messageid;
    
    }
    ////////////////////////////////////////////////////////////////////////////
    public String sendMessage2(String message, String subject,String userName,String sentToWho,String userNo,ArrayList<MsgProviderData> providers,String attach, String pdfAttach, Integer type ){

      oscar.oscarMessenger.util.MsgStringQuote str = new oscar.oscarMessenger.util.MsgStringQuote();
      
     
        if (attach != null){
            attach = str.q(attach);
        }

        if (pdfAttach != null){
            pdfAttach = str.q(pdfAttach);
        }

     sentToWho = org.apache.commons.lang.StringEscapeUtils.escapeSql(sentToWho);
     userName = org.apache.commons.lang.StringEscapeUtils.escapeSql(userName);
     
     MessageTbl mt = new MessageTbl();
     mt.setDate(new Date());
     mt.setTime(new Date());
     mt.setMessage(message);
     mt.setSubject(subject);
     mt.setSentBy(userName);
     mt.setSentTo(sentToWho);
     mt.setSentByNo(userNo);
     mt.setSentByLocation(Integer.parseInt(getCurrentLocationId()));
     mt.setAttachment(attach);
     mt.setType(type);
     if(pdfAttach !=null){
    	 mt.setPdfAttachment(pdfAttach.getBytes());
     }
     messageTblDao.persist(mt);
     
     
     String messageid = String.valueOf(mt.getId());

   

     for (MsgProviderData providerData : providers){
    	 MessageList ml = new MessageList();
    	 ml.setMessage(Integer.parseInt(messageid));
    	 ml.setProviderNo(providerData.providerNo);
    	 ml.setStatus("new");
    	 ml.setRemoteLocation(Integer.parseInt(providerData.locationId));
    	 messageListDao.persist(ml);
     }

      
      return messageid;
    }



    ////////////////////////////////////////////////////////////////////////////
    //This function takes in an Array of provider Numbers
    //It parse through them and looks for an @
    //Providers wiht an @ are remote location providers
    //Its creates an arrayList of class provider
    public java.util.ArrayList<MsgProviderData> getProviderStructure(String[] providerArray){
          providerArrayList = new java.util.ArrayList<MsgProviderData>();

         for (int i =0 ; i < providerArray.length ; i++){
            MsgProviderData pD = new MsgProviderData();
            if (providerArray[i].indexOf('@') == -1){
                areLocals = true;
                pD.providerNo = providerArray[i];
                pD.locationId = getCurrentLocationId();
            }else{
                areRemotes = true;
                String[] theSplit = providerArray[i].split("@");
                pD.providerNo = theSplit[0];
                pD.locationId = theSplit[1];

            }
            providerArrayList.add(pD);
         }
         return providerArrayList;
    }//-=-----------------------------------------------------------------------

    ////////////////////////////////////////////////////////////////////////////
    public java.util.ArrayList<MsgProviderData> getRemoteProvidersStructure(){
        java.util.ArrayList<MsgProviderData> arrayList = new java.util.ArrayList<MsgProviderData>();
        if ( providerArrayList != null){
            for (int i = 0; i < providerArrayList.size(); i++){
                MsgProviderData providerData = providerArrayList.get(i);
                if (!providerData.locationId.equals(getCurrentLocationId())){
                    arrayList.add(providerData);
                }
            }
        }
        return arrayList;
    }//=------------------------------------------------------------------------

    ////////////////////////////////////////////////////////////////////////////
    public java.util.ArrayList<MsgProviderData> getLocalProvidersStructure(){
        java.util.ArrayList<MsgProviderData> arrayList = new java.util.ArrayList<MsgProviderData>();
        if ( providerArrayList != null){
            for (int i = 0; i < providerArrayList.size(); i++){
                MsgProviderData providerData = providerArrayList.get(i);

                if (providerData.locationId.equals(getCurrentLocationId())){
                    arrayList.add(providerData);
                }
            }
        }
        return arrayList;
    }//=------------------------------------------------------------------------

    ////////////////////////////////////////////////////////////////////////////
    //Tells whether there are remotes recievers of this message
    public boolean isRemotes(){return areRemotes;}//=---------------------------

    ////////////////////////////////////////////////////////////////////////////
    //Tells whether there are local recievers of this message
    public boolean isLocals(){return areLocals;}//=-----------------------------




  public String getRemoteNames(java.util.ArrayList<MsgProviderData> arrayList){

        String[] arrayOfLocations = new String[arrayList.size()];
        String[] sortedArrayOfLocations;
        MsgProviderData providerData ;
        for (int i = 0; i < arrayList.size();i++){
            providerData        = arrayList.get(i);
            arrayOfLocations[i] = providerData.locationId;
        }
        sortedArrayOfLocations  =  getDups4(arrayOfLocations);

        java.util.ArrayList <ArrayList<String> >vectOfSortedProvs = new java.util.ArrayList<ArrayList<String>>();

        for (int i = 0; i < sortedArrayOfLocations.length; i++){
            java.util.ArrayList<String> sortedProvs = new java.util.ArrayList<String>();
            for (int j = 0; j < arrayList.size(); j++){
                providerData = arrayList.get(j);
                if (providerData.locationId.equals( sortedArrayOfLocations[i] )){

                   sortedProvs.add(providerData.providerNo);
                }
            }
            vectOfSortedProvs.add(sortedProvs);
        }

    StringBuilder stringBuffer = new StringBuilder();
    for (int i=0; i < sortedArrayOfLocations.length; i++){
        //for each location get there address book and there locationDesc
        String theAddressBook = new String();
        String theLocationDesc = new String();
        try{
            DBPreparedHandler db = new DBPreparedHandler();
            java.sql.ResultSet rs;
            String sql = new String("select  locationDesc, addressBook from oscarcommlocations where locationId = "+(sortedArrayOfLocations[i])     );
            rs = db.queryResults(sql);
            if (rs.next()){
               theLocationDesc = oscar.Misc.getString(rs, "locationDesc");
               theAddressBook = oscar.Misc.getString(rs, "addressBook");
            }
            rs.close();
        }catch (java.sql.SQLException e){MiscUtils.getLogger().error("Error", e); }
        // get a node list of all the providers for that addressBook then search for ones i need

        Document xmlDoc = Msgxml.parseXML(theAddressBook);

        if ( xmlDoc != null  ){
        	ArrayList<String> sortedProvs = vectOfSortedProvs.get(i);

           stringBuffer.append("<br/><br/>Providers at "+theLocationDesc+" receiving this message: <br/> ");

           Element addressBook = xmlDoc.getDocumentElement();
           NodeList lst = addressBook.getElementsByTagName("address");

           for (int z=0; z < sortedProvs.size(); z++){

              String providerNo = sortedProvs.get(z);

              for (int j = 0; j < lst.getLength(); j++){
                 Node currNode = lst.item(j);
                 Element elly = (Element) currNode;


                 if (  providerNo.equals(  elly.getAttribute("id")  ) ){
                    j = lst.getLength();
                    stringBuffer.append(elly.getAttribute("desc")+". ");
                 }
              }

           }//for

           stringBuffer.append(" ) ");

        }//if

    }//for

        return stringBuffer.toString();
  }

  public String getSubject(String msgID){
      String subject=null;
      try{
            DBPreparedHandler db = new DBPreparedHandler();
            String sql = "";
            //sql = "select tbl.thedate, tbl.thesubject from msgDemoMap map, messagetbl tbl where demographic_no ='"+ demographic_no
            //        + "' and tbl.messageid = map.messageID order by tbl.thedate";
            sql = "select thesubject from messagetbl where messageid='"+msgID+"'";

            ResultSet rs = db.queryResults(sql);
            if(rs.next()){
                subject = oscar.Misc.getString(rs, "thesubject");
            }
        }
        catch (java.sql.SQLException e){
            subject="error: subject not found!";
        }
      return subject;
  }

  public String getSubject(){
      return this.messageSubject;
  }

  public String getDate(){
      return this.messageDate;
  }

  public String getTime(){
     return this.messageTime;
  }
}

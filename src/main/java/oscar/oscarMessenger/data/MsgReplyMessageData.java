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


public class MsgReplyMessageData {
    public java.util.ArrayList<MsgProviderData> localList = null;
    public java.util.ArrayList<MsgProviderData> remoList = null;
    String localId;


    public void estLists(){
        localList = new java.util.ArrayList<MsgProviderData>();
        remoList = new java.util.ArrayList<MsgProviderData>();

        oscar.oscarMessenger.data.MsgMessageData messageData = new oscar.oscarMessenger.data.MsgMessageData();
        localId = messageData.getCurrentLocationId();
    }

    public void add(String proId, String locoId){
        if (locoId.equals(localId)){
            MsgProviderData providerData = new MsgProviderData();
            providerData.providerNo = proId;
            providerData.locationId = locoId;
            localList.add(providerData);
        }else{
            MsgProviderData providerData = new MsgProviderData();
            providerData.providerNo = proId;
            providerData.locationId = locoId;
            remoList.add(providerData);
        }
    }

    public boolean remoContains(String proId,String locoId){
        boolean retval = false;
        if (remoList != null){
            for (int i = 0; i < remoList.size(); i++){
                MsgProviderData pD = remoList.get(i);
                if ( ( pD.providerNo.equals(proId) ) && ( pD.locationId.equals(locoId) ) ){
                    retval = true;
                    i = remoList.size();
                }
            }
        }
        return retval;
    }





}

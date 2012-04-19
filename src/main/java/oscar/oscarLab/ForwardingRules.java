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
 * ForwardingRules.java
 *
 * Created on July 16, 2007, 10:29 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package oscar.oscarLab;

import java.sql.ResultSet;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import oscar.oscarDB.DBHandler;

/**
 *
 * @author wrighd
 */
public class ForwardingRules {

    Logger logger = Logger.getLogger(ForwardingRules.class);

    /** Creates a new instance of ForwardingRules */
    public ForwardingRules() {
    }

    public ArrayList<ArrayList<String>> getProviders(String providerNo){
        ArrayList<ArrayList<String>> ret = new ArrayList<ArrayList<String>>();
        String sql = "SELECT p.provider_no, p.first_name, p.last_name FROM incomingLabRules i, provider p WHERE i.archive='0' AND i.provider_no='"+providerNo+"' AND p.provider_no=i.frwdProvider_no";
        try{

            ResultSet rs = DBHandler.GetSQL(sql);
            while (rs.next()){
                ArrayList<String> info = new ArrayList<String>();
                info.add(oscar.Misc.getString(rs, "provider_no"));
                info.add(oscar.Misc.getString(rs, "first_name"));
                info.add(oscar.Misc.getString(rs, "last_name"));
                ret.add(info);
            }
        }catch(Exception e){
            logger.error("Could not retrieve forwarding rules", e);
        }
        return ret;
    }

    public String getStatus(String providerNo){
        String ret = "N";
        String sql = "SELECT status FROM incomingLabRules WHERE archive='0' AND provider_no='"+providerNo+"'";
        try{

            ResultSet rs = DBHandler.GetSQL(sql);
            if (rs.next()){
                ret = oscar.Misc.getString(rs, "status");
            }
        }catch(Exception e){
            logger.error("Could not retrieve forwarding rules", e);
        }
        return ret;
    }

    public boolean isSet(String providerNo){
        boolean ret = false;
        String sql = "SELECT status FROM incomingLabRules WHERE archive='0' AND provider_no='"+providerNo+"'";
        try{

            ResultSet rs = DBHandler.GetSQL(sql);
            if (rs.next()){
                ret = true;
            }
        }catch(Exception e){
            logger.error("Could not retrieve forwarding rules", e);
        }
        return ret;
    }

}

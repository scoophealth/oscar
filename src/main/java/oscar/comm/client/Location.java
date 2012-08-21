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


package oscar.comm.client;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import oscar.oscarDB.DBHandler;
import oscar.util.UtilXML;

@Deprecated
class Location {

    public Element getLocal(Document doc) throws SQLException {
        Element local = doc.createElement("local");

        ResultSet rs = DBHandler.GetSQL("SELECT * FROM oscarcommlocations WHERE current1 = 1");
        if(rs.next()) {
            UtilXML.addNode(local, "locationId", String.valueOf(rs.getInt("locationId")));
            UtilXML.addNode(local, "locationDesc", oscar.Misc.getString(rs, "locationDesc"));
            UtilXML.addNode(local, "locationAuth", oscar.Misc.getString(rs, "locationAuth"));
        }
        rs.close();

        return local;
    }

    public Element getRemotes(Document doc) throws SQLException {
        Element remoteLocations = doc.createElement("recipients");

        ResultSet rs = DBHandler.GetSQL("SELECT * FROM oscarcommlocations WHERE current1 = 0");
        while(rs.next()) {
            UtilXML.addNode(remoteLocations, "remote").setAttribute("locationId", String.valueOf(rs.getInt("locationId")));
        }
        rs.close();

        return remoteLocations;
    }
}

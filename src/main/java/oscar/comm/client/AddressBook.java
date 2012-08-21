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
import org.w3c.dom.NodeList;

import oscar.oscarDB.DBHandler;
import oscar.util.UtilXML;

@Deprecated
class AddressBook {

    public Element getLocalAddressBook(Document doc) throws SQLException {
        Element root = doc.createElement("localAddressBook");

        Element addressBook = doc.createElement("addressBook");
        addressBook.appendChild(this.getChildren(doc, 0, ""));

        ResultSet rs = DBHandler.GetSQL("SELECT addressBook FROM oscarcommlocations WHERE current1 = 1");
        if(rs.next()) {
            String newAddressBook = UtilXML.toXML(addressBook);

            if((oscar.Misc.getString(rs, "addressBook")==null) || (oscar.Misc.getString(rs, "addressBook").equals(newAddressBook)==false)) {
            	DBHandler.RunSQL("UPDATE oscarcommlocations SET addressBook = '" + newAddressBook + "' WHERE current1 = 1");
            } else {
                addressBook = null;
            }
        }
        rs.close();

        if(addressBook!=null) {
            root.setAttribute("updated", "true");
            root.appendChild(new Location().getRemotes(doc));
            root.appendChild(addressBook);
        } else {
            root.setAttribute("updated", "false");
        }

        return root;
    }

    private Element getChildren(Document doc, int groupId, String desc) throws SQLException {
        Element group = doc.createElement("group");
        if(desc.length()>0) {
            group.setAttribute("id", String.valueOf(groupId));
            group.setAttribute("desc", desc);
        }

        String sql = "SELECT * FROM groups_tbl WHERE parentID = " + groupId;
        ResultSet rs = DBHandler.GetSQL(sql);
        while(rs.next()) {
            group.appendChild(getChildren(doc, rs.getInt("groupID"), oscar.Misc.getString(rs, "groupDesc")));
        }
        rs.close();

        sql = "SELECT p.provider_no, p.last_name, p.first_name "
            + "FROM groupMembers_tbl g INNER JOIN provider p ON g.provider_No = p.provider_no "
            + "WHERE groupID = " + groupId + " ORDER BY p.last_name, p.first_name";
        rs = DBHandler.GetSQL(sql);
        while(rs.next()) {
            Element address = UtilXML.addNode(group, "address");
            address.setAttribute("id", oscar.Misc.getString(rs, "provider_no"));
            address.setAttribute("desc", new String(oscar.Misc.getString(rs, "last_name") + ", " + oscar.Misc.getString(rs, "first_name")));
        }
        rs.close();

        return group;
    }

    public void setRemoteAddressBooks(Element remoteAddressBooks) throws SQLException {
        NodeList locations = remoteAddressBooks.getChildNodes();

        for(int i=0; i<locations.getLength(); i++) {
            Element location = (Element)locations.item(i);

            String locationId = location.getAttribute("locationId");
            String locationDesc = location.getAttribute("locationDesc");
            String addressBook = UtilXML.toXML(location.getElementsByTagName("addressBook").item(0));

            String sql = "SELECT 1 FROM oscarcommlocations WHERE locationId = " + locationId;
            ResultSet rs = DBHandler.GetSQL(sql);
            if(rs.next()) {
                sql = "UPDATE oscarcommlocations SET locationDesc = '"
                    + locationDesc + "', addressBook = '" + addressBook
                    + "' WHERE locationId = " + locationId;
            } else {
                sql = "INSERT INTO oscarcommlocations (locationId, locationDesc, addressBook) "
                + "VALUES (" + locationId + ", '" + locationDesc + "', '" + addressBook + "')";
            }
            rs.close();

            DBHandler.RunSQL(sql);
        }
    }
}

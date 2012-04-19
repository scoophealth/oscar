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
import java.sql.SQLException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import oscar.oscarDB.DBHandler;
import oscar.oscarMessenger.docxfer.util.MsgCommxml;
import oscar.oscarMessenger.docxfer.util.MsgUtil;

// This is a modified version of oscar.comm.client.AddressBook
public class MsgAddressBookMaker
{
    // Update the local address book and return true if changed
    public boolean updateAddressBook() throws SQLException
    {
        Document doc = MsgCommxml.newDocument();

        Element addressBook = doc.createElement("addressBook");

        addressBook.appendChild(this.getChildren(doc, 0, ""));

        ResultSet rs = DBHandler.GetSQL("SELECT addressBook FROM oscarcommlocations WHERE current1 = 1");
        if(rs.next())
        {
            String newAddressBook = MsgCommxml.toXML(addressBook);

            //if((oscar.Misc.getString(rs,"addressBook")==null) /*|| (oscar.Misc.getString(rs,"addressBook").equals(newAddressBook)==false)*/)
            //{
                DBHandler.RunSQL("UPDATE oscarcommlocations SET addressBook = '" + MsgUtil.replaceQuote(newAddressBook) + "' WHERE current1 = 1");
            //}
            //else
            //{
            //    addressBook = null;
            //}
        }
        rs.close();

        return (addressBook != null);
    }

    // Recursive function to get the children in a group
    private Element getChildren(Document doc, int groupId, String desc) throws SQLException
    {
        Element group = doc.createElement("group");
        if(desc.length()>0)
        {
            group.setAttribute("id", String.valueOf(groupId));
            group.setAttribute("desc", desc);
        }

        String sql = "SELECT * FROM groups_tbl WHERE parentID = " + groupId;

        ResultSet rs = DBHandler.GetSQL(sql);

        while(rs.next())
        {
            Element subGrp = getChildren(doc, rs.getInt("groupID"), oscar.Misc.getString(rs, "groupDesc"));

            if(subGrp.hasChildNodes())
            {
                group.appendChild(subGrp);
            }
        }
        rs.close();

        sql = "SELECT p.provider_no, p.last_name, p.first_name "
            + "FROM groupMembers_tbl g INNER JOIN provider p ON g.provider_No = p.provider_no "
            + "WHERE groupID = " + groupId + " ORDER BY p.last_name, p.first_name";

        rs = DBHandler.GetSQL(sql);

        while(rs.next())
        {
            Element address = MsgCommxml.addNode(group, "address");
            address.setAttribute("id", oscar.Misc.getString(rs, "provider_no"));
            address.setAttribute("desc", new String(oscar.Misc.getString(rs, "last_name") + ", " + oscar.Misc.getString(rs, "first_name")));
        }
        rs.close();

        return group;
    }

}

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


import java.util.List;

import org.oscarehr.common.dao.GroupMembersDao;
import org.oscarehr.common.dao.GroupsDao;
import org.oscarehr.common.dao.OscarCommLocationsDao;
import org.oscarehr.common.model.Groups;
import org.oscarehr.common.model.OscarCommLocations;
import org.oscarehr.common.model.Provider;
import org.oscarehr.util.SpringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import oscar.oscarMessenger.docxfer.util.MsgCommxml;

// This is a modified version of oscar.comm.client.AddressBook
public class MsgAddressBookMaker
{
	private OscarCommLocationsDao oscarCommLocationsDao = SpringUtils.getBean(OscarCommLocationsDao.class);

	
    // Update the local address book and return true if changed
    public boolean updateAddressBook() {
        Document doc = MsgCommxml.newDocument();

        Element addressBook = doc.createElement("addressBook");

        addressBook.appendChild(this.getChildren(doc, 0, ""));

        if(!oscarCommLocationsDao.findByCurrent1(1).isEmpty())
        {
            String newAddressBook = MsgCommxml.toXML(addressBook);
            
            List<OscarCommLocations> ls = oscarCommLocationsDao.findByCurrent1(1);
            for(OscarCommLocations l:ls) {
            	l.setAddressBook(newAddressBook);
            	oscarCommLocationsDao.merge(l);
            }
       
        }

        return (addressBook != null);
    }

    // Recursive function to get the children in a group
    private Element getChildren(Document doc, int groupId, String desc) {
        Element group = doc.createElement("group");
        if(desc.length()>0)
        {
            group.setAttribute("id", String.valueOf(groupId));
            group.setAttribute("desc", desc);
        }

        GroupsDao dao = SpringUtils.getBean(GroupsDao.class);
        for(Groups g : dao.findByParentId(groupId))
        {
            Element subGrp = getChildren(doc, g.getId(), g.getGroupDesc());

            if(subGrp.hasChildNodes())
            {
                group.appendChild(subGrp);
            }
        }
        
        GroupMembersDao gDao = SpringUtils.getBean(GroupMembersDao.class);
        for(Object[] g : gDao.findMembersByGroupId(groupId)) {
        	Provider p = (Provider) g[1]; 
        	
            Element address = MsgCommxml.addNode(group, "address");
            address.setAttribute("id", p.getProviderNo());
            address.setAttribute("desc", p.getFormattedName());
        }
        return group;
    }

}

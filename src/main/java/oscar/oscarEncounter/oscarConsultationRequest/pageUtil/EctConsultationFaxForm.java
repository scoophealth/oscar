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


package oscar.oscarEncounter.oscarConsultationRequest.pageUtil;

import org.apache.struts.action.ActionForm;
import org.oscarehr.util.MiscUtils;

public final class EctConsultationFaxForm extends ActionForm {

    public String getRecipient()
    {
        if(recipient == null)
            recipient = new String();
        return recipient;
    }

    public void setRecipient(String str)
    {
        MiscUtils.getLogger().debug("recipient has been set");
        recipient = str;
    }

    public String getFrom()
    {
        if(from == null)
            from = new String();
        return from;
    }

    public void setFrom(String str)
    {
        MiscUtils.getLogger().debug("from has been set");
        from = str;
    }

    public String getRecipientsFaxNumber()
    {
        if(recipientsFaxNumber == null)
            recipientsFaxNumber = new String();
        return recipientsFaxNumber;
    }

    public void setRecipientsFaxNumber(String str)
    {
        MiscUtils.getLogger().debug("recipientsFaxNumber setter");
        recipientsFaxNumber = str;
    }

    public String getSendersPhone()
    {
        if(sendersPhone == null)
            sendersPhone = new String();
        return sendersPhone;
    }

    public void setSendersPhone(String str)
    {
        MiscUtils.getLogger().debug(" setter");
        sendersPhone = str;
    }

    public String getSendersFax()
    {
        if(sendersFax == null)
            sendersFax = new String();
        return sendersFax;
    }

    public void setSendersFax(String str)
    {
        MiscUtils.getLogger().debug("sendersFax setter");
        sendersFax = str;
    }

    public String getComments()
    {
        if(comments == null)
            comments = new String();
        return comments;
    }

    public void setComments(String str)
    {
        MiscUtils.getLogger().debug("appointmentDay setter");
        comments = str;
    }

    public String getRequestId()
    {
        if(requestId == null)
            requestId = new String();
        return requestId;
    }

    public void setRequestId(String str)
    {
        MiscUtils.getLogger().debug("requestId setter");
        requestId = str;
    }
    String recipient;
    String from;
    String recipientsFaxNumber;
    String sendersPhone;
    String sendersFax;
    String comments;
    String requestId;
}

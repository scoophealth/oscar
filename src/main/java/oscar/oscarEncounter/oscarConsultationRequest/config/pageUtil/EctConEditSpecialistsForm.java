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


package oscar.oscarEncounter.oscarConsultationRequest.config.pageUtil;

import org.apache.struts.action.ActionForm;
import org.oscarehr.util.MiscUtils;

public final class EctConEditSpecialistsForm extends ActionForm
{

    public String getSpecId()
    {
        MiscUtils.getLogger().debug("getter Specid");
        if(specId == null)
            specId = new String();
        return specId;
    }

    public void setSpecId(String str)
    {
        MiscUtils.getLogger().debug("setter specId");
        specId = str;
    }

    public String getDelete()
    {
        MiscUtils.getLogger().debug("getter delete");
        if(delete == null)
            delete = new String();
        return delete;
    }

    public void setDelete(String str)
    {
        MiscUtils.getLogger().debug("setter delete");
        delete = str;
    }

    public String[] getSpecialists()
    {
        MiscUtils.getLogger().debug("getter specialists");
        if(specialists == null)
            specialists = new String[0];
        return specialists;
    }

    public void setSpecialists(String str[])
    {
        MiscUtils.getLogger().debug("setter specialists");
        specialists = str;
    }

    String specId;
    String delete;
    String specialists[];
}

/*
 * 
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
 * This software is published under the GPL GNU General Public License. 
 * This program is free software; you can redistribute it and/or 
 * modify it under the terms of the GNU General Public License 
 * as published by the Free Software Foundation; either version 2 
 * of the License, or (at your option) any later version. * 
 * This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
 * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
 * along with this program; if not, write to the Free Software 
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
 * 
 * <OSCAR TEAM>
 * 
 * This software was written for the 
 * Department of Family Medicine 
 * McMaster Unviersity 
 * Hamilton 
 * Ontario, Canada 
 */
package oscar.oscarEncounter.immunization.config.pageUtil;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.*;

public final class EctImmCreateImmunizationSetInitForm extends ActionForm
{

    public String getSetName()
    {
        if(setName == null)
            setName = new String();
        return setName;
    }

    public void setSetName(String str)
    {
        setName = str;
    }

    public String getNumRows()
    {
        if(numRows == null)
            numRows = new String();
        return numRows;
    }

    public void setNumRows(String str)
    {
        numRows = str;
    }

    public String getNumCols()
    {
        if(numCols == null)
            numCols = new String();
        return numCols;
    }

    public void setNumCols(String str)
    {
        numCols = str;
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
    {
        ActionErrors errors = new ActionErrors();
        if(setName == null || setName.length() == 0)
            errors.add("setName", new ActionError("Error.setName.missing"));
        if(numRows == null || numRows.length() == 0)
            errors.add("numRows", new ActionError("Error.numRows.missing"));
        if(numCols == null || numCols.length() == 0)
            errors.add("numCols", new ActionError("Error.numCols.missing"));
        if(errors.empty())
        {
            try
            {
                int cols = Integer.parseInt(numCols);
                if(cols <= 0)
                    errors.add("numCols2", new ActionError("Error.numCols.below.zero"));
            }
            catch(Exception e)
            {
                errors.add("numCols3", new ActionError("Error.numCols.non.numeric"));
            }
            try
            {
                int rows = Integer.parseInt(numRows);
                if(rows <= 0)
                    errors.add("numRows2", new ActionError("Error.numRows.below.zero"));
            }
            catch(Exception e)
            {
                errors.add("numRows3", new ActionError("Error.numRows.non.numeric"));
            }
        }
        return errors;
    }

    String setName;
    String numRows;
    String numCols;
}

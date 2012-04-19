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


package oscar.oscarEncounter.immunization.config.data;

import java.util.Arrays;
import java.util.Vector;

import org.oscarehr.util.MiscUtils;

public class EctImmImmunizations
{

    public void sortAgeVector()
    {
        int tempIntArray[] = new int[indexAge.size()];
        Vector newVector = new Vector();
        newVector.setSize(indexAge.size());
        for(int r = 0; r < indexAge.size(); r++)
        {
            String str = (String)indexAge.elementAt(r);
            int intVal = 0;
            try
            {
                intVal = Integer.parseInt(str);
            }
            catch(Exception ee)
            {
                MiscUtils.getLogger().debug("kick'em out");
            }
            tempIntArray[r] = intVal;
        }

        Arrays.sort(tempIntArray);
        for(int r = 0; r < tempIntArray.length; r++)
        {
            MiscUtils.getLogger().debug(String.valueOf(String.valueOf((new StringBuilder("line ")).append(r).append(" value ").append(tempIntArray[r]))));
            indexAge.setElementAt(Integer.toString(tempIntArray[r]), r);
        }

    }

    public String immunizationName;
    public int col;
    public int row;
    public Vector indexAge;
}

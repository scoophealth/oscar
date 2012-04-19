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


package oscar.form.study;

import org.oscarehr.util.MiscUtils;

public class FrmStudyRecordFactory {
    public FrmStudyRecord factory (String which) {
        String fullName = "oscar.form.study.FrmStudy" + which + "Record"; // get reference to the class
        FrmStudyRecord myClass = null;

        try {
            Class classDefinition = Class.forName( fullName );
            myClass = (FrmStudyRecord) classDefinition.newInstance();
        } catch (InstantiationException e) {
            MiscUtils.getLogger().debug("debug", e);
        } catch (IllegalAccessException e) {
            MiscUtils.getLogger().debug("debug", e);
        } catch (ClassNotFoundException e) {
            MiscUtils.getLogger().debug("debug", e);
        }

        return myClass;
/*
//String paramString1 = "one";
//String paramString2 = "two";
//Class string = Class.forName("java.lang.String");            // get reference to constructor taking those param types
//Class[] paramTypes = { string, string };
//        Constructor constr = c.getConstructor(); //paramTypes);            // use this constructor get an instance
//        Object[] params = { paramString1, paramString2 };
        if (which.equalsIgnoreCase("diabete2")) { //keyword - study_name
            return new FrmStudydiabete2Record();
        } else {
            throw new IOException();
        }
*/
    }
}

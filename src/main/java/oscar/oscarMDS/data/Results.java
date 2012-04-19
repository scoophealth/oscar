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


// Decompiled by DJ v3.5.5.77 Copyright 2003 Atanas Neshkov  Date: 05/12/2003 11:02:22 AM
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   Results.java

package oscar.oscarMDS.data;

import java.util.ArrayList;

public class Results
{

    Results(String n, String rR, String u, String oV, String aF, String oI, String rS, ArrayList newnotes, String lID)
    {
        name = n;
        referenceRange = rR;
        units = u;
        observationValue = oV;
        abnormalFlags = aF;
        observationIden = oI;
        resultStatus = resultInterpret(rS);
        notes = newnotes;
        labID = lID;
    }

    public String name;
    public String units;
    public String referenceRange;
    public String observationValue;
    public String abnormalFlags;
    public String observationIden;
    public String resultStatus;
    public ArrayList notes;    
    public String labID;
    
    private String resultInterpret(String rS)
    {
        switch (rS.toUpperCase().charAt(0)) {
            case 'C' : return "Corrected";
            case 'D' : return "Deleted";
            case 'F' : return "Final";
            case 'I' : return "Pending";
            case 'P' : return "Preliminary";
            case 'R' : return "Unverified";
            case 'S' : return "Partial";
            case 'X' : return "DNR";
            case 'U' : return "Final";
            case 'W' : return "Deleted";
            default  : return "Invalid";
        }        
    }
    
    
    //notes ArrayList should only contain String objects.
    public String getLabNotes(int i){
        String ret = "";
        if (notes != null && notes.size() > i){
            ret = (String) notes.get(i);
        }
        return ret;
    }
}

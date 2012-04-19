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


// Decompiled by DJ v3.5.5.77 Copyright 2003 Atanas Neshkov  Date: 05/12/2003 11:01:28 AM
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3)
// Source File Name:   Headers.java

package oscar.oscarMDS.data;

import java.util.ArrayList;

public class Headers
{

    Headers(String rF, String rS, String[] rH)
    {
        groupedReportsArray = new ArrayList<GroupedReports>();
        reportFlag = rF;
        reportSequence = rS;
        reportHeading = rH;
    }

    public String reportFlag;
    public String reportSequence;
    public String[] reportHeading;
    public ArrayList<GroupedReports> groupedReportsArray;
}

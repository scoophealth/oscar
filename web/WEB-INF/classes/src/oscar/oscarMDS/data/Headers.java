// Decompiled by DJ v3.5.5.77 Copyright 2003 Atanas Neshkov  Date: 05/12/2003 11:01:28 AM
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   Headers.java

package oscar.oscarMDS.data;

import java.util.ArrayList;

public class Headers
{

    Headers(String rF, String rS)
    {
        groupedReportsArray = new ArrayList();
        reportFlag = rF;
        reportSequence = rS;
    }

    public String reportFlag;
    public String reportSequence;
    public ArrayList groupedReportsArray;
}
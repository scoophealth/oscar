// Decompiled by DJ v3.5.5.77 Copyright 2003 Atanas Neshkov  Date: 05/12/2003 11:02:22 AM
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   Results.java

package oscar.oscarMDS.data;

import java.util.ArrayList;

public class Results
{

    Results(String n, String rR, String u, String oV, String aF, String oI, ArrayList newnotes, String lID)
    {
        name = n;
        referenceRange = rR;
        units = u;
        observationValue = oV;
        abnormalFlags = aF;
        observationIden = oI;
        notes = newnotes;
        labID = lID;
    }

    public String name;
    public String units;
    public String referenceRange;
    public String observationValue;
    public String abnormalFlags;
    public String observationIden;
    public ArrayList notes;
    public String labID;
}
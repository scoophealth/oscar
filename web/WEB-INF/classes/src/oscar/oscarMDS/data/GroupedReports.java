// Decompiled by DJ v3.5.5.77 Copyright 2003 Atanas Neshkov  Date: 05/12/2003 11:02:37 AM
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   GroupedReports.java

package oscar.oscarMDS.data;

import java.util.*;
import java.text.SimpleDateFormat;

public class GroupedReports
{

    GroupedReports(String oBR, String hL7TimeStamp)
    {
        resultsArray = new ArrayList();
        associatedOBR = oBR;
        GregorianCalendar cal = new GregorianCalendar(Locale.ENGLISH);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yy HH:mm", Locale.ENGLISH);
        
        // boneheaded calendar numbers months from 0
        cal.set(Integer.parseInt(hL7TimeStamp.substring(0,4)), Integer.parseInt(hL7TimeStamp.substring(4,6))-1, Integer.parseInt(hL7TimeStamp.substring(6,8)),
                Integer.parseInt(hL7TimeStamp.substring(8,10)), Integer.parseInt(hL7TimeStamp.substring(10,12)), Integer.parseInt(hL7TimeStamp.substring(12,14)));
       
        timeStamp = dateFormat.format(cal.getTime());
    }

    public String associatedOBR;
    public ArrayList resultsArray;
    public String timeStamp;
}
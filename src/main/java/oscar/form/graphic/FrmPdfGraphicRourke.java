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


package oscar.form.graphic;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Properties;

import org.oscarehr.util.MiscUtils;

import oscar.util.UtilDateUtilities;

/**
 *
 * Generates x,y co-ordinates for Rourke 2006 Growth Chart
 */
public class FrmPdfGraphicRourke extends FrmPdfGraphic {
    
    private final static String DATEFORMAT = new String("dd/MM/yyyy");    
    
    int xDateScale;
    int nMaxPixX;
    int nMaxPixY;
    float fStartX;
    float fEndX;
    float fStartY;
    float fEndY;
    float deltaX;
    float deltaY;
    String dateFormat_;
    GregorianCalendar startDate;
    Properties xyProp;
    
    public void init( Properties prop ) {
    			xDateScale = Integer.parseInt(prop.getProperty("__xDateScale","-1"));
    	
                String str = prop.getProperty("__nMaxPixX");
                nMaxPixX = toInt(str);
                
                str = prop.getProperty("__nMaxPixY");
                nMaxPixY = toInt(str);
		
		str = prop.getProperty("__fStartX");
                fStartX = toFloat(str);
		
                str = prop.getProperty("__fEndX");
                fEndX = toFloat(str);
                
                float range = fEndX - fStartX;
                if( (int)range == 0 || range < 0 )
                    range = 1;
                
                deltaX = nMaxPixX / range;
                MiscUtils.getLogger().debug("deltaX " + deltaX);
                
                str = prop.getProperty("__fStartY");
                fStartY = toFloat(str);
                
		str = prop.getProperty("__fEndY");
                fEndY = toFloat(str);
                
                range = fEndY - fStartY;
                if( (int)range == 0 || range < 0 )
                    range = 1;
                
                deltaY = nMaxPixY / range;
                MiscUtils.getLogger().debug("deltaY " + deltaY);
                
                dateFormat_ = prop.getProperty("__dateFormat");
                
                //use __finalEDB as place holder for start date
                str = prop.getProperty("__finalEDB");
                str = makeDateStr(str);  
                MiscUtils.getLogger().debug("Setting start date " + str);
                startDate = createCalendar(str);
    }        
    
    private GregorianCalendar createCalendar( String strDate ) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATEFORMAT);
        Date date;
        try {
            date = dateFormat.parse(strDate);
        }
        catch( ParseException ex ) {
            MiscUtils.getLogger().debug("FrmPdfGraphicRourke: Error creating calendar " + ex.getMessage());
            date = new Date();
        }
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        return cal;
    }
    
    public Properties getGraphicXYProp(List xDate, List yHeight) {
        xyProp = new Properties();
        String x,y;
	for (int i = 0; i < xDate.size(); i++) {
            x = (String) xDate.get(i);
            y = (String) yHeight.get(i);
            if ( x != null &&  y != null && !x.equals("") && !y.equals(""))
                getGraphicXYProp( x, y );
			
	}

	return xyProp;
    }
    
    private void getGraphicXYProp(String xDate, String yHeight) {
        float xcoord,ycoord,smonth,emonth;
        float sday,eday;
        GregorianCalendar curDate;
        xDate = makeDateStr(xDate);
        
        MiscUtils.getLogger().debug("xDate: " + xDate);
        MiscUtils.getLogger().debug("yHeight = " + yHeight );
        if( (ycoord = toFloat(yHeight)) > -1 ) {
            MiscUtils.getLogger().debug("ycoord = " + ycoord );
            //calc diff between start date and current date
            curDate = createCalendar(xDate);                        
            
            //what months are we dealing with?
            smonth = startDate.get(Calendar.MONTH);
            emonth = curDate.get(Calendar.MONTH);
            
            //what fraction of each month do we have?
            sday = startDate.get(Calendar.DAY_OF_MONTH);
            eday = curDate.get(Calendar.DAY_OF_MONTH);
            
            MiscUtils.getLogger().debug("sday, eday " + sday + ", " + eday);
            
            smonth += (sday / startDate.getActualMaximum(Calendar.DAY_OF_MONTH));
            emonth += (eday / curDate.getActualMaximum(Calendar.DAY_OF_MONTH));                        
            
            //don't forget to add years
            switch (xDateScale) {
            case Calendar.YEAR:
            	smonth = (smonth/12.0f) + startDate.get(Calendar.YEAR);
                emonth = (emonth/12.0f) + curDate.get(Calendar.YEAR);
                break;
                
            case Calendar.MONTH:
            	smonth += startDate.get(Calendar.YEAR) * 12.0;
                emonth += curDate.get(Calendar.YEAR) * 12.0;
                break;
                
            default:
            	break;
            }
            
            if ( smonth > emonth ) {
                MiscUtils.getLogger().debug("FrmPdfGraphicRourke: Start date after xDate");
                return;
            }                
            if( fStartY > ycoord ) {
                MiscUtils.getLogger().debug("FrmPdfGraphicRourke: Ycoord less than starting Y value");
                return;
            }
                
            MiscUtils.getLogger().debug("emonth,  smonth " + emonth + ", " + smonth);
            
            //calc xcoord and ycoord
            xcoord = deltaX * (emonth - smonth);            
            ycoord = deltaY * (ycoord - fStartY);
            
            MiscUtils.getLogger().debug("Graphic x y: " + xcoord + ", " + ycoord );
            
            if( xyProp.containsKey(String.valueOf(xcoord)) ) {
            	StringBuilder yvalue = new StringBuilder(xyProp.getProperty(String.valueOf(xcoord)));
            	yvalue = yvalue.append(",");
            	yvalue = yvalue.append(String.valueOf(ycoord));
            	xyProp.setProperty(String.valueOf(xcoord), yvalue.toString());
            }
            else {
            	xyProp.setProperty(String.valueOf(xcoord), String.valueOf(ycoord));
            }
        }
    }
    
    private String makeDateStr( String str ) {                        
        
        str = UtilDateUtilities.DateToString(UtilDateUtilities.StringToDate(str, dateFormat_), DATEFORMAT);                        
        if( str.equals("") )            
            MiscUtils.getLogger().debug("FrmPdfGraphicRourke: bad date format " + str);            
        
        return str;
    } 
    
    private float toFloat(String str) {
        float num = 0f;
        
        try {
            num = Float.parseFloat(str);
        }
        catch( NumberFormatException ex) {
            num = -1f;
            MiscUtils.getLogger().debug("FrmPdfGraphicRourke class: error parsing float " + ex.getMessage() );
        }
        
        return num;
        
    }
    
    private int toInt(String str) {        
        int num = 0;
        
        try {
            num = Integer.parseInt(str);
        }
        catch( NumberFormatException ex) {
            num = 0;
            MiscUtils.getLogger().debug("FrmPdfGraphicRourke class: error parsing integer " + ex.getMessage());
        }
        
        return num;
    }
}

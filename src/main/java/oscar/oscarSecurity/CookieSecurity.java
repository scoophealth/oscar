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


package oscar.oscarSecurity;

import java.util.Random;
import java.util.zip.Adler32;

import javax.servlet.http.Cookie;

public class CookieSecurity
{
    public static String providerCookie = "oscprvid";
    
    public Cookie GiveMeACookie(String cookieName) {
        Random rndGenerator = new Random();
        Adler32 adler32 = new Adler32();
        String cookieVal = "";        
        for (int i = 0; i < 32; i++) {
            int j = rndGenerator.nextInt(10);                       
            cookieVal = cookieVal.concat(Integer.toString(j));            
            adler32.update(j);            
        }

        cookieVal = cookieVal.concat(Long.toString(adler32.getValue()));

        Cookie cookie = new Cookie(cookieName, cookieVal);
        cookie.setPath("/");
        return cookie;
    }
    
    public boolean FindThisCookie(Cookie[] cookies, String cookieName) {        
        if ( cookies != null )
        {
            for (int i=0; i<cookies.length; i++)
            {         
                if (cookieName.equals(cookies[i].getName()))
                {                    
                    return CheckCookieValue(cookies[i].getValue());
                }
            }
        }
        return false;
    }
    
    private boolean CheckCookieValue(String cookieValue) {
        try {
            Adler32 adler32 = new Adler32();        
            //adler32.update(cookieValue.substring(0, 32).getBytes());
            for (int i=0; i < 32; i++) {
                adler32.update(Integer.parseInt(cookieValue.substring(i, i+1)));                
            }        
            if (Long.parseLong(cookieValue.substring(32, cookieValue.length())) == adler32.getValue()) {            
                return true;
            } else {            
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }        
    
}

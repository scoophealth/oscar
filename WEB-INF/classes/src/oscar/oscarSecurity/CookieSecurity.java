// -----------------------------------------------------------------------------------------------------------------------
// *
// *
// * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
// * This software is published under the GPL GNU General Public License. 
// * This program is free software; you can redistribute it and/or 
// * modify it under the terms of the GNU General Public License 
// * as published by the Free Software Foundation; either version 2 
// * of the License, or (at your option) any later version. * 
// * This program is distributed in the hope that it will be useful, 
// * but WITHOUT ANY WARRANTY; without even the implied warranty of 
// * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
// * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
// * along with this program; if not, write to the Free Software 
// * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
// * 
// * <OSCAR TEAM>
// * This software was written for the 
// * Department of Family Medicine 
// * McMaster Unviersity 
// * Hamilton 
// * Ontario, Canada 
// *
// -----------------------------------------------------------------------------------------------------------------------
package oscar.oscarSecurity;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.Properties;

public class CookieSecurity extends HttpServlet
{

    private String cookieName;
    private String cookieValue;

    private void setCookieName( String tree )
    {
        this.cookieName = tree;
    }

    public String getCookieName()
    {
        if( this.cookieName == null )
        {
            setCookieNameValue();
        }
        return this.cookieName;
    }

    private void setCookieValue( String tree )
    {
        this.cookieValue = tree;
    }
    public String getCookieValue()
    {
        if( this.cookieValue == null )
        {
            setCookieNameValue();
        }
        return this.cookieValue;
    }

    public void setCookieNameValue()
    {
        String userHomePath = System.getProperty("user.home", "user.dir");
        FileInputStream pStream = null;
        Properties ap = new Properties();
        try
        {
            File pFile = new File(userHomePath, "oscar_security.properties");
            pStream = new FileInputStream(pFile.getPath());
            ap.load(pStream);
            setCookieName(ap.getProperty("cookieName"));
            setCookieValue(ap.getProperty("cookieValue"));
        }
        catch(IOException e)
        {
            System.err.println("IO error in setCookieNameValue: " + e.getMessage());
        }
        finally
        {
            if (pStream != null)
            {
                try {
                     pStream.close();
                }
                catch (IOException ioe) {
                }
            }
        }
    }

    public Cookie GiveMeACookie()
    {
        CookieSecurity cs = new CookieSecurity();
        cs.setCookieNameValue();
        String name = cs.cookieName;
        String val = cs.cookieValue;
        Cookie cookie = new Cookie(cs.getCookieName(), cs.getCookieValue());
        cookie.setPath("/");
        return cookie;
    }

    public boolean FindThisCookie(Cookie[] cookies, String reqName, String reqValue)
    {
        boolean pass = false;
        if( cookies != null )
        {
            Cookie cookie;
            String storedValue = null;
            for (int i=0; i<cookies.length; i++)
            {
                cookie = cookies[i];
                if (reqName.equals(cookie.getName()))
                {
                    storedValue = cookie.getValue();
                }
            }
            if (!reqValue.equalsIgnoreCase(storedValue))
            {
                //System.out.println("You did not pass the security test");
            }
            else
            {
                //System.out.println("You passed the security test");
                pass = true;
            }
        }
        return pass;
    }
}

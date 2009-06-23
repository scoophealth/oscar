/*******************************************************************************
 * Copyright (c) 2008, 2009 Quatro Group Inc. and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU General Public License
 * which accompanies this distribution, and is available at
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * Contributors:
 *     <Quatro Group Software Systems inc.>  <OSCAR Team>
 *******************************************************************************/
package com.quatro.util;

public class PhoneNo {
    private String phone = "";
    private String extension = "";

    public PhoneNo()
    {
        if (phone == null) phone = "";
    }
    public PhoneNo(String phoneNo)
    {
        this.phone = GetPhoneNo(phoneNo);
    }
    
    public PhoneNo(String phoneNo, String extension)
    {
        this.phone = GetPhoneNo(phoneNo);
        this.extension = extension;
    }

	public String getPhoneNoMain() {
        StringBuffer phoneNumber = new StringBuffer(20);
        String pPhoneNumber = this.phone;

        if (!Utility.IsEmpty(pPhoneNumber))
        {
            int k = 1;
            for (int i = pPhoneNumber.length() - 1; i >= 0; i--, k++)
            {
                String c = pPhoneNumber.substring(i, 1);
                phoneNumber.insert(0, c);
                if (k == 4) phoneNumber.insert(0, '-');
                if (k == 7 && pPhoneNumber.length() > 7) phoneNumber.insert(0, ')');
                if (k == 10) phoneNumber.insert(0, '(');
            }
        }
        return phoneNumber.toString();
	}
	public void setPhoneNoMain(String PhoneNo) {
		this.phone = GetPhoneNo(PhoneNo);
	}	
/*    public String PhoneNoMain
    {
        get
        {
            StringBuffer phoneNumber = new StringBuffer(20);
            String pPhoneNumber = this.phone;

            if (!Utility.IsEmpty(pPhoneNumber))
            {
                int k = 1;
                for (int i = pPhoneNumber.length() - 1; i >= 0; i--, k++)
                {
                    String c = pPhoneNumber.substring(i, 1);
                    phoneNumber.insert(0, c);
                    if (k == 4) phoneNumber.insert(0, '-');
                    if (k == 7 && pPhoneNumber.length() > 7) phoneNumber.insert(0, ')');
                    if (k == 10) phoneNumber.insert(0, '(');
                }
            }
            return phoneNumber.toString();
        }
        set {phone = GetPhoneNo(value);}
    }
*/
	
	public String getExtension() {
		return extension;
	}
	public void setExtension(String extension) {
		this.extension = GetPhoneNo(extension);
	}
/*	public String Extension
    {
        get {return extension;}
        set { extension = GetPhoneNo(value);}
    }
*/
	
	public String getPhoneNoInternal() {
        if (Utility.IsEmpty(extension))
        {
            return phone;
        }
        else
        {
            return phone + "x" + extension;
        }
	}
/*    public String PhoneNoInternal
    {
        get
        {
            if (Utility.IsEmpty(extension))
            {
                return phone;
            }
            else
            {
                return phone + "x" + extension;
            }
        }
    }
*/
	
    public String ToString()
    {
        return phone;
    }
    
    public boolean Equals(Object phone2)
    {
        if (phone2 == null) return false; 
        String ph2 = ((PhoneNo)phone2).phone;
        return phone.equals(ph2);
    }
    
    public int GetHashCode()
    {
        return phone.hashCode();
    }
    
    public String GetPhoneNo(String phoneNo)
    {
        StringBuffer p = new StringBuffer(20);
        if (!Utility.IsEmpty(phoneNo))
        {
            for (int i = 0; i < phoneNo.length(); i++)
            {
                String c = phoneNo.substring(i, 1);
                if (("0".compareTo(c) <= 0 && "9".compareTo(c) >=0) || ("A".compareTo(c.toUpperCase()) <= 0 && "Z".compareTo(c.toUpperCase()) >=0))
                {
                    p.append(c);
                }
            }
        }
        return p.toString();
    }
    
    public String FormatPhoneNumber()
    {
        StringBuffer phoneNumber = new StringBuffer(20);
        String pPhoneNumber = this.phone;

        if (!Utility.IsEmpty(pPhoneNumber))
        {
            int k = 1;
            for (int i = pPhoneNumber.length()-1; i>=0 ;i--, k++){
                String c = pPhoneNumber.substring(i,1);
                phoneNumber.insert(0, c);
                if (k == 4) phoneNumber.insert(0,'-');     
                if (k == 7 && pPhoneNumber.length()>7) phoneNumber.insert(0,')') ;
                if (k == 10) phoneNumber.insert(0,'(');
            }
        }
        if (Utility.IsEmpty(this.extension))
        {
           return phoneNumber.toString();
        }
        else
        {
           return phoneNumber.toString() + "x" + this.extension;
        }
    }
}
